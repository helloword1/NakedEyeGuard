package com.goockr.nakedeyeguard.Http;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by JJT-ssd on 2016/12/31.
 */

public class HttpHelper {


    public static String baseUrl="https://maverickhunteraxel.leanapp.cn";

    //获取用户列表
    public static String getUserList() {
        return baseUrl+"lean/pullUserList";
    }

    //获取用户详细信息:
    public static String getUserInfo() {
        return baseUrl+"lean/pullUserInfo";
    }

    //开始电击:
    public static String getStartTreat() {
        return baseUrl+"lean/pushStartTreat";
    }

    //暂停电击
    public static String getPauseTreat() {
        return baseUrl+"lean/pushPauseTreat";
    }
   // 完成电击
    public static String getCompeleteTreat() {
     return baseUrl+"lean/pushPauseTreat";
    }


    public  static  void httpInit()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//              .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


   public static void httpPost(String urlStr, Map<String,String> params, Callback callback)
   {
       OkHttpUtils
               .post()
               .url(urlStr)
               .params(params)
               .build()
               .execute(callback);
   }
}
