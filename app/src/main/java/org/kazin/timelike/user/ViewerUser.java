package org.kazin.timelike.user;

import android.content.Intent;

import com.squareup.picasso.Picasso;

import org.kazin.timelike.comments.UserCommentsActivity;
import org.kazin.timelike.main.MainActivity;
import org.kazin.timelike.main.feed.FragmentFeed;
import org.kazin.timelike.misc.RecentAdapter2;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ViewerUser implements FragmentFeed.SetTimelikeInterface{

    private static ViewerUser viewer;
    private static ModelUser model;
    private UserActivity activity;

    public static final int VIEWER_USER_CLASS_ID = 3;

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
        activity.mRippleBackground.startRippleAnimation();
        model.onLaunch(username);
    }

    public void onClickReload() {
        model.onReloadFeed();
    }

    public void onClickUpdateFeed(){
        model.onUpdateFeed();
    }


    public void setUserFeed(ArrayList<ImageTimelike> mFeedLastState) {
        activity.mFeedAdapter = new RecentAdapter2(activity, mFeedLastState, viewer);
        activity.mUserFeed.setAdapter(activity.mFeedAdapter);

        setUsername(mFeedLastState.get(0).getUsername());
        setAvatar(mFeedLastState.get(0).getAvatarUrl());
        activity.mRippleBackground.stopRippleAnimation();
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
        activity.mUsernameTextView.setText(activity.mUsernameString);
    }

    public void setAvatar(String avatarUrl){
        activity.mAvatarUrl = avatarUrl;
        Picasso.with(TimelikeApp.getContext()).load(activity.mAvatarUrl).into(activity.mAvatar);
    }

    @Override
    public void setTimelike(String imageId, long timelike) {
        ImageTimelike image = new ImageTimelike();
        image.setImageId(imageId);
        image.setTimelike(timelike/1000);
        activity.mFeedAdapter.addTimelike(image);
    }

    @Override
    public void onLikeReceived(String imageId, long timelike) {
        model.onLikeReceived(imageId, timelike);
    }

    @Override
    public int getViewerClassId() {
        return VIEWER_USER_CLASS_ID;
    }

    @Override
    public void navigateToUserActivity(String userId) {
        Intent intent = new Intent(MainActivity.getMainActivity(), UserActivity.class);
        intent.putExtra(UserActivity.USERID_USERACTIVITY_EXTRAS, userId);
        MainActivity.getMainActivity().startActivity(intent);
    }

    @Override
    public void navigateToComments(ImageTimelike image) {
        Intent intent = new Intent(MainActivity.getMainActivity(), UserCommentsActivity.class);
        intent.putExtra(UserCommentsActivity.IMAGE_USERCOMMENTSACTIVITY_EXTRAS, image);
        MainActivity.getMainActivity().startActivity(intent);
    }
}
