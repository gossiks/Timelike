package org.kazin.timelike.fragment.recent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import net.grobas.view.MovingImageView;

import org.kazin.timelike.R;
import org.kazin.timelike.fragment.photo.ViewerPhoto;
import org.kazin.timelike.misc.RecentAdapter;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;


public class FragmentRecent extends Fragment  {



   private static FragmentRecent fragment;
   private ViewerRecent viewer;

   private Context mContext;

   private SelectableRoundedImageView mAvatar;
   private TextView mUsername;
   private ListView mRecent;

   private SwipeRefreshLayout mSwipeRefreshLayout;

   private void setMVP(FragmentRecent fragment){
       viewer = ViewerRecent.getInstance(fragment);
   }

   public static Fragment getInstance(){
       if(fragment == null){
           fragment = new FragmentRecent();
           fragment.setMVP(fragment);
       }
       return fragment;
   }

    public FragmentRecent() {
        mContext = TimelikeApp.getContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_recent, container, false);

        mAvatar = (SelectableRoundedImageView) convertView.findViewById(R.id.avatar_user_item_fragment_recent);
        mRecent = (ListView) convertView.findViewById(R.id.recent_feed_fragment_fragment_recent);
        mUsername = (TextView) convertView.findViewById(R.id.username_item_user_fragment_recent);
        mSwipeRefreshLayout = (SwipeRefreshLayout) convertView.findViewById(R.id.recent_pull_to_refresh);
        Button logOffButton = (Button) convertView.findViewById(R.id.log_off_fragment_recent);

        if(viewer==null){
            viewer = ViewerRecent.getInstance(fragment);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewer.onClickReload();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange_light_timelike, R.color.blue_medium_timelike);

        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewer.onClickLogOff();
            }
        });

        viewer.onLaunch();

        return convertView;
    }

    public void setAvatar(String url) {
        Picasso.with(mContext).load(url).into(mAvatar);
    }

    public void setUsername(String username){
        mUsername.setText(username);
    }

    public void setRecentFeed(RecentAdapter adapter){
        mRecent.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setTimelike(ImageTimelike image) {
        ((RecentAdapter)mRecent.getAdapter()).setTimelike(image);
    }

    //misc
}