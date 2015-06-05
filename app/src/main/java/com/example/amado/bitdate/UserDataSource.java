package com.example.amado.bitdate;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amado on 03/06/2015.
 */
public class UserDataSource {
    private static User sCurrentUser;
    private static final String COLUMN_FIRST_NAME= "firstName";
    private static final String COLUMN_PICTURE_URL = "pictureURL";


    public static User getCurrentUser(){
        if(sCurrentUser == null&& ParseUser.getCurrentUser()!= null){

            sCurrentUser = parseUsertoUser(ParseUser.getCurrentUser());

        }
        return sCurrentUser;
    }

    public static void getUnSeenUsers(final UserDataCallbacks callbacks){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", getCurrentUser().getId());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e == null) {
                    List<User> users = new ArrayList<User>();
                    for (ParseUser user : list){
                        User newUser = parseUsertoUser(user);
                        users.add(newUser);
                    }
                    if(callbacks != null) {
                        callbacks.onUsersFetched(users);
                    }
                }
            }
        });
    }


    private static User parseUsertoUser(ParseUser parseUser){
        User user = new User();
        user.setFirstName(parseUser.getString(COLUMN_FIRST_NAME));
        user.setPictureURL(parseUser.getString(COLUMN_PICTURE_URL));
        user.setId(parseUser.getObjectId());
        return user;
    }

    public interface UserDataCallbacks{
        public void onUsersFetched(List<User> users);
    }
}
