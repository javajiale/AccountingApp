package com.javajiale.accounting;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by javajiale on 2015/12/8.
 */
public class QueryFragment extends Fragment implements SlideCutListView.RemoveListener {

    private SlideCutListView slideCutListView ;
    private ArrayAdapter adapter;
    private List<String> dataSourceList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.queryfragment, null);
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        init();

    }
    private void init(){
        slideCutListView = (SlideCutListView) getActivity().findViewById(R.id.slideCutListView);
        slideCutListView.setRemoveListener(this);

        List<String> list = new ArrayList<String>();
        dataSourceList.clear();
        list = getData();
        for(int i=0; i<list.size(); i++){
            dataSourceList.add(list.get(i));
        }
     //   R.layout.listview_item, R.id.list_item,
        adapter = new ArrayAdapter(getActivity(),R.layout.listview_item, R.id.list_item,dataSourceList);
        slideCutListView.setAdapter(adapter);

        slideCutListView.setonRefreshListener(new SlideCutListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        List<String> list = new ArrayList<String>();
                        dataSourceList.clear();
                        list = getData();
                        for(int i=0; i<list.size(); i++){
                            dataSourceList.add(list.get(i));
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.notifyDataSetChanged();
                        slideCutListView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });


        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), dataSourceList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private List<String> getData(){

        List<String> data = new ArrayList<String>();

        DBHelper dbHelper = new DBHelper(getActivity(),"acc.db",null,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("acc_list", null, null, null, null, null, null);
        while(cursor.moveToNext()) {
//            String id = cursor.getString(0);
//            String beizhu = cursor.getString(1);
//            String shopping = cursor.getString(2);
//            String food = cursor.getString(3);
//            String live = cursor.getString(4);
//            String car = cursor.getString(5);
            String put = cursor.getString(0)+"  ";
            for(int i = 1; i < 6; i++)
            {
                if(i == 2)
                    put+="   ";

                if(cursor.getString(i)!=null)
                    put+=cursor.getString(i);

            }
            data.add(put);
        }

        db.close();
        return data;
    }


    //滑动删除之后的回调方法
    @Override
    public void removeItem(SlideCutListView.RemoveDirection direction, int position) {
      //  adapter.remove(adapter.getItem(position));
        switch (direction) {
            case RIGHT:
            //    Toast.makeText(getActivity(), "删除  "+ adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                delete(adapter.getItem(position-1).toString().charAt(0));
                init();
                break;
            case LEFT:
            //    Toast.makeText(getActivity(), "删除  "+ position, Toast.LENGTH_SHORT).show();
                delete(adapter.getItem(position-1).toString().charAt(0));
                init();
                break;

            default:
                break;
        }

    }

    private void delete(char id){
        DBHelper dbHelper = new DBHelper(getActivity(),"acc.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "delete from acc_list where id = " + id;
        db.execSQL(sql);
    }

//    public void onActivityCreated(Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//        query_text=(TextView)getActivity().findViewById(R.id.query_button);
//        query_text.setOnClickListener(this);
//    }





}
