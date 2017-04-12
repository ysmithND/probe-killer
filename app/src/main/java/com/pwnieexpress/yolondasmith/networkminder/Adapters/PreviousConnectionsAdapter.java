package com.pwnieexpress.yolondasmith.networkminder.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pwnieexpress.yolondasmith.networkminder.R;
import com.pwnieexpress.yolondasmith.networkminder.models.Connections;
import com.pwnieexpress.yolondasmith.networkminder.models.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PreviousConnectionsAdapter extends BaseAdapter {
    private JSONArray mData;
    private Context mContext;
    private Connections mConnections;

    public PreviousConnectionsAdapter (Context context, JSONArray dataBlob, Connections connections){
        mContext = context;
        Log.d("Adapter3", dataBlob.toString());
        mData = dataBlob;
        mConnections = connections;

    }

    @Override
    public int getCount() {
        return mData.length();
    }

    @Override
    public Object getItem(int position) {
        Object bloop = null;
        try {
            bloop = mData.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bloop;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            //brand new
            convertView = LayoutInflater.from(mContext).inflate(R.layout.previous_network_items, null);
            holder = new ViewHolder();
            holder.ssidInfo = (TextView) convertView.findViewById(R.id.prevSsidTextView);
            holder.bssidInfo = (TextView) convertView.findViewById(R.id.prevBssidTextView);
            holder.encryptionInfo = (TextView) convertView.findViewById(R.id.prevNetworkEncryption);
            //holder.evilIcon = (ImageView) convertView.findViewById(R.id.evilIcon);
            holder.dateInfo = (TextView) convertView.findViewById(R.id.prevDateConnectTextView);
            holder.preferredNetworkInfo = (TextView) convertView.findViewById(R.id.prevPrefNetworkTextView);
            holder.currentNetworkIndicator = (TextView) convertView.findViewById(R.id.currentNetworkIndicator);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            Log.d("Adapter", mData.toString());
            JSONArray value = mData.getJSONArray(position);
            //JSONArray seriously = value.getJSONArray(position);

            holder.ssidInfo.setText(value.getString(1));
            Log.d("Adapter4", value.getString(1));
            if (value.getString(1).equals(mConnections.getWifiInfo().getSSID())){
                holder.currentNetworkIndicator.setVisibility(View.VISIBLE);
            }else{
                holder.currentNetworkIndicator.setVisibility(View.INVISIBLE);
            }
            holder.bssidInfo.setText(value.getString(2));
            holder.encryptionInfo.setText(value.getString(3));
            if (value.getString(3).equals("WEP")|| value.getString(3).equals("NONE")){
                holder.encryptionInfo.setTextColor(Color.RED);
            }

            holder.dateInfo.setText(value.getString(4));
            if(value.getBoolean(5)){
                holder.preferredNetworkInfo.setText(R.string.prev_pref_network_positive);
            }else{
                holder.preferredNetworkInfo.setText(R.string.prev_pref_network);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


            /*if(value.getBoolean(6)){
                holder.evilIcon.setVisibility(View.VISIBLE);
            }else {
                holder.evilIcon.setVisibility(View.INVISIBLE);
            }*/

        return convertView;
    }

    private static class ViewHolder {
        TextView ssidInfo;
        //ImageView evilIcon;
        TextView bssidInfo;
        TextView encryptionInfo;
        TextView dateInfo;
        TextView preferredNetworkInfo;
        TextView currentNetworkIndicator;
    }
}
