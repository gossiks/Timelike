package org.kazin.timelike.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.kazin.timelike.R;
import org.kazin.timelike.main.feed.FragmentFeed;
import org.kazin.timelike.misc.ArrayAdapterWithTags;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.user.UserActivity;

import java.util.ArrayList;

/**
 * Created by Alexey on 03.08.2015.
 */
public class ViewerComments implements FragmentFeed.SetTimelikeInterface{

    private static ViewerComments viewer;
    private static ModelComments model;

    private UserCommentsActivity a;
    private ArrayAdapterWithTags mCommentsAdapter;

    private void setActivity(UserCommentsActivity activity){
        this.a = activity;
    }


    private UserCommentsActivity getActivity(){
        return a;
    }

    public static ViewerComments getInstance(UserCommentsActivity activity) {
        if (viewer == null) {
            viewer = new ViewerComments();
            model = ModelComments.getInstance(viewer);
        }
        if(viewer.getActivity()!=activity&activity!=null){
            viewer.setActivity(activity);
        }
        return viewer;
    }

    public void onCreate(){
        a.mSwipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
               @Override
               public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {
                   model.onRefresh();
               }
        });
                a.mSwipeRefreshLayout.setColorSchemeResources(R.color.accent_orange_timelike, R.color.blue_medium_timelike);
        a.mExpandableHeightListView.setExpanded(false);
        a.mRippleBackground.startRippleAnimation();
    }


    public void onStart(Bundle mSavedInstanceState, Intent intent) {
        if(mSavedInstanceState!=null & mCommentsAdapter!=null){
            setComments(mCommentsAdapter);
        }
        else{
            model.onStart(intent);
        }
    }


    //"set" methods

    public void setComments(ArrayList<String> comments, ImageTimelike image) {
        mCommentsAdapter = new ArrayAdapterWithTags(a,
                R.layout.item_comment_frament_feed,
                comments.toArray(), image, viewer);
        a.mExpandableHeightListView.setAdapter(
                mCommentsAdapter);
        a.mExpandableHeightListView.setSelection(comments.size() - 1);
        a.mRippleBackground.stopRippleAnimation();
        a.mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setComments(ArrayAdapterWithTags adapter){
        a.mExpandableHeightListView.setAdapter(adapter);
    }


    //SetTimelike Interface

    @Override
    public void setTimelike(String imageId, long timelike) {

    }

    @Override
    public void onLikeReceived(String imageId, long timelike) {

    }

    @Override
    public int getViewerClassId() {
        return 0;
    }

    @Override
    public void navigateToUserActivity(String userId) {
        model.onNavigateToUserActivity(userId);
    }

    public void navigateToUserActivityFromModel(String userId){
        Intent intent = new Intent(a, UserActivity.class);
        intent.putExtra(UserActivity.USERID_USERACTIVITY_EXTRAS, userId);
        a.startActivity(intent);
    }

    @Override
    public void navigateToComments(ImageTimelike mImage) {

    }

}
