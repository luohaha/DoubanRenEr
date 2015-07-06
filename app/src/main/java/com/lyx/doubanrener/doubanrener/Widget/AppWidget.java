package com.lyx.doubanrener.doubanrener.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
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

            Intent intent_plus = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent_plus, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_plus, pendingIntent);
            /**
             * 设置响应 “listView” 的intent模板
             * */
            Intent intent_listview_item = new Intent(context, HamdlerService.class);
            intent_listview_item.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent_listview_item = PendingIntent.getService(context, 0, intent_listview_item, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_listview, pendingIntent_listview_item);


            awm.updateAppWidget(appWidgetId, views);
//            awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listview);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName cmpName = new ComponentName(context, AppWidget.class);

       if (action.equals(LISTVIEW_REFRESH_ACTION)) {
            int[] appIds = appWidgetManager.getAppWidgetIds(cmpName);
            appWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.widget_listview);
        }
    }



}
