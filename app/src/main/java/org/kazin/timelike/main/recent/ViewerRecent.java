package org.kazin.timelike.main.recent;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.AbsListView;

import org.kazin.timelike.main.feed.FragmentFeed;
import org.kazin.timelike.misc.RecentAdapter2;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ViewerRecent implements FragmentFeed.SetTimelikeInterface{
    private static ViewerRecent viewer;
    private PresenterRecent presenter;
    private static Fragment mFragment;

    public final static int VIEWER_RECENT_CLASS_ID = 2;

    private void setMVP(FragmentRecent fragment, ViewerRecent viewer){
        this.mFragment = fragment;
        presenter = PresenterRecent.getInstance(viewer);
    }

    public static ViewerRecent getInstance(FragmentRecent fragment){
        Log.d("apkapk", "mFragmetn: " + mFragment + " fragment: " + fragment);


        if(viewer==null|mFragment != fragment){
            viewer = new ViewerRecent();
            viewer.setMVP(fragment, viewer);
        }
        return viewer;
    }

    public static ViewerRecent getInstance() {
        return viewer;
    }

    public Fragment getFragmentContext() {
        return mFragment;
    }


    public void onLaunch() {
        presenter.onLaunch();
    }

    public void setRecentFeed(ArrayList<ImageTimelike> images) {
        ((FragmentRecent) mFragment).setAvatar(images.get(0).getAvatarUrl());
        ((FragmentRecent) mFragment).setUsername(images.get(0).getUsername());
        ((FragmentRecent) mFragment).setRecentFeed(new RecentAdapter2(TimelikeApp.getContext(), images, viewer));
    }

    public void setTimelike(ImageTimelike image) {
        ((FragmentRecent) mFragment).setTimelike(image);
    }

    public void onClickReload() {
        presenter.onClickReload();
    }

    public void onClickLogOff() {
        presenter.onClickLogOff();
    }

    public void setRecentFeedAdapterOld(ArrayList<ImageTimelike> images) {
        ((FragmentRecent) mFragment).setAvatar(images.get(0).getAvatarUrl());
        ((FragmentRecent) mFragment).setUsername(images.get(0).getUsername());
        ((FragmentRecent) mFragment).setRecentFeedAdapterOld();
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        ((FragmentRecent) mFragment).updateFeed(image);
    }

    public SimpleCallback getEndListListener() {
        return presenter.getEndListListener();
    }

    @Override
    public void setTimelike(String imageId, long timelike) {
        ((FragmentRecent) mFragment).addTimelike(imageId, timelike);
    }

    @Override
    public void onLikeReceived(String imageId, long timelike) {
        presenter.onLikeReceived(imageId, timelike);
    }

    @Override
    public int getViewerClassId() {
        return VIEWER_RECENT_CLASS_ID;
    }
}