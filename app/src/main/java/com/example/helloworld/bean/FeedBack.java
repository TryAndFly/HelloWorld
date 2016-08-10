package com.example.helloworld.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FeedBack extends BmobObject{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
