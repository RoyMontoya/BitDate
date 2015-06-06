package com.example.amado.bitdate;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
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
    private static final String COLUMN_FACEBOOK_ID = "FacebookId";


    public static User getCurrentUser(){
        if(sCurrentUser == null&& ParseUser.getCurrentUser()!= null){

            sCurrentUser = parseUserToUser(ParseUser.getCurrentUser());

        }
        return sCurrentUser;
    }

    public static void getUnSeenUsers(final UserDataCallbacks callbacks){
        ParseQuery<ParseObject> seenUsersQuery = new ParseQuery<ParseObject>(ActionDataSource.TABLE_NAME);
        seenUsersQuery.whereNotEqualTo(ActionDataSource.COLUMN_BY_USER, ParseUser.getCurrentUser().getObjectId());
        seenUsersQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null){
                    List<String> ids = new ArrayList<String>();
                    for(ParseObject parseObject: list){
                       ids.add(parseObject.getString(ActionDataSource.COLUMN_TO_USER));
                    }
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereNotEqualTo("objectId", getCurrentUser().getId());
                    query.whereContainedIn("objectid", ids);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            if(e == null) {
                                List<User> users = new ArrayList<User>();
                                for (ParseUser user : list){
                                    User newUser = parseUserToUser(user);
                                    users.add(newUser);
                                }
                                if(callbacks != null) {
                                    callbacks.onUsersFetched(users);
                                }
                            }
                        }
                    });
                }
            }
        });

    }


    private static User parseUserToUser(ParseUser parseUser){
        User user = new User();
        user.setFirstName(parseUser.getString(COLUMN_FIRST_NAME));
        user.setPictureURL(parseUser.getString(COLUMN_PICTURE_URL));
        user.setFacebookId(parseUser.getString(COLUMN_FACEBOOK_ID));
        user.setId(parseUser.getObjectId());
        return user;
    }

    public interface UserDataCallbacks{
        public void onUsersFetched(List<User> users);
    }
}
