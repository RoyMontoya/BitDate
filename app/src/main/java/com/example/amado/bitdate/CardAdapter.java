package com.example.amado.bitdate;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amado on 03/06/2015.
 */
public class CardAdapter extends ArrayAdapter<User> {
    public CardAdapter(Context context, List<User> users) {
        super(context, R.layout.card, R.id.name, users);
    }

    @Override
    public CardView getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
       CardView v = (CardView)super.getView(position, convertView, parent);

       TextView nameView = (TextView)v.findViewById(R.id.name);
        nameView.setText(user.getFirstName());
        ImageView image = (ImageView)v.findViewById(R.id.user_photo);
        Picasso.with(getContext()).load(user.getLargePictureURL()).into(image);
        return v;

    }
}
