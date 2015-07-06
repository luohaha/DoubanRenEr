package com.lyx.doubanrener.doubanrener.Widget;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.lyx.doubanrener.doubanrener.DbModule.DatabaseClient;
import com.lyx.doubanrener.doubanrener.MovieItemActivity.MovieItemActivity;

/**
 * Created by root on 15-7-6.
 */
public class HamdlerService extends Service {
    public static final String LISTVIEW_REFRESH_ACTION= "com.doubanrener.test.LISTVIEW_REFRESH_ACTION";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String movieid = intent.getStringExtra("movie_id");
        if (intent.getStringExtra("type").equals("textview")) {
            Intent intent_activity = new Intent(this, MovieItemActivity.class);
            intent_activity.putExtra("movie_id", movieid);
            intent_activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent_activity);
        } else if (intent.getStringExtra("type").equals("imageview")) {
            String image = intent.getStringExtra("image");
            String name = intent.getStringExtra("name");
            DatabaseClient databaseClient = new DatabaseClient(this);
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
            /**
             * 广播通知widget
             * */
            Intent widget_intent = new Intent().setAction(LISTVIEW_REFRESH_ACTION);
            this.sendBroadcast(widget_intent);

            Toast.makeText(this, "你已经观看 " + name + " 啦!", Toast.LENGTH_SHORT).show();
        }
        onDestroy();
        return super.onStartCommand(intent, flags, startId);
    }
}
