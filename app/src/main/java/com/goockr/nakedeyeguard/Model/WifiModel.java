package com.goockr.nakedeyeguard.Model;

/**
 * Created by JJT-ssd on 2017/3/2.
 */

public class WifiModel {

    private String wifiName;
    public String getWifiName() {return wifiName;}
    public void setWifiName(String wifiName) {this.wifiName = wifiName;}

    private boolean connectState;
    public boolean isConnectState() {return connectState;}
    public void setConnectState(boolean connectState) {this.connectState = connectState;}

    private boolean wifiLock;
    public boolean isWifiLock() {return wifiLock;}
    public void setWifiLock(boolean wifiLock) {this.wifiLock = wifiLock;}


}
