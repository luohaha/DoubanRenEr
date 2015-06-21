package com.lyx.doubanrener.doubanrener.MovieActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lyx.doubanrener.doubanrener.Fragment.ViewPagerAdapter;
import com.lyx.doubanrener.doubanrener.MaterialDesign.SlidingTabLayout;
import com.lyx.doubanrener.doubanrener.R;

/**
 * Created by root on 15-6-21.
 */
public class MovieActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView mDrawerList;
    ViewPager pager;
    private String titles[] = new String[]{"Top250", "热门", "最新", "经典", "豆瓣高分", "冷门佳片", "华语",
    "欧美", "韩国", "日本", "动作", "喜剧", "爱情", "科幻", "悬疑", "恐怖", "治愈"};
    private Toolbar toolbar;

    SlidingTabLayout slidingTabLayout;

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
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
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
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        String[] values = new String[]{
                "登陆/注册", "喜欢", "收藏", "MATERIAL"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        /**
         * 设置整个的主题颜色
         * */
        setColor();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:


                        break;
                    case 2:


                        break;
                    case 3:

                        break;
                }

            }
        });
    }
    /**
     * 设置整个的主题颜色
     * */
    private void setColor() {
        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        mDrawerLayout.closeDrawer(Gravity.START);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
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
