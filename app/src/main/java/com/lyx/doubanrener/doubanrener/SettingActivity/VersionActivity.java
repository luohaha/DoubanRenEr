package com.lyx.doubanrener.doubanrener.SettingActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lyx.doubanrener.doubanrener.MaterialDesign.RoundImageView;
import com.lyx.doubanrener.doubanrener.R;

/**
 * Created by root on 15-6-30.
 */
public class VersionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView title;
    private RoundImageView imageview;
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        toolbar = (Toolbar) findViewById(R.id.activity_version_toolbar);
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
            toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
            toolbar.setTitle("版本说明");
        }

        title = (TextView) findViewById(R.id.activity_version_title);
        title.setText("来自 计划电影 的说明");
        imageview = (RoundImageView) findViewById(R.id.activity_version_imageview);
        imageview.setImageResource(R.drawable.ic_launcher);
        textView = (TextView) findViewById(R.id.activity_version_textview);
        textView.setText("当前版本的主要功能有：\n1.电影信息查看，影人信息查看\n2.观影计划制定"+
                "\n预计在下一版本推出在线播放的功能,让体验更人性化!\n谢谢支持！");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }

}
