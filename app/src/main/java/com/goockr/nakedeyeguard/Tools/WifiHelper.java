package com.goockr.nakedeyeguard.Tools;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by JJT-ssd on 2016/8/31.
 */
public class WifiHelper {

    /**
     * 检查当前网络是否可用
     */
    static ConnectivityManager connectivityManager;
    public static boolean isNetworkAvailable(Context mContext)
    {
        Map netWorkState = WifiHelper.networkState(mContext);
        boolean isAvailable = (boolean)netWorkState.get("isNetworkAvailable");
        return isAvailable;
    }

    public static Map networkState(Context mContext)
    {
        Context context = mContext.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Map maps =new HashMap();
        maps.put("isNetworkAvailable",false);
        if (connectivityManager == null)
        {
            maps.put("isNetworkAvailable",false);
            return maps;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
  //                  System.out.println(i + "===状态===" + networkInfo[i].getState());
                    //                 System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    maps.put(networkInfo[i].getTypeName(),networkInfo[i].getState());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        maps.put("isNetworkAvailable",true);
                    }
                }
            }
        }
        return maps;
    }

    //IP地址转换
    private static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

//////////////////////////////////////

    //获取周边wifi列表
//    public  List<ScanResult> getScanResult()
//    {
//
//        startScan();
//        List<ScanResult> listb = getWifiList();
//
//        //数组初始化要注意
//        List<String> wifiList=new ArrayList<>();
//        if(listb!=null){
//            for( int i=0;i<listb.size();i++){
//                ScanResult scanResult = listb.get(i);
//                wifiList.add(scanResult.SSID);
//                Log.e("SSID",scanResult.SSID);
//                Log.e("preSharedKey",mWifiConfiguration.get(i).preSharedKey);
//            }
//        }
//        return listb;
//    }

    //获取当前WiFi名称
    public static String getConnectWifiSsid(Context mContext){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", wifiInfo.toString());
        Log.d("SSID",wifiInfo.getSSID());
        return wifiInfo.getSSID();
    }


        // 定义WifiManager对象
        private WifiManager mWifiManager;
        // 定义WifiInfo对象
        private WifiInfo mWifiInfo;
        // 扫描出的网络连接列表
        private List<ScanResult> mWifiList;
        // 网络连接列表
        private List<WifiConfiguration> mWifiConfiguration;
        // 定义一个WifiLock
        WifiManager.WifiLock mWifiLock;


        //构造函数
        public WifiHelper(Context context) {
            // 取得WifiManager对象
            mWifiManager = (WifiManager) context
                    .getSystemService(WIFI_SERVICE);
            // 取得WifiInfo对象
            mWifiInfo = mWifiManager.getConnectionInfo();
        }

    public WifiInfo connectInfo()
    {
      return  mWifiManager.getConnectionInfo();
    }

        // 打开WIFI
        public void openWifi() {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }
        }

        // 关闭WIFI
        public void closeWifi() {
            if (mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(false);
            }
        }

        // 检查当前WIFI状态
        public int checkState() {
            return mWifiManager.getWifiState();
        }

        // 锁定WifiLock
        public void acquireWifiLock() {
            mWifiLock.acquire();
        }

        // 解锁WifiLock
        public void releaseWifiLock() {
            // 判断时候锁定
            if (mWifiLock.isHeld()) {
                mWifiLock.acquire();
            }
        }

        // 创建一个WifiLock
        public void creatWifiLock() {
            mWifiLock = mWifiManager.createWifiLock("Test");
        }

        // 得到配置好的网络
        public List<WifiConfiguration> getConfiguration() {
            return mWifiConfiguration;
        }

        // 指定配置好的网络进行连接
        public void connectConfiguration(int index) {
            // 索引大于配置好的网络索引返回
            if (index > mWifiConfiguration.size()) {
                return;
            }
            // 连接配置好的指定ID的网络
            mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);

        }

        public List<ScanResult>  startScan() {
            mWifiManager.startScan();
            // 得到扫描结果
            mWifiList = mWifiManager.getScanResults();
            // 得到配置好的网络连接
            mWifiConfiguration = mWifiManager.getConfiguredNetworks();
            return mWifiList;
        }

        // 得到网络列表
        public List<ScanResult> getWifiList() {
            return mWifiList;
        }

        // 查看扫描结果
        public StringBuilder lookUpScan() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mWifiList.size(); i++) {
                stringBuilder
                        .append("Index_" + new Integer(i + 1).toString() + ":");
                // 将ScanResult信息转换成一个字符串包
                // 其中把包括：BSSID、SSID、capabilities、frequency、level
                stringBuilder.append((mWifiList.get(i)).toString());
                stringBuilder.append("/n");
            }
            return stringBuilder;
        }

        // 得到MAC地址
        public String getMacAddress() {
            return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
        }

        // 得到接入点的BSSID
        public String getBSSID() {
            return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
        }

        // 得到IP地址
        public int getIPAddress() {
            return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
        }

        // 得到连接的ID
        public int getNetworkId() {
            return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
        }

        // 得到WifiInfo的所有信息包
        public String getWifiInfo() {
            return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
        }

        // 添加一个网络并连接
        //返回true表示连接成功，false表示连接失败
        public boolean addNetwork(WifiConfiguration wcg) {
            if (mWifiInfo!=null) disconnectWifi();
            int wcgID = mWifiManager.addNetwork(wcg);
            boolean b = mWifiManager.enableNetwork(wcgID, true);
            return b;
        }

        // 断开指定ID的网络
        public void disconnectWifi(int netId) {
            mWifiManager.disableNetwork(netId);
            mWifiManager.disconnect();
        }
        // 断开当前连接的网络
        public void disconnectWifi() {
            mWifiManager.disableNetwork(mWifiInfo.getNetworkId());
            mWifiManager.disconnect();
        }

        //然后是一个实际应用方法，只验证过没有密码的情况：

        public WifiConfiguration CreateWifiInfo(String SSID, String Password, int type) {
            WifiConfiguration config = new WifiConfiguration();
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.SSID = "\"" + SSID + "\"";

            WifiConfiguration tempConfig = this.IsExsits(SSID);
            if (tempConfig != null) {
                mWifiManager.removeNetwork(tempConfig.networkId);
            }

            switch (type)
            {
                case 0://没有密码的情况
                    config.wepKeys[0] = "";
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    config.wepTxKeyIndex = 0;
                    break;
                case 1://WPA_EAP加密的情况
                    config.preSharedKey = "\"" + Password + "\"";
                    config.hiddenSSID = true;
                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    config.status = WifiConfiguration.Status.ENABLED;
                    break;
                case 2://WPA_PSK加密的情况
                    config.preSharedKey = "\"" + Password + "\"";
                    config.hiddenSSID = true;
                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                    config.status = WifiConfiguration.Status.ENABLED;
                    break;
                case 3:
                    //WEP加密的情况
                    config.hiddenSSID = true;
                    config.wepKeys[0] = "\"" + Password + "\"";
                    config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
                    config.wepTxKeyIndex = 0;
                    break;
            }

            return config;
        }

        private WifiConfiguration IsExsits(String SSID) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
            return null;
        }

}
