package com.example.nikhil.offinaheartbeat;

import java.util.Observable;

public class DataHandler extends Observable{
    private static DataHandler dh = new DataHandler();

    //DATA FOR SAVING
    boolean newValue = true;
    //ConnectThread reader;
    ConnectH7 H7;

    int pos=0;
    int val=0;
    int min=0;
    int max=0;

    //for the average maths
    int data=0;
    int total=0;

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
        if(val!=0){
            data+=val;//Average maths
            total++;//Average maths
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

    public void setNewValue(boolean newValue) {
        this.newValue = newValue;
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


    /*public ConnectThread getReader() {
        return reader;
    }*/

    /*public void setReader(ConnectThread reader) {
        this.reader = reader;
    }*/



}
