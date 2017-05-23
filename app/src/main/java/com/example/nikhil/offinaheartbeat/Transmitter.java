package com.example.nikhil.offinaheartbeat;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;

/**
 * Created by kchui on 5/21/17.
 */

public class Transmitter {
    int frequency;
    int[] pattern;

    Transmitter(int frequency, int[] pattern) {
        this.frequency=frequency;
        this.pattern=pattern;
    }
    public void transmit(Context context) {

        ConsumerIrManager IR = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);
        //Log.d("transmitting","transmitting");

        IR.transmit(frequency, pattern);
        //Log.d("done transmitting","done transmitting");
    }
}

