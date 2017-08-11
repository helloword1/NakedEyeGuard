package com.goockr.nakedeyeguard.Tools;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by LZJ on 2017/1/25.
 */

public class NetUtils {

    public static String WIFINAME = "wifiname";
    public static String WIFIPASSWORD = "wifipassword";
    public static String WIFITYPE = "wifitype";

    /**
     * ping百度测试是否能联网
     * */
    public static final boolean ping() {
        String result = null;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.d("------ping-----", "result content : " + stringBuffer.toString());
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }

    /**
     * 连接网络
     * */

    public static boolean connect(){
        openWifi();
        String wifiName = ACache.get(App.getContext()).getAsString(NetUtils.WIFINAME);
        String wifiPWD = ACache.get(App.getContext()).getAsString(NetUtils.WIFIPASSWORD);
        int type = Integer.valueOf(ACache.get(App.getContext()).getAsString(NetUtils.WIFITYPE));

        WifiAutoConnectManager manager = new WifiAutoConnectManager((WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        switch (type){
            case 0://没有密码的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS);
                break;
            case 1://WPA_EAP加密的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                break;
            case 2://WPA_PSK加密的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                break;
            case 3:
                //WEP加密的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WEP);
                break;
        }

        boolean isConnect = false;
        int i = 7;
        while (i!=0){
            isConnect = NetUtils.ping();
            if (isConnect){
                return isConnect;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            i--;
        }
        return false;
    }


    /**
     * 连接网络
     * */

    public static boolean connect(String wifiName,String wifiPWD,int type){
        openWifi();

        ACache.get(App.getContext()).put(WIFINAME,wifiName);
        ACache.get(App.getContext()).put(WIFIPASSWORD,wifiPWD);
        ACache.get(App.getContext()).put(WIFITYPE,String.valueOf(type));

        WifiAutoConnectManager manager = new WifiAutoConnectManager((WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE));
        switch (type){
            case 0://没有密码的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS);
                break;
            case 1://WPA_EAP加密的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                break;
            case 2://WPA_PSK加密的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                break;
            case 3:
                //WEP加密的情况
                manager.connect(wifiName,wifiPWD,WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WEP);
                break;
        }

        boolean isConnect = false;
        int i = 7;
        while (i!=0){
            isConnect = NetUtils.ping();
            if (isConnect){
                return isConnect;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            i--;
        }
        return false;
    }


    /**
     * 返回当前绑定的wifi名字
     * */

    public static String getCurWifi(){
        WifiManager wifiManager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    /**
     * 是否已经绑定过wifi
     * */

    public static boolean isBind(){
        String wifiName = ACache.get(App.getContext()).getAsString(NetUtils.WIFINAME);
        if (wifiName!=null&&!wifiName.equals("")){
            return true;
        }
        return false;
    }




    /**
     * 打开网络
     * */
    public static void openWifi(){
        WifiManager wm = (WifiManager) App.getContext().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(true);
    }



    /**
     * 断开网络
     * */

    public static void disconWfi(){
        WifiManager mWifiManager = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int netId = mWifiInfo.getNetworkId();
        try {
            mWifiManager.disableNetwork(netId);
            mWifiManager.disconnect();
            mWifiInfo = null;
        }catch (Exception e){

        }
    }

}

