package com.example.nikhil.offinaheartbeat;

import android.support.v7.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Observer;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidplot.Plot;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

import java.util.Observable;

import static android.R.attr.data;



public class DisplayHeartRate extends AppCompatActivity implements Observer {

    private XYPlot dynamicPlot;
    private Thread myThread;

    private boolean turnedOffTV;
    private boolean dimmedLight; //once flip, won't change once get out of range?
    private int MAX_SIZE = 20; //max size of graph


    @Override
    public void onCreate(Bundle savedInstanceState) {

        // android boilerplate stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_heart_rate);
        DataHandler.getInstance().addObserver(this);

        // get handles to our View defined in layout.xml:
        dynamicPlot = (XYPlot) findViewById(R.id.dynamicPlot);

        Number[] series1Numbers = {};
        DataHandler.getInstance().setSeries1(new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Heart Rate"));
        DataHandler.getInstance().setNewValue(false);

        //LOAD Graph
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.argb(255,255, 64, 129), null, null, null);
        //series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        series1Format.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(DataHandler.getInstance().getSeries1(), series1Format);
        //dynamicPlot.setTicksPerRangeLabel(3);
        //dynamicPlot.getGraphWidget().setDomainLabelOrientation(-45);

        dynamicPlot.getGraph().getGridBackgroundPaint().setColor(Color.argb(255,212,212,212));
        dynamicPlot.getGraph().getBackgroundPaint().setColor(Color.argb(255,47,50,122));

        // getInstance and position datasets:
//
//        LineAndPointFormatter formatter1 = new LineAndPointFormatter(
//                Color.rgb(0, 200, 0), null, null, null);
//        formatter1.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
//        formatter1.getLinePaint().setStrokeWidth(10);
//        dynamicPlot.addSeries(sine1Series,
//                formatter1);


        // thin out domain tick labels so they dont overlap each other:
//        dynamicPlot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
//        dynamicPlot.setDomainStepValue(5);
//
//        dynamicPlot.setRangeStepMode(StepMode.INCREMENT_BY_VAL);
//        dynamicPlot.setRangeStepValue(10);

        // uncomment this line to freeze the range boundaries:
//        dynamicPlot.setRangeBoundaries(55, 80, BoundaryMode.FIXED);
//
//        // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[] {PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        dynamicPlot.getGraph().getDomainGridLinePaint().setPathEffect(dashFx);
        dynamicPlot.getGraph().getRangeGridLinePaint().setPathEffect(dashFx);
    }

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

                if (DataHandler.getInstance().getAvgVal() <= 70){
                    TextView change = (TextView) findViewById(R.id.changeNotification);
                    change.setText("Dimming lights now");
                }

            }
        });
    }
}

