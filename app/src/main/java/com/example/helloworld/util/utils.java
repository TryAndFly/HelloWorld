package com.example.helloworld.util;

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

    //
}
