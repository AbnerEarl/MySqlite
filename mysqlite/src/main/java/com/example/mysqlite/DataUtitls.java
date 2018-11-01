package com.example.mysqlite;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * PROJECT_NAME:MyApplication
 * PACKAGE_NAME:com.example.mysqlite
 * USER:Frank
 * DATE:2018/10/31
 * TIME:21:36
 * DAY_NAME_FULL:星期三
 * DESCRIPTION:On the description and function of the document
 **/
public class DataUtitls {
    public static byte[] BitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
