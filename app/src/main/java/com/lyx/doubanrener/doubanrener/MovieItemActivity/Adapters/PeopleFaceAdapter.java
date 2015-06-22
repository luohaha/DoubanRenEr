package com.lyx.doubanrener.doubanrener.MovieItemActivity.Adapters;

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
 * Created by root on 15-6-22.
 */
public class PeopleFaceAdapter extends RecyclerView.Adapter<PeopleFaceAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener = null;
    private MyItemLongClickListener mItemLongClickListener = null;

    public PeopleFaceAdapter(LayoutInflater mLayoutInflater, Context mContext, ArrayList<HashMap<String, Object>> mList) {
        this.mLayoutInflater = mLayoutInflater;
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).get("name").toString());
        Ion.with(holder.imageView)
                .placeholder(R.color.light)
                .error(R.color.grey)
                .load((String) mList.get(position).get("image"));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.from(mContext).inflate(R.layout.people_face_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.people_face_item_image);
        viewHolder.textView = (TextView) view.findViewById(R.id.people_face_item_name);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView textView;
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
