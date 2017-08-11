package com.goockr.nakedeyeguard.data.Model;

/**
 * Created by JJT-ssd on 2017/3/2.
 * WIFI模型
 */

public class WifiModel {
    public enum SecurityType{
        NONE,WPA_EAP,WPA_PSK,WEP
    };

    private String wifiName;
    public String getWifiName() {return wifiName;}
    public void setWifiName(String wifiName) {this.wifiName = wifiName;}

    private boolean connectState;
    public boolean isConnectState() {return connectState;}
    public void setConnectState(boolean connectState) {this.connectState = connectState;}

    private boolean wifiLock;
    public boolean isWifiLock() {return wifiLock;}
    public void setWifiLock(boolean wifiLock) {this.wifiLock = wifiLock;}

    public SecurityType getSecurityType() {return securityType;}
    public void setSecurityType(SecurityType securityType) {this.securityType = securityType;}
    private SecurityType securityType;

}
