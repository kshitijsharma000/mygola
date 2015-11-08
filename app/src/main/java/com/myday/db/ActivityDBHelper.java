package com.myday.db;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kshitij on 11/7/2015.
 */
public class ActivityDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "activitydb";

    // Table constants..
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " Integer";
    public static final String FLOAT_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String CREATE_ENTRIES =
            "CREATE TABLE " + MydayContract.Activity_entry.TABLE_NAME + " (" +
                    MydayContract.Activity_entry._ID + " INTEGER PRIMARY KEY," +
                    MydayContract.Activity_entry.COL_FAV + INTEGER_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_NAME + TEXT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_IMAGE + TEXT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_PRICE + FLOAT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_DISCOUNT + FLOAT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_RATING + FLOAT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_CITY + TEXT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_LOCATION + TEXT_TYPE + COMMA_SEP +
                    MydayContract.Activity_entry.COL_DESC + TEXT_TYPE + " );";

    private static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MydayContract.Activity_entry.TABLE_NAME;

    public ActivityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("inside activity db construcor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("inside db on create : " + CREATE_ENTRIES);
        db.execSQL(CREATE_ENTRIES);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("inside db on upgrade");
        db.execSQL(DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
