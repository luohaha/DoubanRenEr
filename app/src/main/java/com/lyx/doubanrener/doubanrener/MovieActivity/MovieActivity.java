package com.lyx.doubanrener.doubanrener.MovieActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.lyx.doubanrener.doubanrener.MaterialDesign.LollipopUtils;
import com.lyx.doubanrener.doubanrener.R;
import com.lyx.doubanrener.doubanrener.SearchActivity.SearchActivity;

import java.util.HashMap;

/**
 * Created by root on 15-6-21.
 * 用于加载MovieAreaFragment
 */
public class MovieActivity extends AppCompatActivity {

    ViewPager pager;
    private String titles[] = new String[]{"top250", "热门", "华语",
    "欧美", "动作", "喜剧", "爱情", "科幻", "悬疑", "恐怖"};
    private Toolbar toolbar;

    TabLayout slidingTabLayout;

    private HashMap<String, Integer> MoviehashMap;

    private AppBarLayout mToolbarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        /**
         * drawerlayout 整个的头布局
         * drawerlist 左侧抽屉列表
         * toolbar 标题栏
         * */
        toolbar = (Toolbar) findViewById(R.id.activity_movie_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //设置左上角图标
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, R.anim.slide_bottom_out);
                }
            });
            toolbar.setTitle("分类");
        }

        mToolbarHolder = (AppBarLayout) findViewById(R.id.toolbar_holder);
        LollipopUtils.setStatusbarColor(this, mToolbarHolder);

        initHashMap();
        /**
         * 获取viewpager,和tabs的布局
         * */
        pager = (ViewPager) findViewById(R.id.activity_movie_viewpager);
        slidingTabLayout = (TabLayout) findViewById(R.id.activity_movie_sliding_tabs);
        pager.setAdapter(new MovieViewPagerAdapter(getSupportFragmentManager(), titles));
        pager.setCurrentItem(MoviehashMap.get(getIntent().getStringExtra("page")));

        slidingTabLayout.setupWithViewPager(pager);
        slidingTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        /**
         * 设置整个的主题颜色
         * */
        setColor();

    }
    private void initHashMap() {
        MoviehashMap = new HashMap<String, Integer>();
        MoviehashMap.put("top250", 0);
        MoviehashMap.put("热门", 1);
        MoviehashMap.put("华语", 2);
        MoviehashMap.put("欧美", 3);
        MoviehashMap.put("动作", 4);
        MoviehashMap.put("喜剧", 5);
        MoviehashMap.put("爱情", 6);
        MoviehashMap.put("科幻", 7);
        MoviehashMap.put("悬疑", 8);
        MoviehashMap.put("恐怖", 9);
    }
    /**
     * 设置整个的主题颜色
     * */
    private void setColor() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
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
                Intent intent = new Intent(MovieActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }

}
