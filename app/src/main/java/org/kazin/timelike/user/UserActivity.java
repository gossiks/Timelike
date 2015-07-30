package org.kazin.timelike.user;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.EndlessScrollListener;
import org.kazin.timelike.misc.RecentAdapter2;

public class UserActivity extends ActionBarActivity {

    public static final String USERNAME_USERACTIVITY_EXTRAS = "username";
    public static final String AVATAR_URL_USERACTIVITY_EXTRAS = "avatar";
    private final String ADAPTER_SAVE_INSTANCE = "adapter";

    private ViewerUser viewer;
    SelectableRoundedImageView mAvatar;
    ListView mUserFeed;
    TextView mUsername;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecentAdapter2 mFeedAdapter;

    String mUsernameString;
    String mAvatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recent);

        mAvatar = (SelectableRoundedImageView) findViewById(R.id.avatar_user_item_fragment_recent);
        mUserFeed = (ListView) findViewById(R.id.recent_feed_fragment_fragment_recent);
        mUsername = (TextView) findViewById(R.id.username_item_user_fragment_recent);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.recent_pull_to_refresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewer.onClickReload();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent_orange_timelike, R.color.blue_medium_timelike);

        Button logoff = (Button) findViewById(R.id.log_off_fragment_recent);
        logoff.setVisibility(View.INVISIBLE);

        viewer = ViewerUser.getInstance();
        viewer.setActivity(this);

        mUserFeed.setOnScrollListener(new EndlessScrollListener(viewer.getEndFeedListener()));
        if(savedInstanceState!=null){
            savedInstanceState.setClassLoader(RecentAdapter2.class.getClassLoader());
            mFeedAdapter = savedInstanceState.getParcelable(ADAPTER_SAVE_INSTANCE); //Store mFeedAdapter in bundle, because of better reliability comparing to viewer of model.
            viewer.setUserFeedAdapter(mFeedAdapter);

            viewer.setUsername(savedInstanceState.getString(USERNAME_USERACTIVITY_EXTRAS));
            viewer.setAvatar(savedInstanceState.getString(AVATAR_URL_USERACTIVITY_EXTRAS));
        }else{
            viewer.onLaunch(getIntent().getStringExtra(USERNAME_USERACTIVITY_EXTRAS));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mFeedAdapter!=null){
            outState.putParcelable(ADAPTER_SAVE_INSTANCE , mFeedAdapter);
            outState.putString(USERNAME_USERACTIVITY_EXTRAS, mUsernameString);
            outState.putString(AVATAR_URL_USERACTIVITY_EXTRAS, mAvatarUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
