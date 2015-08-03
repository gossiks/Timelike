package org.kazin.timelike.comments;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.skyfishjy.library.RippleBackground;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.ExpandableHeightListView;

/**
 * Created by Alexey on 03.08.2015.
 */
public class UserCommentsActivity extends ActionBarActivity {
    public static final String IMAGE_USERCOMMENTSACTIVITY_EXTRAS = "image";

    ViewerComments viewer;

    SwipyRefreshLayout mSwipeRefreshLayout;
    ExpandableHeightListView mExpandableHeightListView;
    RippleBackground mRippleBackground;

    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_comments);
        mSwipeRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipe_refresh_user_comments_activity);
        mExpandableHeightListView = (ExpandableHeightListView) findViewById(R.id.comments_user_comments_activity);
        mRippleBackground = (RippleBackground) findViewById(R.id.ripple_activity_user_comments);

        if(viewer == null){
            viewer = ViewerComments.getInstance(this);
        }
        viewer.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(viewer == null){
            viewer = ViewerComments.getInstance(this);
        }
        viewer.onStart(mSavedInstanceState, getIntent());
    }
}
