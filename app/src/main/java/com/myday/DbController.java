package com.myday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myday.db.ActivityDBHelper;
import com.myday.db.MydayContract;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kshitij on 11/7/2015.
 */
public class DbController {

    private Context context;
    private SQLiteDatabase database;
    private ActivityDBHelper dbHelper;

    public DbController(Context context) {
        this.context = context;
        this.database = database;
        dbHelper = new ActivityDBHelper(context);
    }

    public void open_write() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void open_read() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        database.close();
    }

    public void createActivityDB(ArrayList<ActivityData> activityDatas) {
        if (activityDatas == null || activityDatas.size() == 0) {
            System.out.println("nothing to insert");
            return;
        }
        ContentValues values;
        try {
            open_write();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbHelper.onUpgrade(database, 0, 0); //upgrade existing db each time
        for (int i = 0; i < activityDatas.size(); i++) {
            values = new ContentValues();
            values.put(MydayContract.Activity_entry.COL_FAV, 0);
            values.put(MydayContract.Activity_entry.COL_NAME, activityDatas.get(i).getName());
            values.put(MydayContract.Activity_entry.COL_CITY, activityDatas.get(i).getCity());
            values.put(MydayContract.Activity_entry.COL_DISCOUNT, activityDatas.get(i).getDiscount());
            values.put(MydayContract.Activity_entry.COL_IMAGE, activityDatas.get(i).getImage_url());
            values.put(MydayContract.Activity_entry.COL_LOCATION, activityDatas.get(i).getLocation());
            values.put(MydayContract.Activity_entry.COL_PRICE, activityDatas.get(i).getActual_price());
            values.put(MydayContract.Activity_entry.COL_RATING, activityDatas.get(i).getRating());
            values.put(MydayContract.Activity_entry.COL_DESC, activityDatas.get(i).getDesc());

            long rowid = database.insert(MydayContract.Activity_entry.TABLE_NAME, null, values);
            System.out.println("inserted with rowID : " + rowid);
        }
    }

    public ArrayList<ActivityData> get_city_wise_data(String city) {
        ArrayList<ActivityData> list = new ArrayList<>();
        ActivityData data;
        Cursor cursor;
        String query = "SELECT * FROM " + MydayContract.Activity_entry.TABLE_NAME + " WHERE " + MydayContract.Activity_entry.COL_CITY
                + " = " + "\"" + city + "\"" + ";";

        cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                data = new ActivityData();
                data.setName(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_NAME)));
                data.setCity(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_CITY)));
                data.setDesc(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_DESC)));
                data.setDiscount(cursor.getFloat(cursor.getColumnIndex(MydayContract.Activity_entry.COL_DISCOUNT)));
                data.setImage_url(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_IMAGE)));
                data.setLocation(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_LOCATION)));
                data.setRating(cursor.getFloat(cursor.getColumnIndex(MydayContract.Activity_entry.COL_RATING)));
                data.setActual_price(cursor.getFloat(cursor.getColumnIndex(MydayContract.Activity_entry.COL_PRICE)));
                list.add(data);
            } while (cursor.moveToNext());
        }
        System.out.println("found elements : " + list.size());
        return list;
    }

    public ArrayList<ActivityData> get_sorted_data_from_db(String sortby, String city) {
        ArrayList<ActivityData> list = new ArrayList<>();
        ActivityData data;
        Cursor cursor;

        String query = "SELECT * FROM " + MydayContract.Activity_entry.TABLE_NAME + " WHERE " + MydayContract.Activity_entry.COL_CITY
                + " = " + "\"" + city + "\"" + " ORDER  BY " + sortby + ";";

        cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                data = new ActivityData();
                data.setName(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_NAME)));
                data.setCity(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_CITY)));
                data.setDesc(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_DESC)));
                data.setDiscount(cursor.getFloat(cursor.getColumnIndex(MydayContract.Activity_entry.COL_DISCOUNT)));
                data.setImage_url(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_IMAGE)));
                data.setLocation(cursor.getString(cursor.getColumnIndex(MydayContract.Activity_entry.COL_LOCATION)));
                data.setRating(cursor.getFloat(cursor.getColumnIndex(MydayContract.Activity_entry.COL_RATING)));
                data.setActual_price(cursor.getFloat(cursor.getColumnIndex(MydayContract.Activity_entry.COL_PRICE)));
                list.add(data);
            } while (cursor.moveToNext());
        }
        System.out.println("found elements : " + list.size());
        return list;
    }

    public boolean set_favorite_to_db(String city, String name) {
        String query = "UPDATE " + MydayContract.Activity_entry.TABLE_NAME + " SET " + MydayContract.Activity_entry.COL_FAV + " = " + 1
                + " WHERE " + MydayContract.Activity_entry.COL_CITY + " = " + "\"" + city + "\""
                + " AND " + MydayContract.Activity_entry.COL_NAME + " = " + "\"" + name + "\"" + ";";
        System.out.println("fav query : " + query);
        Cursor cursor = database.rawQuery(query, null);
        System.out.println("update status : " + cursor.getCount());
        if (cursor.getCount() <= 0)
            return false;
        System.out.println("update status : " + cursor.getCount());
        return true;
    }
}
