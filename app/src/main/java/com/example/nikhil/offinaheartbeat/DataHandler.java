package com.example.nikhil.offinaheartbeat;

import android.util.Log;

import com.androidplot.xy.SimpleXYSeries;

import java.util.Observable;

/**
 * Datahandler class does calculation on heart rate data
 */

public class DataHandler extends Observable{
    private static DataHandler dh = new DataHandler();

    //DATA FOR SAVING
    boolean newValue = true;
    SimpleXYSeries series1;
    //ConnectThread reader;
    ConnectH7 H7;

    int pos=0;
    int val=0;
    int min=0;
    int max=0;

    //for the average maths
    int data=0;
    int total=0;

    int baseline = 0;

    int id;

    private DataHandler(){

    }

    public static DataHandler getInstance(){
        return dh;
    }

    public void acqui(int i){
        if (i==254){
            pos=0;
        }
        else if (pos==5){
            cleanInput(i);
        }
        pos++;
    }

    public void cleanInput(int i){
        val=i;
        Log.d("total",String.valueOf(total));
        if(val!=0){
            data+=val;//Average maths
            total++;//Average maths
        }
        if (total == 10){
            setBaseline();
        }
        if(val<min||min==0)
            min=val;
        else if(val>max)
            max=val;
        setChanged();
        notifyObservers();
    }

    public String getLastValue(){

        return val + " BPM";
    }

    public int getLastIntValue(){

        return val;
    }

    public String getMin(){
        return "Min: " + min;
    }

    public String getMax(){

        return "Max: " + max;
    }

    public String getAvg(){
        if(total==0)
            return "Avg " + 0 + " BPM";
        return "Avg: " + data/total;
    }

    public int getAvgVal(){
        return data/total;
    }


    //Set baseline variable
    public void setBaseline(){
        baseline = data/total;
    }

    public String getBaseline(){
        return "Baseline: " + baseline;
    }

    public int getBaselineValue(){
        return baseline;
    }

    public void setNewValue(boolean newValue) {
        this.newValue = newValue;
    }

    public SimpleXYSeries getSeries1() {
        return series1;
    }

    public void setSeries1(SimpleXYSeries series1) {
        this.series1 = series1;
    }

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id=id;
    }

    public void setH7(ConnectH7 H7){
        this.H7=H7;
    }
    public ConnectH7 getH7(){
        return H7;
    }
}
