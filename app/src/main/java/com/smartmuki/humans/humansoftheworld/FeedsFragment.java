package com.smartmuki.humans.humansoftheworld;


import android.accounts.Account;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.smartmuki.humans.data.PostsContract;
import com.smartmuki.humans.entities.Post;
import com.smartmuki.humans.sync.RecyclerFeedAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class FeedsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>,SwipeRefreshLayout.OnRefreshListener{


//    @Bind(R.id.material_listview)
//    MaterialListView mListView ;

    Account mAccount;
    String pref_name = "firstRun";
    private static final int FEED_LOADER = 0;
    RecyclerFeedAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeLayout;
    int index = 0;

    public FeedsFragment() {
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder;
        switch (id){
            case FEED_LOADER:
                return new CursorLoader(getActivity(), PostsContract.PostEntry.buildUriForPosts(), Constants.POST_COLUMNS, null, null, Constants.POST_COLUMNS[Constants.COL_CREATED_DATE] + " DESC");
            default: throw new UnsupportedOperationException("No such loader");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            index = savedInstanceState.getInt("index",0);
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        final Activity currentActivity = this.getActivity();
        recyclerView = (RecyclerView) currentActivity.findViewById(R.id.feed_recycler_view_id);
        if(layoutManager==null){
            layoutManager = new LinearLayoutManager(currentActivity);
            recyclerView.setLayoutManager(layoutManager);
        }

        if(index ==0){
            index  = layoutManager.findFirstVisibleItemPosition();
        }



        final ArrayList<Post> posts = new ArrayList<Post>();
        if(data!=null){
            while (data.moveToNext()){
                Post post = new Post(data);
                posts.add(post);
            }
        }

        adapter = new RecyclerFeedAdapter(this.getActivity(), posts);
        recyclerView.setAdapter(adapter);
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }



                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Post post = posts.get(position);
                                    post.setIs_favorite(true);
                                    getActivity().getContentResolver().update(PostsContract.PostEntry.buildUriForPost(posts.get(position).get_ID()), Utility.changePostToContentValue(post),"_id=" + post.get_ID(),null);
                                    posts.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(currentActivity, "Added to favourites.", Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Post post = posts.get(position);
                                    post.setIsDeleted(true);
                                    getActivity().getContentResolver().update(PostsContract.PostEntry.buildUriForPost(posts.get(position).get_ID()), Utility.changePostToContentValue(post),"_id=" + post.get_ID(),null);
                                    posts.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(currentActivity, "Deleted from your feed.", Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
        layoutManager.scrollToPosition(index);
        index = 0;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Bundle settingsBundle = new Bundle();
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_MANUAL, true);
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                ContentResolver.requestSync(mAccount, PostsContract.CONTENT_AUTHORITY, settingsBundle);
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }

    @Override
    public void onStart() {
        if(getLoaderManager().getLoader(FEED_LOADER)==null){
            getLoaderManager().initLoader(FEED_LOADER, null, this);
        } else {
            getLoaderManager().restartLoader(FEED_LOADER, null, this);

        }

        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        mAccount = Utility.CreateSyncAccount(this.getActivity());
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        SharedPreferences prefs = getActivity().getSharedPreferences(
                PostsContract.CONTENT_AUTHORITY, Context.MODE_PRIVATE);

        if(prefs.getBoolean(pref_name,false)){
        } else {
            ContentResolver.requestSync(mAccount, PostsContract.CONTENT_AUTHORITY, settingsBundle);
            prefs.edit().putBoolean(pref_name,true).apply();
        }

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onPause() {
        if(layoutManager!=null){

            index = layoutManager.findFirstVisibleItemPosition();
        }

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index",index);
        super.onSaveInstanceState(outState);
    }
}
