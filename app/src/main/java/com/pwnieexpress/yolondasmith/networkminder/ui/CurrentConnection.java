package com.pwnieexpress.yolondasmith.networkminder.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pwnieexpress.yolondasmith.networkminder.R;
import com.pwnieexpress.yolondasmith.networkminder.models.Connections;
import com.pwnieexpress.yolondasmith.networkminder.models.Data;
import com.pwnieexpress.yolondasmith.networkminder.models.SecurityInfo;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentConnection extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //member variable declaration
    private TextView mNetNameExplainer;
    private TextView mMacAddress;
    private TextView mIpAddress;
    private TextView mVendorLookUp;
    private ImageView mEvilNetworkIcon;
    private TextView  mEncryptionExplainer;
    private ImageView mSignalStrengthIndicator;
    private TextView mLinkSpeedData;
    private TextView mPreferredNetworkExplainer;
    private TextView mEvilNetworkExplainer;
    private TextView mNetworkGrader;
    private TextView mNetworkChanges;
    private TextView mTip1;
    private TextView mTip2;
    private TextView mTip3;
    private TextView mNumberRiskPointsEarned;
    private Data mData;
    private Connections mCurrentConnectionInfo;
    private TextView mNoDataTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;


//Todo: 1. add a refresh button or option
//Todo: 2. make sure that there's a way to detect if wifi is turned off and, if so, provide some message to the user to that effect
//Todo: 5. Allow users to set up home/preferred networks


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_connection);

        //link the member variables to the layout variables
        mNetNameExplainer = (TextView) findViewById(R.id.netNameExplainer);
        mMacAddress = (TextView) findViewById(R.id.macAddress);
        mIpAddress = (TextView) findViewById(R.id.ipAddress);
        mVendorLookUp = (TextView) findViewById(R.id.vendorLookUp);
        mEvilNetworkIcon = (ImageView) findViewById(R.id.evilNetworkIcon);
        mEncryptionExplainer = (TextView) findViewById(R.id.encryptionExplainerView);
        mSignalStrengthIndicator = (ImageView) findViewById(R.id.signalStrengthIndicator);
        mLinkSpeedData = (TextView) findViewById(R.id.linkSpeedData);
        mPreferredNetworkExplainer = (TextView) findViewById(R.id.preferredNetworkExplainerView);
        mEvilNetworkExplainer = (TextView) findViewById(R.id.evilNetworkExplainer);
        mNetworkGrader = (TextView) findViewById(R.id.networkGrader);
        mNetworkChanges = (TextView) findViewById(R.id.networkChanges);
        mTip1 = (TextView) findViewById(R.id.tip1);
        mTip2 = (TextView) findViewById(R.id.tip2);
        mTip3 = (TextView) findViewById(R.id.tip3);
        mNumberRiskPointsEarned = (TextView) findViewById(R.id.numberRiskPointsEarned);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();
        //start setting these variables
        mCurrentConnectionInfo = new Connections(this);
        mData = new Data(this, mCurrentConnectionInfo);
        String dataBlob = mData.getData();
        //Toast.makeText(CurrentConnection.this, dataBlob, Toast.LENGTH_LONG).show();
        List<ScanResult> scanResults = mCurrentConnectionInfo.getScanResultList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //check to see if wifi is available
        if(mCurrentConnectionInfo.getWifiManager().isWifiEnabled()) {
            String ssid = mCurrentConnectionInfo.getWifiInfo().getSSID();
            String bssid = mCurrentConnectionInfo.getWifiInfo().getBSSID();
            SecurityInfo securityInfo = new SecurityInfo(mCurrentConnectionInfo);
            String encryption = securityInfo.getEncryption(mCurrentConnectionInfo);

            mNetNameExplainer.setText(ssid);
            String ipAddr = Formatter.formatIpAddress(mCurrentConnectionInfo.getWifiInfo().getIpAddress());
            mMacAddress.setText(mCurrentConnectionInfo.getWifiInfo().getMacAddress());
            mIpAddress.setText(ipAddr);
            String mURL = "http://api.macvendors.com/" + bssid;
            OkHttpClient client = new OkHttpClient();
            Request mRequest = new Request.Builder().url(mURL).build();
            Call call = client.newCall(mRequest);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String fail = "No data returned";
                    Log.d("Failure", fail);
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        String mAnswer = response.body().string();
                        mVendorLookUp.setText(mAnswer);
                    }
                }
            });

            mLinkSpeedData.setText(mCurrentConnectionInfo.getWifiInfo().getLinkSpeed()+"");

            int [] networkComparisons = mCurrentConnectionInfo
                                        .compareNetworks(mCurrentConnectionInfo.getScanResultList(), encryption);
            //TODO: finish up the copy for this section
            mEncryptionExplainer.setText(encryption);

            String changeData = null;
            try {
                changeData = mCurrentConnectionInfo.checkProfileChanges(encryption, dataBlob, ssid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mNetworkChanges.setText(changeData);

            if (encryption.equals("WPA2_PSK")){
                mEncryptionExplainer.setTextColor(Color.GREEN);
                mEvilNetworkIcon.setImageResource(R.mipmap.tiny_thumbs_up);
                mNetworkGrader.setText("There are "+networkComparisons[0]+" surrounding " +
                        "networks which have equal or stronger protections than the current network");
                mTip1.setText(R.string.remove_network_tip);
                mTip2.setText(R.string.tip2);
                mTip3.setText(R.string.tip3);

            } else if (encryption.equals("WPA_PSK")){
                mEncryptionExplainer.setTextColor(Color.GREEN);
                mEvilNetworkIcon.setImageResource(R.mipmap.tiny_thumbs_up);
                mNetworkGrader.setText("There are "+networkComparisons[3]+" surrounding " +
                        "networks which have equal or stronger protections than the current network");
                mTip1.setText(R.string.tip1);
                mTip2.setText(R.string.tip2);
                mTip3.setText(R.string.tip3);
            }
            else if (encryption.equals("EAP")){
                mEncryptionExplainer.setTextColor(Color.WHITE);
                mEvilNetworkIcon.setImageResource(R.mipmap.tiny_thumbs_up);
                mNetworkGrader.setText("There are "+networkComparisons[2]+" surrounding " +
                        "networks which have equal or stronger protections than the current network");
                mTip1.setText(R.string.cert_check);
                mTip2.setText(R.string.tip3);
                mTip3.setText(R.string.tip2);
            }
            else if (encryption.equals("WEP")){
                mEncryptionExplainer.setTextColor(Color.RED);
                mEvilNetworkIcon.setImageResource(R.mipmap.tiny_thumbs_down);
                mNetworkGrader.setText("There are "+networkComparisons[1]+" surrounding " +
                        " networks which have equal or stronger protections than the current network");
                mTip1.setText(R.string.tip1);
                mTip2.setText(R.string.tip2);
                mTip3.setText(R.string.remove_network_tip);
            }
            else {
                mEncryptionExplainer.setTextColor(Color.RED);
                mNetworkGrader.setText("There are "+mCurrentConnectionInfo.getScanResultList().size()+" surrounding " +
                        "networks which have equal or stronger protections than the current network");
                mEvilNetworkIcon.setImageResource(R.mipmap.tiny_thumbs_down);
                mTip1.setText(R.string.tip1);
                mTip2.setText(R.string.tip3);
                mTip3.setText(R.string.remove_network_tip);
            }
            if(mCurrentConnectionInfo.evilAPAround()){
                mEncryptionExplainer.setText("WARNING");
                mNetworkGrader.setText(R.string.evil_ap_in_area);
                mNetworkChanges.setText(R.string.evilAPNextStepsExplainer);
                mTip1.setText(R.string.stop_transactions);
                mTip2.setText(R.string.kill_wifi);
                mTip3.setText(R.string.tell_owner);
                mEvilNetworkIcon.setImageResource(R.mipmap.evil_icon);
            }
            int rssi = mCurrentConnectionInfo.getWifiInfo().getRssi();
            if (rssi < -100) {
                mSignalStrengthIndicator.setImageResource(R.mipmap.poor_signal);
            } else if (rssi >= -99 && rssi <= -60){
                mSignalStrengthIndicator.setImageResource(R.mipmap.fair_signal);
            } else if (rssi >= -59 && rssi <= 0){
                mSignalStrengthIndicator.setImageResource(R.mipmap.good_signal);
            }else{
                mSignalStrengthIndicator.setImageResource(R.mipmap.no_signal);
            }
            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

        }
    }
    private void addDrawerItems() {
        String[] optionsArray = { "Current Network", "Available Networks","Previous Networks",
                "My Device Risk Score", "Settings", "Learn About WiFi Risks",
                "About Network Minder", "About Pwnie Express" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionsArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getNextActivity(position);
                //Toast.makeText(CurrentConnection.this, position+"", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getNextActivity(int position){
        if (position == 0){
            Intent startConnectionView = new Intent(this, CurrentConnection.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(startConnectionView);
        }else if (position == 1){
            //Toast.makeText(CurrentConnection.this, position+"", Toast.LENGTH_LONG).show();
            Intent startNetworkView = new Intent(this, AvailableNetworks.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(startNetworkView);
        }else if (position == 2) {
            //Toast.makeText(CurrentConnection.this, position+"", Toast.LENGTH_LONG).show();
            Intent previousNetworksView = new Intent(this, PreviousConnections.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(previousNetworksView);
        } else if (position == 3){
            Intent deviceRiskScoreView = new Intent(this, DeviceScore.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(deviceRiskScoreView);
        }else {
            Toast.makeText(CurrentConnection.this, position+"", Toast.LENGTH_LONG).show();
        }


    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        Connections connection = new Connections(this);
        SecurityInfo securityInfo = new SecurityInfo(connection);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        boolean isPreferred = false;

        //set my data blob
        try {
            Data mData = new Data(this, connection);

            mData.setData(this, connection, securityInfo.getEncryption(connection),
                    Data.getConnectionDate(), mLastLocation, isPreferred);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
