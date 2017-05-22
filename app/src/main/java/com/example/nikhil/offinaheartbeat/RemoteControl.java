package com.example.nikhil.offinaheartbeat;

/**
 * Created by kchui on 5/21/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class RemoteControl {

    private Pattern[] patterns;
    private Pattern mute;
    boolean dotransmit = true;
    private String designation;

    protected RemoteControl(String designation, Pattern[] patterns){
        Log.d("rc1","herro");
        this.patterns = patterns;
        this.designation = designation;
    }

    protected RemoteControl(String designation, Pattern[] patterns, Pattern mute){
        Log.d("rc2","herro1");
        this.patterns = patterns;
        this.designation = designation;
        this.mute = mute;
    }


    public static void showWorks(){
        Log.d("t","meow");
    }

    public static void kill(Context c){
        Log.d("k","killing tv");
        int depth = 1;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        if (preferences.getBoolean("depth",false)){
            depth = 2;
        }
//        //Transmit all patterns
        for (int i = 0; i < depth; i++){
            for (RemoteControl r: RemoteControlContainer.getAllBrands()){
                if (r.dotransmit){
                    if (i < r.patterns.length){
                        Log.d("transmitting","woot");
                        r.patterns[i].send(c);
//                        wait(c);
                    }
                }
            }
        }
    }

    //Wait for a certain time to avoid a misinterpretation of the commands when they are sent succecevly
    private static void wait(Context c) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        try {
            Thread.sleep(Long.parseLong(preferences.getString("delay","0")));
        } catch (InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
