package top.onepp.tw.tw_crm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.onepp.tw.tw_crm.util.Helper;
import top.onepp.tw.tw_crm.util.Json;
import top.onepp.tw.tw_crm.util.SwipeRefreshView;


public class OrderListActivity extends BaseActivity {

    private ListView orderListBox;
    private SimpleAdapter mAdapter;
    private SharedPreferences preferences = null;
    private SwipeRefreshView mSwipeRefreshView;
    private String nextPageUrl;
    private List<Map<String, Object>> mList;
    private FloatingActionButton addOrderButton;

    private TableLayout detailTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mList = new ArrayList<>();

        orderListBox = findViewById(R.id.order_list_box);
        mSwipeRefreshView = findViewById(R.id.order_list_swipe_refresh);
        mSwipeRefreshView.setItemCount(5);
        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);
        addOrderButton = findViewById(R.id.add_order_button);
        initEvent();
        initData();
    }


    private void initEvent() {
        //下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        // 设置下拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });

        orderListBox.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap item = (HashMap) parent.getItemAtPosition(position);
                Integer entityId = (Integer) item.get("entityId");
                loadOrderDetail(entityId);
                return false;
            }
        });
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this,OrderNewActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String url = Helper.getConfigValue(OrderListActivity.this, "order_list");
                httpOrderListRequest(url, true);
                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (mSwipeRefreshView.isRefreshing()) {
                    mSwipeRefreshView.setRefreshing(false);
                }
            }
        }, 2000);

    }

    private void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println(nextPageUrl);
                if (!nextPageUrl.equals("null")) {
                    httpOrderListRequest(nextPageUrl, false);
                } else {
                    Toast.makeText(OrderListActivity.this, getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                }
                // 加载完数据设置为不加载状态，将加载进度收起来
                mSwipeRefreshView.setLoading(false);
            }
        }, 2000);
    }

    private void loadOrderDetail(Integer entityId) {
        String baseUrl = Helper.getConfigValue(this, "base_url");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority(baseUrl)
                .appendPath("api")
                .appendPath("order")
                .appendPath("detail")
                .appendQueryParameter("id", entityId.toString());
        String url = builder.build().toString();
        httpOrderDetailRequest(url);
    }

    private void httpOrderDetailRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Json json = Json.init();
                JSONObject jsonObject = json.parse(response);
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    showOrderDetail(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                if (preferences.contains("user_id") && preferences.contains("user_token")) {
                    Integer userId = preferences.getInt("user_id", 0);
                    String usetToken = preferences.getString("user_token", null);
                    headers.put("user-token", usetToken);
                    headers.put("user-id", userId.toString());
                }
                return headers;
            }
        };
        queue.add(request);
    }


    private void httpOrderListRequest(String url, final boolean status) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Json json = Json.init();
                JSONObject jsonObject = json.parse(response);
                JSONObject links = json.getJsonObject(jsonObject,"links");
                nextPageUrl = json.getString(links, "next");
                JSONArray data = json.getJsonArray(jsonObject, "data");
                showOrderList(data, status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                if (preferences.contains("user_id") && preferences.contains("user_token")) {
                    Integer userId = preferences.getInt("user_id", 0);
                    String usetToken = preferences.getString("user_token", null);
                    headers.put("user-token", usetToken);
                    headers.put("user-id", userId.toString());
                }
                return headers;
            }
        };
        queue.add(request);
        //Http Request End;
    }

    private void showOrderList(JSONArray data, Boolean isFirst) {
        try {
            if (isFirst) {
                mList.clear();
            }
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = (JSONObject) data.get(i);
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("companies", item.getString("company_name"));
                listItem.put("orders", "Order Number:" + item.getString("order_num"));
                listItem.put("customers", "Customer Name:" + item.getString("user_id"));
                listItem.put("qty", "Qty:" + item.getString("product_qty"));
                listItem.put("prices", "Price:" + item.getString("order_total"));
                listItem.put("opportunity", "Oppor:" + item.getString("deal_opportunity"));
                listItem.put("entityId", item.getInt("id"));
                mList.add(listItem);
            }
            mAdapter = new SimpleAdapter(this, mList, R.layout.order_list_item,
                    new String[]{"companies", "orders", "customers", "qty", "prices", "opportunity", "entityId"},
                    new int[]{R.id.company_name, R.id.order_id, R.id.customer_name, R.id.order_qty, R.id.order_price, R.id.deal_opportunity, R.id.entity_id});
            orderListBox.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showOrderDetail(JSONObject data) {
        System.out.println(data);
        try {
            Map<String, Object> datail = new HashMap<String, Object>();
            datail.put("Status",data.getString("status"));
            datail.put("Order Type",data.getString("order_type"));
            datail.put("Customer",data.getString("customer_id"));
            datail.put("Order Total",data.getString("order_total"));
            datail.put("Deal Opportunity",data.getString("deal_opportunity"));
            datail.put("Product Qty",data.getString("product_qty"));
            datail.put("Company Name",data.getString("company_name"));
            datail.put("Order Number",data.getString("order_num"));
            System.out.println(datail);
            AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
            final View dialogView = LayoutInflater.from(OrderListActivity.this)
                    .inflate(R.layout.order_detail, null);
            detailTableLayout = (TableLayout) dialogView.findViewById(R.id.order_detail_table);
            for (Map.Entry<String, Object> entry : datail.entrySet()) {
                TableRow tablerow = new TableRow(getApplicationContext());
                String key = entry.getKey();
                Object value = entry.getValue() ;
                TextView viewName = new TextView(OrderListActivity.this);
                TextView viewValue = new TextView(OrderListActivity.this);
                viewName.setPadding(10,10,10,10);
                viewValue.setPadding(10,10,10,10);
                viewName.setText(key.toString());
                viewValue.setText(value.toString());
                tablerow.addView(viewName);
                tablerow.addView(viewValue);
                detailTableLayout.addView(tablerow);
            }
            customizeDialog.setIcon(getDrawable(R.drawable.ic_menu_share));
            customizeDialog.setTitle("我是一个自定义Dialog");
            customizeDialog.setView(dialogView);
            customizeDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("test");
                        }
                    });
            customizeDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
