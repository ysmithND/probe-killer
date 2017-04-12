package com.pwnieexpress.yolondasmith.networkminder.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwnieexpress.yolondasmith.networkminder.R;
import com.pwnieexpress.yolondasmith.networkminder.models.Connections;

public class AvailableNetworksAdapter extends BaseAdapter{
    private Context mContext;
    private ScanResult [] mScanResults;

    public AvailableNetworksAdapter (Context context, ScanResult[] results){
        mContext = context;
        mScanResults = results;

    }

    @Override
    public int getCount() {
        return mScanResults.length;
    }

    @Override
    public Object getItem(int position) {
        return mScanResults[position];
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.available_network_items, null);
            holder = new ViewHolder();
            holder.ssidInfo = (TextView) convertView.findViewById(R.id.ssidTextView);
            holder.bssidInfo = (TextView) convertView.findViewById(R.id.bssidTextView);
            holder.encryptionInfo = (TextView) convertView.findViewById(R.id.encryptionTextView);
            holder.evilIcon = (ImageView) convertView.findViewById(R.id.evilIconSmall);
            holder.signalStrengthInfo = (ImageView) convertView.findViewById(R.id.sigStrengthImageView);
            holder.networkRecommender = (TextView) convertView.findViewById(R.id.networkRecommender);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult result = mScanResults[position];
        holder.ssidInfo.setText(result.SSID);
        holder.bssidInfo.setText(result.BSSID);

        //TODO: identify network's running security profile
        if (result.capabilities.contains("WPA2")){
            holder.encryptionInfo.setText(R.string.wpa2_psk);
            holder.encryptionInfo.setTextColor(Color.GREEN);
            holder.networkRecommender.setText(R.string.net_recommendation);
            holder.evilIcon.setVisibility(View.INVISIBLE);
        } else if (result.capabilities.contains("WPA")){
            holder.encryptionInfo.setText(R.string.wpa_psk);
            holder.encryptionInfo.setTextColor(Color.GREEN);
            holder.networkRecommender.setText(R.string.net_recommendation);
            holder.evilIcon.setVisibility(View.INVISIBLE);
        } else if (result.capabilities.contains("EAP")){
            holder.encryptionInfo.setText(R.string.eap);
            holder.encryptionInfo.setTextColor(Color.WHITE);
            holder.networkRecommender.setText(R.string.maybe_good_network);
            holder.evilIcon.setVisibility(View.INVISIBLE);
        }else if (result.capabilities.contains("WEP")){
            holder.encryptionInfo.setText(R.string.wep);
            holder.encryptionInfo.setTextColor(Color.BLACK);
            holder.networkRecommender.setText(R.string.non_secure_network);
            holder.evilIcon.setImageResource(R.mipmap.tiny_thumbs_down);
        }else if (result.capabilities.contains("ESS") || result.capabilities.contains("NONE")){
            holder.encryptionInfo.setText(R.string.no_encryption);
            holder.encryptionInfo.setTextColor(Color.BLACK);
            holder.networkRecommender.setText(R.string.non_secure_network);
            holder.evilIcon.setImageResource(R.mipmap.evil_icon_small);
        } else{
            holder.encryptionInfo.setText(R.string.no_data);
            holder.encryptionInfo.setTextColor(Color.LTGRAY);
            holder.networkRecommender.setText(R.string.no_info_found);
            holder.evilIcon.setImageResource(R.mipmap.evil_icon_small);
        }

        //// TODO: 1/15/17 figure out a way to only show this icon if evilAP returns true

        if (result.level < -100){
            holder.signalStrengthInfo.setImageResource(R.mipmap.poor_signal);
        }else if (result.level >= -99 && result.level <= -60){
            holder.signalStrengthInfo.setImageResource(R.mipmap.fair_signal);
        } else if (result.level >= -59 && result.level <= 0){
            holder.signalStrengthInfo.setImageResource(R.mipmap.good_signal);
        } else {
            holder.signalStrengthInfo.setImageResource(R.mipmap.no_signal);
        }
        return convertView;

    }
    private static class ViewHolder {
        TextView ssidInfo;
        ImageView evilIcon;
        TextView bssidInfo;
        TextView encryptionInfo;
        ImageView signalStrengthInfo;
        TextView networkRecommender;
    }
}
