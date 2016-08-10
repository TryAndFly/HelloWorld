package com.example.helloworld.bmob_bean;

/**
 * Created by Administrator on 2016/8/8.
 */

import cn.bmob.v3.BmobObject;

/**
 * 用户帐号表
 */
public class User extends BmobObject{
    private String username;
    private String password;
    private int level;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
