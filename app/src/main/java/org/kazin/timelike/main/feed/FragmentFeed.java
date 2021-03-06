package org.kazin.timelike.main.feed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.skyfishjy.library.RippleBackground;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.EndlessScrollListener;
import org.kazin.timelike.misc.FeedAdapter;
import org.kazin.timelike.misc.TimelikeApp;
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

    private final String ADAPTER_SAVE_INSTANCE = "adapter";
    private FeedAdapter mFeedAdapter;
    private RippleBackground mRippleBackground;

    private boolean onDestroyViewWasFired = false;

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
        mRippleBackground = (RippleBackground) rootView.findViewById(R.id.ripple_feed_fragment);

        if(viewer==null){
            viewer = ViewerFeed.getInstance(fragment);
        }


        mFeedView.setOnScrollListener(new EndlessScrollListener(viewer.getEndFeedListener()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewer.onClickReload();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent_orange_timelike, R.color.blue_medium_timelike);


        if(savedInstanceState!=null){
            savedInstanceState.setClassLoader(FeedAdapter.class.getClassLoader());
            mFeedAdapter = savedInstanceState.getParcelable(ADAPTER_SAVE_INSTANCE);
            setFeedAdapter(mFeedAdapter);
        }

        if(viewer==null){
            viewer = ViewerFeed.getInstance(fragment);
        }

        if(!viewer.isOnDestroyViewFired()){
            mRippleBackground.startRippleAnimation();
        }


        viewer.onLaunch();

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewer.onDestroyViewFragment();
     }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mFeedAdapter!=null){
            outState.putParcelable(ADAPTER_SAVE_INSTANCE, mFeedAdapter);
        }
    }

    public void setFeedAdapter(FeedAdapter adapter){
        mFeedAdapter = adapter;
        mFeedView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mRippleBackground.stopRippleAnimation();
    }

    public void setTimelike(ImageTimelike image) {
        ((FeedAdapter)mFeedView.getAdapter()).setTimelike(image);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void addTimelike(ImageTimelike image) {
        ((FeedAdapter)mFeedView.getAdapter()).addTimelike(image);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        ((FeedAdapter)mFeedView.getAdapter()).addAll(image);
        mSwipeRefreshLayout.setRefreshing(false);
    }



    //misc

    public static class LikeListener implements View.OnTouchListener { // for like button. It is used in adapter of feed.

        private long lastDown;
        private long lastDuration;

        private String imageId;
        private Button thisView;
        //Animator.AnimatorListener animatorListener;
        private SetTimelikeInterface viewer;
        private RippleBackground ripple;

        private int lastMotionEvent;
        long systemtime;

        public LikeListener(String imageId, Button thisView, SetTimelikeInterface viewer, RippleBackground ripple) {
            this.imageId = imageId;
            this.thisView = thisView;
            //animatorListener = getAnimatorListener();
            this.viewer = viewer;
            this.ripple = ripple;
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            lastMotionEvent = event.getAction();
            Log.d("apk", "lastMovionEvent: " + lastMotionEvent);
            if(lastMotionEvent == MotionEvent.ACTION_DOWN) {
                lastDown = System.currentTimeMillis();
                v.getParent().requestDisallowInterceptTouchEvent(true); //remember this works only for this amount of parents
                //v.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                ripple.startRippleAnimation();
                showWoble();
            } else if(lastMotionEvent!=MotionEvent.ACTION_MOVE){
                /*if(lastMotionEvent == MotionEvent.ACTION_CANCEL|lastMotionEvent == MotionEvent.ACTION_MOVE){
                    return false;
                }*/
                systemtime = System.currentTimeMillis();
                //Log.d("apkapk", "System time is: "+systemtime );

                ripple.stopRippleAnimation();
                //stopWoble();


                lastDuration = systemtime - lastDown;
                if (lastDown==0){
                    return false;
                }
                if(lastDuration<500){
                    return false;
                }
                if(lastDuration<1000){
                    lastDuration = 1000;
                }


                Log.d("apkapk", "LastDuration  time is: "+lastDuration +" LastDonw is: "+lastDown);
                viewer.setTimelike(imageId, lastDuration);
                viewer.onLikeReceived(imageId, lastDuration);
                lastDown = System.currentTimeMillis();
                v.getParent().requestDisallowInterceptTouchEvent(false);
                //v.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        }

        private void showWoble(){
            if(!YoYo.with(Techniques.RubberBand).duration(1500).playOn(thisView).isRunning()){
                YoYo.with(Techniques.RubberBand).duration(1500).playOn(thisView);
            }

            /*if(!YoYo.with(Techniques.RubberBand).duration(1500).withListener(animatorListener).playOn(thisView).isRunning()){
                YoYo.with(Techniques.RubberBand).duration(1500).withListener(animatorListener).playOn(thisView);
            }*/
        }

        private Animator.AnimatorListener getAnimatorListener(){
            return new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (lastMotionEvent == MotionEvent.ACTION_DOWN | lastMotionEvent == MotionEvent.ACTION_MOVE) {
                        //showWoble();
                        animation.start();
                    }

                    if (lastMotionEvent == MotionEvent.ACTION_CANCEL | lastMotionEvent == MotionEvent.ACTION_UP) {
                        //stopWoble();
                        animation.cancel();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        }
    }

    public interface SetTimelikeInterface{
        void setTimelike(String imageId, long timelike);
        void onLikeReceived(String imageId, long timelike);
        int getViewerClassId();

        void navigateToUserActivity(String userId);

        void navigateToComments(ImageTimelike mImage);
    }

}
