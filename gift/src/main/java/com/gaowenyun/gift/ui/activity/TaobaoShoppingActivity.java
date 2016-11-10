package com.gaowenyun.gift.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gaowenyun.gift.R;


/**
 *
 */
public class TaobaoShoppingActivity extends AbsBaseActivity {
    private WebView taobaoWb;
    @Override
    protected int setLayout() {
        return R.layout.activity_taobao_detail;
    }

    @Override
    protected void initViews() {
        taobaoWb = byView(R.id.taobao_wb);
    }

    @Override
    protected void initDatas() {
        taobaoWb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("taobao");
        // 设置WebView加载网页的属性
        WebSettings set = taobaoWb.getSettings();
        // 让WebView能够执行javaScript
        set.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        set.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
        set.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
        set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        set.setSupportZoom(false);
        // 将图片调整到合适的大小
        set.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        set.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以被显示的屏幕控制
        set.setDisplayZoomControls(true);
        // 设置默认字体大小
        set.setDefaultFontSize(12);
        taobaoWb.loadUrl(url);
    }
}
