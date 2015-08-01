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
public class FeedsFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{


//    @Bind(R.id.material_listview)
//    MaterialListView mListView ;

    Account mAccount;
    String pref_name = "firstRun";
    private static final int FEED_LOADER = 0;
    RecyclerFeedAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mListView.clear();
        final Activity currentActivity = this.getActivity();
        recyclerView = (RecyclerView) currentActivity.findViewById(R.id.feed_recycler_view_id);
        layoutManager = new LinearLayoutManager(currentActivity);
        recyclerView.setLayoutManager(layoutManager);

        final ArrayList<Post> posts = new ArrayList<Post>();
        if(data!=null){
            while (data.moveToNext()){
                Post post = new Post(data);
                posts.add(post);
//                BigImageButtonsCard card = new BigImageButtonsCard (getActivity());
//                card.setDescription(post.getMessage());
//                card.setTitle(post.getPage_title());
//                card.setDrawable(post.getFull_pictureUrlString());
//                mListView.add(card);
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
                                    //@ABHIK: update the db here.
                                    posts.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(currentActivity, "Added to favourites.", Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //@ABHIK: update the db here.
                                    posts.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    Toast.makeText(currentActivity, "Deleted from your feed.", Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onStart() {
        getLoaderManager().initLoader(FEED_LOADER, null, this);
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

        return view;
    }

}
