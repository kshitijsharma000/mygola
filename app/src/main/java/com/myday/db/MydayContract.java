package com.myday.db;

import android.provider.BaseColumns;

/**
 * Created by Kshitij on 11/7/2015.
 */
public final class MydayContract {
    public MydayContract() {
    }

    public static abstract class Activity_entry implements BaseColumns {
        public static final String TABLE_NAME = "activity_table";
        public static final String COL_FAV = "fav";
        public static final String COL_NAME = "name";
        public static final String COL_IMAGE = "image_url";
        public static final String COL_PRICE = "actual_price";
        public static final String COL_DISCOUNT = "discount";
        public static final String COL_RATING = "rating";
        public static final String COL_CITY = "city";
        public static final String COL_LOCATION = "loc";
        public static final String COL_DESC = "desc";

    }
}
