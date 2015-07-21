package org.kazin.timelike.fragment.recent;


import android.content.Intent;
import android.support.v4.app.Fragment;

import org.kazin.timelike.fragment.photo.FragmentPhoto;
import org.kazin.timelike.misc.RecentAdapter;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ViewerRecent {
    private static ViewerRecent viewer;
    private PresenterRecent presenter;
    private Fragment fragment;

    private void setMVP(FragmentRecent fragment, ViewerRecent viewer){
        this.fragment = fragment;
        presenter = PresenterRecent.getInstance(viewer);
    }

    public static ViewerRecent getInstance(FragmentRecent fragment){
        if(viewer==null){
            viewer = new ViewerRecent();
            viewer.setMVP(fragment, viewer);
        }
        return viewer;
    }


    public Fragment getFragmentContext() {
        return fragment;
    }


    public void onLaunch() {
        presenter.onLaunch();
    }

    public void setRecentFeed(ArrayList<ImageTimelike> images) {
        ((FragmentRecent)fragment).setAvatar(images.get(0).getAvatarUrl());
        ((FragmentRecent)fragment).setUsername(images.get(0).getUsername());
        ((FragmentRecent) fragment).setRecentFeed(new RecentAdapter(images));
    }

    public void setTimelike(ImageTimelike image) {
        ((FragmentRecent)fragment).setTimelike(image);
    }

    public void onClickReload() {
        presenter.onClickReload();
    }

    public void onClickLogOff() {
        presenter.onClickLogOff();
    }
}