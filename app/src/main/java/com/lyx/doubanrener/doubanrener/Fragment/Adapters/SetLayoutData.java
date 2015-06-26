package com.lyx.doubanrener.doubanrener.Fragment.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-6-25.
 */
public class SetLayoutData {
    private Context mContext;
    private View mView;
    private ArrayList<HashMap<String, Object>> mList;

    public SetLayoutData(Context context, View mView, ArrayList<HashMap<String, Object>> mList) {
        this.mContext = context;
        this.mView = mView;
        this.mList = mList;
    }

    public void startDraw() {
        SetItemData setItemData = null;
        View view = mView.findViewById(R.id.love_item_1);
        setItemData = new SetItemData(this.mContext, view, mList.get(0));
        setItemData.begin();

        view = mView.findViewById(R.id.love_item_2);
        setItemData = new SetItemData(this.mContext, view, mList.get(1));
        setItemData.begin();

        view = mView.findViewById(R.id.love_item_3);
        setItemData = new SetItemData(this.mContext, view, mList.get(2));
        setItemData.begin();

        view = mView.findViewById(R.id.love_item_4);
        setItemData = new SetItemData(this.mContext, view, mList.get(3));
        setItemData.begin();

        view = mView.findViewById(R.id.love_item_5);
        setItemData = new SetItemData(this.mContext, view, mList.get(4));
        setItemData.begin();

        view = mView.findViewById(R.id.love_item_6);
        setItemData = new SetItemData(this.mContext, view, mList.get(5));
        setItemData.begin();
    }
}
