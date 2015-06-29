package com.lyx.doubanrener.doubanrener.MovieActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lyx.doubanrener.doubanrener.R;
import com.lyx.doubanrener.doubanrener.SearchActivity.SearchActivity;

/**
 * Created by root on 15-6-21.
 */
public class MovieActivity extends ActionBarActivity {

    ViewPager pager;
    private String titles[] = new String[]{"top250", "热门", "华语",
    "欧美", "动作", "喜剧", "爱情", "科幻", "悬疑", "恐怖"};
    private Toolbar toolbar;

    TabLayout slidingTabLayout;

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
                }
            });
            toolbar.setTitle("分类");
        }
        /**
         * 获取viewpager,和tabs的布局
         * */
        pager = (ViewPager) findViewById(R.id.activity_movie_viewpager);
        slidingTabLayout = (TabLayout) findViewById(R.id.activity_movie_sliding_tabs);
        pager.setAdapter(new MovieViewPagerAdapter(getSupportFragmentManager(), titles));
        pager.setCurrentItem(getIntent().getIntExtra("page", 0));

        slidingTabLayout.setupWithViewPager(pager);
        slidingTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        /**
         * 设置整个的主题颜色
         * */
        setColor();

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
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
