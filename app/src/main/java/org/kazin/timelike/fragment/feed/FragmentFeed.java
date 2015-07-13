package org.kazin.timelike.fragment.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.FeedAdapter2;
import org.kazin.timelike.object.ImageTimelike;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Alexey on 16.06.2015.
 */
public class FragmentFeed extends Fragment{

    private static FragmentFeed fragment;
    private ViewerFeed viewer;

    StickyListHeadersListView mFeedView;

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

        viewer.onLaunch();
        return rootView;
    }



    public void setFeedAdapter(FeedAdapter2 adapter){
        mFeedView.setAdapter(adapter);
    }

    public void setTimelike(ImageTimelike timelike) {
        ((FeedAdapter2)mFeedView.getAdapter()).setTimelike(timelike);
    }

    //misc

    public static class LikeListener implements View.OnTouchListener { // for like button. It is used in adapter of feed.

        private long lastDown;
        private long lastDuration;

        private String imageId;
        private long timelike;
        private Button thisView;

        private int lastMotionEvent;


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
                showWoble();
            } else {
                lastDuration = System.currentTimeMillis() - lastDown;
                stopWoble();
                ViewerFeed viewerFeed = ViewerFeed.getInstance(null); //TODO. Can crush here if viewer is not initalized yet. (barely possible)
                viewerFeed.setTimelike(imageId, lastDuration);
                viewerFeed.onLikeReceived(imageId, lastDuration);
            }
            return false;
        }

        private void showWoble(){
            YoYo.with(Techniques.Wobble).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (lastMotionEvent == MotionEvent.ACTION_DOWN) {
                        showWoble();
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
