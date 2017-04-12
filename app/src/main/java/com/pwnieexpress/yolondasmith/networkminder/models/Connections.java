package com.pwnieexpress.yolondasmith.networkminder.models;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Connections{
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<WifiConfiguration> mNetworkList;
    private List<ScanResult> mScanResultList;
    private Data mData;
    private ConnectivityManager mConnectivityManager;
    private static final String NONE = "NONE";
    private static final String POOR = "WEP";
    private static final String FAIR = "EAP";
    private static final String BETTER = "WPA-PSK";
    private static final String BEST = "WPA2-PSK";


    //Constructor
    public Connections(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public boolean checkIfWiFiAvailable(Context context){

        boolean available;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            available = true;
        }
        else { available = false;}

        return available;
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    public WifiConfiguration buildConfig(){
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "\"network\""; //IMP! This should be in Quotes!!
        wc.hiddenSSID = true;
        wc.status = WifiConfiguration.Status.DISABLED;
        wc.priority = 40;
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wc.wepKeys[0] = "\"aaabbb1234\""; //This is the WEP Password
        wc.wepTxKeyIndex = 0;
        return wc;
    }

    public void setWifiManager(WifiManager wifiManager) {
        mWifiManager = wifiManager;

    }

    public WifiInfo getWifiInfo() {
        mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        mWifiInfo = wifiInfo;
    }

    public List<WifiConfiguration> getNetworkList() {
        mNetworkList = mWifiManager.getConfiguredNetworks();
        return mNetworkList;
    }

    public void setNetworkList(List<WifiConfiguration> networkList) {
        mNetworkList = networkList;
    }

    public List<ScanResult> getScanResultList() {
        mScanResultList = mWifiManager.getScanResults();
        return mScanResultList;
    }

    public int[] compareNetworks(List<ScanResult> resultList, String encryption){
        int poorComparison = 0;
        int fairComparison = 0;
        int betterComparison = 0;
        int equality = 0;

        for (ScanResult result : resultList){
            if (result.capabilities.contains(encryption)){
                equality++;
            }
            Log.d("Variable", equality+"");
            if ((encryption.equals(POOR)|| encryption.equals(NONE)) && (result.capabilities.contains(FAIR) || result.capabilities.contains(BETTER)
                                             || result.capabilities.contains(BEST))){
                poorComparison++;
            }
            if (encryption.equals(FAIR) && (result.capabilities.contains(BETTER)
                    || result.capabilities.contains(BEST))){
                fairComparison++;
            }
            if (encryption.equals(BETTER) && result.capabilities.contains(BEST)){
                betterComparison++;
            }
        }
        int[] allComparisons = {equality, poorComparison, fairComparison, betterComparison};
        return allComparisons;
    }
    public String checkProfileChanges(String encryption, String oldData, String ssid) throws JSONException {
        String value = "";
        if (oldData !="Not found!") {
            JSONObject jsonObject = new JSONObject(oldData);
            JSONArray jsonArray = jsonObject.getJSONArray(ssid);

            if (encryption == jsonArray.getString(3)) {
                value = "No change to this network";
            } else {
                value = "This network's security profile has changed since the last time you connected. This is unusual";
            }
        }
        return value;
    }
    public void setScanResultList(List<ScanResult> scanResultList) {
        mScanResultList = scanResultList;
    }

    public boolean evilAPAround() {
        boolean mEvilAP = false;

        for (ScanResult result : mScanResultList){
            if (result.BSSID.contains("00:13:37") || result.SSID.contains("VoCore") || result.SSID.contains("r00tabega")
                    || result.SSID.contains("Pineapple") || result.SSID.contains("FON_AP") || result.SSID.contains("MyPlace")){
                mEvilAP = true;
            }
        }
        return mEvilAP;
    }

}
