package com.lyx.doubanrener.doubanrener.Fragment;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.DragAndDropAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.FullyLinearLayoutManager;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.NoDragAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.helper.SimpleItemTouchHelperCallback;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
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
        mAdapter.setOnItemClickListener(new DragAndDropAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getActivity(), MovieItemActivity.class);
                intent.putExtra("movie_id", mList.get(postion).get("doubanid").toString());
                startActivity(intent);
            }
        });

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


        /**
         * 观看列表
         * */

        mNoDragAdapter = new NoDragAdapter(mListDone, mInflater, getActivity());
        mRecyclerViewDone.setAdapter(mNoDragAdapter);
        mNoDragAdapter.setOnItemClickListener(new NoDragAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getActivity(), MovieItemActivity.class);
                intent.putExtra("movie_id", mListDone.get(postion).get("doubanid").toString());
                startActivity(intent);
            }
        });
        mNoDragAdapter.setOnItemLongClickListener(new NoDragAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int postion) {
               // DatabaseClient databaseClient = new DatabaseClient(getActivity());
               // Cursor cursor = databaseClient.queryData("donepage", "doubanid", new String[]{mListDone.get(postion).get("doubanid").toString()});
                String isLove = mListDone.get(postion).get("islove").toString();
                if (isLove == null || isLove.equals("") || isLove.equals("no")) {
                    new MaterialDialog.Builder(getActivity())
                            .title("标记喜欢")
                            .content("是否要将 " + mListDone.get(postion).get("name") + " 标记为喜欢的电影?")
                            .positiveText("是的")
                            .negativeText("不了")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    DatabaseClient databaseClient = new DatabaseClient(getActivity());
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("name", mListDone.get(postion).get("name").toString());
                                    contentValues.put("doubanid", mListDone.get(postion).get("doubanid").toString());
                                    contentValues.put("image", mListDone.get(postion).get("image").toString());
                                    databaseClient.insertData("lovemoviepage", contentValues);
                                    contentValues = new ContentValues();
                                    contentValues.put("islove", "yes");
                                    databaseClient.updateData("donepage", contentValues, "doubanid=?", new String[]{mListDone.get(postion).get("doubanid").toString()});
                                    mListDone.get(postion).put("islove", "yes");
                                    doFuck();

                                    dialog.dismiss();
                                    Snackbar.make(mView, "已将 " + mListDone.get(postion).get("name") + " 标记为喜欢", Snackbar.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else if (isLove.equals("yes")){
                    new MaterialDialog.Builder(getActivity())
                            .title("已经喜欢")
                            .content("该电影已在您的喜欢列表中")
                            .positiveText("是的")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {

                }

            }
        });
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
            hashMap.put("islove", cursor.getString(cursor.getColumnIndex("islove")));
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
