package org.kazin.timelike.user;

import android.util.Log;

import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ModelUser   {

    private static ModelUser model;
    private ViewerUser viewer;
    private BackendManager mBackend;
    private ArrayList<ImageTimelike> mFeedLastState;
    private String mUserId;

    public ModelUser() {
        mBackend = BackendManager.getInstance();
    }

    public static ModelUser getInstance() {
        if(model == null){
            model = new ModelUser();
        }
        return model;
    }

    public void setViewer(ViewerUser viewer){
        this.viewer = viewer;
    }

    public void onLaunch(String username) {
        mUserId = username;
        loadFeed(false);
    }

    public void onReloadFeed(){
        loadFeed(true);
    }

    public void onUpdateFeed(){
        loadFeedUpdate();
    }

    private void loadFeed(boolean reload){
        if(mBackend.checkInstLoggedIn()){
            if(mFeedLastState!=null && !reload){
                viewer.setUserFeed(mFeedLastState);
                return;
            }
            mBackend.getUserFeed(mUserId, new BackendManager.GetFeedClk() {
                @Override
                public void success(ArrayList<ImageTimelike> feed) {
                    mFeedLastState = feed;
                    viewer.setUserFeed(feed);
                    mBackend.getFeedTimeLikes(feed, new BackendManager.GetFeedTimelikes() {
                        @Override
                        public void success(ImageTimelike image) {
                            setTimelike(image);
                        }

                        @Override
                        public void error(String error) {
                            Log.d("apkapk", "GetFeedTimeLikes error: " + error);
                        }
                    });
                }

                @Override
                public void error(String error) {
                    Log.d("apkapk", "Error Logging instagram: " + error);
                }
            });
        }
        else {
            mBackend.LoginInst(new BackendManager.LoginInstClk() {
                @Override
                public void success(UserTimelike user) {
                    Log.d("apkapk","InstUser logged in: "+user.username);
                    loadFeed(false);
                }

                @Override
                public void error(String error) {
                    Log.d("apkapk", "Error logging "+error);
                }
            });
            mBackend.LoginFireAnon(new BackendManager.LoginFireAnonClk() {
                @Override
                public void success() {
                    Log.d("apkapk", "FireBase Loggedin anono successfully");
                }

                @Override
                public void error(String error) {
                    Log.d("apkapk", "FireBase login Anon error: " + error);
                }
            });
        }
    }

    private void loadFeedUpdate(){
        if(mBackend.checkInstLoggedIn()){
            mBackend.getUserFeed(mUserId, new BackendManager.GetFeedClk() {
                @Override
                public void success(ArrayList<ImageTimelike> feed) {
                    updateFeed(feed);
                    mBackend.getFeedTimeLikes(feed, new BackendManager.GetFeedTimelikes() {
                        @Override
                        public void success(ImageTimelike image) {
                            setTimelike(image);
                        }

                        @Override
                        public void error(String error) {
                            Log.d("apkapk", "GetFeedTimeLikes error: " + error);
                        }
                    });
                }

                @Override
                public void error(String error) {
                    Log.d("apkapk", "Error Logging instagram: " + error);
                }
            });
        }
    }


    public void setTimelike(ImageTimelike image){
        viewer.setTimelike(image);
    }

    public void updateFeed(ArrayList<ImageTimelike> image){
        viewer.updateFeed(image);
    }

    public SimpleCallback getEndFeedListener() {
        return new SimpleCallback(){

            @Override
            public void success(Object object) {
                //object is current page int
                loadFeedUpdate();
            }

            @Override
            public void fail(Object object) {

            }
        };
    }
}
