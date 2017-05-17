package com.example.nikhil.offinaheartbeat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class SmartDeviceSetup extends AppCompatActivity {

//    BluetoothAdapter mBluetoothAdapter;
//    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
//    public DeviceListAdapter mDeviceListAdapter;
//    List<BluetoothDevice> pairedDevices = new ArrayList<>();
    ListView lvSmartDevices;
//    boolean H7 = false;
    private Spinner spinnerSmart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_device_setup);
    }

    public void Next(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
