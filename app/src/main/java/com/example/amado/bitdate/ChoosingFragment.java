package com.example.amado.bitdate;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChoosingFragment extends android.app.Fragment implements UserDataSource.UserDataCallbacks {

    private CardStackContainer mCardStack;
    private List<User> mUsers;
    private CardAdapter mCardAdapter;

    public ChoosingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mCardStack = (CardStackContainer)v.findViewById(R.id.card_stack);
        UserDataSource.getUnSeenUsers(this);
        mUsers = new ArrayList<>();
        mCardAdapter = new CardAdapter(getActivity(), mUsers);
        mCardStack.setAdapter(mCardAdapter);
        ImageButton nahButton = (ImageButton)v.findViewById(R.id.nah_button);
        nahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mCardStack.swipeLeft();
            }
        });
        ImageButton yeahButton = (ImageButton)v.findViewById(R.id.yeah_button);
        yeahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStack.swipeRight();
            }
        });

        return v;
    }

    @Override
    public void onUsersFetched(List<User> users) {
      mUsers.addAll(users);
        mCardAdapter.notifyDataSetChanged();
    }
}
