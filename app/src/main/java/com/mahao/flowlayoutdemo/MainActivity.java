package com.mahao.flowlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FlowLayoutDemo.ViewClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowLayoutDemo demo = (FlowLayoutDemo) findViewById(R.id.flow_demo_mahao);
        demo.setOnclickListener(this);
    }


    /**
     *
     * @param view 点击的view
     * @param position  点击的位置
     * @param content  点击的内容
     * @param parent 父容器
     * @param flag //是否展示选中状态
     */
    @Override
    public void getOnclickListener(View view, int position, String content, FlowLayoutDemo parent, boolean flag) {

        if(flag == false){  //未选中

            //xml----> drawable
            view.setBackground(getResources().getDrawable(R.drawable.normal_bg));

        }else{  //选中
            view.setBackground(getResources().getDrawable(R.drawable.checked_bg));
        }
        Toast.makeText(this,position + "..." + content,Toast.LENGTH_SHORT).show();
    }
}
