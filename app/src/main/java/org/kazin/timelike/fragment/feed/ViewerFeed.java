package org.kazin.timelike.fragment.feed;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.kazin.timelike.misc.FeedAdapter2;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ViewerFeed {

    private static ViewerFeed viewer;
    private PresenterFeed presenter;
    private FragmentFeed fragment;

    private void setMVP(FragmentFeed fragment, ViewerFeed viewer){
        this.fragment = fragment;
        presenter = PresenterFeed.getInstance(viewer);
    }

    public static ViewerFeed getInstance(FragmentFeed fragment) {
        if(viewer==null){
            viewer = new ViewerFeed();
            viewer.setMVP(fragment,viewer);
            return viewer;
        }
        else {
            return viewer;
        }
    }

    public void onLaunch(){
        presenter.onLaunch();
    }

    public void onClickReload(){
        presenter.onClickReload();
    }

    public void onLikeReceived(String imageid, long timelike){
        presenter.onLikeReceived(imageid, timelike);
    }

    public void setFeed(ArrayList<ImageTimelike> feed){
        /*fragment.setFeedAdapter(FeedAdapter.getInstanceWithPinSections(TimelikeApp.getContext(),
                R.layout.item_fragment_feed,
                feed));*/
        /*fragment.setFeedAdapter(new FeedAdapter(TimelikeApp.getContext(),
                R.layout.item_fragment_feed,
                feed));*/
        fragment.setFeedAdapter(new FeedAdapter2(TimelikeApp.getContext(),
                feed));
    }

    public void setMessage(String message){
        Toast.makeText(TimelikeApp.getContext(), message, Toast.LENGTH_SHORT);
    }

    public void setTimelike(ImageTimelike timelike) {
        fragment.setTimelike(timelike);
    }

    public void setTimelike(String imageId, long lastDuration) {
        ImageTimelike image = new ImageTimelike();
        image.setImageId(imageId);
        image.setTimelike(lastDuration);
        setTimelike(image);
    }
}
