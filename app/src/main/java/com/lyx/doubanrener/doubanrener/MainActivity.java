package com.lyx.doubanrener.doubanrener;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;

import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.Fragment.ViewPagerAdapter;
import com.lyx.doubanrener.doubanrener.MaterialDesign.RoundImageView;
import com.lyx.doubanrener.doubanrener.MultiDisplayActivity.MultiDisplayActivity;
import com.lyx.doubanrener.doubanrener.SearchActivity.SearchActivity;
import com.lyx.doubanrener.doubanrener.SettingActivity.SettingActivity;
import com.lyx.doubanrener.doubanrener.SettingActivity.VersionActivity;


public class MainActivity extends AppCompatActivity {

    private long exitTime;

    private ImageView mBack;
    /**
     * user
     * */
    private RoundImageView mUserHeadImage;
    private TextView mUserName;

    private String mBackImage;
    private String mUserImage;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView mNavigationView;

    ViewPager pager;
    private String titles[] = new String[]{"电影", "计划"};
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

        mNavigationView = (NavigationView)findViewById(R.id.nv_main_navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_movies:
                        Intent intent = new Intent(MainActivity.this, MultiDisplayActivity.class);
                        intent.putExtra("type", "movies");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                        break;
                    case R.id.nav_peoples:
                        Intent intent2 = new Intent(MainActivity.this, MultiDisplayActivity.class);
                        intent2.putExtra("type", "peoples");
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                        break;
                    case R.id.nav_records:
                        Intent intent3 = new Intent(MainActivity.this, MultiDisplayActivity.class);
                        intent3.putExtra("type", "records");
                        startActivity(intent3);
                        overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                        break;
                    case R.id.nav_setting:
                        Intent intent4 = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                        break;
                    case R.id.nav_version:
                        Intent intent5 = new Intent(MainActivity.this, VersionActivity.class);
                        startActivity(intent5);
                        overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                        break;
                }
                return true;
            }
        });

        /**
         * 设置整个的主题颜色
         * */
        setColor();

        mBack = (ImageView) findViewById(R.id.nav_head_backgroud);
        DatabaseClient databaseClient = new DatabaseClient(this);
        Cursor cursor = databaseClient.queryLastPage("donepage", "1");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                mBackImage = cursor.getString(cursor.getColumnIndex("image"));
            }
            Ion.with(mBack)
                    .placeholder(R.color.colorPrimaryDark)
                    .error(R.color.colorPrimaryDark)
                    .load(mBackImage);
        } else {
            mBack.setBackgroundResource(R.color.colorPrimaryDark);
        }
        cursor.close();
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
                overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {

            if((System.currentTimeMillis()-exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
