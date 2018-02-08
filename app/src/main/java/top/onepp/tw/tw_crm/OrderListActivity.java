package top.onepp.tw.tw_crm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class OrderListActivity extends AppCompatActivity {

    private ListView orderListBox;
    private ArrayAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        orderListBox = findViewById(R.id.order_list_box);
        String[]myStringArray = {"1231313","cdsacdsacdsacdsa","3213131#@!#!","3213131#@!#!"} ;
        mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,myStringArray);
        orderListBox.setAdapter(mAdapter);
    }
}
