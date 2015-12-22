package com.javajiale.accounting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by javajiale on 2015/12/8.
 */
public class StaticFragment extends Fragment  {

    private LineChart mChart;
    private LineData data;
    private ArrayList<String> xVals;
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals;
    private Random random;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.staticfragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mChart = (LineChart)getActivity().findViewById(R.id.lineChart);

        xVals=new ArrayList<>();
        yVals=new ArrayList<>();
        random=new Random();
        for(int i=0;i<7;i++){
            float profix=random.nextFloat();
            yVals.add(new Entry(profix,i));
            xVals.add((i+1)+"天");
        }
        dataSet=new LineDataSet(yVals,"周账目统计");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
        mChart.setData(data);
        mChart.setDescription("周账目统计");
        mChart.animateY(3000);

    }

}
