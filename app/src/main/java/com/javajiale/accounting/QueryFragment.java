package com.javajiale.accounting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javajiale.accounting.R;

/**
 * Created by javajiale on 2015/12/8.
 */
public class QueryFragment extends Fragment implements OnClickListener {

    private TextView query_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.queryfragment, null);
    }

//    public void onActivityCreated(Bundle savedInstanceState){
//        super.onActivityCreated(savedInstanceState);
//        query_text=(TextView)getActivity().findViewById(R.id.query_button);
//        query_text.setOnClickListener(this);
//    }

    @Override
    public void onClick(View v) {

    }

}
