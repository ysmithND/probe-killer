package com.pwnieexpress.yolondasmith.networkminder.ui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pwnieexpress.yolondasmith.networkminder.Adapters.PreviousConnectionsAdapter;
import com.pwnieexpress.yolondasmith.networkminder.R;
import com.pwnieexpress.yolondasmith.networkminder.models.Connections;
import com.pwnieexpress.yolondasmith.networkminder.models.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PreviousConnections extends ListActivity {

    public Data mData;
    //private Context mContext;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private Connections mConnections;
    //private WifiConfiguration[] mConfigList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_connections);
        mDrawerList = (ListView)findViewById(R.id.navList4);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout4);
        mActivityTitle = getTitle().toString();

        mConnections = new Connections(this);
        mData = new Data(this, mConnections);
        //mConnections.buildConfig();
        String previousConnections = mData.getData();

        try {
            JSONObject jsonObject = new JSONObject(previousConnections);
            Log.d("Adapter2", jsonObject.toString());
            JSONArray keys = jsonObject.names();
            JSONArray jsonArray = jsonObject.toJSONArray(keys);

            PreviousConnectionsAdapter adapter = new PreviousConnectionsAdapter(this, jsonArray, mConnections);
            setListAdapter(adapter);
            } catch (JSONException e) {
            e.printStackTrace();
        }
        addDrawerItems();
        setupDrawer();
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
            Intent startNetworkView = new Intent(this, AvailableNetworks.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(startNetworkView);
        }else if (position == 2) {
            Intent previousNetworksView = new Intent(this, PreviousConnections.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(previousNetworksView);
        }else if (position == 3){
            Intent deviceRiskScoreView = new Intent(this, DeviceScore.class);
            //startProbeList.putExtra(getString(R.string.key_name), variable);
            startActivity(deviceRiskScoreView);
        }else {
            Toast.makeText(PreviousConnections.this, position+"", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Connections myConnection = new Connections(this);
        Data myData = new Data(this, myConnection);
        String previousConnections = myData.getData();

        try {
            JSONObject newJson = new JSONObject(previousConnections);
            JSONArray keys = newJson.names();
            JSONArray jsonArray = newJson.toJSONArray(keys);
            PreviousConnectionsAdapter adapter = new PreviousConnectionsAdapter(this,jsonArray,myConnection);
            Object myObject = adapter.getItem(position);
            //Toast.makeText(this, previousConnections, Toast.LENGTH_LONG).show();
            //pass this to a new function in data that removes the network and the data blob
            String myString = myObject.toString();
            JSONArray json = new JSONArray(myString);
            String key = json.getString(1);
            int netID = json.getInt(0);
            boolean removed = myData.removeData(netID, key, previousConnections);
            //Toast.makeText(this, removed+"", Toast.LENGTH_LONG).show();
            if(removed){
                Toast.makeText(this, key + " was removed from your list", Toast.LENGTH_LONG).show();
                this.recreate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //String stuff = String.format("This is the value of the test variable: %s", );
        //Toast.makeText(this, stuff, Toast.LENGTH_LONG).show();
    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
