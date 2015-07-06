package org.kazin.timelike.backend;

import android.content.Context;
import android.util.Log;

import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class BackendManager {
    private static BackendManager manager;
    private InstagramManager mInstagramManager;
    private FireManager mFireManager;

    private Context mContext;

    private UserTimelike mCurrentUser;

    public BackendManager() {
        mContext = TimelikeApp.getContext();
        mInstagramManager = InstagramManager.getInstance();
        mFireManager = FireManager.getInstance();
    }

    public static BackendManager getInstance() {
        if(manager == null){
            return manager = new BackendManager();
        }
        else {
            return manager;
        }
    }

    public void initialize(final BackendInitializeClk callback){
        if(!mInstagramManager.isInstagramActive()){
            mInstagramManager.initialize();
        }
        if(mInstagramManager.getCurrentUser()!=null){
            mCurrentUser = mInstagramManager.getCurrentUser();
            authorizeFireIfNecessary(callback);
        }
        else{
            mInstagramManager.authorize(new InstagramAutorizeClk() {
                @Override
                public void success(UserTimelike user) {
                    mCurrentUser = mInstagramManager.getCurrentUser();
                    authorizeFireIfNecessary(callback);

                }

                @Override
                public void error(String error) {

                }

                @Override
                public void cancel() {

                }
            });
        }

    }

    private void authorizeFireIfNecessary(final BackendInitializeClk callback) {
        if(mCurrentUser.username != mFireManager.getFireUsername()){
            mFireManager.loginFire(mCurrentUser, new ParseLoginClk() {
                @Override
                public void success() {
                    //initialize(callback);
                    callback.success();
                }

                @Override
                public void error(String error) {
                    Log.e("apk", "BackendManager error Parse Login: " + error);
                }
            });

            return;
        }
        else{
            callback.success();
        }
    }

    public UserTimelike getCurrentUser(){
        return mCurrentUser;
    }

    public void getFeedInst(BackendGetFeedClk callback){
        mInstagramManager.getFeed(callback);
    }

    public void getFeedParse(ArrayList<ImageTimelike> feed,BackendGetFeedClk callback){
        mFireManager.getFeed(feed, callback);
    }

    //misc to other
    public interface BackendInitializeClk{
        void success();
    }

    public interface BackendGetFeedClk {
        void successInst(ArrayList<ImageTimelike> feed);
    }

    //misc to BackendManager

    public interface InstagramAutorizeClk {
        void success(UserTimelike user);
        void error(String error);
        void cancel();
    }

    public interface ParseLoginClk{
        void success();
        void error(String error);
    }
}
