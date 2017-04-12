package com.pwnieexpress.yolondasmith.networkminder.models;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class SecurityInfo {
    private List<ScanResult> mAvailableNetworks;
    private List<WifiConfiguration> mConfiguredNetworks;
    private String encryption;
    private String returnStatement;

    public SecurityInfo(Connections connection) {
        mAvailableNetworks = connection.getScanResultList();
        mConfiguredNetworks = connection.getNetworkList();
    }
    public String getEncryption(Connections currentConnection) {
        String mBSSID = currentConnection.getWifiInfo().getBSSID();
        for(ScanResult config : mAvailableNetworks){
            if (mBSSID.equals(config.BSSID)){
                if (config.capabilities.contains("WPA2")) {
                    encryption = "WPA2_PSK";}
                else if (config.capabilities.contains("WPA")) {
                    encryption = "WPA_PSK";}
                else if (config.capabilities.contains("WPA_EAP") || config.capabilities.contains("IEEE8021X")) {
                    encryption = "EAP";}
                else if (config.capabilities.contains("WEP")){
                    encryption = "WEP";}
                else if (config.capabilities.contains("ESS")){
                    encryption = "NONE";}
                else{ encryption = "No Data Found";}
            }
        }
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }
    public String getSecurityCapabilities(Connections currentConnection)
    {
        String mBSSID = currentConnection.getWifiInfo().getBSSID();

        for (ScanResult result : mAvailableNetworks){
            if(mBSSID.equals(result.BSSID)){
                if(result.capabilities.contains("WEP")){
                    returnStatement = "The network you are connected to is operating at a lower security level than what is capable. " +
                            "Strongly encrypted networks make it much harder for attackers to eavesdrop on private connections. " +
                            "You should switch to a network with higher encryption, utilize additional security measures such as VPN or " +
                            "refrain from conducting sensitive transactions (banking, purchases, email, etc) until you can connect to a safer network. ";
                }
                else if (result.capabilities.contains("PSK")){
                    returnStatement = "You good fam! Check out www.pwnieexpress.com to get more tips to keep your devices and data secure!";
                }
                else if (result.capabilities.contains("EAP")){
                    returnStatement = "The network you are connected to uses certificates to validate network authorization. " +
                            "If you trust the certificate issuer, you should be fine to continue to operate. Otherwise, you should refrain from conducting " +
                            "sensitive transactions (banking, payments, email)" +
                            "until you can validate the certificate's authenticity";
                }
                else {
                    returnStatement = "The network you are connected to is operating with no encryption enabled. Strongly encrypted networks make it much harder " +
                            "for attackers to eavesdrop on private connections. You should switch to a network with higher encryption, utilize additional " +
                            "security measures such as VPN or refrain from conducting sensitive transactions (banking, purchases, email, etc) " +
                            "until you can connect to a safer network.";
                }

                if (result.BSSID.contains("00:13:37") || result.SSID.contains("VoCore") || result.SSID.contains("r00tabega")
                        || result.SSID.contains("Pineapple") || result.SSID.contains("FON_AP") || result.SSID.contains("MyPlace")){
                    returnStatement = "There is a malicious WiFi device in your immediate area. You should discontinue use of WiFi until further notice";
                }
            }
        }
        return returnStatement;
    }



}
