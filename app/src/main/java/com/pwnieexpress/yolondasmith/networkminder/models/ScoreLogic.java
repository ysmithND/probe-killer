package com.pwnieexpress.yolondasmith.networkminder.models;


import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ScoreLogic {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public JSONObject getData() {
        String fetchData = mSharedPreferences.getString("DATA", "Not found!");
        JSONObject inflateObject = null;
        if (fetchData.equals("Not found!")) {
            try {
                inflateObject = new JSONObject(fetchData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return inflateObject;
    }
    public int countProbes(JSONObject probeObject) throws JSONException {
        return probeObject.length();
    }

    public int findWeakEncryptionConnections(JSONObject jsonObject) throws JSONException {
        JSONArray myArray = jsonObject.names();
        int weakEncryptionFound = 0;
        for (int i = 0; i < myArray.length(); i++){
            JSONArray bloop = jsonObject.getJSONArray(myArray.getString(i));
            if (bloop.getString(3).equals("WEP")){
                weakEncryptionFound++;
            }
        }

        return weakEncryptionFound;
    }

    public int noEncryptionConnections(JSONObject jsonObject) throws  JSONException{
        JSONArray myArray = jsonObject.names();
        int noEncryptionFound = 0;
        for (int i = 0; i < myArray.length(); i++){
            JSONArray bloop = jsonObject.getJSONArray(myArray.getString(i));
            if (bloop.getString(3).equals("NONE")){
                noEncryptionFound++;
            }
        }

        return noEncryptionFound;
    }

    public int weakPreferredNetworks (JSONObject jsonObject) throws JSONException{
        JSONArray myArray = jsonObject.names();
        int weakPreferredNetwork = 0;
        for (int i = 0; i < myArray.length(); i++){
            JSONArray bloop = jsonObject.getJSONArray(myArray.getString(i));
            if (bloop.getString(3).equals("WEP") && bloop.getBoolean(5)){
                weakPreferredNetwork++;
            }
        }

        return weakPreferredNetwork;
    }

    public int confirmedEvilConnections (JSONObject jsonObject) throws JSONException{
        JSONArray myArray = jsonObject.names();
        int evilAPConnection = 0;
        for (int i = 0; i < myArray.length(); i++){
            JSONArray bloop = jsonObject.getJSONArray(myArray.getString(i));
            if (bloop.getBoolean(6)){
                evilAPConnection++;
            }
        }

        return evilAPConnection;
    }
}
