package com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.MaterialDesign.RoundImageView;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-28.
 */
public class NoDragAdapter extends RecyclerView.Adapter<NoDragAdapter.ViewHolder> {
    private ArrayList<HashMap<String, Object>> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MyItemClickListener mItemClickListener = null;
    private MyItemLongClickListener mItemLongClickListener = null;

    public NoDragAdapter(ArrayList<HashMap<String, Object>> mList, LayoutInflater mLayoutInflater, Context mContext) {
        this.mList = mList;
        this.mLayoutInflater = mLayoutInflater;
        this.mContext = mContext;
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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).get("name").toString());
        Ion.with(holder.imageView)
                .placeholder(R.color.light)
                .error(R.color.red)
                .load((String) mList.get(position).get("image"));
        holder.handler.setVisibility(View.GONE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.from(mContext).inflate(R.layout.drag_drop_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
        viewHolder.textView = (TextView) view.findViewById(R.id.dd_text);
        viewHolder.imageView = (RoundImageView) view.findViewById(R.id.dd_image);
        viewHolder.handler = (ImageView) view.findViewById(R.id.dd_handle);

        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView textView;
        public RoundImageView imageView;
        public ImageView handler;
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
