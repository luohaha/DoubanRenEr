package com.lyx.doubanrener.doubanrener.Fragment.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.R;

import java.util.HashMap;


/**
 * Created by root on 15-6-25.
 */
public class SetItemData {
    private Context mContext;
    private View mView;
    private HashMap<String, Object> hashMap;

    public SetItemData(Context context, View mView, HashMap<String, Object> map) {
        this.mContext = context;
        this.mView = mView;
        this.hashMap = map;
    }

    public void begin() {
        ImageView imageView = (ImageView)mView.findViewById(R.id.love_item_image);
        Ion.with(imageView)
                .placeholder(R.color.light)
                .error(R.color.grey)
                .load((String) hashMap.get("image"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MovieItemActivity.class);
                intent.putExtra("movie_id", hashMap.get("id").toString());
                mContext.startActivity(intent);
            }
        });
        TextView name = (TextView) mView.findViewById(R.id.love_item_name);
        name.setText(hashMap.get("name").toString());
        TextView rating = (TextView) mView.findViewById(R.id.love_item_rating);
        rating.setText(hashMap.get("rating").toString());
    }
}
