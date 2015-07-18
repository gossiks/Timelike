package org.kazin.timelike.backend;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 06.07.2015.
 */
public class BackendManager {

    private Context mContext;
    public  static BackendManager mManager;
    private FireManager mFireManager2;
    private InstagramManager mInstagramManager;

    public BackendManager() {
        mContext = TimelikeApp.getContext();
        mFireManager2 = new FireManager();
        mInstagramManager = InstagramManager.getInstance();



    }

    public static BackendManager getInstance(){
        if(mManager==null){
            mManager = new BackendManager();
        }
        return mManager;
    }

    public void getFeed(GetFeedClk callback){
        mInstagramManager.getFeed(callback);
    }

    public void getRecentFeed(GetFeedClk callback){
        mInstagramManager.getRecentFeed(callback);
    }

    public void getFeedUpdate(GetFeedClk callback){
        mInstagramManager.getFeedUpdate(callback);
    }

    public void LoginInst(LoginInstClk callback){
        mInstagramManager.login(callback);
    }

    public boolean checkInstLoggedIn(){
        return mInstagramManager.getCurrentUser()!=null;
    }

    public void LoginFireAnon(LoginFireAnonClk callback){
        mFireManager2.loginAnon(callback);
    }

    public void getFeedTimeLikes(ArrayList<ImageTimelike> images, GetFeedTimelikes callback){
        ArrayList<String> imageIds = new ArrayList<>(images.size());
        for(ImageTimelike image:images){
            imageIds.add(image.getImageId());
        }
        mFireManager2.getTimelikes(imageIds, callback);
    }

    public void saveTimelike(String imageid, long timelike) {
        mFireManager2.saveTimelike(imageid, timelike);
    }

    //misc for other

    public interface LoginInstClk{
        void success(UserTimelike user);
        void error(String error);
    }

    public interface LoginFireAnonClk{
        void success();
        void error(String error);
    }

    public interface GetFeedClk{
        void success(ArrayList<ImageTimelike> feed);
        void error(String error);
    }

    public interface GetFeedTimelikes{
        void success(ImageTimelike image);
        void error(String error);
    }
}
