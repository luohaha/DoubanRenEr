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
    public void onDateInsert(ArrayList<HashMap<String, Object>> list, int start, int count) {
        this.mList = list;
        this.notifyItemRangeInserted(start, count);
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
        viewHolder.rating = (TextView)view.findViewById(R.id.movie_gridview_1_item_rating);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String t = (String)mList.get(i).get("title");
        viewHolder.textView.setText((t.length() > 9)?t.substring(0, 9)+"...":t);
        viewHolder.rating.setText(String.valueOf(mList.get(i).get("rating"))+" 分");
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
        public TextView rating;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
