package com.lyx.doubanrener.doubanrener.DbModule;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by root on 15-6-27.
 */
public interface DatabaseService {
    public long insertData(String tableName, ContentValues contentValues);

    public int deleteData(String tableName, String whereClause, String[] whereArgs);

    public int updateData(String tableName, ContentValues contentValues, String whereClause,
                          String[] whereArgs);

    public Cursor queryData(String tableName, String selectionClause, String[] selectionArgs);
}
