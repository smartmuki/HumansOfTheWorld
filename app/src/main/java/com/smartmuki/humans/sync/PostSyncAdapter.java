package com.smartmuki.humans.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartmuki.humans.data.PostsContract;
import com.smartmuki.humans.entities.Post;
import com.smartmuki.humans.humansoftheworld.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by abmitra on 6/28/2015.
 */
public class PostSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String TAG = PostSyncAdapter.class.getSimpleName();
    ContentResolver contentResolver ;
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
       int nextPage =  extras.getInt("nextPage");
        String sort = extras.getString("sortBy");
        doInBackground(nextPage + "",sort,"a670cb5c49630b38e1ca06f0cd82b8eb","api.themoviedb.org");

    }
    protected ArrayList<Post> doInBackground(String pageNumber,String sort,String api_key,String Url) {
        if(!Utility.isInternetAvailable(getContext())){

            return null;
        }
        GraphResponse res = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAndWait();
//        Uri.Builder builder = new Uri.Builder();
//        builder
//                .scheme("http")
//                .authority(Url)
//                .appendPath("3")
//                .appendPath("discover")
//                .appendPath("movie")
//                .appendQueryParameter("sort_by", sort)
//                .appendQueryParameter("page", pageNumber.toString())
//                .appendQueryParameter("api_key", api_key);
//        String jsonStr = getDataFromServer(builder.build().toString());
//        String jsonStr = '{"data":[{"id":"578651408856087_687323097988917","object_id":"687323011322259","message":"'If I could do anything differently I would take more risks and not be too scared. I would go with my heart instead of going with the safe option.''What's the safe option?''Just sticking to what I know or following everybody else. I would go with what I think is best and if it didn't work out I could just go back to where I started and try again.'","full_picture":"https://scontent.xx.fbcdn.net/hphotos-xap1/v/t1.0-9/s720x720/10177475_68732_1927566292692213994_n.jpg?oh=d43968bdbd8b53be169dcc93eac0e101&oe=5648424B"}]}';
        return getPosts("");
    }

    protected ArrayList<Post> getPosts(String jsonStr){
        ArrayList<Post> posts = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray list = jsonObj.getJSONArray("results");
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<Post>>() {
            }.getType();
            posts =(ArrayList<Post>) gson.fromJson(list.toString(), collectionType);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(list.length());
            for(Post post:posts){
                cVVector.add(Utility.changePostToContentValue(post));
            }
            int inserted = 0;
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = getContext().getContentResolver().bulkInsert(PostsContract.PostEntry.CONTENT_URI, cvArray);
            }
            Log.d(TAG, "Fetching Movies Complete. " + inserted + " Inserted");

        } catch (JSONException e ){
            Log.e(TAG, "Internet is probably off", e);
        }
        catch (NullPointerException e){
            Log.e(TAG, "Internet is probably off", e);
        }
        catch (Exception e){
            Log.e(TAG, "Internet is probably off", e);
        }
        return posts;


    }
    protected  String getDataFromServer(String urlStr){
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
            reader = new BufferedReader(new InputStreamReader(inputStream));
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

    protected void closeConnections(HttpURLConnection urlConnection, BufferedReader reader){
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
    public PostSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
       contentResolver = context.getContentResolver();

    }


}
