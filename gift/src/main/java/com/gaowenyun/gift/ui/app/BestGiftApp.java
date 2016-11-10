package com.gaowenyun.gift.ui.app;


import android.app.Application;


import android.app.Application;
import android.content.Context;

import cn.bmob.sms.BmobSMS;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongIM;


public class BestGiftApp extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ShareSDK.initSDK(this);
        RongIM.init(this);
        BmobSMS.initialize(context,"9af5cb216bcb74698c7c673c2c001616");
    }
    public static Context getContext() {
        return context;
    }
}
