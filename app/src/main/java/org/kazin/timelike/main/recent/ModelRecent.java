package org.kazin.timelike.main.recent;

import android.util.Log;
import android.widget.AbsListView;

import org.kazin.timelike.main.MainActivity;
import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;
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

    public ModelRecent(){
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
                    TimelikeApp.showToast("Recent feed load fail: " + error);
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

    private void loadRecentUpdate(){
        if(mBackend.checkInstLoggedIn()){
            mBackend.getRecentUpdate(new BackendManager.GetFeedClk() {
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
                    TimelikeApp.showToast("Recent feed update load fail: " + error);
                    Log.d("apkapk", "Error Logging instagram: " + error);
                }
            });
        }
    }

    public void setTimelike(ImageTimelike image){
        presenter.setTimelike(image);
    }


    public void updateFeed(ArrayList<ImageTimelike> image){
        presenter.updateFeed(image);
    }
    public void onClickLogOff() {
        mBackend.logOff();
        ((MainActivity)MainActivity.getMainActivity()).setFirstTab();
    }

    public SimpleCallback getEndListListener() {
        return new SimpleCallback() {
            @Override
            public void success(Object object) {
                //object is current page int
                loadRecentUpdate();
            }

            @Override
            public void fail(Object object) {

            }
        };
    }

    public void onLikeReceived(String imageId, long timelike) {
        mBackend.saveTimelike(imageId, Math.abs(timelike / 1000));
    }
}
