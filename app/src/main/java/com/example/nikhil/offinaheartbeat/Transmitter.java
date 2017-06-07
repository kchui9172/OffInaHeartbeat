package com.example.nikhil.offinaheartbeat;

import android.content.Context;
import android.hardware.ConsumerIrManager;

/**
 * Created by kchui on 5/21/17.
 */

/**
 * Transmitter class uses COnsumerIrManager object to transmit particular IR pattern
 */
public class Transmitter {
    int frequency;
    int[] pattern;

    Transmitter(int frequency, int[] pattern) {
        this.frequency=frequency;
        this.pattern=pattern;
    }

    //Transmits IR pattern
    public void transmit(Context context) {
        ConsumerIrManager IR = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);
        IR.transmit(frequency, pattern);
    }
}

