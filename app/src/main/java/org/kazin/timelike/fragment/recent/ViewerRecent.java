package org.kazin.timelike.fragment.recent;


import android.support.v4.app.Fragment;
import android.util.Log;

import org.kazin.timelike.misc.RecentAdapter2;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ViewerRecent {
    private static ViewerRecent viewer;
    private PresenterRecent presenter;
    private static Fragment mFragment;

    private void setMVP(FragmentRecent fragment, ViewerRecent viewer){
        this.mFragment = fragment;
        presenter = PresenterRecent.getInstance(viewer);
    }

    public static ViewerRecent getInstance(FragmentRecent fragment){
        Log.d("apkapk", "mFragmetn: "+mFragment+" fragment: "+fragment);


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
        ((FragmentRecent) mFragment).setRecentFeed(new RecentAdapter2(TimelikeApp.getContext(), images));
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
}