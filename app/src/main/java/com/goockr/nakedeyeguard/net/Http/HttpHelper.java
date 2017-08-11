package com.goockr.nakedeyeguard.net.Http;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * api接口
 * Created by JJT-ssd on 2016/12/31.
 */

public class HttpHelper {
    public static String deviceId = "12345";

    private static String qrCodeUrl = "http://weixin.qq.com/r/8jpieqrEcfcOre4Z92-g";

    /**
     * 获取设备Id
     */
    public static String getDeviceId(String devId) {
        return qrCodeUrl + "?c_pad_id=" + devId;
    }

    /**
     * 获取设备绑定接口
     */
    public static String getBinding() {
        return baseUrl + "/lean/isBindingSuccess";
    }

    //base url
    public static String baseUrl = "https://maverickhunteraxel.leanapp.cn";

    //获取用户列表
    public static String getUserList() {
        return baseUrl + "/lean/pullUserListCPad";
    }

    //获取用户详细信息:
    public static String getUserInfo() {
        return baseUrl + "/lean/pullUserInfoCPad";
    }

    //开始电击:
    public static String getStartTreat() {
        return baseUrl + "/lean/pushStartTreat";
    }

    //暂停电击
    public static String getPauseTreat() {
        return baseUrl + "/lean/pushPauseTreat";
    }

    // 完成电击
    public static String getCompeleteTreat() {
        return baseUrl + "/lean/pushFinishTreat";
    }

    //删除用户
    public static String getReset() {
        return baseUrl + "/lean/cleanBinding";
    }

    //下载apk
    public static String getTheNewstApkStr() {
        return "http://120.24.5.252:8080/light/testDemo.apk";
    }
    //获取apk版本信息
    public static String getNewstApkinfo() {
        return "http://192.168.1.187:8080/vision/servlet/AppServlet?functype=dv";
    }



    /**
     * 初始化HTTPS
     */
    public static void httpInits() {
        //       ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("HTTPTAG"))
                // .cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }


    /**
     * 发送POST请求
     *
     * @param urlStr   请求连接
     * @param params   传参
     * @param callback 数据返回
     */
    public static void httpPost(String urlStr, Map<String, String> params, Callback callback) {
        OkHttpUtils
                .post()
                .url(urlStr)
                .params(params)
                .build()
                .execute(callback);
    }

}
