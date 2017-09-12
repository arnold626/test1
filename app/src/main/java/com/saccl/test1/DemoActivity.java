package com.saccl.test1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity implements DemoRVAdapter.OnItemClickListener{

    private Toolbar mToolbar;
    private RecyclerView mDemoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // https://www.androidhive.info/2012/02/android-gridview-layout-tutorial/
        /*
        setContentView(R.layout.grid_layout);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new DemoAdapter(this));
        */

        // See below for RecycleView
        // http://camposha.info/source/android-navigationview-fragments-recyclerview/

        setContentView(R.layout.activity_demo);

        mToolbar = (Toolbar) findViewById(R.id.demo_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Demo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<DemoObject> demoObjects = new ArrayList<>();
        demoObjects.add(new DemoObject(R.drawable.sport_9_min_run,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_burpee,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_hand_grip,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_plank,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_push_up,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_single_leg_stand,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_sit_and_reach,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_sit_up,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_stand_long_jump,1,1,1,1,1,1));
        demoObjects.add(new DemoObject(R.drawable.sport_t_test,1,1,1,1,1,1));

        DemoRVAdapter demoAdapter = new DemoRVAdapter(this, demoObjects, this);
        mDemoList = (RecyclerView) findViewById(R.id.demo_list);
        mDemoList.setAdapter(demoAdapter);

        // AutoFitGridLayoutManager that auto fits the cells by the column width defined.

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        mDemoList.setLayoutManager(layoutManager);

        //GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        //mDemoList.setLayoutManager(layoutManager);

    }

    @Override
    public void onItemClick(DemoObject inDemo) {
        Toast.makeText(this, inDemo.getImageId(), Toast.LENGTH_LONG).show();;
    }
}
