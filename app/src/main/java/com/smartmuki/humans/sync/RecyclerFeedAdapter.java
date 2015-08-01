package com.smartmuki.humans.sync;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartmuki.humans.entities.Post;
import com.smartmuki.humans.humansoftheworld.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mukeshag on 08/01/2015.
 */

public class RecyclerFeedAdapter extends RecyclerView.Adapter<RecyclerFeedAdapter.CustomViewHolder>{
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView feedImage;
        protected TextView feedText;
        protected TextView feedSource;

        public CustomViewHolder(View view) {
            super(view);
            this.feedImage = (ImageView) view.findViewById(R.id.feed_photo);
            this.feedText = (TextView) view.findViewById(R.id.feed_text);
            this.feedSource = (TextView) view.findViewById(R.id.feed_source);
        }
    }

    private ArrayList<Post> feedItemList;
    private Context mContext;

    public RecyclerFeedAdapter(Context context, ArrayList<Post> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_post, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Post feedItem = feedItemList.get(i);

        //Download image using picasso library
//        Picasso.with(mContext).load(feedItem.getThumbnail())
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .into(customViewHolder.imageView);

        customViewHolder.feedSource.setText(feedItem.getPage_title());
        customViewHolder.feedText.setText(feedItem.getMessage());
//        customViewHolder.feedImage.setImageDrawable(LoadImageFromWebOperations(feedItem.getFull_pictureUrlString()));
        Picasso.with(mContext).load(feedItem.getFull_pictureUrlString()).into(customViewHolder.feedImage);
    }

    private Drawable LoadImageFromWebOperations(String strPhotoUrl) {
        try {
            InputStream is = (InputStream) new URL(strPhotoUrl).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e("ERRRROR", e.toString());
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
