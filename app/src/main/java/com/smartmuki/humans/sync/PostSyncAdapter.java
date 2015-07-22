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

import java.lang.reflect.Type;
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
        doInBackground();

    }
    protected ArrayList<Post> doInBackground() {
        Log.e(TAG,"I am executing");
//        if(!Utility.isInternetAvailable(getContext())){
//
//            return null;
//        }
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,object_id,message,description,full_picture,source");
        GraphResponse res = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/HumansofNotts/posts",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAndWait();
        return getPosts(res.getJSONObject());
    }

    protected ArrayList<Post> getPosts( JSONObject jsonObj){
        ArrayList<Post> posts = null;
        try {
            JSONArray list = jsonObj.getJSONArray("data");
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


    public PostSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
       contentResolver = context.getContentResolver();

    }


}
