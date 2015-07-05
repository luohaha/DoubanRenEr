package com.lyx.doubanrener.doubanrener.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.MainActivity;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;
import com.lyx.doubanrener.doubanrener.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 15-7-4.
 */
public class AppWidget extends AppWidgetProvider {


    public static final String PLUS_ACTION = "com.doubanrener.test.PLUS_ACTION";
    public static final String LISTVIEW_ITEM_ACTION = "com.doubanrener.test.LISTVIEW_ITEM_ACTION";

    public static final String LISTVIEW_REFRESH_ACTION= "com.doubanrener.test.LISTVIEW_REFRESH_ACTION";


    /** package */
    static ComponentName getComponentName(Context context) {
        return new ComponentName(context, AppWidget.class);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // TODO Auto-generated method stub

        performUpdate(context, appWidgetManager, appWidgetIds, null);
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    private void performUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds, long[] changedEvents) {

        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setRemoteAdapter(R.id.widget_listview, intent);

            /**
             * 设置响应 “按钮(plus)” 的intent
             * */
            Intent intent_plus = new Intent().setAction(PLUS_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent_plus, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_plus, pendingIntent);
            /**
             * 设置响应 “listView” 的intent模板
             * */
            Intent intent_listview_item = new Intent().setAction(LISTVIEW_ITEM_ACTION);
            intent_listview_item.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent_listview_item = PendingIntent.getBroadcast(context, 0, intent_listview_item, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_listview, pendingIntent_listview_item);


            awm.updateAppWidget(appWidgetId, views);
//            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName cmpName = new ComponentName(context, AppWidget.class);


        if (action.equals(PLUS_ACTION)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (action.equals(LISTVIEW_ITEM_ACTION)) {
            String type = intent.getStringExtra("type");
            String movieid = intent.getStringExtra("movie_id");
            if (type.equals("textview")) {
                Intent intent1 = new Intent(context, MovieItemActivity.class);
                intent1.putExtra("movie_id", movieid);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);

            } else if (type.equals("imageview")) {
                String image = intent.getStringExtra("image");
                String name = intent.getStringExtra("name");
                DatabaseClient databaseClient = new DatabaseClient(context);
                databaseClient.deleteData("todopage", "doubanid=?", new String[]{movieid});
                /**
                 * insert done page
                 * */
                ContentValues values = new ContentValues();

                values.put("image", image);
                values.put("doubanid", movieid);
                values.put("name", name);
                values.put("islove", "no");
                databaseClient.insertData("donepage", values);

                int[] appIds = appWidgetManager.getAppWidgetIds(cmpName);
                appWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.widget_listview);

                Toast.makeText(context, "你已经观看 "+name+ " 啦!", Toast.LENGTH_SHORT).show();
            }
        } else if (action.equals(LISTVIEW_REFRESH_ACTION)) {
            int[] appIds = appWidgetManager.getAppWidgetIds(cmpName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.widget_listview);
        }
        super.onReceive(context, intent);
    }
}
