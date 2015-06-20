package com.lyx.doubanrener.doubanrener.Fragment.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-20.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MovieAdapter(LayoutInflater mLayoutInflater, Context mContext, ArrayList<HashMap<String, Object>> mList) {
        this.mLayoutInflater = mLayoutInflater;
        this.mContext = mContext;
        this.mList = mList;
    }

    public void onDateChange(ArrayList<HashMap<String, Object>> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.from(mContext).inflate(R.layout.movie_gridview_1_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textView = (TextView)view.findViewById(R.id.movie_gridview_1_item_title);
        viewHolder.imageView = (ImageView)view.findViewById(R.id.movie_gridview_1_item_image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textView.setText((String)mList.get(i).get("title"));
        Ion.with(viewHolder.imageView)
                .placeholder(R.color.light)
                .error(R.color.red)
                .load((String) mList.get(i).get("image"));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
