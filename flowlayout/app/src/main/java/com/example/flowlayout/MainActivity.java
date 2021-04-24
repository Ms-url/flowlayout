package com.example.flowlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyView myView = findViewById(R.id.my_view);

        final Animation animation1= AnimationUtils.loadAnimation(MainActivity.this,R.anim.scan_circle);
        final Animation animation2= AnimationUtils.loadAnimation(MainActivity.this,R.anim.scan_circle);
        final Animation animation3= AnimationUtils.loadAnimation(MainActivity.this,R.anim.scan_circle);
        final Animation animation4= AnimationUtils.loadAnimation(MainActivity.this,R.anim.scan_circle);
        final Animation animation5= AnimationUtils.loadAnimation(MainActivity.this,R.anim.ainm_my_rect);

        final ImageView imageView1=findViewById(R.id.circle1);
        final ImageView imageView2=findViewById(R.id.circle2);
        final ImageView imageView3=findViewById(R.id.circle3);
        final ImageView imageView4=findViewById(R.id.circle4);

        animation1.setInterpolator(new LinearInterpolator());
        animation2.setInterpolator(new LinearInterpolator());
        animation3.setInterpolator(new LinearInterpolator());
        animation4.setInterpolator(new LinearInterpolator());
        //animation2.setInterpolator(new AccelerateInterpolator());
        // animation5.setInterpolator(new BounceInterpolator());
        animation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                myView.startAnimation(animation5);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent1=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ImageButton imageButton = findViewById(R.id.botton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView1.startAnimation(animation1);

                animation2.setStartOffset(600);
                imageView2.startAnimation(animation2);

                animation3.setStartOffset(1200);
                imageView3.startAnimation(animation3);

                animation4.setStartOffset(1800);
                imageView4.startAnimation(animation4);
            }
        });

        FlowLayout1 mFlowLayout =  findViewById(R.id.my_flow);
        List<String> list = new ArrayList<>();
        list.add("this");
        list.add("is");
        list.add("not");
        list.add("my");
        list.add("flowlayout");
        list.add("!");
        list.add("please");
        list.add("click");
        list.add("this");
        list.add("button");
        list.add("My");
        list.add("flowlayout");
        list.add("will");
        list.add("show");
        list.add("up");
        list.add("after");
        list.add("5");
        list.add("second");
        mFlowLayout.setAdapter(list, R.layout.item_text, new FlowLayout1.ItemView<String>() {
            @Override
            protected void getCover(String item, FlowLayout1.ViewHolder holder, View inflate, int position) {
                holder.setText(R.id.item_f,item);
            }

        });


    }
}