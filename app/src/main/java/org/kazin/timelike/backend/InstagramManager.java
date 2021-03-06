package org.kazin.timelike.backend;

import android.content.Context;
import android.util.Log;

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
import org.kazin.timelike.main.MainActivity;
import org.kazin.timelike.R;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 06.07.2015.
 */
public class InstagramManager {

    private static InstagramManager manager;
    private final int FEED_IDENTIFIER = 0;
    private final int RECENT_IDENTIFIER = 1;
    private final int USER_FEED_IDENTIFIER = 2;
    private Instagram mInstagram;
    private InstagramSession mInstagramSession;
    private Context mContext;
    String mNextMaxImageIdFeed;
    String mNextMaxImageIdRecent;
    private String mNextMaxUserFeedId;


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
        params.add(new BasicNameValuePair("count", "40"));

        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.feed_user_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {

                    callback.success(parseFeed(response, FEED_IDENTIFIER));
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

    public void getRecentFeed(final BackendManager.GetFeedClk callback) {
        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("count", "10"));

        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.recent_feed_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    callback.success(parseFeed(response, RECENT_IDENTIFIER));
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

    public void getFeedUpdate(final BackendManager.GetFeedClk callback){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("count", "20"));
        params.add(new BasicNameValuePair("max_id", mNextMaxImageIdFeed));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.feed_user_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {

                    ArrayList<ImageTimelike> imagesParsed = parseFeed(response, FEED_IDENTIFIER);
                    callback.success(imagesParsed);
                } else {
                    callback.error("empty response");
                }
            }

            @Override
            public void onError(String error) {
                callback.error(error);
            }
        });
    }

    public void getRecentUpdate(final BackendManager.GetFeedClk callback){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("count", "20"));
        params.add(new BasicNameValuePair("max_id", mNextMaxImageIdRecent));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.recent_feed_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    ArrayList<ImageTimelike> imagesParsed = parseFeed(response, RECENT_IDENTIFIER);
                    callback.success(imagesParsed);
                } else {
                    callback.error("empty response");
                }
            }

            @Override
            public void onError(String error) {
                callback.error(error);
            }
        });
    }

    public void getUserFeed(String userId, final BackendManager.GetFeedClk callback) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("count", "20"));
        //params.add(new BasicNameValuePair("max_id", mNextMaxUserFeedId));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());

        String endpoint = mContext.getString(R.string.user_feed_instagram_api_first_part)+userId
                +mContext.getString(R.string.user_feed_instagram_api_second_part);
        request.createRequest("GET", endpoint, params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    ArrayList<ImageTimelike> imagesParsed = parseFeed(response, USER_FEED_IDENTIFIER);
                    callback.success(imagesParsed);
                } else {
                    callback.error("empty response");
                }
            }

            @Override
            public void onError(String error) {
                callback.error(error);
            }
        });
    }

    public void getUserFeedUpdate(String userId, final BackendManager.GetFeedClk callback) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("count", "20"));
        params.add(new BasicNameValuePair("max_id", mNextMaxUserFeedId));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());

        String endpoint = mContext.getString(R.string.user_feed_instagram_api_first_part)+userId
                +mContext.getString(R.string.user_feed_instagram_api_second_part);
        request.createRequest("GET", endpoint, params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    ArrayList<ImageTimelike> imagesParsed = parseFeed(response, USER_FEED_IDENTIFIER);
                    callback.success(imagesParsed);
                } else {
                    callback.error("empty response");
                }
            }

            @Override
            public void onError(String error) {
                callback.error(error);
            }
        });
    }

    //fortest
    public void testUpdateFeed(){
        getFeedUpdateTEST(0);
        getFeedUpdateTEST(5);
        getFeedUpdateTEST(10);
    }


    public void getFeedUpdateTEST(int startItem){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("max_id",String.valueOf(startItem)));
        params.add(new BasicNameValuePair("count", String.valueOf(10)));

        //params.add(new BasicNameValuePair("MIN_ID", String.valueOf(startItem)));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
        request.createRequest("GET", mContext.getString(R.string.feed_user_instagram_api), params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {

                    ArrayList<ImageTimelike> imagesParsed = parseFeed(response, FEED_IDENTIFIER);
                    Log.d("apkapkTest", "response is: 1st - " + imagesParsed.get(0).getImageId() + ". 2nd - " + imagesParsed.get(1).getImageId());
                } else {
                    Log.d("apkapkTest", "empty response");
                }
            }

            @Override
            public void onError(String error) {
                Log.d("apkapkTest", "error request");
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

    public void getComments(String imageId, final SimpleCallback callback) {
        List<NameValuePair> params = new ArrayList<>();
        //params.add(new BasicNameValuePair("count", "20"));
        InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());

        String endpoint = mContext.getString(R.string.user_comments_instagram_api_first_part)+imageId
                +mContext.getString(R.string.user_comments_instagram_api_second_part);
        request.createRequest("GET", endpoint, params, new InstagramRequest.InstagramRequestListener() {
            @Override
            public void onSuccess(String response) {
                if (!response.equals("")) {
                    ArrayList<ImageTimelike.Comment> comments = parseComments(response);
                    callback.success(comments);
                } else {
                    callback.fail("empty response");
                }
            }

            @Override
            public void onError(String error) {
                callback.fail(error);
            }
        });
    }



    //misc methods

    private ArrayList<ImageTimelike> parseFeed(String response, int nextId){
        ArrayList<ImageTimelike> feed = new ArrayList<>();
        try {
            JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
            try {
                JSONObject paginationData = json.getJSONObject("pagination");
                setNextId(nextId, paginationData.getString("next_max_id"));
            }
            catch (Exception error){
                error.printStackTrace();
            }


            JSONArray jsonData = json.getJSONArray("data");
            int length = jsonData.length();
            if (length > 0) {

                for (int i = 0; i < length; i++) {
                    JSONObject img = jsonData.getJSONObject(i);
                    ArrayList<ImageTimelike.Comment> comments =  parseComments(img.getJSONObject("comments"));

                    String caption;
                    try{
                        caption = img.getJSONObject("caption").getString("text");
                    } catch (JSONException error){
                        caption = "";
                    }


                    ImageTimelike imageTimelike = new ImageTimelike(
                            img.getJSONObject("images")
                                    .getJSONObject("standard_resolution").getString("url"),
                            img.getString("id"), img.getJSONObject("user").getString("username"),img.getJSONObject("user").getString("id"), 0L,
                            img.getJSONObject("user").getString("profile_picture"), caption
                            , img.getString("type"), comments);
                    feed.add(imageTimelike);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return feed;
    }


    private void setNextId(int type, String nextMaxId){
        switch (type){
            case(FEED_IDENTIFIER):
                mNextMaxImageIdFeed = nextMaxId;
                break;
            case(RECENT_IDENTIFIER):
                mNextMaxImageIdRecent = nextMaxId;
                break;
            case(USER_FEED_IDENTIFIER):
                mNextMaxUserFeedId = nextMaxId;
                break;
            default:
                break;
        }
    }

    private ArrayList<ImageTimelike.Comment> parseComments(String response){
        try {
            return parseComments(
                    (JSONObject)new JSONTokener(response).nextValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
                    cmnt.getString("text"), cmnt.getJSONObject("from").getString("id"), cmnt.getLong("created_time"));
            response.add(comment);
        }
        return response;
    }


    public void logOff() {
        mInstagramSession.reset();
    }



}
