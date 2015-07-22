package com.smartmuki.humans.humansoftheworld;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.view.MaterialListView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class PostActivityFragment extends Fragment {

    @Bind(R.id.material_listview)
    MaterialListView mListView ;
    public PostActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        BigImageButtonsCard card = new BigImageButtonsCard (getActivity());
        card.setDescription("Test");
        card.setTitle("Title");
        card.setDrawable(R.mipmap.pic);

        mListView.add(card);
        mListView.add(card);
        mListView.add(card);
        mListView.add(card);
        mListView.add(card);
        return view;
    }
}
