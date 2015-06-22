package com.lyx.doubanrener.doubanrener.MoviePeopleActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.MaterialDesign.ProgressBarCircular;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.MoviePeopleActivity.Adapters.MovieFaceAdapter;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-22.
 */
public class MoviePeopleActivity extends AppCompatActivity implements MovieFaceAdapter.MyItemClickListener{
    private static int REFRESH_FINISH = 1;

    private ProgressBarCircular progressBarCircular;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;
    private TextView mBaseInfoTv;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MovieFaceAdapter mMovieFaceAdapter;
    private NestedScrollView mNestedScrollView;

    private String mItemUrl = "http://api.douban.com/v2/movie/celebrity/";
    private String mPeopleId;

    private ArrayList<HashMap<String, Object>> mList;
    /**
     * UI data
     * */
    private String imageView_data;
    private String collapsingToolbarLayout_data;
    private String mBaseInfoTv_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_people);
        initToolbar();
        initCollapsingToolbarLayout();
        initInfoUI();
        initPregress();
        getPeopleId();
        getDataViaHttp();
    }

    /**
     * 初始化内容页面UI
     * */
    private void initInfoUI() {
        mBaseInfoTv = (TextView) findViewById(R.id.activity_movie_people_baseinfo_tv);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.activity_movie_people_scrollview);
        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRecyclerView.setNestedScrollingEnabled(false);
                return false;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_movie_people_movie_list);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mList = new ArrayList<HashMap<String, Object>>();
        /**
         * set adapter
         * */
        initMovieFaceAdapter();
    }
    /**
     * 初始化toolbar
     * */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_movie_people_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }
    /**
     *初始化progress
     * */
    private void initPregress() {
        progressBarCircular = (ProgressBarCircular) findViewById(R.id.activity_movie_people_progress);
        progressBarCircular.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
    }
    /**
     * 初始化CollapsingToolbarLayout
     * */
    private void initCollapsingToolbarLayout() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_movie_people_collapsing_toolbar);

        imageView = (ImageView) findViewById(R.id.activity_movie_people_image);
    }
    /**
     * get movie id from intent
     * */
    private void getPeopleId() {
        Intent intent = getIntent();
        mPeopleId = intent.getStringExtra("people_id");
    }
    /**
     *
     * */
    private void getDataViaHttp() {
        progressBarCircular.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(mItemUrl+mPeopleId+"?"+getResources().getString(R.string.apikey))
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
                    Toast.makeText(MoviePeopleActivity.this, "网络错误~~", Toast.LENGTH_SHORT);
                } else {
                    imageView_data = result.get("avatars").getAsJsonObject().get("large").getAsString();
                    collapsingToolbarLayout_data = result.get("name").getAsString();
                    /**
                     * 基本信息栏目读取
                     * */
                    String name_en = result.get("name_en").getAsString();
                    String aka = "";
                    for (int i = 0; i < result.get("aka").getAsJsonArray().size(); i++) {
                        aka += result.get("aka").getAsJsonArray().get(i).getAsString() + " / ";
                    }
                    String aka_en = "";
                    for (int i = 0; i < result.get("aka_en").getAsJsonArray().size(); i++) {
                        aka_en += result.get("aka_en").getAsJsonArray().get(i).getAsString() + " / ";
                    }
                    String gender = result.get("gender").getAsString();
                    String born_place = result.get("born_place").getAsString();
                    mBaseInfoTv_data = "英文名: " + name_en + "\n"+
                                       "更多中文名: " + aka + "\n"+
                                       "更多英文名: "+ aka_en + "\n"+
                                       "性别: " + gender + "\n"+
                                       "出生地: " + born_place;
                    JsonArray works = result.get("works").getAsJsonArray();
                    for (int i = 0; i < works.size(); i++) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("movie_id", works.get(i).getAsJsonObject().get("subject").getAsJsonObject().get("id").getAsString());
                        hashMap.put("name", works.get(i).getAsJsonObject().get("subject").getAsJsonObject().get("title").getAsString());
                        hashMap.put("image", works.get(i).getAsJsonObject().get("subject").getAsJsonObject().get("images").getAsJsonObject().get("large").getAsString());
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
                    RefreshUI();
                }
            }
        };
    }

    private void RefreshUI() {
        Ion.with(imageView)
                .placeholder(R.color.light)
                .error(R.color.red)
                .load(imageView_data);
        collapsingToolbarLayout.setTitle(collapsingToolbarLayout_data);
        mBaseInfoTv.setText(mBaseInfoTv_data);
        initMovieFaceAdapter();
        progressBarCircular.setVisibility(View.GONE);
        mNestedScrollView.smoothScrollTo(0,0);
    }

    /**
     * 初始化人物信息的adapter
     * */
    private void initMovieFaceAdapter() {
        mMovieFaceAdapter = new MovieFaceAdapter(getLayoutInflater(), this, mList);
        mRecyclerView.setAdapter(mMovieFaceAdapter);
        mMovieFaceAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent(MoviePeopleActivity.this, MovieItemActivity.class);
        intent.putExtra("movie_id", mList.get(postion).get("movie_id").toString());
        startActivity(intent);
    }

}
