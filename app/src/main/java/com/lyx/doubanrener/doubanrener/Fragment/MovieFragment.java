package com.lyx.doubanrener.doubanrener.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.Fragment.Adapters.BoxAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.Adapters.LoveAdapter;
import com.lyx.doubanrener.doubanrener.Fragment.Adapters.MovieAdapter;
import com.lyx.doubanrener.doubanrener.MaterialDesign.FloatingActionButton;
import com.lyx.doubanrener.doubanrener.MaterialDesign.Other.LayoutRipple;
import com.lyx.doubanrener.doubanrener.MaterialDesign.ProgressBarCircular;
import com.lyx.doubanrener.doubanrener.MovieActivity.MovieActivity;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-20.
 */
public class MovieFragment extends Fragment{

    private static int REFRESH_FINISH = 1;

    private int mRefreshCount = 0;
    /**
     * 是否向下滑动
     * */
    private boolean isScrollDown = false;

    //加载线程锁,
    private String mGetNewUrl = "http://api.douban.com/v2/movie/us_box";
    private String mGetLoveMovieUrl = "http://api.douban.com/v2/movie/search?tag=爱情&start=10&count=6";
    private String mGetActionMovieUrl = "http://api.douban.com/v2/movie/search?tag=动作&start=10&count=6";
    private String mGetScienceMovieUrl = "http://api.douban.com/v2/movie/search?tag=科幻&start=10&count=6";


    private View mView;
    private LayoutInflater mInflater;
    /**
     * box
     * */
    private BoxAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> mList;
    private RecyclerView mRecyclerView;
    private GridLayoutManager manager;

    /**
     * love
     * */
    private LoveAdapter mLoveAdapter;
    private ArrayList<HashMap<String, Object>> mLoveMovieList;
    private RecyclerView mRecyclerViewLove;
    private GridLayoutManager loveManager;

    /**
     * action
     * */
    private LoveAdapter mActionAdapter;
    private ArrayList<HashMap<String, Object>> mActionMovieList;
    private RecyclerView mRecyclerViewAction;
    private GridLayoutManager actionManager;

    /**
     * science
     * */
    private LoveAdapter mScienceAdapter;
    private ArrayList<HashMap<String, Object>> mScienceMovieList;
    private RecyclerView mRecyclerViewScience;
    private GridLayoutManager scienceManager;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBarCircular progressBarCircular;
    private NestedScrollView mNestedScrollView;
    private FloatingActionButton fab;

    private TextView mBoxDateTv;
    private String BoxDate;

