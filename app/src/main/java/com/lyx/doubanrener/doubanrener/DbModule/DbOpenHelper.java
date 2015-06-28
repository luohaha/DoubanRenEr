package com.lyx.doubanrener.doubanrener.DbModule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 15-6-27.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    /**
     * data base's name and version
     * */
    private static String mDataBaseName = "DoubanRenEr.db";
    private static int mDataBaseVersion = 1;
    private static DbOpenHelper mDbOpenHelper;
    /**
     * using the singleton
     * */
    public static synchronized DbOpenHelper getInstance (Context context) {
        if (mDbOpenHelper == null) {
            mDbOpenHelper = new DbOpenHelper(context.getApplicationContext());
        }
        return mDbOpenHelper;
    }
    public DbOpenHelper(Context context) {
        super(context, mDataBaseName, null, mDataBaseVersion);
    }
    /**
     * create new data base, it will exec the first time, and it won't start next time,
     * we can use it to init
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table todopage(id integer primary key autoincrement, name varchar(64)," +
                " image varchar(64), doubanid varchar(64))";
        db.execSQL(sql);
        sql = "create table donepage(id integer primary key autoincrement, name varchar(64)," +
                " image varchar(64), doubanid varchar(64))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
