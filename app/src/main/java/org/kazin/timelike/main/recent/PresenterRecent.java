package org.kazin.timelike.main.recent;

import android.support.v4.app.Fragment;

import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class PresenterRecent {

    private static PresenterRecent presenter;
    private ViewerRecent viewer;
    private ModelRecent model;

    private void setMVP(ViewerRecent viewer, PresenterRecent presenter){
        this.viewer = viewer;
        model = ModelRecent.getInstance(presenter);
    }

    public static PresenterRecent getInstance(ViewerRecent viewer){
        if(presenter == null){
            presenter = new PresenterRecent();
            presenter.setMVP(viewer,presenter);
        }
        return presenter;
    }


    public Fragment getFragmentContext() {
        return viewer.getFragmentContext();
    }

    public void onLaunch() {
        model.onLaunch();
    }

    public void setRecentFeed(ArrayList<ImageTimelike> images){
        viewer.setRecentFeed(images);
    }

    public void setTimelike(ImageTimelike image) {
        viewer.setTimelike(image);
    }

    public void onClickReload() {
        model.onClickReload();
    }

    public void onClickLogOff() {
        model.onClickLogOff();
    }

    public void setRecentFeedAdapterOld(ArrayList<ImageTimelike> feed) {
        viewer.setRecentFeedAdapterOld(feed);
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        viewer.updateFeed(image);
    }
}
