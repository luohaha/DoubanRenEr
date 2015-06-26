package com.lyx.doubanrener.doubanrener.SearchActivity.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.Fragment.Adapters.MovieAdapter;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-26.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener = null;
    private MyItemLongClickListener mItemLongClickListener = null;
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

    public SearchAdapter(ArrayList<HashMap<String, Object>> mList, Context mContext, LayoutInflater mLayoutInflater) {
        this.mList = mList;
        this.mContext = mContext;
        this.mLayoutInflater = mLayoutInflater;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.from(mContext).inflate(R.layout.search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
        viewHolder.name = (TextView) view.findViewById(R.id.search_item_name);
        viewHolder.year = (TextView) view.findViewById(R.id.search_item_year);
        viewHolder.rating = (TextView) view.findViewById(R.id.search_item_rating);

        viewHolder.imageView = (ImageView) view.findViewById(R.id.search_item_image);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mList.get(position).get("name").toString();
        holder.name.setText((name.length() > 7)?name.substring(0, 6)+"...":name);
        holder.year.setText(mList.get(position).get("year").toString());
        holder.rating.setText(mList.get(position).get("rating").toString());
        Ion.with(holder.imageView)
                .placeholder(R.color.light)
                .error(R.color.grey)
                .load((String) mList.get(position).get("image"));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView name;
        public TextView year;
        public TextView rating;
        public ImageView imageView;
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
