package com.example.helloworld.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FeedBack extends BmobObject{
    private BmobFile face;

    public BmobFile getFace() {
        return face;
    }

    public void setFace(BmobFile face) {
        this.face = face;
    }
}
