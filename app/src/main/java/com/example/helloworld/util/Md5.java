package com.example.helloworld.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Md5 {
    public static String md5Password(String password){
        MessageDigest messageDigest;
        try {
            //获取MessageDigest信息摘要器对象，调用MessageDigest.getInstance(“md5”)
            messageDigest = MessageDigest.getInstance("md5");
            //调用MessageDigest对象的digest(bytes)方法，得到加密的byte[] 数组
            byte [] bytes = messageDigest.digest(password.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            //用每一个byte去和11111111八个二进制位做与运算并且得到的是int类型
            for (byte b: bytes) {
                //运行 byte & 0xff得到 int值
                int number=b & 0xff;
                /**
                 * 调用Integer.toHexString(number)，
                 * 得到16进制并返回String类型判断String的长度是1的，在它的前面拼接上0
                 * 循环外面定义一个StringBuffer对象，调用StringBuffer对象的append()拼接起来字符串
                 * 调用StringBuffer对象的toString()方法，得到加密后的标准字符串结果
                 */
                String str=Integer.toHexString(number);
                if(str.length()==1){
                    stringBuffer.append("0");
                }
                stringBuffer.append(str);
            }

            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
