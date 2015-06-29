package com.lyx.doubanrener.doubanrener.MultiDisplayActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.Fragment.DragAndDrop.NoDragAdapter;
import com.lyx.doubanrener.doubanrener.MovieActivity.MovieViewPagerAdapter;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.MoviePeopleActivity.MoviePeopleActivity;
import com.lyx.doubanrener.doubanrener.R;
import com.lyx.doubanrener.doubanrener.SearchActivity.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-28.
 */
public class MultiDisplayActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private NoDragAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mType;
    private ArrayList<HashMap<String, Object>> mList;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_display);
        getIntentType();
        /**
         * drawerlayout 整个的头布局
         * drawerlist 左侧抽屉列表
         * toolbar 标题栏
         * */
        toolbar = (Toolbar) findViewById(R.id.activity_multi_display_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //设置左上角图标
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        mTextView = (TextView)findViewById(R.id.activity_multi_display_tv);

        /**
         * 设置整个的主题颜色
         * */
        setColor();

        initSwipeRefreshLayout();
        initRecyclerView();
        getDataViaDb();

    }
    /**
     * 设置整个的主题颜色
     * */
    private void setColor() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_search:
                Intent intent = new Intent(MultiDisplayActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView)findViewById(R.id.activity_multi_display_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void getDataViaDb() {
        mList = new ArrayList<HashMap<String, Object>>();
        if (mType.equals("movies")) {
            toolbar.setTitle("喜欢的电影");
            mTextView.setText("长按删除喜欢");
            DatabaseClient databaseClient = new DatabaseClient(this);
            Cursor cursor = databaseClient.queryData("lovemoviepage", null, null);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("doubanid", cursor.getString(cursor.getColumnIndex("doubanid")));
                hashMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                hashMap.put("image", cursor.getString(cursor.getColumnIndex("image")));
                hashMap.put("islove", "");
                mList.add(hashMap);
            }
            cursor.close();
            mAdapter = new NoDragAdapter(mList, getLayoutInflater(), this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new NoDragAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent = new Intent(MultiDisplayActivity.this, MovieItemActivity.class);
                    intent.putExtra("movie_id", mList.get(postion).get("doubanid").toString());
                    startActivity(intent);
                }
            });
            mAdapter.setOnItemLongClickListener(new NoDragAdapter.MyItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final int postion) {
                    new MaterialDialog.Builder(MultiDisplayActivity.this)
                            .title("删除喜欢电影")
                            .content("是否要将 "+mList.get(postion).get("name")+" 移除?")
                            .positiveText("是的")
                            .negativeText("不了")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    String doubanid = mList.get(postion).get("doubanid").toString();
                                    DatabaseClient peopledb = new DatabaseClient(MultiDisplayActivity.this);
                                    peopledb.deleteData("lovemoviepage", "doubanid=?", new String[]{doubanid});
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("islove", "no");
                                    peopledb.updateData("donepage", contentValues, "doubanid=?", new String[]{doubanid});
                                    getDataViaDb();
                                    dialog.dismiss();
                                    Toast.makeText(MultiDisplayActivity.this, "删除成功~~", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        } else if (mType.equals("peoples")) {
            toolbar.setTitle("喜欢的影人");
            mTextView.setText("长按删除喜欢");
            final DatabaseClient databaseClient = new DatabaseClient(this);
            Cursor cursor = databaseClient.queryData("lovepeoplepage", null, null);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("doubanid", cursor.getString(cursor.getColumnIndex("doubanid")));
                hashMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                hashMap.put("image", cursor.getString(cursor.getColumnIndex("image")));
                hashMap.put("islove", "");
                mList.add(hashMap);
            }
            cursor.close();
            mAdapter = new NoDragAdapter(mList, getLayoutInflater(), this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new NoDragAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent = new Intent(MultiDisplayActivity.this, MoviePeopleActivity.class);
                    intent.putExtra("people_id", mList.get(postion).get("doubanid").toString());
                    startActivity(intent);
                }
            });
            mAdapter.setOnItemLongClickListener(new NoDragAdapter.MyItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final int postion) {
                    new MaterialDialog.Builder(MultiDisplayActivity.this)
                            .title("删除喜欢影人")
                            .content("是否要将 "+mList.get(postion).get("name")+" 移除?")
                            .positiveText("是的")
                            .negativeText("不了")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    DatabaseClient peopledb = new DatabaseClient(MultiDisplayActivity.this);
                                    peopledb.deleteData("lovepeoplepage", "doubanid=?", new String[]{mList.get(postion).get("doubanid").toString()});
                                    getDataViaDb();

                                    dialog.dismiss();
                                    Toast.makeText(MultiDisplayActivity.this, "删除成功~~", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });

        } else if (mType.equals("records")) {
            toolbar.setTitle("观看记录");
            mTextView.setText("观看时间由远到近排序");
            DatabaseClient databaseClient = new DatabaseClient(this);
            Cursor cursor = databaseClient.queryData("donepage", null, null);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("doubanid", cursor.getString(cursor.getColumnIndex("doubanid")));
                hashMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                hashMap.put("image", cursor.getString(cursor.getColumnIndex("image")));
                hashMap.put("islove", cursor.getString(cursor.getColumnIndex("islove")));
                mList.add(hashMap);
            }
            cursor.close();
            mAdapter = new NoDragAdapter(mList, getLayoutInflater(), this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new NoDragAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int postion) {
                    Intent intent = new Intent(MultiDisplayActivity.this, MovieItemActivity.class);
                    intent.putExtra("movie_id", mList.get(postion).get("doubanid").toString());
                    startActivity(intent);
                }
            });
            mAdapter.setOnItemLongClickListener(new NoDragAdapter.MyItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final int postion) {
                    String isLove = mList.get(postion).get("islove").toString();
                    if (isLove == null || isLove.equals("") || isLove.equals("no")) {
                        new MaterialDialog.Builder(MultiDisplayActivity.this)
                                .title("标记喜欢")
                                .content("是否要将 " + mList.get(postion).get("name") + " 标记为喜欢的电影?")
                                .positiveText("是的")
                                .negativeText("不了")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        DatabaseClient databaseClient = new DatabaseClient(MultiDisplayActivity.this);
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("name", mList.get(postion).get("name").toString());
                                        contentValues.put("doubanid", mList.get(postion).get("doubanid").toString());
                                        contentValues.put("image", mList.get(postion).get("image").toString());
                                        databaseClient.insertData("lovemoviepage", contentValues);
                                        contentValues = new ContentValues();
                                        contentValues.put("islove", "yes");
                                        databaseClient.updateData("donepage", contentValues, "doubanid=?", new String[]{mList.get(postion).get("doubanid").toString()});
                                        mList.get(postion).put("islove", "yes");
                                        getDataViaDb();

                                        dialog.dismiss();
                                        Toast.makeText(MultiDisplayActivity.this, "已将 " + mList.get(postion).get("name") + " 标记为喜欢", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else if (isLove.equals("yes")){
                        new MaterialDialog.Builder(MultiDisplayActivity.this)
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
    }
    /**
     * get the display type
     * */
    private void getIntentType() {
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
    }
    /**
     * init SwipeRefreshLayout
     * */
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_multi_display_swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataViaDb();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
 }
