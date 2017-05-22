package com.example.nikhil.offinaheartbeat;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by kchui on 5/21/17.
 */

public class Pattern {
    private int[] pattern;
    private int frequency;
    private boolean converted=false;

    //This method converts the remotes pattern, if this has not been done yet, and passes the converted pattern to the initiate-method.
    public void send(Context c) {
        //Log.d("in pattern","send function");
        if (!converted) {
            pattern = convert(pattern, frequency);
            converted = true;
            //Log.d("converted","CONVERTED");
        }
        initiate(c);
    }

    //This method initiates the remote's transmission.
    private void initiate(Context c) {
        Transmitter remote = new Transmitter(frequency, pattern);
        //Log.d("created transmitter","transmitter");
        remote.transmit(c);
    }


    protected Pattern(int frequency, int[] pattern){
        this.frequency=frequency;
        this.pattern=pattern;
    }

    protected Pattern(int[] ircode){
        this.frequency=ircode[0];
        this.pattern= Arrays.copyOfRange(ircode, 1, ircode.length);
    }
    /*
* The following method preforms calculations on the IR-codes in order to make them work on certain devices.
*
* Patterns have to be converted on devices running on Android Lollipop or newer and HTC devices.
* Certain Samsung devices also require a converted PAttern,depending on their Android version.
* Older devices including Samsung devices running on an android version older than 4.4.3 can use the unconverted patterns, in that case, this method simply returns the input data.
*
*/
    private static int[] convert(int[] irData, int frequency) {

        // 1. Determine which conversion-algorithm shall be used (see the comment above this method for more details)
        byte convert = 0;
        //Devices running on Android Lollipop (Android 5.0.1 (API 21)) and beyond, HTC devices
        if (Build.VERSION.SDK_INT >= 21||Build.MANUFACTURER.equalsIgnoreCase("HTC")) {
            convert = 1;
        } else {
            //Samsung devices running on anything lower than Android 5
            if (Build.MANUFACTURER.equalsIgnoreCase("SAMSUNG")) {
                int lastIdx = Build.VERSION.RELEASE.lastIndexOf(".");
                int VERSION_MR = Integer.valueOf(Build.VERSION.RELEASE.substring(lastIdx + 1));
                if (VERSION_MR < 3) {
                    // Samsung devices with Android-version before Android 4.4.2
                    //-> no calculations required
                    convert = 0;
                } else {
                    // Later version of Android 4.4.3
                    //-> use the special Samsung-formula
                    convert = 2;
                }
            }
        }

        // 2. Convert the patterns
        if (convert != 0) {
            for (int i = 0; i < irData.length; i++) {
                switch (convert) {
                    case 1:
                        irData[i] = irData[i] * (1000000 / frequency);
                        break;
                    case 2:
                        irData[i] = (int) Math.ceil(irData[i] * 26.27272727272727); //converted as suggested by Samsung: http://developer.samsung.com/android/technical-docs/Workaround-to-solve-issues-with-the-ConsumerIrManager-in-Android-version-lower-than-4-4-3-KitKat
                        break;
                }
            }
        }
        return irData;

    }
}

