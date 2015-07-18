package org.kazin.timelike.fragment.feed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.EndlessScrollListener;
import org.kazin.timelike.misc.FeedAdapter;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Alexey on 16.06.2015.
 */
public class FragmentFeed extends Fragment{

    private static FragmentFeed fragment;
    private ViewerFeed viewer;

    private static StickyListHeadersListView mFeedView;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    private void setMVP(FragmentFeed fragment){
        viewer = ViewerFeed.getInstance(fragment);
    }

    public static FragmentFeed getInstance() {
       if(fragment == null){
           fragment = new FragmentFeed();
           fragment.setMVP(fragment);
           return fragment;
       }
       else {
           return fragment;
       }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        mFeedView = (StickyListHeadersListView) rootView.findViewById(R.id.feed_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.feed_pull_to_refresh);

        if(viewer==null){
            viewer = ViewerFeed.getInstance(fragment);
        }
        viewer.onLaunch();
        mFeedView.setOnScrollListener(new EndlessScrollListener(viewer.getEndFeedListener()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewer.onClickReload();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_light_timelike,R.color.blue_medium_timelike);

        return rootView;
    }



    public void setFeedAdapter(FeedAdapter adapter){
        mFeedView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setTimelike(ImageTimelike timelike) {
        ((FeedAdapter)mFeedView.getAdapter()).setTimelike(timelike);
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        ((FeedAdapter)mFeedView.getAdapter()).addAll(image);
    }

    //misc

    public static class LikeListener implements View.OnTouchListener { // for like button. It is used in adapter of feed.

        private long lastDown;
        private long lastDuration;

        private String imageId;
        private long timelike;
        private Button thisView;

        private int lastMotionEvent;
        long systemtime;

        public LikeListener(String imageId, Button thisView) {
            this.imageId = imageId;
            this.thisView = thisView;
        }



        @Override
        public boolean onTouch(View v, MotionEvent event) {

            lastMotionEvent = event.getAction();
            Log.d("apk", "lastMovionEvent: " + lastMotionEvent);
            if(lastMotionEvent == MotionEvent.ACTION_DOWN) {
                lastDown = System.currentTimeMillis();
                v.getParent().requestDisallowInterceptTouchEvent(true); //remember this works only for this amount of parents
                v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                showWoble();
            } else if(lastMotionEvent!=MotionEvent.ACTION_MOVE){
                /*if(lastMotionEvent == MotionEvent.ACTION_CANCEL|lastMotionEvent == MotionEvent.ACTION_MOVE){
                    return false;
                }*/
                systemtime = System.currentTimeMillis();
                Log.d("apkapk", "Sytem time is: "+systemtime );

                lastDuration = systemtime - lastDown;
                if (lastDuration<1000||lastDown==0){
                    return false;
                }
                stopWoble();
                ViewerFeed viewerFeed = ViewerFeed.getInstance(null); //TODO. Can crush here if viewer is not initalized yet. (barely possible)

                Log.d("apkapk", "LastDuration  time is: "+lastDuration +" LastDonw is: "+lastDown);
                viewerFeed.setTimelike(imageId, lastDuration);
                viewerFeed.onLikeReceived(imageId, lastDuration);
                lastDown = System.currentTimeMillis();
                v.getParent().requestDisallowInterceptTouchEvent(false);
                v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        }

        private void showWoble(){
            YoYo.with(Techniques.Wobble).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) { //todo clear
                    if (lastMotionEvent == MotionEvent.ACTION_DOWN|lastMotionEvent==MotionEvent.ACTION_MOVE) {
                        showWoble();
                    }

                    if(lastMotionEvent==MotionEvent.ACTION_CANCEL|lastMotionEvent==MotionEvent.ACTION_UP){
                        stopWoble();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).playOn(thisView);
        }

        private void stopWoble(){
            YoYo.with(Techniques.Wobble).playOn(thisView).stop(true);
        }
    }

}
