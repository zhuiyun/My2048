package com.gaowenyun.gift.utils;


//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易  
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//                  不见满街漂亮妹，哪个归得程序员？  


import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/1 0001.
 */
public class UpdateInfoService {

        public UpdateInfoService(Context context) {
        }

        public UpdateInfo getUpDateInfo() throws Exception {
            String path = GetServerUrl.url+"update.txt";
            StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader reader = null;
            try {
                // 创建一个url对象
                URL url = new URL(path);
                // 通過url对象，创建一个HttpURLConnection对象（连接）
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                // 通过HttpURLConnection对象，得到InputStream
                reader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                // 使用io流读取文件
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String info = sb.toString();
            UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.setVersion(info.split("&")[1]);
            updateInfo.setDescription(info.split("&")[2]);
            updateInfo.setUrl(info.split("&")[3]);
            return updateInfo;
        }


}
