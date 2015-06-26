package com.lyx.doubanrener.doubanrener;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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


import com.lyx.doubanrener.doubanrener.Fragment.ViewPagerAdapter;
import com.lyx.doubanrener.doubanrener.SearchActivity.SearchActivity;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    ViewPager pager;
    private String titles[] = new String[]{"电影", "读书", "音乐", "其他"};
    private Toolbar toolbar;

    TabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * drawerlayout 整个的头布局
         * drawerlist 左侧抽屉列表
         * toolbar 标题栏
         * */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //设置左上角图标
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);

        }
        /**
         * 获取viewpager,和tabs的布局
         * */
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        slidingTabLayout.setupWithViewPager(pager);

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);

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
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}
