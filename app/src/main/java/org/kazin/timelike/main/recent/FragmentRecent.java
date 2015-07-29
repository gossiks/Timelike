package org.kazin.timelike.main.recent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.EndlessScrollListener;
import org.kazin.timelike.misc.RecentAdapter2;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;


public class FragmentRecent extends Fragment  {



   private static FragmentRecent fragment;
   private ViewerRecent viewer;

   private Context mContext = TimelikeApp.getContext();

   private SelectableRoundedImageView mAvatar;
   private TextView mUsername;
   private ListView mRecent;

    private final String ADAPTER_SAVE_INSTANCE = "adapter";

   private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecentAdapter2 mRecentAdapter;
    private View mConvertView;

    private void setMVP(FragmentRecent fragment){
       viewer = ViewerRecent.getInstance(fragment);
   }

   public static Fragment getInstance(){
       if(fragment == null){
           fragment = new FragmentRecent();
           fragment.setMVP(fragment);
           return fragment;
       } else {
           return fragment;
       }
   }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_recent, container, false);

        if(viewer==null){
            viewer = ViewerRecent.getInstance(this);
        }

        mAvatar = (SelectableRoundedImageView) convertView.findViewById(R.id.avatar_user_item_fragment_recent);
        mRecent = (ListView) convertView.findViewById(R.id.recent_feed_fragment_fragment_recent);
        mUsername = (TextView) convertView.findViewById(R.id.username_item_user_fragment_recent);
        mSwipeRefreshLayout = (SwipeRefreshLayout) convertView.findViewById(R.id.recent_pull_to_refresh);
        Button logOffButton = (Button) convertView.findViewById(R.id.log_off_fragment_recent);

        mRecent.setOnScrollListener(new EndlessScrollListener(viewer.getEndListListener()));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewer.onClickReload();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent_orange_timelike, R.color.blue_medium_timelike);

        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewer.onClickLogOff();
            }
        });


        mConvertView = convertView;

        if(savedInstanceState!=null){
            mRecentAdapter = savedInstanceState.getParcelable(ADAPTER_SAVE_INSTANCE);
            setRecentFeed(mRecentAdapter);
        }

        return convertView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRecentAdapter!=null){
            outState.putParcelable(ADAPTER_SAVE_INSTANCE, mRecentAdapter);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(viewer==null){
            viewer = ViewerRecent.getInstance(this);
        }
        viewer.onLaunch();
    }

    public void setAvatar(String url) {
        Picasso.with(mContext).load(url).into(mAvatar);
    }

    public void setUsername(String username){
        mUsername.setText(username);
    }

    public void setRecentFeed(RecentAdapter2 adapter){
        mRecentAdapter = adapter;
        mRecent.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setRecentFeedAdapterOld(){
        mRecent.setAdapter(mRecentAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setTimelike(ImageTimelike image) {
        ((RecentAdapter2)mRecent.getAdapter()).setTimelike(image);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeed(ArrayList<ImageTimelike> image) {
        ((RecentAdapter2)mRecent.getAdapter()).addAll(image);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //misc
}
