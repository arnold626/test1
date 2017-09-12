package com.saccl.test1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by user on 12/09/2017.
 */

public class DemoAdapter extends BaseAdapter {

    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.sport_9_min_run,
            R.drawable.sport_burpee,
            R.drawable.sport_hand_grip,
            R.drawable.sport_plank,
            R.drawable.sport_push_up,
            R.drawable.sport_single_leg_stand,
            R.drawable.sport_sit_and_reach,
            R.drawable.sport_sit_up,
            R.drawable.sport_stand_long_jump,
            R.drawable.sport_t_test
    };

    // Constructor
    public DemoAdapter(Context inContext){
        mContext = inContext;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(450, 450));
        return imageView;
    }


}
