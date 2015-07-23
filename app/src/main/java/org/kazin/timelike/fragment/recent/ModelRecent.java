package org.kazin.timelike.fragment.recent;

import android.util.Log;

import org.kazin.timelike.MainActivity;
import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ModelRecent {

    private static ModelRecent model;
    private PresenterRecent presenter;

    private BackendManager mBackend;

    private ArrayList<ImageTimelike> mFeedLastState;

    private ModelRecent(){
        mBackend = BackendManager.getInstance();
    }

    private void setMVP(PresenterRecent presenter){
        this.presenter = presenter;
    }

    public static ModelRecent getInstance(PresenterRecent presenter){
        if(model == null){
            model = new ModelRecent();
            model.setMVP(presenter);
        }
        return model;
    }

    public void onLaunch() {
        mBackend = BackendManager.getInstance();
        loadFeed(false);
    }

    public void onClickReload() {
        loadFeed(true);
    }

    private void loadFeed(boolean reload){
        if(mBackend.checkInstLoggedIn()){
            if(mFeedLastState!=null && !reload){
                presenter.setRecentFeed(mFeedLastState);
                return;
            }
            mBackend.getRecentFeed(new BackendManager.GetFeedClk() {
                @Override
                public void success(ArrayList<ImageTimelike> feed) {
                    mFeedLastState = feed;
                    presenter.setRecentFeed(feed);
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

    public void setTimelike(ImageTimelike image){
        presenter.setTimelike(image);
    }


    public void onClickLogOff() {
        mBackend.logOff();
        ((MainActivity)MainActivity.getMainActivity()).setFirstTab();
    }
}
