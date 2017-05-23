package com.example.nikhil.offinaheartbeat;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import android.hardware.ConsumerIrManager;
import android.widget.Toast;

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

        //Check for the IR-emitter
        ConsumerIrManager  IR = (ConsumerIrManager)getSystemService(CONSUMER_IR_SERVICE);
        if (IR.hasIrEmitter()) {
            //Inform the user about the presence of his IR-emitter
            Toast.makeText(getApplicationContext(),R.string.toast_found,Toast.LENGTH_SHORT).show();
        }
        else {
            //Display a Dialog that tells the user to buy a different phone
            AlertDialog alertDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle(R.string.blaster_dialog_title);
            builder.setMessage(R.string.blaster_dialog_body);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNeutralButton(R.string.learn_more, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.blaster_dialog_more_blaster_url)));
                    startActivity(browserIntent);
                    finish();
                }
            });
            alertDialog=builder.create();
            alertDialog.show();
        }
    }

    public void Next(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
