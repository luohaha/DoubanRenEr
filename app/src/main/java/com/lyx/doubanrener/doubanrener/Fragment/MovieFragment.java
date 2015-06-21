package com.lyx.doubanrener.doubanrener.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.Fragment.Adapters.MovieAdapter;
import com.lyx.doubanrener.doubanrener.MainActivity;
import com.lyx.doubanrener.doubanrener.MaterialDesign.FloatingActionButton;
import com.lyx.doubanrener.doubanrener.MaterialDesign.ProgressBarCircular;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by root on 15-6-20.
 */
public class MovieFragment extends Fragment {

    private static int REFRESH_FINISH = 1;
    /**
     * 是否向下滑动
     * */
    private boolean isScrollDown = false;

    /**
     * 实现分页
     * */
    private int countPage = 10;
    private int startPage = 0;
    //加载线程锁,
    private Boolean LoadingThreadLock = true;
    private String mGetNewUrl = "http://api.douban.com/v2/movie/search?tag=最新";
    private View mView;
    private LayoutInflater mInflater;
    private MovieAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> mList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBarCircular progressBarCircular;
    private GridLayoutManager manager;
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_movie, container, false);
        }
        this.mInflater = inflater;
        initPregress();
        initFloatButton();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.movie_recyclerview);
        manager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(manager);
        ScrollDownLoadMore();
        initSwipeRefresh();
        initView(inflater);
        progressBarCircular.setVisibility(View.VISIBLE);
        getDataViaHttp(0);
        return mView;
    }

    /**
     * 初始化浮动按键
     * */
    private void initFloatButton() {
        fab = (FloatingActionButton) mView.findViewById(R.id.movie_more_button);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    /**
     *初始化progress
     * */
    private void initPregress() {
        progressBarCircular = (ProgressBarCircular) mView.findViewById(R.id.movie_progress);
        progressBarCircular.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
    }
    /**
     * 初始化swipe fresh
     */
    private void initSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.movie_swipe_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.red, R.color.blue, R.color.colorPrimary, R.color.white);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * 刷新操作
                 * */
                mSwipeRefreshLayout.setRefreshing(true);
                getDataViaHttp(0);
            }
        });
    }

    /**
     * 网络加载
     * @param flag 为0时刷新,为1时加载
     */
    private void getDataViaHttp(int flag) {
        if (flag == 0) {
            mList = new ArrayList<HashMap<String, Object>>();
            startPage = 0;
        }
        Ion.with(getActivity())
                .load(mGetNewUrl+"&start="+String.valueOf(startPage)+"&count="+String.valueOf(countPage))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            startPage += countPage;
                            new Thread(new DecodeJsonThead(result, getActivity(), mList)).start();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 给gridview赋值
     */
    private void initView(LayoutInflater layoutInflater) {
        if (mAdapter == null) {
            mAdapter = new MovieAdapter(layoutInflater, getActivity(), mList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.onDateChange(mList);
        }
    }

    /**
     * 新建处理费时操作的线程
     */
    public class DecodeJsonThead implements Runnable {
        //返回值
        private JsonObject result;
        private Context mContext;
        private ArrayList<HashMap<String, Object>> mList;

        public DecodeJsonThead(JsonObject result, Context mContext, ArrayList<HashMap<String, Object>> mList) {
            this.result = result;
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public void run() {
            try {
                JsonArray jsonArray = result.get("subjects").getAsJsonArray();
                if (jsonArray == null) {
                    Toast.makeText(mContext, "网络错误~~", Toast.LENGTH_SHORT);
                } else {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("title", jsonArray.get(i).getAsJsonObject().get("title").getAsString());
                        hashMap.put("image", jsonArray.get(i).getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString());
                        hashMap.put("rating", jsonArray.get(i).getAsJsonObject().get("rating").getAsJsonObject().get("average").getAsFloat());
                        mList.add(hashMap);
                    }
                }
                Message message = Message.obtain();
                message.what = REFRESH_FINISH;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 处理当刷新完成时的handler信息
         */
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REFRESH_FINISH) {
                    initView(mInflater);
                    mSwipeRefreshLayout.setRefreshing(false);
                    progressBarCircular.setVisibility(View.GONE);
                    LoadingThreadLock = true;
                }
            }
        };
    }
    /**
     * 实现RecyclerView下拉加载更多
     * */
    private void ScrollDownLoadMore() {

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlideToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemtCount = manager.getItemCount();

                    if (lastItem == (totalItemtCount - 1) && isScrollDown && LoadingThreadLock) {
                        LoadingThreadLock = false;
                        getDataViaHttp(1);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if(dy > 0){
                    //大于0表示，正在向down滚动
                    isScrollDown = true;
                }else{
                    //小于等于0 表示停止或向down滚动
                    isScrollDown = false;
                }
            }
        });
    }
}
