package org.kazin.timelike.main.feed;

import android.content.Intent;
import android.widget.Toast;

import org.kazin.timelike.comments.UserCommentsActivity;
import org.kazin.timelike.main.MainActivity;
import org.kazin.timelike.misc.FeedAdapter;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;
import org.kazin.timelike.user.UserActivity;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ViewerFeed implements FragmentFeed.SetTimelikeInterface {

    private static ViewerFeed viewer;
    private PresenterFeed presenter;
    private FragmentFeed fragment;
    public final static int VIEWER_FEED_CLASS_ID = 1;

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

    @Override
    public int getViewerClassId() {
        return VIEWER_FEED_CLASS_ID;
    }

    @Override
    public void navigateToUserActivity(String userId) {
        Intent intent = new Intent(MainActivity.getMainActivity(), UserActivity.class);
        intent.putExtra(UserActivity.USERID_USERACTIVITY_EXTRAS, userId);
        MainActivity.getMainActivity().startActivity(intent);
    }

    @Override
    public void navigateToComments(ImageTimelike image) {
        Intent intent = new Intent(TimelikeApp.getContext(), UserCommentsActivity.class);
        intent.putExtra(UserCommentsActivity.IMAGE_USERCOMMENTSACTIVITY_EXTRAS, image);
        MainActivity.getMainActivity().startActivity(intent);
    }

    public void setFeed(ArrayList<ImageTimelike> feed){
        /*fragment.setFeedAdapter(FeedAdapter.getInstanceWithPinSections(TimelikeApp.getContext(),
                R.layout.item_fragment_feed,
                feed));*/
        /*fragment.setFeedAdapter(new FeedAdapter(TimelikeApp.getContext(),
                R.layout.item_fragment_feed,
                feed));*/
        fragment.setFeedAdapter(new FeedAdapter(TimelikeApp.getContext(),
                feed, viewer));
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
        image.setTimelike(lastDuration/1000);
        fragment.addTimelike(image);
    }



    public SimpleCallback getEndFeedListener() {
        return presenter.getEndFeedListener();
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        fragment.updateFeed(image);
    }
}
