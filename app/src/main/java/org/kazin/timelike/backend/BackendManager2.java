package org.kazin.timelike.backend;

import android.content.Context;

import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 06.07.2015.
 */
public class BackendManager2 {

    Context mContext;
    BackendManager2 mManager;
    FireManager2 mFireManager2;
    InstagramManager2 mInstagramManager2;

    public BackendManager2() {
        mContext = TimelikeApp.getContext();
        mFireManager2 = new FireManager2();
        mInstagramManager2 = new InstagramManager2();
    }

    public BackendManager2 getInstance(){
        if(mManager==null){
            mManager = new BackendManager2();
        }
        return mManager;
    }

    public void getFeed(GetFeedClk callback){
        mInstagramManager2.getFeed(callback);
    }

    public void LoginInst(String username, String password,LoginInstClk callback){
        mInstagramManager2.login(username, password, callback);
    }

    public void LoginFireAnon(LoginFireAnonClk callback){
        mFireManager2.loginAnon(callback);
    }

    //misc for other

    interface LoginInstClk{
        void success(UserTimelike user);
        void error(String error);
    }

    interface LoginFireAnonClk{
        void success();
        void error(String error);
    }

    interface GetFeedClk{
        void success(ArrayList<ImageTimelike> feed);
        void error(String error);
    }

    interface GetFeedTimelikes{
        void
    }
}
