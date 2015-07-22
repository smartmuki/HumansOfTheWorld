package com.smartmuki.humans.humansoftheworld;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.smartmuki.humans.data.PostsContract;
import com.smartmuki.humans.entities.Post;

/**
 * Created by abmitra on 7/21/2015.
 */
public class Utility {
    private Utility(){

    }
    public static ContentValues changePostToContentValue(Post post){
        ContentValues postDetails = new ContentValues();
        postDetails.put(PostsContract.PostEntry.COLUMN_ID,post.getId());
        postDetails.put(PostsContract.PostEntry.COLUMN_OBJECT_ID,post.getObject_id());
        postDetails.put(PostsContract.PostEntry.COLUMN_MESSAGE,post.getMessage());
        postDetails.put(PostsContract.PostEntry.COLUMN_PICTURE, post.getFull_pictureUrlString());
        return postDetails;
    }
    public static boolean isInternetAvailable(Context c){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
