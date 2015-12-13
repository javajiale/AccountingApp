package com.javajiale.accounting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by javajiale on 2015/12/8.
 */
public class MainFragment extends Fragment {
    private TextView text;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.mainfragment, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        text= (TextView)getActivity().findViewById(R.id.xy_textView);
        final ImageButton btn = (ImageButton)getActivity().findViewById(R.id.food_imageButton);
        btn.setOnTouchListener(new View.OnTouchListener() {
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
                        text.setText(temp[0]+" "+temp[1]+"\n"+ (x + v.getWidth()
                                - temp[0])+" "+( y - temp[1] + v.getHeight()));
                        break;
                }

                return false;
            }



        });
    }
}
