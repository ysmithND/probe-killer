package com.pwnieexpress.yolondasmith.networkminder.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pwnieexpress.yolondasmith.networkminder.Adapters.AvailableNetworksAdapter;
import com.pwnieexpress.yolondasmith.networkminder.R;
import com.pwnieexpress.yolondasmith.networkminder.models.Connections;
import com.pwnieexpress.yolondasmith.networkminder.models.Data;

import java.util.List;

public class AvailableNetworks extends ListActivity {
    private Data mData;
    private Connections mConnections;
    private List<ScanResult> mScanResults;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

//TODO: figure out why navigation drawer not fully working
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_networks);
        mDrawerList = (ListView)findViewById(R.id.navList3);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout3);
        mActivityTitle = getTitle().toString();

        mConnections = new Connections(this);
        mData = new Data(this, mConnections);
        mScanResults = mConnections.getScanResultList();
        ScanResult[] scanResults = mData.makeScanResultArray(mScanResults);
        AvailableNetworksAdapter adapter = new AvailableNetworksAdapter(this, scanResults);
        setListAdapter(adapter);

        addDrawerItems();
        setupDrawer();
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Connections connection = new Connections(this);
        List<ScanResult> results = connection.getScanResultList();
        Data myData = new Data(this, connection);
        ScanResult[] scanResults = myData.makeScanResultArray(results);
        String bssid = scanResults[position].BSSID;
        //askToDelete(netID);

        String stuff = String.format("You would like to connect to the network with BSSID: %s", bssid);
        Toast.makeText(this, stuff, Toast.LENGTH_LONG).show();
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
            Toast.makeText(AvailableNetworks.this, position+"", Toast.LENGTH_LONG).show();
        }
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
