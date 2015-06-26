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
import org.kazin.timelike.object.SimpleCallback;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 16.06.2015.
 */
public class InstagramManager {
    private static InstagramManager manager;

    private Context mContext;
    private Instagram mInstagram;
    private InstagramSession mInstagramSession;

    private InstagramManager() {
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

    public void initialize(){
            createConnection();
    }

    public boolean isInstagramActive(){
        return mInstagramSession.isActive();
    }

    public void authorize(BackendManager.InstagramAutorizeClk callback){
        mInstagram.authorize(new InstagramAuthLstn(callback));
    }


    public String getInstagramUserName(){
        return  mInstagramSession.getUser().username;
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

    public void getFeed(final BackendManager.BackendGetFeedClk callback){
        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("count", "10"));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.feed_user_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    callback.successInst(parseFeed(response));
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    //misc for other

    private class InstagramAuthLstn implements Instagram.InstagramAuthListener{

        private BackendManager.InstagramAutorizeClk mInstagramAutorizeClk;

        public InstagramAuthLstn(BackendManager.InstagramAutorizeClk mInstagramAutorizeClk) {
            this.mInstagramAutorizeClk = mInstagramAutorizeClk;
        }

        @Override
        public void onSuccess(InstagramUser user) {
            UserTimelike userTimelike = new UserTimelike();
            userTimelike.accessToken = user.accessToken;
            userTimelike.fullName = user.fullName;
            userTimelike.id = user.id;
            userTimelike.profilPicture = user.profilPicture;
            userTimelike.username = user.username;

            mInstagramAutorizeClk.success(userTimelike);
        }

        @Override
        public void onError(String error) {
            mInstagramAutorizeClk.error(error);
        }

        @Override
        public void onCancel() {
            mInstagramAutorizeClk.cancel();
        }
    }

    //misc outer
    public String getToken(){
        return mInstagramSession.getAccessToken();
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
                                    .getJSONObject("low_resolution").getString("url"),
                            img.getString("id"), img.getJSONObject("user").getString("username"), img.getJSONObject("likes").getLong("count"),
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
