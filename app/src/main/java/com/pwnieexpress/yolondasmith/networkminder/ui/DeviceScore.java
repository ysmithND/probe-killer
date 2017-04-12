package com.pwnieexpress.yolondasmith.networkminder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pwnieexpress.yolondasmith.networkminder.R;
import com.pwnieexpress.yolondasmith.networkminder.models.Connections;
import com.pwnieexpress.yolondasmith.networkminder.models.Data;
import com.pwnieexpress.yolondasmith.networkminder.models.ScoreLogic;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceScore extends AppCompatActivity {
    //declare member variables
    private TextView mOverallScore;
    private TextView mRetainedNetworkScore;
    private TextView mWeakApScore;
    private TextView mNoEncryptionScore;
    private TextView mWeakPreferredNetwork;
    private TextView mEvilApScore;
    private TextView mEncourager;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    // Todo: 4. Set up structure to pass this information back to Pulse
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_score);

        //connect variables to layout
        mOverallScore = (TextView) findViewById(R.id.scoreValue);
        mRetainedNetworkScore = (TextView) findViewById(R.id.retainedScore);
        mWeakApScore = (TextView) findViewById(R.id.weakAPScore);
        mNoEncryptionScore = (TextView) findViewById(R.id.noEncryptionScore);
        mWeakPreferredNetwork = (TextView) findViewById(R.id.weakPrefNetScore);
        mEvilApScore = (TextView) findViewById(R.id.evilAPScore);
        mEncourager = (TextView) findViewById(R.id.encouragerView);
        mDrawerList = (ListView)findViewById(R.id.navList2);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout2);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ScoreLogic scoreLogic = new ScoreLogic();
        Connections currentConnection = new Connections(this);
        Data grabData = new Data(this, currentConnection);
        String getSharedPreferencesData = grabData.getData();

        try {
            JSONObject newObject = new JSONObject(getSharedPreferencesData);
            int retainedNetworkScore = scoreLogic.countProbes(newObject);
            mRetainedNetworkScore.setText(retainedNetworkScore + "");
            int weakAPScore = scoreLogic.findWeakEncryptionConnections(newObject);
            mWeakApScore.setText(weakAPScore+"");
            int noEncryptionScore = scoreLogic.noEncryptionConnections(newObject);
            mNoEncryptionScore.setText(noEncryptionScore+"");
            int weakPreferredNetworkScore = scoreLogic.weakPreferredNetworks(newObject);
            mWeakPreferredNetwork.setText(weakPreferredNetworkScore + "");
            int evilApScore = scoreLogic.confirmedEvilConnections(newObject);
            mEvilApScore.setText(evilApScore+"");
            int totalScore = setOverallScore(retainedNetworkScore, noEncryptionScore, noEncryptionScore, evilApScore, weakPreferredNetworkScore);
            setEncourager(totalScore);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void addDrawerItems() {
        String[] optionsArray = { "Current Network", "Available Networks", "Previous Networks",
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
            Intent startNetworkView = new Intent(this, AvailableNetworks.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(startNetworkView);
        }else if (position == 2) {
            Intent previousNetworksView = new Intent(this, PreviousConnections.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(previousNetworksView);
        } else if (position == 3){
            Intent deviceRiskScoreView = new Intent(this, DeviceScore.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(deviceRiskScoreView);
        }else {
            Toast.makeText(DeviceScore.this, position+"", Toast.LENGTH_LONG).show();
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
    private int setOverallScore(int probes, int weakAPs, int noEncryption, int evilAP, int weakPrefNetwork){

        //set the overall score
        int overallScore = 10;
        if(probes > 15){
            overallScore = overallScore - 1;
        } else if (probes >= 35){
            overallScore = overallScore - 1;
        } else if (probes >= 50){
            overallScore = overallScore - 2;
        }else {
            overallScore = overallScore - 0;
        }
        //1 point per infraction
        if (weakAPs > 0){
            overallScore = overallScore - weakAPs;
        }
        //2 points per infraction
        if (noEncryption > 0){
            overallScore = overallScore - (2*noEncryption);
        }
        //2 points per infraction
        if (weakPrefNetwork > 0){
            overallScore = overallScore - (2*weakPrefNetwork);
        }
        //3 points per infraction
        if (evilAP > 0){
            overallScore = overallScore - (3*evilAP);
        }
        mOverallScore.setText(overallScore+"");
        if (overallScore <= 10 && overallScore >= 7){
            mOverallScore.setTextColor(Color.GREEN);
        } else if (overallScore < 7 && overallScore > 4){
            mOverallScore.setTextColor(Color.YELLOW);
        }else {
            mOverallScore.setTextColor(Color.RED);
        }
        return overallScore;
    }
    private void setEncourager(int overallScore){
        if (overallScore == 10){
            mEncourager.setText(R.string.excellent);
        }
        if (overallScore > 5){
            mEncourager.setText(R.string.good_work);
        }
        if (overallScore <= 5 && overallScore >= 2){
            mEncourager.setText(R.string.not_bad);
        }
        if (overallScore < 2){
            mEncourager.setText(R.string.bad_news);
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
}
