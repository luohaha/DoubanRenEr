package com.lyx.doubanrener.doubanrener.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.koushikdutta.ion.Ion;
import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-7-4.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetFactory(this.getApplicationContext(), intent);
    }

    public static class MyWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;


        private ArrayList<HashMap<String, Object>> mList;

        // 构造
        public MyWidgetFactory(Context context, Intent intent) {
            mContext = context;

        }

        private void getDataViaDb() {
            mList = new ArrayList<HashMap<String, Object>>();
            DatabaseClient databaseClient = new DatabaseClient(mContext);
            Cursor cursor = databaseClient.queryData("todopage", null, null);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("id", cursor.getString(cursor.getColumnIndex("id")));
                hashMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                hashMap.put("image", cursor.getString(cursor.getColumnIndex("image")));
                hashMap.put("doubanid", cursor.getString(cursor.getColumnIndex("doubanid")));
                mList.add(hashMap);
            }
            cursor.close();
        }
        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        // 在调用getViewAt的过程中，显示一个LoadingView。
        // 如果return null，那么将会有一个默认的loadingView
        @Override
        public RemoteViews getLoadingView() {

            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            views.setTextViewText(R.id.widget_item_textView, mList.get(position).get("name").toString());

            Intent intent = new Intent();
            intent.putExtra("type", "textview");
            intent.putExtra("movie_id", mList.get(position).get("doubanid").toString());
            views.setOnClickFillInIntent(R.id.widget_item_textView, intent);

            Intent intent_image = new Intent();
            intent_image.putExtra("type", "imageview");
            intent_image.putExtra("movie_id", mList.get(position).get("doubanid").toString());
            intent_image.putExtra("image", mList.get(position).get("image").toString());
            intent_image.putExtra("name", mList.get(position).get("name").toString());
            views.setOnClickFillInIntent(R.id.widget_item_button, intent_image);
            return views;
        }

        @Override
        public int getViewTypeCount() {

            return 1;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public void onCreate() {
            getDataViaDb();
        }

        @Override
        public void onDataSetChanged() {
            getDataViaDb();
        }

        @Override
        public void onDestroy() {

        }
    }

}
