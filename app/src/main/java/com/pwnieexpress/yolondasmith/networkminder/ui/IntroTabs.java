package com.pwnieexpress.yolondasmith.networkminder.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwnieexpress.yolondasmith.networkminder.R;


public class IntroTabs extends AppCompatActivity {
    //set member variables
    private TextView mSettings;
    private TextView mCurrentNetwork;
    private TextView mAvailableNetworks;
    private ImageView mTinyHorse;
    private TextView mRemoveNetworks;
    private TextView mNews;
    private TextView mEnterprise;
    private TextView mDeviceRiskScore;
    private int whichText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_tabs);

        mSettings = (TextView) findViewById(R.id.setttingsTextView);
        mCurrentNetwork = (TextView) findViewById(R.id.currNetwork);
        mAvailableNetworks = (TextView) findViewById(R.id.availNetworks);
        mTinyHorse = (ImageView) findViewById(R.id.tinyHorse);
        mRemoveNetworks = (TextView) findViewById(R.id.removeNetworks);
        mNews = (TextView) findViewById(R.id.news);
        mDeviceRiskScore = (TextView) findViewById(R.id.deviceRiskScore);
        mEnterprise = (TextView) findViewById(R.id.enterprise);

        //add in the one for settings once its built

        //2. Current Networks
        mCurrentNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity(CurrentConnection.class);
            }
        });
        //3. Available Networks
        mAvailableNetworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity(AvailableNetworks.class);
            }
        });
        //4. Clear Stored networks
        mRemoveNetworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity(PreviousConnections.class);
            }
        });
        //5. Device Risk Score
        mDeviceRiskScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity(DeviceScore.class);
            }
        });


    }
    private void getActivity(Class name){
        Intent startConnectionView = new Intent(this, name);
        //startProbeList.putExtra(getString(R.string.key_name), variable);
        startActivity(startConnectionView);
    }
}
