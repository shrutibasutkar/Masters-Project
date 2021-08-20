//COMBINE CODE OF WI-FI SCANNER ALONG WITH LISTENER

package com.example.wifiscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        buttonScan = findViewById(R.id.scanBtn);
          buttonScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                scanWifi();
            }

        });*/

        listView = findViewById(R.id.wifiList);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        scanWifi();

        addTouchListener(); // Touch listener for image
    }

    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            results = wifiManager.getScanResults();
            //WifiInfo wifiInfo = wifiManager.getConnectionInfo(); // for wifiinfo to work on RSSI value
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                //WifiInfo wifiInfo = wifiManager.getConnectionInfo(); // for wifiinfo to work on RSSI value
                arrayList.add(scanResult.SSID + " : " + scanResult.level);
                //arrayList.add(scanResult.SSID + " : " + wifiInfo.getRssi());
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void addTouchListener(){
        ImageView image = (ImageView) findViewById(R.id.touch_image);
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = (int) event.getX();
                float y = (int) event.getY();
                //String msg  = String.format("co-ordinate: (%.2f, %.2f)", x, y);
                //Log.d(MainActivity.DEBUGTAG, msg);
                System.out.println("column " +x + " row "+ y );
                TextView coordinate = findViewById(R.id.textView);
                coordinate.setText("column " +x + " row "+ y);

                // trying on touch listener to call scanwifi method
                scanWifi();
                return false;
            }
        });
    }
}