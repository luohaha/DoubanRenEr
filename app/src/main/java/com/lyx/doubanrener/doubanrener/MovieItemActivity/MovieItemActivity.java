package com.lyx.doubanrener.doubanrener.MovieItemActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lyx.doubanrener.doubanrener.R;

/**
 * Created by root on 15-6-21.
 */
public class MovieItemActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_movie_item_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.activity_movie_item_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("haha");
    }
}
