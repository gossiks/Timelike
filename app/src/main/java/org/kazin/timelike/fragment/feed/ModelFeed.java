package org.kazin.timelike.fragment.feed;

import android.util.Log;

import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ModelFeed {

    private static ModelFeed model;
    private PresenterFeed presenter;
    private BackendManager mBackend;

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

    public void setTimelike(ImageTimelike image){
        presenter.setTimelike(image);
    }

    public void onLikeReceived( String imageid, long timelike){
        //TODO save to firebase
        mBackend.saveTimelike(imageid, timelike);
    }



}
