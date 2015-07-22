package com.smartmuki.humans.humansoftheworld;

import com.smartmuki.humans.data.PostsContract;

/**
 * Created by abmitra on 7/23/2015.
 */
public class Constants {
    private Constants(){

    }
    public static final String[] POST_COLUMNS = {
            PostsContract.PostEntry._ID,
            PostsContract.PostEntry.COLUMN_ID,
            PostsContract.PostEntry.COLUMN_OBJECT_ID,
            PostsContract.PostEntry.COLUMN_MESSAGE,
            PostsContract.PostEntry.COLUMN_PICTURE
    };
    public static final int COL_POST_ID = 0;
    public static final int COL_POST_FB_ID = 1;
    public static final int COL_POST_OBJECT_ID= 2;
    public static final int COL_POST_MESSAGE = 3;
    public static final int COL_POST_PICTURE = 4;
}