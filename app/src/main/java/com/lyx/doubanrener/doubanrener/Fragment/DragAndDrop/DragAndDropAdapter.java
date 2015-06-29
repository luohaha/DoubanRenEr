package com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.helper.ItemTouchHelperAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.helper.ItemTouchHelperViewHolder;
import com.lyx.doubanrener.doubanrener.Fragment.PlanFragment;
import com.lyx.doubanrener.doubanrener.MainActivity;
import com.lyx.doubanrener.doubanrener.MaterialDesign.FloatingActionButton;
import com.lyx.doubanrener.doubanrener.MaterialDesign.RoundImageView;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 15-6-27.
 */
public class DragAndDropAdapter extends RecyclerView.Adapter<DragAndDropAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private ArrayList<HashMap<String, Object>> mList;
    private Context mContext;
    private View mView;
    private MyItemClickListener mItemClickListener = null;
    private MyItemLongClickListener mItemLongClickListener = null;
    /**
     * Listener for manual initiation of a drag.
     */
    public interface OnStartDragListener {
        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    private OnStartDragListener mDragStartListener;
    public DragAndDropAdapter(OnStartDragListener dragStartListener, ArrayList<HashMap<String, Object>> list, Context context,
                              View view) {
        this.mDragStartListener = dragStartListener;
        this.mList = list;
        this.mContext = context;
        this.mView = view;
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
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drag_drop_item, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view, mItemClickListener, mItemLongClickListener);
        itemViewHolder.textView = (TextView) view.findViewById(R.id.dd_text);
        itemViewHolder.handleView = (ImageView) view.findViewById(R.id.dd_handle);
        itemViewHolder.imageView = (RoundImageView) view.findViewById(R.id.dd_image);
        return itemViewHolder;
    }
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        Object object = mList.get(position).get("name");
        if (object!=null) {
            holder.textView.setText(object.toString());
        }
        Ion.with(holder.imageView)
                .placeholder(R.color.light)
                .error(R.color.grey)
                .load((String) mList.get(position).get("image"));
// Start a drag whenever the handle view it touched
        holder.handleView.setImageResource(R.drawable.left);
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemDismiss(int position) {

        DatabaseClient databaseClient = new DatabaseClient(mContext);
        databaseClient.deleteData("todopage", "doubanid=?", new String[]{mList.get(position).get("doubanid").toString()});
        /**
         * insert done page
         * */
        ContentValues values = new ContentValues();
        Object object = mList.get(position).get("image");
        if (object != null) {
            values.put("image", mList.get(position).get("image").toString());
        }
        values.put("doubanid", mList.get(position).get("doubanid").toString());
        values.put("name", mList.get(position).get("name").toString());
        values.put("islove", "no");
        databaseClient.insertData("donepage", values);
        Snackbar.make(mView, "已经观看 : "+mList.get(position).get("name").toString(), Snackbar.LENGTH_LONG)
                .show();
        mList.remove(position);
        notifyItemRemoved(position);

    }
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(this.mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
    @Override
    public int getItemCount() {
        return this.mList.size();
    }
    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder, View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public ImageView handleView;
        public RoundImageView imageView;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public ItemViewHolder(View itemView, MyItemClickListener listener, MyItemLongClickListener longListener) {
            super(itemView);
            this.mListener = listener;
            this.mLongClickListener = longListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
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
