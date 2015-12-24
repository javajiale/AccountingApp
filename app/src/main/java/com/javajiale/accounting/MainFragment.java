package com.javajiale.accounting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

/**
 * Created by javajiale on 2015/12/8.
 */
public class MainFragment extends Fragment {
    private TextView text;
    private PieChart mChart;


    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.mainfragment, null);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        text= (TextView)getActivity().findViewById(R.id.text);
        final ImageButton btn_food = (ImageButton)getActivity().findViewById(R.id.food_imageButton);
        final ImageButton btn_shopping = (ImageButton)getActivity().findViewById(R.id.shopping_imageButton);
        final ImageButton btn_car = (ImageButton)getActivity().findViewById(R.id.car_imageButton);
        final ImageButton btn_live = (ImageButton)getActivity().findViewById(R.id.live_imageButton);
        final ImageButton btn_refresh = (ImageButton)getActivity().findViewById(R.id.refresh);

        mChart = (PieChart) getActivity().findViewById(R.id.spread_pie_chart);

        btn_food.setOnTouchListener(new tuoListener1());
        btn_shopping.setOnTouchListener(new tuoListener2());
        btn_live.setOnTouchListener(new tuoListener3());
        btn_car.setOnTouchListener(new tuoListener4());
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PieData mPieData = getPieData(4, 100);
                showChart(mChart, mPieData);
            }
        });


        PieData mPieData = getPieData(4, 100);
        showChart(mChart, mPieData);
    }

    private class tuoListener1 implements View.OnTouchListener{

        int[] temp = new int[]{0, 0};

        public boolean onTouch(View v, MotionEvent event) {

            int eventaction = event.getAction();

            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (eventaction) {

                case MotionEvent.ACTION_DOWN: // touch down so check if the
                    temp[0] = (int) event.getX();
                    temp[1] = y - v.getTop();
                    break;

                case MotionEvent.ACTION_MOVE: // touch drag with the ball

                    v.layout(x - temp[0], y - temp[1], x + v.getWidth()
                            - temp[0], y - temp[1] + v.getHeight());
                    break;

                case MotionEvent.ACTION_UP:
                    int xx =  (x + v.getWidth() - temp[0]);
                    int yy = ( y - temp[1] + v.getHeight());
                    text.setText("");//xx+","+yy
                    if( xx > 200 && ( yy > 392 && yy < 897 )){
                        tankuang("food");

                    }

                    break;
            }

            return false;
        }


    }

    private void tankuang(final String type){
        final CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                DBHelper dbHelper = new DBHelper(getActivity(),"acc.db",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                String sql = "insert into acc_list(beizhu,"+type+") values ( '"+builder.getMessage2()+"',"+
                        builder.getMessage1()+")";
                db.execSQL(sql);

                dialog.dismiss();
                //设置你的操作事项

                PieData mPieData = getPieData(4, 100);
                showChart(mChart, mPieData);

            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


    private class tuoListener2 implements View.OnTouchListener{

        int[] temp = new int[]{0, 0};

        public boolean onTouch(View v, MotionEvent event) {

            int eventaction = event.getAction();

            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (eventaction) {

                case MotionEvent.ACTION_DOWN: // touch down so check if the
                    temp[0] = (int) event.getX();
                    temp[1] = y - v.getTop();
                    break;

                case MotionEvent.ACTION_MOVE: // touch drag with the ball

                    v.layout(x - temp[0], y - temp[1], x + v.getWidth()
                            - temp[0], y - temp[1] + v.getHeight());
                    break;

                case MotionEvent.ACTION_UP:
                    int xx =  (x + v.getWidth() - temp[0]);
                    int yy = ( y - temp[1] + v.getHeight());
                    text.setText("");//xx+","+yy
                    if( xx > 200 && ( yy > 392 && yy < 897 )){
                        tankuang("shopping");
                    }

                    break;
            }

            return false;
        }


    }

    private class tuoListener3 implements View.OnTouchListener{

        int[] temp = new int[]{0, 0};

        public boolean onTouch(View v, MotionEvent event) {

            int eventaction = event.getAction();

            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (eventaction) {

                case MotionEvent.ACTION_DOWN: // touch down so check if the
                    temp[0] = (int) event.getX();
                    temp[1] = y - v.getTop();
                    break;

                case MotionEvent.ACTION_MOVE: // touch drag with the ball

                    v.layout(x - temp[0], y - temp[1], x + v.getWidth()
                            - temp[0], y - temp[1] + v.getHeight());
                    break;

                case MotionEvent.ACTION_UP:
                    int xx =  (x + v.getWidth() - temp[0]);
                    int yy = ( y - temp[1] + v.getHeight());
                    text.setText("");//xx+","+yy
                    if( xx > 200 && ( yy > 392 && yy < 897 )){
                        tankuang("live");
                    }

                    break;
            }

            return false;
        }


    }

    private class tuoListener4 implements View.OnTouchListener{

        int[] temp = new int[]{0, 0};

        public boolean onTouch(View v, MotionEvent event) {

            int eventaction = event.getAction();

            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (eventaction) {

                case MotionEvent.ACTION_DOWN: // touch down so check if the
                    temp[0] = (int) event.getX();
                    temp[1] = y - v.getTop();
                    break;

                case MotionEvent.ACTION_MOVE: // touch drag with the ball

                    v.layout(x - temp[0], y - temp[1], x + v.getWidth()
                            - temp[0], y - temp[1] + v.getHeight());
                    break;

                case MotionEvent.ACTION_UP:
                    int xx =  (x + v.getWidth() - temp[0]);
                    int yy = ( y - temp[1] + v.getHeight());
                    text.setText("");//xx+","+yy
                    if( xx > 200 && ( yy > 392 && yy < 897 )){
                        tankuang("car");
                    }

                    break;
            }

            return false;
        }


    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(45f);  //半径
        pieChart.setTransparentCircleRadius(45f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription(""); //测试饼状图

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("本月账单");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
//        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     *
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

//        for (int i = 0; i < count; i++) {
//            xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
//        }

        xValues.add("衣");
        xValues.add("食");
        xValues.add("住");
        xValues.add("行");

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */

        DBHelper dbHelper = new DBHelper(getActivity(),"acc.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("acc_list", new String[]{"sum(shopping)", "sum(food)", "sum(live)", "sum(car)"}, null, null, null, null, null);
        float shopping = 0;
        float food = 0;
        float live = 0;
        float car = 0;
        while(cursor.moveToNext()) {
             shopping = cursor.getFloat(0);
             food = cursor.getFloat(1);
             live = cursor.getFloat(2);
             car = cursor.getFloat(3);
        }
        float sum = shopping+food+live+car;

        float quarterly1 = shopping/sum;
        float quarterly2 = food/sum;
        float quarterly3 = live/sum;
        float quarterly4 = car/sum;

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        yValues.add(new Entry(quarterly4, 3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }


}
