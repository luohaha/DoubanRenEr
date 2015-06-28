package com.lyx.doubanrener.doubanrener.MovieItemActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.MaterialDesign.ProgressBarCircular;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.Adapters.PeopleFaceAdapter;
import com.lyx.doubanrener.doubanrener.MoviePeopleActivity.MoviePeopleActivity;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-21.
 */
public class MovieItemActivity extends AppCompatActivity implements PeopleFaceAdapter.MyItemClickListener{
    private static int REFRESH_FINISH = 1;

    private ProgressBarCircular progressBarCircular;

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageView;
    private TextView mBaseInfoTv;
    private TextView mSummaryTv;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PeopleFaceAdapter mPeopleFaceAdapter;
    private NestedScrollView mNestedScrollView;

    private FloatingActionButton mFloatingActionButton;

    private String mItemUrl = "http://api.douban.com/v2/movie/subject/";
    private String mMovieId;

    private ArrayList<HashMap<String, Object>> mList;
    /**
     * UI data
     * */
    private String imageView_data;
    private String imageview_data_small;
    private String collapsingToolbarLayout_data;
    private String mBaseInfoTv_data;
    private String mSummaryTv_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item);
        initToolbar();
        initCollapsingToolbarLayout();
        initInfoUI();
        initPregress();
        getMovieId();
        getDataViaHttp();
    }

    /**
     * 初始化内容页面UI
     * */
    private void initInfoUI() {
        mBaseInfoTv = (TextView) findViewById(R.id.activity_movie_item_baseinfo_tv);
        mSummaryTv = (TextView) findViewById(R.id.activity_movie_item_summary_tv);
        mNestedScrollView = (NestedScrollView) findViewById(R.id.activity_movie_item_scrollview);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_movie_item_people_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mList = new ArrayList<HashMap<String, Object>>();
        /**
         * set adapter
         * */
        initPeopleFaceAdapter();
    }
    /**
     * 初始化FloatingActionButton
     * */
    private void initFloatingActionButton() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.activity_movie_item_float_button);
        DatabaseClient databaseClient = new DatabaseClient(MovieItemActivity.this);
        Cursor cursor = databaseClient.queryData("todopage", "doubanid=?", new String[]{mMovieId});
        if (cursor == null || cursor.getCount() == 0) {
            /**
             * 还未加入计划
             * */
            mFloatingActionButton.setImageResource(R.drawable.ic_add_shopping_cart_white_24dp);
            mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (collapsingToolbarLayout_data != null && mMovieId != null) {
                        DatabaseClient databaseClient = new DatabaseClient(MovieItemActivity.this);
                        ContentValues values = new ContentValues();
                        values.put("name", collapsingToolbarLayout_data);
                        values.put("doubanid", mMovieId);
                        values.put("image", imageview_data_small);
                        databaseClient.insertData("todopage", values);
                        Toast.makeText(getApplicationContext(), "加入电影计划~~", Toast.LENGTH_SHORT).show();
                        mFloatingActionButton.setImageResource(R.drawable.ic_bookmark_white_24dp);
                        mFloatingActionButton.setOnClickListener(null);
                    }
                }
            });
        } else {
            /**
             * 加入计划
             * */
            mFloatingActionButton.setImageResource(R.drawable.ic_bookmark_white_24dp);
        }
        cursor.close();
    }
    /**
     * 初始化toolbar
     * */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_movie_item_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }
    /**
     *初始化progress
     * */
    private void initPregress() {
        progressBarCircular = (ProgressBarCircular) findViewById(R.id.activity_movie_item_progress);
        progressBarCircular.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
    }
    /**
     * 初始化CollapsingToolbarLayout
     * */
    private void initCollapsingToolbarLayout() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_movie_item_collapsing_toolbar);

        imageView = (ImageView) findViewById(R.id.activity_movie_item_image);
    }
    /**
     * get movie id from intent
     * */
    private void getMovieId() {
        Intent intent = getIntent();
        mMovieId = intent.getStringExtra("movie_id");
    }
    /**
     *
     * */
    private void getDataViaHttp() {
        progressBarCircular.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(mItemUrl+mMovieId+"?"+getResources().getString(R.string.apikey))
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
                    Toast.makeText(MovieItemActivity.this, "网络错误~~", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject images = result.get("images").getAsJsonObject();
                    imageView_data = images.get("large").getAsString();
                    imageview_data_small = images.get("small").getAsString();


                    collapsingToolbarLayout_data = result.get("title").getAsString();
                    /**
                     * 基本信息栏目读取
                     * */
                    String directors = "";
                    for (int i = 0; i < result.get("directors").getAsJsonArray().size(); i++) {
                        String name = result.get("directors").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString();
                        directors += name+" / ";
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("name", name);
                        //
                        if (result.get("directors").getAsJsonArray().get(i).getAsJsonObject().get("id").isJsonNull()) {
                            hashMap.put("people_id", "null");
                        } else {
                            hashMap.put("people_id", result.get("directors").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString());
                        }
                        //
                        if (result.get("directors").getAsJsonArray().get(i).getAsJsonObject().get("avatars").isJsonNull()) {
                            hashMap.put("image", "null");
                        } else {
                            hashMap.put("image", result.get("directors").getAsJsonArray().get(i).getAsJsonObject().get("avatars").getAsJsonObject().get("large").getAsString());
                        }
                        mList.add(hashMap);
                    }

                    String genres = "";
                    for (int i = 0; i < result.get("genres").getAsJsonArray().size(); i++) {
                        genres += result.get("genres").getAsJsonArray().get(i).getAsString()+" / ";
                    }
                    String casts = "";
                    for (int i = 0; i < result.get("casts").getAsJsonArray().size(); i++) {
                        String name = result.get("casts").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString();
                        casts += name + " / ";
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("name", name);
                        //
                        if (result.get("casts").getAsJsonArray().get(i).getAsJsonObject().get("id").isJsonNull()) {
                            hashMap.put("people_id", "null");
                        } else {
                            hashMap.put("people_id", result.get("casts").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString());
                        }
                        //
                        if (result.get("casts").getAsJsonArray().get(i).getAsJsonObject().get("avatars").isJsonNull()) {
                            hashMap.put("image", "null");
                        } else {
                            hashMap.put("image", result.get("casts").getAsJsonArray().get(i).getAsJsonObject().get("avatars").getAsJsonObject().get("large").getAsString());
                        }
                        mList.add(hashMap);
                    }
                    String countries = "";
                    for (int i = 0; i < result.get("countries").getAsJsonArray().size(); i++) {
                        countries += result.get("countries").getAsJsonArray().get(i).getAsString()+" / ";
                    }
                    String aka = "";
                    for (int i = 0; i < result.get("aka").getAsJsonArray().size(); i++) {
                        aka += result.get("aka").getAsJsonArray().get(i).getAsString()+" / ";
                    }
                    float rating = result.get("rating").getAsJsonObject().get("average").getAsFloat();
                    String year = result.get("year").getAsString();
                    int ratings_count = result.get("ratings_count").getAsInt();
                    String original_title = result.get("original_title").getAsString();
                    mBaseInfoTv_data = "导演: "+ directors+"\n"+
                                       "主演: "+ casts + "\n"+
                                       "年代: "+ year + "\n"+
                                       "类型: "+ genres + "\n"+
                                       "制片国家/地区: "+countries + "\n"+
                                       "原名: "+ original_title + "\n"+
                                       "又名: "+ aka + "\n"+
                                       "评分: "+ String.valueOf(rating) + "\n"+
                                       "评分人数: "+ String.valueOf(ratings_count);

                    /**
                     * 电影简介
                     * */
                    mSummaryTv_data = result.get("summary").getAsString();


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
        mSummaryTv.setText(mSummaryTv_data);
        initPeopleFaceAdapter();
        progressBarCircular.setVisibility(View.GONE);
        mNestedScrollView.smoothScrollTo(0, 0);
        mNestedScrollView.setVisibility(View.VISIBLE);
        initFloatingActionButton();
    }

    /**
     * 初始化人物信息的adapter
     * */
    private void initPeopleFaceAdapter() {
        mPeopleFaceAdapter = new PeopleFaceAdapter(getLayoutInflater(), this, mList);
        mRecyclerView.setAdapter(mPeopleFaceAdapter);
        mPeopleFaceAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Intent intent = new Intent(MovieItemActivity.this, MoviePeopleActivity.class);
        intent.putExtra("people_id", mList.get(postion).get("people_id").toString());
        startActivity(intent);
    }
}
