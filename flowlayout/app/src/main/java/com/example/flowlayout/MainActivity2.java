package com.example.flowlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.flowlayout.flow.FlowLayout2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FlowLayout2 mFlowLayout =  findViewById(R.id.my_flow2);
        List<String> list = new ArrayList<>();
        list.add("this");
        list.add("is");
        list.add("my");
        list.add("flowlayout");
        list.add("!");
        list.add("text1");
        list.add("text2");
        list.add("text3");
        list.add("text4");
        list.add("kkkkkkkkkkkkkkk");
        list.add("kkkkkk44kkkkkkkk");
        list.add("kkkkk777kkkkk");
        list.add("k666kkkk");
        mFlowLayout.setAdapter(list, R.layout.item_text, new FlowLayout2.ItemView<String>() {
            @Override
            protected void getCover(String item, FlowLayout2.ViewHolder holder, View inflate, int position) {
                holder.setText(R.id.item_f,item);
            }


        });




    }
}