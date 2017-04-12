package com.pwnieexpress.yolondasmith.networkminder.models;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Data {
    private Connections mConnections;
    private SecurityInfo mSecurityInfo;
    private Date mAccessDate;
    private SharedPreferences mDataPreferences;
    private SharedPreferences.Editor mDataPreferencesEditor;
    private List<ScanResult> mScanResultList;
    private List<WifiConfiguration> mNetworkList;
    private boolean removed;

    public Data(Context context, Connections connections) {
        mConnections = connections;
        mDataPreferences = context.getSharedPreferences("DATA2", Context.MODE_PRIVATE);
        mDataPreferencesEditor = mDataPreferences.edit();
        mScanResultList = mConnections.getScanResultList();
        mNetworkList = mConnections.getNetworkList();
    }

    public WifiConfiguration[] makeWifiConfigArray(){
        WifiConfiguration[] configuration = new WifiConfiguration[mNetworkList.size()];
        mNetworkList.toArray(configuration);
        return configuration;
    }

    public boolean removeData (int netID, String key, String dataBlob) throws JSONException {
        removed = mConnections.getWifiManager().removeNetwork(netID);
        mConnections.getWifiManager().saveConfiguration();
        //JSONObject jsonObject = new JSONObject(dataBlob);
        //String newString = jsonObject.remove(key).toString();
        //mDataPreferencesEditor.putString("DATA", newString).apply();
        return removed;
    }
    public ScanResult[] makeScanResultArray(List<ScanResult> scanResults){
        ScanResult[] availableNetworks = new ScanResult[scanResults.size()];
        mScanResultList.toArray(availableNetworks);
        return availableNetworks;
    }
    public String getData() {
        return mDataPreferences.getString("DATA2", "Not found!");
    }

    public void setData(Context context, Connections currentConnection, String encryption, String dateConnected,
                        Location locationConnected, boolean preferred) throws JSONException {
        String connectedSSID = currentConnection.getWifiInfo().getSSID();
        String connectedBSSID = currentConnection.getWifiInfo().getBSSID();
        int connectedNetID = currentConnection.getWifiInfo().getNetworkId();

        boolean evilCheck = false;
        if (currentConnection.evilAPAround() && connectedBSSID.contains("00:13:37")|| connectedSSID.contains("VoCore")
                || connectedSSID.contains("r00tabega") || connectedSSID.contains("Pineapple")
                || connectedSSID.contains("FON_AP") || connectedSSID.contains("MyPlace")) {
            evilCheck = true;
        }

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(connectedNetID);
        jsonArray.put(connectedSSID);
        jsonArray.put(connectedBSSID);
        jsonArray.put(encryption);
        jsonArray.put(dateConnected);
        jsonArray.put(preferred);
        jsonArray.put(evilCheck);
        jsonArray.put(locationConnected);

        JSONObject dataObject = new JSONObject();
        String fetchData = getData();

        if (fetchData.equals("Not found!")){
            try{
                dataObject.put(connectedSSID, jsonArray);
                String flattenObject = dataObject.toString();

                mDataPreferencesEditor.putString("DATA2", flattenObject).apply();
            } catch (JSONException e){
                e.printStackTrace();
            }
        } else{
            JSONObject fetchedString = new JSONObject(fetchData);
            fetchedString.put(connectedSSID, jsonArray);
            String reflattenObject = fetchedString.toString();
            //Toast.makeText(context, reflattenObject, Toast.LENGTH_LONG).show();
            mDataPreferencesEditor.putString("DATA2", reflattenObject).apply();

        }
    }

    public static String getConnectionDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM d yyyy, h:mm a z");
        return dateFormat.format(new Date());
    }
}

