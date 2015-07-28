package org.kazin.timelike.user;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.main.recent.ModelRecent;
import org.kazin.timelike.main.recent.ViewerRecent;
import org.kazin.timelike.misc.RecentAdapter2;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ViewerUser {

    private static ViewerUser viewer;
    private static ModelUser model;
    private UserActivity activity;

    public ViewerUser() {

    }

    public static ViewerUser getInstance() {
        if(viewer == null){
            viewer = new ViewerUser();
            model = ModelUser.getInstance();
            model.setViewer(viewer);
        }

        return viewer;
    }

    public void setActivity(UserActivity activity){
        this.activity = activity;
    }


    public void onLaunch(String username) {
        model.onLaunch(username);
    }

    public void onClickReload() {
        model.onReloadFeed();
    }

    public void onClickUpdateFeed(){
        model.onUpdateFeed();
    }


    public void setUserFeed(ArrayList<ImageTimelike> mFeedLastState) {
        activity.mFeedAdapter = new RecentAdapter2(activity, mFeedLastState);
        activity.mUserFeed.setAdapter(activity.mFeedAdapter);

        setUsername(mFeedLastState.get(0).getUsername());
        setAvatar(mFeedLastState.get(0).getAvatarUrl());
        activity.mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setUserFeedAdapter(RecentAdapter2 mFeedAdapter) {
        activity.mUserFeed.setAdapter(mFeedAdapter);
        activity.mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setTimelike(ImageTimelike image) {
        ((RecentAdapter2)activity.mUserFeed.getAdapter()).setTimelike(image);
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        ((RecentAdapter2)activity.mUserFeed.getAdapter()).addAll(image);
        activity.mSwipeRefreshLayout.setRefreshing(false);
    }

    public SimpleCallback getEndFeedListener() {
        return model.getEndFeedListener();
    }

    public void setUsername(String username){
        activity.mUsernameString = username;
        activity.mUsername.setText(activity.mUsernameString);
    }

    public void setAvatar(String avatarUrl){
        activity.mAvatarUrl = avatarUrl;
        Picasso.with(TimelikeApp.getContext()).load(activity.mAvatarUrl).into(activity.mAvatar);
    }
}
