package com.gaowenyun.gift.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 联网工具类
 */
public class HttpUtils {

    /**
     * 联网下载图片
     * 在下载之前，先从内存缓存中，取出对应的Bitmap，
     * 1.取出来的Bitmap为null:
     * 说明还没下载过，就应该下载
     * 2。取出来的Bitmap不为null:
     * 说明已经下载过了，并保存在内存中，直接返回该 Bitmap
     *
     * @param http
     * @return
     * @throws Exception
     */
    public static Bitmap getBitmap(String http) throws Exception {
        HttpURLConnection conn = null;
        Bitmap bitmap = null;
        //先从内存中取出来
        bitmap = ImageCache.getInstance().get(http);
        //如果bitmap不为null，说明已经下载过了，直接返回
        if (bitmap != null) {
            return bitmap;
        }
        //bitmap为null,              ==3级
        URL url = new URL(http);
        Log.d("ytmfdw", "联网下载：" + http);
        conn = (HttpURLConnection) url.openConnection();
        InputStream in = conn.getInputStream();
        bitmap = BitmapFactory.decodeStream(in);
        // 保存一份到内存中
        ImageCache.getInstance().put(http, bitmap);
        //关流
        in.close();
        conn.disconnect();
        return bitmap;
    }

}
