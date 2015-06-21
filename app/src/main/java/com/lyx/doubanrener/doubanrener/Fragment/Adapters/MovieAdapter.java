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
    private MyItemClickListener mItemClickListener = null;
    private MyItemLongClickListener mItemLongClickListener = null;

    public MovieAdapter(LayoutInflater mLayoutInflater, Context mContext, ArrayList<HashMap<String, Object>> mList) {
        this.mLayoutInflater = mLayoutInflater;
        this.mContext = mContext;
        this.mList = mList;
    }

    //define interface
    public interface MyItemClickListener {
        public void onItemClick(View view,int postion);
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view,int postion);
    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
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
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
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


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView textView;
        public ImageView imageView;
        public TextView rating;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ViewHolder(View itemView, MyItemClickListener listener, MyItemLongClickListener longListener) {
            super(itemView);
            this.mListener = listener;
            this.mLongClickListener = longListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null){
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }
}
