package com.example.helloworld.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.helloworld.R;

/**
 * Created by Administrator on 2016/8/26.
 */
public class utils {
    //用来标识存入图片
    public static final String mBitmapTag = "☆";
    /**
     * 处理文本信息，获取第一张图片设置为封面
     * 剔除图片URL获取前四十个字符
     */
    public static String setSummarys(String summary){
        StringBuffer str = new StringBuffer(summary);
        int index1 = 0, index2 = 0;
        while (true) {
            index1 = str.indexOf(mBitmapTag);
            if(index1 == -1)break;
            index2 = str.indexOf(mBitmapTag, index1 + 1);
            str.replace(index1, index2 + mBitmapTag.length(), "");
        }
        return str.toString();
    }
    /**
     * 将文本中的url解析并加载图片
     */
    public static String parseUrl(String summary){
        StringBuffer str = new StringBuffer(summary);
        StringBuffer str2 = new StringBuffer();
        int index1 = 0, index2 = 0;
        while (true) {
            index1 = str.indexOf(mBitmapTag);
            if(index1 == -1)break;
            index2 = str.indexOf(mBitmapTag, index1 + 1);
            String s = str.subSequence(index1 , index2 + mBitmapTag.length()).toString();
            //使用异步加载先加载文字，然后加载图片

            str.replace(index1, index2 + mBitmapTag.length(), "");
        }
        return str.toString();
    }
}
