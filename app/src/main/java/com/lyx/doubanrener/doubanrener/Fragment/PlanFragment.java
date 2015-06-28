package com.lyx.doubanrener.doubanrener.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.DragAndDropAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.FullyLinearLayoutManager;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.NoDragAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.helper.SimpleItemTouchHelperCallback;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-26.
 */
public class PlanFragment extends Fragment implements DragAndDropAdapter.OnStartDragListener{
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemTouchHelper mItemTouchHelper;

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerViewDone;

    private DragAndDropAdapter mAdapter;
    private NoDragAdapter mNoDragAdapter;

    private ItemTouchHelper.Callback mCallback;

    private View mView;
    private ArrayList<HashMap<String, Object>> mList;
    private ArrayList<HashMap<String, Object>> mListDone;

    private LayoutInflater mInflater;

    public PlanFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_plan, container, false);
        this.mInflater = inflater;
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.drag_drop_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        mRecyclerViewDone = (RecyclerView) mView.findViewById(R.id.drag_drop_recycler_done);
        mRecyclerViewDone.setHasFixedSize(true);
        mRecyclerViewDone.setLayoutManager(new FullyLinearLayoutManager(getActivity()));

        getDataToUse();
        initSwipeRefreshLayout();
        doFuck();
        return mView;
    }


    private void doFuck() {
        mAdapter = new DragAndDropAdapter(this, mList, getActivity(), mView);
        mRecyclerView.setAdapter(mAdapter);

       // mCallback = new SimpleItemTouchHelperCallback(mAdapter);
        mCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (viewHolder.getItemViewType() != target.getItemViewType()) {
                    return false;
                }
// Notify the adapter of the move
                mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
        };
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mNoDragAdapter = new NoDragAdapter(mListDone, mInflater, getActivity());
        mRecyclerViewDone.setAdapter(mNoDragAdapter);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
       // mItemTouchHelper.startDrag(viewHolder);
        mItemTouchHelper.startSwipe(viewHolder);
    }

    private void getDataToUse() {
        mList = new ArrayList<HashMap<String, Object>>();
        mListDone = new ArrayList<HashMap<String, Object>>();

        DatabaseClient databaseClient = new DatabaseClient(getActivity());
        Cursor cursor = databaseClient.queryData("todopage", null, null);
        while (cursor.moveToNext()) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("id", cursor.getString(cursor.getColumnIndex("id")));
            hashMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
            hashMap.put("image", cursor.getString(cursor.getColumnIndex("image")));
            hashMap.put("doubanid", cursor.getString(cursor.getColumnIndex("doubanid")));
            mList.add(hashMap);
        }
        cursor = databaseClient.queryLastPage("donepage");
        while (cursor.moveToNext()) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
            hashMap.put("image", cursor.getString(cursor.getColumnIndex("image")));
            hashMap.put("doubanid", cursor.getString(cursor.getColumnIndex("doubanid")));
            mListDone.add(hashMap);
        }
        cursor.close();
    }
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.plan_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getDataToUse();
                doFuck();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
