package com.example.helloworld.util;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 * 这个栈用于管理继承BaseActivity(只有继承此活动的子类使用这个栈进行管理)
 */
public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList<>();

    public static void addActivity(AppCompatActivity actionBarActivity) {
        activities.add(actionBarActivity);
    }

    public static void removeActivity(AppCompatActivity actionBarActivity) {
        activities.remove(actionBarActivity);
    }

    public static void finishAll() {
        for (AppCompatActivity a : activities) {
            if (!a.isFinishing()) {
                a.finish();
            }
        }
    }

}
