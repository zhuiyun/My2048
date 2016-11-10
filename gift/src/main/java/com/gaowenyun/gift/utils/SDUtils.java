package com.gaowenyun.gift.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 从SD卡上读取文件，写入文件
 */
public class SDUtils {

    /**
     * 缓存路径，不包含SD卡根路径
     */
    final static String CACHE_PATH = "/qf/imgCache/";

    /**
     * 从SD卡中，根据文件名，读取Bitmap
     *
     * @param http 文件的名称
     * @return
     */
    public static Bitmap getBitmap(String http) {
        //1.先判断SD卡是否安装好，如果没有安装好，直接返回 null
        if (!isMount()) {
            return null;
        }
        //2.构建缓存目录/sdcar/qf/imagCache
        //3.构建文件路径/sdcar/qf/imagCache/fileName
        //获取文件名
        String name = MD5Utils.MD5(http);
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + CACHE_PATH + name;
        //4.判断文件是否存在如果不存在，返回null
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        //BitmapFactory
        return BitmapFactory.decodeFile(filePath);
    }


    /**
     * 保存bitmap到指定的目录下
     *
     * @param http   图片的网址http://www.ytmfdw.com/img/img1.jpg http://www.baidu.com/img/img1.jpg
     * @param bitmap 图片
     */
    public static void saveFile(String http, Bitmap bitmap) {
        //1.先判断SD卡是否安装 好，如果没安装好，直接返回
        if (!isMount()) {
            return;
        }
        //2.构建缓存目录/sdcar/qf/imagCache，如果目录不存在，创建
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + CACHE_PATH;
        File parent = new File(filePath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        //文件存在，但不是目录
        if (!parent.isDirectory()) {
            return;
        }
        //3.创建Bitmap的文件:先构建文件
        //获取文件名
        String name = MD5Utils.MD5(http);
        File file = new File(filePath, name);
        //保存图片
        //利用bitmap compress方法，
        //参数一：文件类型：CompressFormat.PNG、JPG、GIF
        //参数二：图片质量0~100
        //参数三：文件输出流
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * SD卡是否安装好
     *
     * @return
     */
    public static boolean isMount() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
