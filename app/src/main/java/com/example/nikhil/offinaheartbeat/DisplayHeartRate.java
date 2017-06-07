package com.example.nikhil.offinaheartbeat;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.*;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;


/**
 * DisplayHeartRate activity shows heart rate values and graph
 * Contains code to change light state and turn off TV
 */

public class DisplayHeartRate extends AppCompatActivity implements Observer {

    private XYPlot dynamicPlot;
    private int MAX_SIZE = 20; //max size of graph
    private boolean baselineSet = false;
    private int baselineVal = 0;

    //Boolean values to control lighting and tv - once triggered once, don't flip again
    private boolean isDimmedLight75 = false;
    private boolean isDimmedLight50 = false;
    private boolean isDimmedLight25 = false;
    private boolean isTurnedOffLight = false;
    private boolean isTurnedOffTV = false;

    private PHHueSDK phHueSDK;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // android boilerplate stuff
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_heart_rate);
        DataHandler.getInstance().addObserver(this);

        //to control lights
        phHueSDK = phHueSDK.create();

        // get handles to our View defined in layout.xml:
        dynamicPlot = (XYPlot) findViewById(R.id.dynamicPlot);

        Number[] series1Numbers = {};
        DataHandler.getInstance().setSeries1(new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Heart Rate"));
        DataHandler.getInstance().setNewValue(false);

        //LOAD Graph
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.argb(255,255, 64, 129), null, null, null);
        series1Format.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        series1Format.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(DataHandler.getInstance().getSeries1(), series1Format);

        dynamicPlot.getGraph().getGridBackgroundPaint().setColor(Color.argb(255,212,212,212));
        dynamicPlot.getGraph().getBackgroundPaint().setColor(Color.argb(255,47,50,122));

        // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[] {PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        dynamicPlot.getGraph().getDomainGridLinePaint().setPathEffect(dashFx);
        dynamicPlot.getGraph().getRangeGridLinePaint().setPathEffect(dashFx);

        //make sure light is on
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightstate = new PHLightState();
            lightstate.setOn(true);
            lightstate.setBrightness(254);
            bridge.updateLightState(light, lightstate, listener);
        }
    }


    //This method returns a ProgressDialog
    public static ProgressDialog getProgressDialog(Context c) {
        return ProgressDialog.show(c, c.getString(R.string.pd_transmission_in_progress), c.getString(R.string.pd_please_wait), true, false);
    }

    //Changes light state based on dim value passed as parameter
    public void controlLights(int dimValue) {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightstate = new PHLightState();
            //turn off light
            if (dimValue == 0){
                lightstate.setOn(false);
            }
            else{
                lightstate.setBrightness(dimValue);
            }

            bridge.updateLightState(light, lightstate, listener);
        }
    }

    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w("tag", "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };

    @Override
    public void update(Observable o, Object arg) {
        receiveData();
    }

    public void receiveData() {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView hr = (TextView) findViewById(R.id.hr);
                hr.setText(DataHandler.getInstance().getLastValue());

                if (DataHandler.getInstance().getLastIntValue() != 0) {
                    DataHandler.getInstance().getSeries1().addLast(0, DataHandler.getInstance().getLastIntValue());
                    if (DataHandler.getInstance().getSeries1().size() > MAX_SIZE)
                        DataHandler.getInstance().getSeries1().removeFirst();//Prevent graph to overload data.
                    dynamicPlot.redraw();
                }

                TextView min = (TextView) findViewById(R.id.min);
                min.setText(DataHandler.getInstance().getMin());

                TextView avg = (TextView) findViewById(R.id.avg);
                avg.setText(DataHandler.getInstance().getAvg());

                TextView max = (TextView) findViewById(R.id.max);
                max.setText(DataHandler.getInstance().getMax());

                //if baseline value not set yet
                if (baselineSet == false && DataHandler.getInstance().getBaselineValue() != 0){
                    TextView baselineValue = (TextView) findViewById(R.id.baselineHR);
                    baselineValue.setText(DataHandler.getInstance().getBaseline());
                    TextView nextInstruction = (TextView) findViewById(R.id.changeNotification);
                    nextInstruction.setText("Monitoring your heart rate now");
                    baselineSet = true;
                    baselineVal = DataHandler.getInstance().getBaselineValue();
                }


                //once baseline heart rate has been set
                if (baselineSet){
                    //if heart rate drops past certain point, start dimming lights
                    //once drop by 5 bpm, dim lights to 75%
                    if (isDimmedLight75 == false && DataHandler.getInstance().getAvgVal() <= (baselineVal - 5)){
                        TextView change = (TextView) findViewById(R.id.changeNotification);
                        change.setText("Dimming lights to 75%");
                        isDimmedLight75 = true;
                        controlLights(191);
                    }
                    //if drop by 10 bpm, dim lights to 50%
                    if (isDimmedLight50 == false && DataHandler.getInstance().getAvgVal() <= (baselineVal - 10)){
                        TextView change = (TextView) findViewById(R.id.changeNotification);
                        change.setText("Dimming lights to 50%");
                        isDimmedLight50 = true;
                        controlLights(127);
                    }
                    //if drop by 15 bpm, dim lights to 50%
                    if (isDimmedLight25 == false && DataHandler.getInstance().getAvgVal() <= (baselineVal - 15)){
                        TextView change = (TextView) findViewById(R.id.changeNotification);
                        change.setText("Dimming lights to 25%");
                        isDimmedLight25 = true;
                        controlLights(64);
                    }
                    //if drop by 20 bpm, turn off lights and turn off tv
                    if (isTurnedOffLight == false && isTurnedOffTV == false && DataHandler.getInstance().getAvgVal() <= (baselineVal - 20)){
                        TextView change = (TextView) findViewById(R.id.changeNotification);
                        change.setText("Turning off lights and tv");
                        isTurnedOffLight = true;
                        isTurnedOffTV = true;
                        controlLights(0);

                        //Code to turn off tv
                        final Context context = getApplicationContext();
                        Thread transmit;

                        try {
                            //Show a progress dialog and transmit all patterns
                            final ProgressDialog transmittingInfo = getProgressDialog(context);
                            transmit = new Thread() {
                                public void run() {
                                    RemoteControl.kill(context);
                                    transmittingInfo.dismiss();
                                }
                            };
                            transmit.start();
                        }catch (android.view.WindowManager.BadTokenException e) {
                            //Show a toast instead of a progress dialog and transmit all patterns
                            final Toast start = Toast.makeText(context, R.string.toast_transmission_initiated, Toast.LENGTH_LONG);
                            final Toast complete = Toast.makeText(context, R.string.toast_transmission_completed, Toast.LENGTH_SHORT);
                            start.show();
                            transmit = new Thread() {
                                public void run() {
                                    RemoteControl.kill(context);
                                    start.cancel();
                                    complete.show();
                                }
                            };
                            transmit.start();
                        }
                    }
                }
            }
        });
    }
}

