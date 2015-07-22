package com.smartmuki.humans.entities;

import android.database.Cursor;

import com.smartmuki.humans.humansoftheworld.Constants;

import java.net.URL;

/**
 * Created by abmitra on 7/21/2015.
 */
public class Post {
    private long _ID;
    private String id;
    private long object_id;
    private String message;
    private String full_picture;

    public Post(String id, long object_id,String message,String full_picture){
        this.id = id;
        this.object_id = object_id;
        this.message = message;
        this.full_picture = full_picture;
    }
    public Post(Cursor c){
        this(c.getString(Constants.COL_POST_FB_ID),
                c.getLong(Constants.COL_POST_OBJECT_ID),
                c.getString(Constants.COL_POST_MESSAGE),
                c.getString(Constants.COL_POST_PICTURE));
        this._ID = c.getLong(Constants.COL_POST_ID);
    }
    public long get_ID() {
        return _ID;
    }

    public String getId() {
        return id;
    }

    public long getObject_id() {
        return object_id;
    }

    public String getMessage() {
        return message;
    }

    public String getFull_pictureUrlString() {
        return full_picture;

    }
    public URL getFull_picture() {
        try{
            URL url = new URL(full_picture);
            return url;
        } catch (Exception e){
            return null;
        }

    }
}
