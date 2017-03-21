package com.goockr.nakedeyeguard.Http;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by JJT-ssd on 2016/12/31.
 */

public class HttpHelper {



    public static String deviceId="12345";

    private static String qrCodeUrl="http://weixin.qq.com/r/8jpieqrEcfcOre4Z92-g";

    /**
     * 获取设备绑定接口
     */
    public static String getDeviceId(String devId)
    {
        return qrCodeUrl+"?c_pad_id="+devId;
    }

    //base url
    public static String baseUrl="https://maverickhunteraxel.leanapp.cn";

    //获取用户列表
    public static String getUserList() {
        return baseUrl+"/lean/pullUserListCPad";
    }

    //获取用户详细信息:
    public static String getUserInfo() {
        return baseUrl+"/lean/pullUserInfoCPad";
    }

    //开始电击:
    public static String getStartTreat() {
        return baseUrl+"/lean/pushStartTreat";
    }

    //暂停电击
    public static String getPauseTreat() {
        return baseUrl+"/lean/pushPauseTreat";
    }
   // 完成电击
    public static String getCompeleteTreat() {
     return baseUrl+"/lean/pushFinishTreat";
    }



    /**
     * 初始化HTTPS
     */
    public  static  void httpInits()
    {

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


    /**
     * 发送POST请求
     * @param urlStr 请求连接
     * @param params 传参
     * @param callback 数据返回
     */
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
