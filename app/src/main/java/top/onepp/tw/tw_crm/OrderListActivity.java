package top.onepp.tw.tw_crm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderListActivity extends AppCompatActivity {

    private ListView orderListBox;
    private SimpleAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        orderListBox = findViewById(R.id.order_list_box);
        String[] companies = { "南山人壽", "力晶科技", "長利科技", "聯穎光電" };
        String[] orders = { "100000007", "100000000", "100000005", "100000004" };
        String[] customers = {"陳先生","廖先生","2014.11.12長利賣黃先生","陳先生"};
        int[] qties = {30,5,1,3};
        double[] prices = {50.00,30.00,50.00,35.00};
        String[] Opportunity = {"询问","询问","询问","询问"};
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 4; ++i) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("companies", companies[i]);
            listItem.put("orders", "Order Number:"+orders[i]);
            listItem.put("customers", "Customer Name:"+customers[i]);
            listItem.put("qty","Qty:"+qties[i]);
            listItem.put("prices","Price:"+prices[i]);
            listItem.put("Opportunity","Oppor:"+Opportunity[i]);
            list.add(listItem);
        }
        System.out.println(list);
        mAdapter = new SimpleAdapter(this,list,R.layout.order_list_item,
                new String[]{ "companies", "orders", "customers","qty","prices","Opportunity" },
                new int[]{R.id.company_name,R.id.order_id,R.id.customer_name,R.id.order_qty,R.id.order_price,R.id.deal_opportunity});
        orderListBox.setAdapter(mAdapter);
    }
}
