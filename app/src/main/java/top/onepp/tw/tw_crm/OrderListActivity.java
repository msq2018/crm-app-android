package top.onepp.tw.tw_crm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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


public class OrderListActivity extends AppCompatActivity {

    private ListView orderListBox;
    private SimpleAdapter mAdapter;
    private SharedPreferences preferences = null;
    private SwipeRefreshView mSwipeRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (preferences == null){
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        orderListBox = findViewById(R.id.order_list_box);
        mSwipeRefreshView = findViewById(R.id.order_list_swipe_refresh);

        // Http Request
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Helper.getConfigValue(this,"order_list");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Json json = Json.init();
                JSONObject jsonObject= json.parse(response);
                JSONArray data = json.getJsonArray(jsonObject,"data");
                orderList(data);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            public Map<String,String> getHeaders(){
                Map<String,String> headers = new HashMap<String, String>();
                if (preferences.contains("user_id") && preferences.contains("user_token")){
                    Integer userId = preferences.getInt("user_id",0);
                    String usetToken = preferences.getString("user_token",null);
                    headers.put("user-token",usetToken);
                    headers.put("user-id",userId.toString());
                }
                return headers;
            }
        };
        queue.add(request);
        //Http Request End;

        mSwipeRefreshView.measure(0, 0);
        mSwipeRefreshView.setRefreshing(true);

        initEvent();
        initData();
    }

    private void initData() {

    }

    private void initEvent() {
        //下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        // 设置下拉加载更多
        mSwipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }

    public void orderList(JSONArray data){
        try {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = (JSONObject) data.get(i);
                Map<String, Object> listItem = new HashMap<String, Object>();
                listItem.put("companies", item.getString("company_name"));
                listItem.put("orders", "Order Number:"+ item.getString("order_num"));
                listItem.put("customers", "Customer Name:"+item.getString("user_id"));
                listItem.put("qty","Qty:"+item.getString("product_qty"));
                listItem.put("prices","Price:"+item.getString("order_total"));
                listItem.put("Opportunity","Oppor:"+item.getString("deal_opportunity"));
                list.add(listItem);
            }
            System.out.println(list);
            mAdapter = new SimpleAdapter(this,list,R.layout.order_list_item,
                    new String[]{ "companies", "orders", "customers","qty","prices","Opportunity" },
                    new int[]{R.id.company_name,R.id.order_id,R.id.customer_name,R.id.order_qty,R.id.order_price,R.id.deal_opportunity});
            orderListBox.setAdapter(mAdapter);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
