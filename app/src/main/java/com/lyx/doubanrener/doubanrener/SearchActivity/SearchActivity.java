package com.lyx.doubanrener.doubanrener.SearchActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.MaterialDesign.ProgressBarCircular;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.R;
import com.lyx.doubanrener.doubanrener.SearchActivity.Adapters.SearchAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-25.
 */
public class SearchActivity extends ActionBarActivity {
    private static final int REFRESH_FINISH = 1;
    private int startPage = 0;
    private int countPage = 5;
    private Boolean LoadingThreadLock = true;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private ProgressBarCircular mProgressBarCircular;
    private LinearLayoutManager linearLayoutManager;
    private String mSearchUrl = "http://api.douban.com/v2/movie/search?q=";
    private String mQuery;

    private SearchAdapter searchAdapter;
    private ArrayList<HashMap<String, Object>> mList;
    /**
     * 是否向下滑动
     * */
    private boolean isScrollDown = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initRecycleView();
        ScrollDownLoadMore();
        mProgressBarCircular = (ProgressBarCircular)findViewById(R.id.search_progress);
        mSearchView = (SearchView) findViewById(R.id.main_search);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.onActionViewExpanded();
        mSearchView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                mProgressBarCircular.setVisibility(View.VISIBLE);
                getDataViaHttp(mQuery, 0);
                if (mSearchView != null) {
                    // 得到输入管理对象
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
                    }
                    mSearchView.clearFocus(); // 不获取焦点
                }


                return true;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
                mQuery = newText;
                if (!mQuery.equals("")) {
                    mProgressBarCircular.setVisibility(View.VISIBLE);
                    getDataViaHttp(mQuery, 0);
                }
                return true;
            }
        });
    }

    private void initRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_search_list);
        mRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    private void getDataViaHttp(String q, int flag) {
        if (flag == 0) {
            startPage = 0;
            mList = new ArrayList<HashMap<String, Object>>();
        }
        Ion.with(this)
                .load(mSearchUrl+q+"&"+getResources().getString(R.string.apikey)+"&start="+String.valueOf(startPage)+"&count="+String.valueOf(countPage))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            new Thread(new DecodeJsonThead(result)).start();
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 新建处理费时操作的线程
     */
    public class DecodeJsonThead implements Runnable {
        //返回值
        private JsonObject result;

        public DecodeJsonThead(JsonObject result) {
            this.result = result;
        }

        @Override
        public void run() {
            try {
                if (result == null) {
                    Toast.makeText(SearchActivity.this, "网络错误~~", Toast.LENGTH_SHORT).show();
                } else {
                    JsonArray subjects = result.get("subjects").getAsJsonArray();
                    for (int i = 0; i < subjects.size(); i++) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        JsonObject object = subjects.get(i).getAsJsonObject();
                        hashMap.put("id", object.get("id").getAsString());
                        hashMap.put("name", object.get("title").getAsString());
                        hashMap.put("year", object.get("year").getAsString());
                        hashMap.put("rating", String.valueOf(object.get("rating").getAsJsonObject().get("average").getAsFloat()));
                        hashMap.put("image", object.get("images").getAsJsonObject().get("large").getAsString());
                        mList.add(hashMap);
                    }
                    Message message = Message.obtain();
                    message.what = REFRESH_FINISH;
                    handler.sendMessage(message);
                }
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
            if (msg.what == REFRESH_FINISH) {
                RefreshUI();
            }
        }
    };

    /**
     * refresh ui
     * */

    private void RefreshUI() {
        doWithAdapter();
        mProgressBarCircular.setVisibility(View.GONE);
        LoadingThreadLock = true;
    }
    /**
     * do with adapter
     * */
    private void doWithAdapter() {
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter(mList, this, getLayoutInflater());
            mRecyclerView.setAdapter(searchAdapter);
        } else {
            if (startPage == 0) {
                searchAdapter.onDateChange(mList);
            } else {
                searchAdapter.onDateInsert(mList, startPage, countPage);
            }
        }
        startPage += countPage;
        searchAdapter.setOnItemClickListener(new SearchAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent = new Intent(SearchActivity.this, MovieItemActivity.class);
                intent.putExtra("movie_id", mList.get(postion).get("id").toString());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
            }
        });
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
                    int lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemtCount = linearLayoutManager.getItemCount();

                    if (lastItem == (totalItemtCount - 1) && isScrollDown && LoadingThreadLock) {
                        LoadingThreadLock = false;
                        getDataViaHttp(mQuery, 1);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }
}
