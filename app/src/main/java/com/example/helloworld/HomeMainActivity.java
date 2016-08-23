package com.example.helloworld;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helloworld.activity.WriteArticleActivity;
import com.example.helloworld.adapter.homeListViewAdapter;
import com.example.helloworld.bean.HomeSummary;
import com.example.helloworld.view.homeScrollView;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;

/**
 * MainActivity是整个程序的入口
 * 1.各个包的功能划分
 *      activity -->各个活动界面
 *      bean  -->实体类
 *      bmob -->操作服务端数据
 *
 */
public class HomeMainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;
    private ListView listView;
    private homeScrollView scrollView;

    private ViewPager viewPager;
    private ArrayList<View> pageview;

    private ImageView imageView;
    private ImageView[] imageViews;
    // 包裹点点的LinearLayout
    private ViewGroup group;


    private boolean tag = false;
    private boolean tagLong = false;

    private float mTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //Bmob接口密钥初始化
        Bmob.initialize(this,"028acd02f32e5fb5fd2af4c29557cdcf");

        //初始化的时候查询是否已经登录


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initView();
        setOnclick();
        scrollView.smoothScrollTo(0,0);


        //测试
//        Intent intent = new Intent(this, TestActivity.class);
//        startActivity(intent);
    }

    public void initView() {

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.home_viewpager_item1, null);
        View view2 = inflater.inflate(R.layout.home_viewpager_item2, null);
        View view3 = inflater.inflate(R.layout.home_viewpager_item3, null);

        // 将view装入数组
        pageview = new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);

        group = (ViewGroup) findViewById(R.id.viewGroup);

        // 有多少张图就有多少个点点
        imageViews = new ImageView[pageview.size()];
        for (int i = 0; i < pageview.size(); i++) {
            imageView = new ImageView(HomeMainActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;

            // 默认第一张图显示为选中状态
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            group.addView(imageViews[i]);
        }
        // 绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        // 绑定监听事件
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());

        scrollView = (homeScrollView) findViewById(R.id.scrollView);
        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab1 = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        imageView = (ImageView) findViewById(R.id.image);
        listView = (ListView) findViewById(R.id.listView);


        HomeSummary homeSummary = new HomeSummary("—人文—",
                "当我读雅典学院的时","喜欢博物馆，因为他是一方水土精粹文化的浓缩，在里面走上几个小时",
                "作者:张三",R.drawable.fly);

        ArrayList<HomeSummary> list = new ArrayList();

        for (int i = 0; i < 20; i++) {
            list.add(homeSummary);
        }

        homeListViewAdapter homeListViewAdapter = new homeListViewAdapter(HomeMainActivity.this,list);
        listView.setAdapter(homeListViewAdapter);


        //获取最小移动位置
        mTouch=20;
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
                Toast.makeText(HomeMainActivity.this,"点击了编辑菜单",0).show();
                Intent i = new Intent(HomeMainActivity.this, WriteArticleActivity.class);
                startActivity(i);
            }
        });

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//                Log.d("test1","开始滑动了");
                if(i1-i3>mTouch){
                    if (!tag) {
                        fab.hide();
                    } else {
                        fab.hide();
                        fab1.hide();
                        fab2.hide();
                        fab3.hide();
                        fab4.hide();
                    }
                }else if (i3- i1 >mTouch){
                    //上翻,显示
                    if (!tag) {
                        fab.show();
                    } else {
                        fab.show();
                        fab1.show();
                        fab2.show();
                        fab3.show();
                        fab4.show();
                    }
                }
            }
        });
    }

    public void setAlphas(Float f) {
        fab1.setAlpha(f);
        fab2.setAlpha(f);
        fab3.setAlpha(f);
        fab4.setAlpha(f);
    }

    // 数据适配器
    PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        // 获取当前窗体界面数
        public int getCount() {
            // TODO Auto-generated method stub
            return pageview.size();
        }

        @Override
        // 断是否由对象生成界面
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        // 是从ViewGroup中移出当前View
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(pageview.get(arg1));
        }

        // 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(pageview.get(arg1));
            return pageview.get(arg1);
        }
    };

    // pageView监听器
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        // 如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }

        }
    }

}
