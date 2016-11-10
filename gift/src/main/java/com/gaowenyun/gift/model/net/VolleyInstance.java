package com.gaowenyun.gift.model.net;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gaowenyun.gift.ui.app.BestGiftApp;


/**
 *
 * Volley使用单例类
 */
public class VolleyInstance {

    /**
     * 单例的写法:(双重校验锁)
     * 1.私有化构造方法: 外部不能调用构造方法随意创建对象
     * 2.对外提供获取对象的方法
     *   (1) 定义静态当前类对象
     *   (2) 对外提供获取方法: 进行单例判断
     */
    // 2-1
    private static VolleyInstance instance;
    private RequestQueue requestQueue;
    // 1.私有构造方法
    private VolleyInstance() {

        requestQueue = Volley.newRequestQueue(BestGiftApp.getContext());
    }
    // 2-2
    public static VolleyInstance getInstance() {
        // 如果该对象是null
        if (instance == null) {
            // 全部线程同步扫描
            synchronized (VolleyInstance.class) {
                // 如果该对象还是null
                if (instance == null) {
                    instance = new VolleyInstance();
                }
            }
        }
        return instance;
    }
    /********************************************************************/
     // 对外提供请求方法
    public void startRequest(String url, final IVolleyResult result) {
        StringRequest sr = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              // 如果请求成功, 将返回的数据存储到接口里
                result.success(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // 请求失败, 通过接口通知调用者请求失败
                result.failure();
            }
        });
        requestQueue.add(sr);
    }
}