    private LayoutRipple loveButton;
    private LayoutRipple scienceButton;
    private LayoutRipple actionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_movie, container, false);
        }
        this.mInflater = inflater;
        initPregress();
        initFloatButton();
        initUI();
        initRecyclerView();

        initSwipeRefresh();
        initView(inflater);
        progressBarCircular.setVisibility(View.VISIBLE);
        getDataViaHttp();
        return mView;
    }

    /**
     * init RecyclerView
     * */

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.fragment_movie_box_list);
        mRecyclerViewLove = (RecyclerView) mView.findViewById(R.id.fragment_movie_love_list);
        mRecyclerViewAction = (RecyclerView) mView.findViewById(R.id.fragment_movie_action_list);
        mRecyclerViewScience = (RecyclerView) mView.findViewById(R.id.fragment_movie_science_list);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewLove.setHasFixedSize(true);
        mRecyclerViewAction.setHasFixedSize(true);
        mRecyclerViewScience.setHasFixedSize(true);
        /**
         * box
         * */
        manager = new GridLayoutManager(getActivity(), 1);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);

        /**
         * love
         * */
        loveManager = new GridLayoutManager(getActivity(), 3);
        loveManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewLove.setLayoutManager(loveManager);
        mRecyclerViewLove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        /**
         * action
         * */
        actionManager = new GridLayoutManager(getActivity(), 3);
        actionManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewAction.setLayoutManager(actionManager);
        mRecyclerViewAction.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        /**
         * science
         * */
        scienceManager = new GridLayoutManager(getActivity(), 3);
        scienceManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewScience.setLayoutManager(scienceManager);
        mRecyclerViewScience.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }
    /**
     * init ui
     * */

    private void initUI() {
        mBoxDateTv = (TextView) mView.findViewById(R.id.box_date_tv);
        mNestedScrollView = (NestedScrollView) mView.findViewById(R.id.fragment_movie_box_scrollview);
        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRecyclerView.setNestedScrollingEnabled(false);
                mRecyclerViewLove.setNestedScrollingEnabled(false);
                mRecyclerViewAction.setNestedScrollingEnabled(false);
                mRecyclerViewScience.setNestedScrollingEnabled(false);
                return false;
            }
        });
        loveButton = (LayoutRipple)mView.findViewById(R.id.love_button);
        loveButton.setRippleColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                intent.putExtra("page", 6);
                startActivity(intent);
            }
        });
        scienceButton = (LayoutRipple)mView.findViewById(R.id.science_button);
        scienceButton.setRippleColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        scienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                intent.putExtra("page", 7);
                startActivity(intent);
            }
        });
        actionButton = (LayoutRipple)mView.findViewById(R.id.action_button);
        actionButton.setRippleColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                intent.putExtra("page", 4);
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(getActivity(), MovieActivity.class);
                startActivity(intent);
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
                getDataViaHttp();
            }
        });
    }

    /**
     * 网络加载
     */
    private void getDataViaHttp() {
        mList = new ArrayList<HashMap<String, Object>>();
        mLoveMovieList = new ArrayList<HashMap<String, Object>>();
        mActionMovieList = new ArrayList<HashMap<String, Object>>();
        mScienceMovieList = new ArrayList<HashMap<String, Object>>();

        Ion.with(getActivity())
                .load(mGetNewUrl+"?"+getResources().getString(R.string.apikey))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            new Thread(new DecodeJsonThead(result, getActivity(), mList)).start();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });

        Ion.with(getActivity())
                .load(mGetLoveMovieUrl+"&"+getResources().getString(R.string.apikey))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            new Thread(new DecodeJsonSecondThead(result, getActivity(), mLoveMovieList)).start();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });

        Ion.with(getActivity())
                .load(mGetActionMovieUrl+"&"+getResources().getString(R.string.apikey))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            new Thread(new DecodeJsonSecondThead(result, getActivity(), mActionMovieList)).start();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });

        Ion.with(getActivity())
                .load(mGetScienceMovieUrl+"&"+getResources().getString(R.string.apikey))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            new Thread(new DecodeJsonSecondThead(result, getActivity(), mScienceMovieList)).start();
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
            mAdapter = new BoxAdapter(layoutInflater, getActivity(), mList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.onDateChange(mList);
        }
        mAdapter.setOnItemClickListener(new BoxAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getActivity(), MovieItemActivity.class);
                intent.putExtra("movie_id", mList.get(postion).get("id").toString());
                startActivity(intent);
            }
        });


        if (mLoveAdapter == null) {
            mLoveAdapter = new LoveAdapter(layoutInflater, getActivity(), mLoveMovieList);
            mRecyclerViewLove.setAdapter(mLoveAdapter);
        } else {
            mLoveAdapter.onDateChange(mLoveMovieList);
        }
        mLoveAdapter.setOnItemClickListener(new LoveAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getActivity(), MovieItemActivity.class);
                intent.putExtra("movie_id", mLoveMovieList.get(postion).get("id").toString());
                startActivity(intent);
            }
        });

        if (mActionAdapter == null) {
            mActionAdapter = new LoveAdapter(layoutInflater, getActivity(), mActionMovieList);
            mRecyclerViewAction.setAdapter(mActionAdapter);
        } else {
            mActionAdapter.onDateChange(mActionMovieList);
        }
        mActionAdapter.setOnItemClickListener(new LoveAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getActivity(), MovieItemActivity.class);
                intent.putExtra("movie_id", mActionMovieList.get(postion).get("id").toString());
                startActivity(intent);
            }
        });

        if (mScienceAdapter == null) {
            mScienceAdapter = new LoveAdapter(layoutInflater, getActivity(), mScienceMovieList);
            mRecyclerViewScience.setAdapter(mScienceAdapter);
        } else {
            mScienceAdapter.onDateChange(mScienceMovieList);
        }
        mScienceAdapter.setOnItemClickListener(new LoveAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(getActivity(), MovieItemActivity.class);
                intent.putExtra("movie_id", mScienceMovieList.get(postion).get("id").toString());
                startActivity(intent);
            }
        });
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
                BoxDate = result.get("date").getAsString();
                JsonArray jsonArray = result.get("subjects").getAsJsonArray();
                if (jsonArray == null) {
                    Toast.makeText(mContext, "网络错误~~", Toast.LENGTH_SHORT);
                } else {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                        int box = object.get("box").getAsInt();
                        String title = object.get("subject").getAsJsonObject().get("title").getAsString();
                        Float rating = object.get("subject").getAsJsonObject().get("rating").getAsJsonObject().get("average").getAsFloat();
                        String image = object.get("subject").getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString();
                        hashMap.put("name", title);
                        hashMap.put("rating", rating);
                        hashMap.put("box", box);
                        hashMap.put("image", image);
                        hashMap.put("id",  object.get("subject").getAsJsonObject().get("id").getAsString());
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
    }

    /**
     * 新建处理费时操作的线程,第二类
     */
    public class DecodeJsonSecondThead implements Runnable {
        //返回值
        private JsonObject result;
        private Context mContext;
        private ArrayList<HashMap<String, Object>> mList;

        public DecodeJsonSecondThead(JsonObject result, Context mContext, ArrayList<HashMap<String, Object>> mList) {
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
                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                        String title = object.get("title").getAsString();
                        Float rating = object.get("rating").getAsJsonObject().get("average").getAsFloat();
                        String image = object.get("images").getAsJsonObject().get("large").getAsString();
                        hashMap.put("name", title);
                        hashMap.put("rating", rating);
                        hashMap.put("box", 0);
                        hashMap.put("image", image);
                        hashMap.put("id", object.get("id").getAsString());
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
    }

    /**
     * 处理当刷新完成时的handler信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                    mRefreshCount++;
            }
            if (mRefreshCount==4) {
                mRefreshCount = 0;
                initView(mInflater);
                mSwipeRefreshLayout.setRefreshing(false);
                progressBarCircular.setVisibility(View.GONE);
                mBoxDateTv.setText("北美票房: "+BoxDate);
            }
        }
    };

}
