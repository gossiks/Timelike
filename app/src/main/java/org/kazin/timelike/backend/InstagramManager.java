package org.kazin.timelike.backend;

import android.content.Context;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramRequest;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.kazin.timelike.MainActivity;
import org.kazin.timelike.R;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 06.07.2015.
 */
public class InstagramManager {

    private static InstagramManager manager;
    private Instagram mInstagram;
    private InstagramSession mInstagramSession;
    private Context mContext;


    private InstagramManager(){
        mContext = TimelikeApp.getContext();
        createConnection();
    }

    private void createConnection( ){
        mInstagram = new Instagram(MainActivity.getMainActivity(),
                mContext.getApplicationContext().getString(R.string.instagram_client_id),
                mContext.getString(R.string.instagram_client_secret),
                mContext.getString(R.string.instagram_callback_url));
        mInstagramSession = mInstagram.getSession();

    }

    public static InstagramManager getInstance() {
        if(manager == null){
            return manager = new InstagramManager();
        }
        else {
            return manager;
        }
    }

    public void getFeed(final BackendManager.GetFeedClk callback) {
        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("count", "10")); //TODO remember to chang 10
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.feed_user_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    callback.success(parseFeed(response));
                }
                else {
                    callback.error("empty response");
                }
            }

            @Override
            public void onError(String error) {
                callback.error(error);
            }
        });
    }

    public void login(final BackendManager.LoginInstClk callback) {
        mInstagram.authorize(new Instagram.InstagramAuthListener() {
            @Override
            public void onSuccess(InstagramUser user) {
                UserTimelike userTimelike = new UserTimelike();
                userTimelike.accessToken = user.accessToken;
                userTimelike.fullName = user.fullName;
                userTimelike.id = user.id;
                userTimelike.profilPicture = user.profilPicture;
                userTimelike.username = user.username;
                callback.success(userTimelike);
            }

            @Override
            public void onError(String error) {
                callback.error(error);
            }

            @Override
            public void onCancel() {
                callback.error("canceled");
            }
        });
    }

    public UserTimelike getCurrentUser(){
        InstagramUser user =  mInstagramSession.getUser();
        if(user==null){return null;}

        UserTimelike userTL = new UserTimelike(
                user.id, user.username, user.fullName,
                user.profilPicture, user.accessToken
        ) ;
        return userTL;
    }



    //misc methods
    private ArrayList<ImageTimelike> parseFeed(String response){
        ArrayList<ImageTimelike> feed = new ArrayList<>();
        try {
            JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray jsonData = json.getJSONArray("data");
            int length = jsonData.length();
            if (length > 0) {

                for (int i = 0; i < length; i++) {
                    JSONObject img = jsonData.getJSONObject(i);
                    ArrayList<ImageTimelike.Comment> comments =  parseComments(img.getJSONObject("comments"));
                    ImageTimelike imageTimelike = new ImageTimelike(
                            img.getJSONObject("images")
                                    .getJSONObject("standard_resolution").getString("url"),
                            img.getString("id"), img.getJSONObject("user").getString("username"), 0L,
                            img.getJSONObject("user").getString("profile_picture"), img.getJSONObject("caption").getString("text")
                            , img.getString("type"), comments);
                    feed.add(imageTimelike);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return feed;
    }

    private ArrayList<ImageTimelike.Comment> parseComments(JSONObject json) throws JSONException {
        JSONArray comments = json.getJSONArray("data");
        JSONObject cmnt;
        ArrayList<ImageTimelike.Comment> response = new ArrayList<>(comments.length());

        for(int i = 0; i<comments.length(); i++){
            cmnt =  comments.getJSONObject(i);
            ImageTimelike.Comment comment;
            comment = new ImageTimelike.Comment(cmnt.getJSONObject("from").getString("username"),
                    cmnt.getJSONObject("from").getString("profile_picture"),
                    cmnt.getString("text"), cmnt.getLong("created_time"));
            response.add(comment);
        }
        return response;
    }


}
