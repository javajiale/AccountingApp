package com.javajiale.accounting;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import android.graphics.Color;
import android.widget.ImageButton;

import java.util.List;
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

    public static final String TABLE_NAME="acc_list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.staticfragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ImageButton btn_refresh = (ImageButton) getActivity().findViewById(R.id.refresh2);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

        init();
    }
    private void init(){
        mChart = (LineChart)getActivity().findViewById(R.id.lineChart);


        xVals = new ArrayList<String>();
        yVals = new ArrayList<Entry>();
        for(int i = 1; i <= 12; i++){
            xVals.add(i+"月");
        }
        yVals = getDate();

        dataSet=new LineDataSet(yVals,"每月账目统计");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data=new LineData(xVals,dataSet);
        mChart.setData(data);
        mChart.setDescription("每月账目统计");
        mChart.animateY(3000);

//        LineData data = getLineData(12, 100);
//        showChart(mChart, data);
    }

    private ArrayList<Entry> getDate(){
        DBHelper dbHelper = new DBHelper(getActivity(),"acc.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        for(int i=1;i<13;i++) {
            String[] columns = new String[]{"sum(shopping)", "sum(food)", "sum(live)", "sum(car)"};
            String selection = "time like '%-" + i+"-%'";
            Cursor cursor = db.query(TABLE_NAME, columns, selection, null, null, null, null);
            float sum = 0;
            while (cursor.moveToNext()) {
                float shopping = cursor.getFloat(0);
                float food = cursor.getFloat(1);
                float live = cursor.getFloat(2);
                float car = cursor.getFloat(3);
                sum = shopping + food + live + car;
            }
            yVals.add(new Entry(sum, i - 1));
        }
        return yVals;
    }
//
//    private void showChart(LineChart lineChart, LineData lineData) {
//        lineChart.setDescription("");// 数据描述
//
//        lineChart.setScaleEnabled(true);// 是否可以缩放
//        // add data
//        lineChart.setData(lineData); // 设置数据
//
//        Legend mLegend = lineChart.getLegend(); // 设置比例图标示
//        mLegend.setForm(LegendForm.CIRCLE);// 样式
//        mLegend.setFormSize(6f);// 字体
//        mLegend.setTextColor(Color.WHITE);// 颜色
//
//    }
//
//    private LineData getLineData(int count, float range){
//        int i=0;
//        xVals=new ArrayList<>();
//
//        for( i=1;i<13;i++){
//            xVals.add(i+"月");
//        }
//
//        yVals=new ArrayList<>();
//        DBHelper dbHelper = new DBHelper(getActivity(),"acc.db",null,1);
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        for(i=1;i<13;i++) {
//            // 要返回的列
//            String[] columns = new String[]{"sum(shopping)", "sum(food)", "sum(live)", "sum(car)","time"};
//            // 查询条件语句
//            String selection = "datepart(mm,getdate())=" + i;
//            //datepart(mm,date)
//            // 查询匹配条件，与selection要对应
//            // String[] selectionArgs = new String[]{Integer.toString(month)};
//            Cursor cursor = db.query(TABLE_NAME, columns, selection, null, null, null, null);
//            float shopping = 0;
//            float food = 0;
//            float live = 0;
//            float car = 0;
//            while (cursor.moveToNext()) {
//                shopping = cursor.getFloat(0);
//                food = cursor.getFloat(1);
//                live = cursor.getFloat(2);
//                car = cursor.getFloat(3);
//            }
//            float sum = shopping + food + live + car;
//            yVals.add(new Entry(sum, i - 1));
//        }
//
//        LineDataSet lineDataSet = new LineDataSet(yVals, "" /*显示在比例图上*/);
//
//        //用y轴的集合来设置参数
//        lineDataSet.setLineWidth(1.75f); // 线宽
//        lineDataSet.setCircleSize(3f);// 显示的圆形大小
//
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//        // 饼图颜色
//        colors.add(Color.rgb(240, 240, 240));
//        colors.add(Color.rgb(220, 220, 220));
//        colors.add(Color.rgb(200, 200, 200));
//        colors.add(Color.rgb(180, 180, 180));
//        colors.add(Color.rgb(160, 160, 160));
//        colors.add(Color.rgb(140, 140, 140));
//        colors.add(Color.rgb(120, 120, 120));
//        colors.add(Color.rgb(100, 100, 100));
//        colors.add(Color.rgb(80, 80, 80));
//        colors.add(Color.rgb(60, 60, 60));
//        colors.add(Color.rgb(40, 40, 40));
//        colors.add(Color.rgb(20, 20, 20));
//
//        lineDataSet.setColors(colors);// 显示颜色
//        lineDataSet.setCircleColors(colors);// 圆形的颜色
//        lineDataSet.setHighLightColor(Color.WHITE); // 高亮的线的颜色
//
//        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
//        lineDataSets.add(lineDataSet); // add the datasets
//
//        // create a data object with the datasets
//        LineData lineData = new LineData(xVals, lineDataSets);
//
//        return lineData;
//    }

}
