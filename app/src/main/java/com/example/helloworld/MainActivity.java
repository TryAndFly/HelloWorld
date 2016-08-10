package com.example.helloworld;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.helloworld.activity.LoginActivity;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;

/**
 * MainActivity是整个程序的入口
 * 1.各个包的功能划分
 *      activity -->各个活动界面
 *      bean  -->实体类
 *      bmob -->操作服务端数据
 *
 */
public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;
    private ImageView imageView;
    private ListView listView;

    private boolean tag = false;
    private boolean tagLong = false;

    private float mFirstY;
    private float mCurrentY;
    private float mTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bmob接口密钥初始化
        Bmob.initialize(this,"028acd02f32e5fb5fd2af4c29557cdcf");

        //初始化的时候查询是否已经登录

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initView();
        setOnclick();
    }
    public void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab1 = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        imageView = (ImageView) findViewById(R.id.image);
        listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add("测试"+i);
        }
        listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        //获取最小移动位置
        mTouch = ViewConfiguration.get(this).getScaledTouchSlop();

        fab1.hide();
        fab2.hide();
        fab3.hide();
        fab4.hide();
    }

    public void setOnclick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启一段动画后显示四个菜单

                //动画暂时省去
                //根据FloatActionBar的位置计算放置的位置
                if (tag == false) {
                    fab1.show();
                    fab2.show();
                    fab3.show();
                    fab4.show();
                    tag = true;
                } else {
                    fab1.hide();
                    fab2.hide();
                    fab3.hide();
                    fab4.hide();
                    tag = false;
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //长按时让按钮渐渐变透明
                ObjectAnimator animator;
                if (!tagLong) {
                    animator = ObjectAnimator.ofFloat(fab, "alpha", .2f);
                    setAlphas(.2f);
                    tagLong = true;
                } else {
                    animator = ObjectAnimator.ofFloat(fab, "alpha", 1);
                    setAlphas(1f);
                    tagLong = false;
                }
                animator.setDuration(1000);
                animator.start();
                return true;
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.list);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageResource(R.drawable.add);
            }
        });

        //监听下滑距离
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //记录起点
                        mFirstY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = motionEvent.getY();
                        if (mFirstY - mCurrentY>mTouch){
                            //下滑,隐藏
                            if (!tag){
                                fab.hide();
                            }else {
                                fab.hide();
                                fab1.hide();
                                fab2.hide();
                                fab3.hide();
                                fab4.hide();
                            }
                        }else if (mCurrentY - mFirstY>mTouch){
                            //上翻,显示
                            if (!tag){
                                fab.show();
                            }else {
                                fab.show();
                                fab1.show();
                                fab2.show();
                                fab3.show();
                                fab4.show();
                            }
                        }
                }
                return false;
            }
        });
    }
    public void setAlphas(Float f){
        fab1.setAlpha(f);
        fab2.setAlpha(f);
        fab3.setAlpha(f);
        fab4.setAlpha(f);
    }
}
