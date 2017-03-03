package com.goockr.nakedeyeguard.Tools;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashMap;
import java.util.Map;

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

//    public static boolean[] getNetworkState(WifiManager wifiManager, ConnectivityManager cwjManager)
//    {
//        boolean [] netWorkState = new boolean[2];
//        if (wifiManager.isWifiEnabled()==true) //判断是否为wifi模式
//        {
//            //获取wifi下IP地址
//            boolean  isWifiEnabled = wifiManager.isWifiEnabled();
//            netWorkState[0]=isWifiEnabled;
//        }
//        else{
//            NetworkInfo info  = cwjManager.getActiveNetworkInfo();
//            if (info!=null) {
//                boolean  isConnected = info.isConnected();
//                netWorkState[1]=isConnected;
//            }
//        }
//        return netWorkState;
//    }


    //IP地址转换
    private static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

//    public static String getGateway(WifiManager wifiManager, ConnectivityManager cwjManager)
//    {
//        String gateWay ="";
//        if (wifiManager.isWifiEnabled()==true) //判断是否为wifi模式
//        {
//            //获取wifi下网关
//            DhcpInfo dhcpInfo =  wifiManager.getDhcpInfo();
//            int gateway =dhcpInfo.gateway;
//            gateWay= intToIp(gateway);
//        }
//        else{
//            NetworkInfo info  = cwjManager.getActiveNetworkInfo();
//            if (info!=null) {
//                boolean isConnect = info.isAvailable();
//                if (isConnect == true) {
//                    //得到网关
//                    Process process = null;
//                    try {
//                        process = Runtime.getRuntime().exec("su");
//                        DataInputStream dis = new DataInputStream(process.getInputStream());
//                        DataOutputStream dos = new DataOutputStream(process.getOutputStream());
//                        dos.writeBytes("getprop | grep eth0.gateway" + "\n");
//                        dos.flush();
//                        gateWay = dis.readLine();
//                        gateWay = gateWay.split(":")[1];
//                        gateWay = gateWay.substring(2, gateWay.length() - 1);
//
//                    } catch (IOException e) {
//                    }
//                }
//            }
//        }
//        return gateWay;
//    }
//
//    //获取wifi下MAC地址
//    public static String getMac(WifiManager wifiManager)
//    {
//        String Mac= "";
//        if (wifiManager.isWifiEnabled()==true) //判断是否为wifi模式
//        {
//            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//            Mac= wifiInfo.getMacAddress();
//        }
//        else{
//            BufferedReader reader = null;
//            try {
//
//                reader = new BufferedReader(new FileReader(
//                        "sys/class/net/eth0/address"));
//                Mac = reader.readLine();
//
//            } catch (Exception e) {}
//            finally {
//                try {
//                    if (reader != null)
//                        reader.close();
//                } catch (IOException e) {}
//            }
//
//        }
//        return Mac;
//    }
//





}
