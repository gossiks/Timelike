package org.kazin.timelike.fragment.feed;

import android.util.Log;

import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ModelFeed {

    private static ModelFeed model;
    private PresenterFeed presenter;
    private BackendManager mBackend;
    private int mLastItemLoaded;

    public ModelFeed() {
        mBackend = new BackendManager();
    }

    private void setMVP(PresenterFeed presenter){
        this.presenter = presenter;
    }

    public static ModelFeed getInstance(PresenterFeed presenter) {
        if(model == null){
            model = new ModelFeed();
            model.setMVP(presenter);
            return model;
        }
        else{
            return model;
        }
    }

    public void onLaunch() {
        mBackend = BackendManager.getInstance();
        loadFeed();
    }

    public void onClickReload(){
        loadFeed();
    }

    //misc

    private void loadFeed(){
        if(mBackend.checkInstLoggedIn()){
            Log.d("apkapk","Instagram is logged!");
            mBackend.getFeed(new BackendManager.GetFeedClk() {
                @Override
                public void success(ArrayList<ImageTimelike> feed) {
                    presenter.setFeed(feed);
                    mBackend.getFeedTimeLikes(feed, new BackendManager.GetFeedTimelikes() {
                        @Override
                        public void success(ImageTimelike image) {
                            setTimelike(image);
                        }

                        @Override
                        public void error(String error) {
                            Log.d("apkapk", "GetFeedTimeLikes error: "+error);
                        }
                    });
                }

                @Override
                public void error(String error) {
                    Log.d("apkapk","Error Logging instagram: "+error);
                }
            });
        }
        else {
            mBackend.LoginInst(new BackendManager.LoginInstClk() {
                @Override
                public void success(UserTimelike user) {
                    Log.d("apkapk","InstUser logged in: "+user.username);
                    loadFeed();
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
                    Log.d("apkapk","FireBase login Anon error: "+ error);
                }
            });
        }
    }

    private void loadFeedUpdate(){
/* TODO Temporary silenced because of not working update feed download.
        if(mBackend.checkInstLoggedIn()){
            mBackend.getFeedUpdate(new BackendManager.GetFeedClk() {
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
        }*/
    }

    public void setTimelike(ImageTimelike image){
        presenter.setTimelike(image);
    }

    public void updateFeed(ArrayList<ImageTimelike> image){
        presenter.updateFeed(image);
    }

    public void onLikeReceived( String imageid, long timelike){
        mBackend.saveTimelike(imageid, Math.abs(timelike / 1000)); //timelike converts to seconds with 1000
    }


    public SimpleCallback getEndFeedListener() {
        return new SimpleCallback() {
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

    public BackendManager getBackend() {
        if(mBackend == null){
            mBackend = BackendManager.getInstance();
        }
        return mBackend;
    }
}
