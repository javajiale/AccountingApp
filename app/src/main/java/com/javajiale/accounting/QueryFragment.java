package com.javajiale.accounting;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by javajiale on 2015/12/8.
 */
public class QueryFragment extends Fragment implements SlideCutListView.RemoveListener {

    EditText eSearch;
    ImageView ivDeleteText;
    Handler myhandler = new Handler();

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

        set_eSearch_TextChanged();//设置eSearch搜索框的文本改变时监听器

        set_ivDeleteText_OnClick();//设置叉叉的监听器





    }

    /**
     * 设置搜索框的文本更改时的监听器
     */
    private void set_eSearch_TextChanged()
    {
        eSearch = (EditText) getActivity().findViewById(R.id.etSearch);

        eSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }

                myhandler.post(eChanged);
            }
        });

    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = eSearch.getText().toString();

            dataSourceList.clear();//先要清空，不然会叠加

            getmDataSub(data);//获取更新数据

//          adapter.notifyDataSetChanged();//更新


        }
    };

    /**
     * 获得根据搜索框的数据data来从元数据筛选，筛选出来的数据放入mDataSubs里
     * @param mDataSubs
     * @param data
     */

    private void getmDataSub( String data)
    {
        List<String> mDataSubs = new ArrayList<String>();
        List<String> list = getData();
        int length = list.size();
        for(int i = 0; i < length; ++i){
            if(list.get(i).contains(data)){
                mDataSubs.add(list.get(i));
            }
        }
        adapter = new ArrayAdapter(getActivity(),R.layout.listview_item, R.id.list_item,mDataSubs);
        slideCutListView.setAdapter(adapter);
    }

    /**
     * 设置叉叉的点击事件，即清空功能
     */

    private void set_ivDeleteText_OnClick()
    {
        ivDeleteText = (ImageView) getActivity().findViewById(R.id.ivDeleteText);
        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                eSearch.setText("");
            }
        });
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


//        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Toast.makeText(getActivity(), dataSourceList.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });

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
            for(int i = 1; i < 7; i++)
            {
                if(i == 2||i == 6)
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
                String[] strs = adapter.getItem(position-1).toString().split(" ");
                delete(strs[0]);
                init();
                break;
            case LEFT:
            //    Toast.makeText(getActivity(), "删除  "+ position, Toast.LENGTH_SHORT).show();
                String[] str = adapter.getItem(position-1).toString().split(" ");
                delete(str[0]);
                init();
                break;

            default:
                break;
        }

    }

    private void delete(String id){
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
