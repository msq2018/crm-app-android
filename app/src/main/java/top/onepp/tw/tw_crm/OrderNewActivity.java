package top.onepp.tw.tw_crm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class OrderNewActivity extends AppCompatActivity {

    private Spinner orderTypeSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_new);
        orderTypeSpinner = findViewById(R.id.order_new_order_type);
        ArrayList<String> spinners = new ArrayList<>();
        spinners.add("请选择");
        spinners.add("今日");
        spinners.add("昨日");
        spinners.add("本周");
        spinners.add("上周");
        spinners.add("本月");
        spinners.add("上月");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.spinner_item,spinners);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        orderTypeSpinner.setAdapter(adapter);
       /* mSpinner = (Spinner) findViewById(R.id.select_customer);
        //只是为了展示我们的实现效果，故可不要
        //mTv = (TextView) findViewById(R.id.tv_content);

        //数据源
        ArrayList<String> spinners = new ArrayList<>();
        spinners.add("请选择");
        spinners.add("今日");
        spinners.add("昨日");
        spinners.add("本周");
        spinners.add("上周");
        spinners.add("本月");
        spinners.add("上月");
        //设置ArrayAdapter内置的item样式-这里是单行显示样式
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinners);
        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //设置Adapter了
        mSpinner.setAdapter(adapter);
        //监听Spinner的操作
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("select Item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("no nothing selected");
            }
        });*/
    }


}
