package com.example.helloworld.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/8/22.
 */
public class homeScrollView extends ScrollView {

    private OnScrollChangeListener scrollChangeListener = null;

    public homeScrollView(Context context) {
        super(context);
    }

    public homeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        scrollChangeListener = l;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //解决api23以下无法使用此接口
        if (scrollChangeListener!=null){
            scrollChangeListener.onScrollChange(this,l,t,oldl,oldt);
        }

    }
}
