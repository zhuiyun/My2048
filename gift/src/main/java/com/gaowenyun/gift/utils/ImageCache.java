package com.gaowenyun.gift.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 该 类用来作图片缓存：图片缓存在内存中
 * <p/>
 * 把下载好的图片保存在集合中
 * Map<key,value>:保存在静态代码块中
 * key:图片的地址
 * value:Bitmap
 * <p/>
 * 问题：当图片非常多的时候，Map占内存非常大，OOM内存溢出
 * 解决：LruCache
 * LruCache<String,Bitmap>
 * 初始化：
 * new LruCache<String,Bitmap>(缓存大小){
 * sizeOf()
 * }
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * 保存方法：
 * put(http,bitmap)
 * <p/>
 * 取出来的方法
 * get(http)
 */
public class ImageCache {
    //单位模式
    static ImageCache cache;

    //构造方法私有
    private ImageCache() {
        //进行初始化
        //初始化LruCache
        //1.先计算最大运行内存
        long total = Runtime.getRuntime().maxMemory();
        //2.设置缓存大小，一般为运行内存的1/8，有效解决因图片缓存过多导致的OOM
        //3.重写sizeOf
        map = new LruCache<String, Bitmap>((int) (total / 18)) {
            /**每当有缓存图片存入时调用
             * 计算存入的图片的占内存大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回bitmap的字节数
                return value.getByteCount();
            }
        };
    }

    //获取实例
    public static ImageCache getInstance() {
        if (cache == null) {
            cache = new ImageCache();
        }
        return cache;
    }


    //定义一个图片缓存的集合
    static LruCache<String, Bitmap> map;

    //提供一个保存的方法
    //在Map中，http与Bitmap一一对应
    public void put(String http, Bitmap bitmap) {
        //向静态集合中，存入一张图片
        map.put(http, bitmap);
        //向SD卡保存一份
        SDUtils.saveFile(http, bitmap);
    }

    //提供一个取的方法
    //根据http来取对应的图片
    public Bitmap get(String http) {
        //从集合中取出对应的图片
        //从内存缓存取出
        Bitmap bitmap = null;
        bitmap = map.get(http);
        //1.取到了，直接返回            ==1级缓存
        if (bitmap != null) {
            return bitmap;
        }
        //2.没有取到，尝试从本地加载    ==2级缓存
        return SDUtils.getBitmap(http);
    }


}
