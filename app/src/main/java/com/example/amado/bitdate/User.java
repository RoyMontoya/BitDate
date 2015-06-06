package com.example.amado.bitdate;

import com.parse.ParseUser;

/**
 * Created by Amado on 03/06/2015.
 */
public class User {
    private String mId;
    private String mFirstName;
    private String mPictureURL;
    private String mFacebookId;
    public String getLargePictureURL(){
        return "https://graph.facebook.com/v2.3/"+mFacebookId+"/picture?type=large";
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public void setFacebookId(String facebookId) {
        mFacebookId = facebookId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    public void setPictureURL(String pictureURL) {
        mPictureURL = pictureURL;
    }
}
