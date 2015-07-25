package com.smartmuki.humans.humansoftheworld;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.smartmuki.humans.data.PostsContract;
import com.smartmuki.humans.entities.Post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by abmitra on 7/21/2015.
 */
public class Utility {
    private Utility(){

    }
    private final static String TAG= "Utility";
    public static ContentValues changePostToContentValue(Post post){
        ContentValues postDetails = new ContentValues();
        postDetails.put(PostsContract.PostEntry.COLUMN_ID,post.getId());
        postDetails.put(PostsContract.PostEntry.COLUMN_OBJECT_ID,post.getObject_id());
        postDetails.put(PostsContract.PostEntry.COLUMN_MESSAGE,post.getMessage());
        postDetails.put(PostsContract.PostEntry.COLUMN_PICTURE, post.getFull_pictureUrlString());
        postDetails.put(PostsContract.PostEntry.COLUMN_PAGE_ID, post.getPage_id());
        postDetails.put(PostsContract.PostEntry.COLUMN_PAGE_TITLE, post.getPage_title());
        postDetails.put(PostsContract.PostEntry.COLUMN_CREATED_TIME, post.getCreated_time());
        return postDetails;
    }
    public static boolean isInternetAvailable(Context c){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static   String getDataFromServer(String urlStr){
        HttpURLConnection urlConnection = null;
        String result = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream==null){
                result = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
            String line;
            while ((line=reader.readLine())!=null){
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0){
                result = null;
            }

            result = buffer.toString();

        } catch (MalformedURLException e){
            Log.e(TAG, "Url is bad", e);

        }catch (IOException e){
            Log.e(TAG, "IO Exception", e);

        }catch (Exception e){
            Log.e(TAG, "Something went wrong", e);
        }
        finally {
            closeConnections(urlConnection,reader);
        }
        return result;
    }
    private static void closeConnections(HttpURLConnection urlConnection, BufferedReader reader){
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error closing stream", e);
            }
        }
    }
}
