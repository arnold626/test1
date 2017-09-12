package com.saccl.test1;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 12/09/2017.
 *
 * http://www.journaldev.com/13792/android-gridlayoutmanager-example
 *
 * OnItemClickListener
 * https://antonioleiva.com/recyclerview-listener/
 */

public class DemoRVAdapter extends RecyclerView.Adapter<DemoRVAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(DemoObject item);
    }

    Context mContext;
    ArrayList<DemoObject> mDemoObjects;
    protected OnItemClickListener mOnItemClickListener;

    public DemoRVAdapter(Context inContext, ArrayList<DemoObject> inDemoObjects, OnItemClickListener inListener) {
        this.mContext = inContext;
        this.mDemoObjects = inDemoObjects;
        this.mOnItemClickListener = inListener;
    }

    @Override
    public int getItemCount() {
        return mDemoObjects.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mDemoObjects.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.demo_single_layout, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mView;
        ImageView mImageView;
        DemoObject mDemoObject;

        public ViewHolder(View inView) {
            super(inView);
            mView=inView;
            mImageView = (ImageView) mView.findViewById(R.id.demoSingle_image);
        }

        public void setData(DemoObject inData) {
            mDemoObject = inData;
            mImageView.setImageResource(inData.getImageId());
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setLayoutParams(new GridView.LayoutParams(450, 450));
        }


        @Override
        public void onClick(View inView) {
            if(mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mDemoObject);
            }
        }

    }
}
