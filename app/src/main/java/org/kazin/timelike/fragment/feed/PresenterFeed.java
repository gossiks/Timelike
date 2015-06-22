package org.kazin.timelike.fragment.feed;

import android.graphics.AvoidXfermode;

import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class PresenterFeed {

    private static PresenterFeed presenter;
    private ViewerFeed viewer;
    private ModelFeed model;

    private void setMVP(ViewerFeed viewer, PresenterFeed presenter){
        this.viewer = viewer;
        model = ModelFeed.getInstance(presenter);
    }

    public static PresenterFeed getInstance(ViewerFeed viewer) {
        if(presenter==null){
            presenter = new PresenterFeed();
            presenter.setMVP(viewer, presenter);
            return presenter;
        }
        else {
            return presenter;
        }
    }

    public void onLaunch() {
        model.onLaunch();
    }

    public void onClickReload(){
        model.onClickReload();
    }

    public void onLikeReceived(String imageid, long timelike){
        model.onLikeReceived(imageid, timelike);
    }

    public void setFeed(ArrayList<ImageTimelike> feed){
        viewer.setFeed(feed);
    }

    public void setMessage(String message){
        viewer.setMessage(message);
    }


}
