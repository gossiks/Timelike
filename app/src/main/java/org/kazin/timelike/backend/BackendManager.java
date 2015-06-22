package org.kazin.timelike.backend;

import android.animation.TimeAnimator;
import android.content.Context;
import android.util.Log;

import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class BackendManager {
    private static BackendManager manager;
    private InstagramManager mInstagramManager;
    private ParseManager mParseManager;

    private Context mContext;

    private UserTimelike mCurrentUser;

    public BackendManager() {
        mContext = TimelikeApp.getContext();
        mInstagramManager = InstagramManager.getInstance();
        mParseManager = ParseManager.getInstance();
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
            callback.success();
        }
        else {mInstagramManager.authorize(new InstagramAutorizeClk() {
            @Override
            public void success(UserTimelike user) {

                mCurrentUser = mInstagramManager.getCurrentUser();

                if(mCurrentUser.username != mParseManager.getParseUsername()){
                    mParseManager.loginParse(mCurrentUser,new ParseLoginClk() {
                        @Override
                        public void success() {
                            initialize(callback);
                        }

                        @Override
                        public void error(String error) {
                            Log.e("apk", "BackendManager error Parse Login: " + error);
                        }
                    });
                    return;
                }

                callback.success();
            }

            @Override
            public void error(String error) {

            }

            @Override
            public void cancel() {

            }
        });}


    }

    public UserTimelike getCurrentUser(){
        return mCurrentUser;
    }

    public void getFeedInst(BackendGetFeedClk callback){
        mInstagramManager.getFeed(callback);
    }

    public void getFeedParse(ArrayList<ImageTimelike> feed,BackendGetFeedClk callback){
        mParseManager.getFeed(feed, callback);
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
