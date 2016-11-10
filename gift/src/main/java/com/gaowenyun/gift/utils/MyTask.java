package com.gaowenyun.gift.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 *
 */
public class MyTask extends AsyncTask<String, Void, Bitmap> {
    ImageView iv;

    public MyTask(ImageView iv) {
        this.iv = iv;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        //获取http
        String http = params[0];
        Bitmap bitmap = null;
        //联网下载
        try {
            bitmap = HttpUtils.getBitmap(http);
            //保存一份到缓存中
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        }
    }
}
