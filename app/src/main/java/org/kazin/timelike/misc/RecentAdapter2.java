package org.kazin.timelike.misc;

import android.content.Context;
import android.os.Parcel;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import org.kazin.timelike.R;
import org.kazin.timelike.main.feed.FragmentFeed;
import org.kazin.timelike.main.feed.ViewerFeed;
import org.kazin.timelike.main.recent.ViewerRecent;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.user.ViewerUser;

import java.util.ArrayList;

/**
 * Created by Alexey on 23.07.2015.
 */
public class RecentAdapter2 extends FeedAdapter {
    public RecentAdapter2(Context context, ArrayList<ImageTimelike> images, FragmentFeed.SetTimelikeInterface viewer) {
        super(context, images,viewer);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderImage holderImage;
        ImageTimelike image = (ImageTimelike) getItem(position);

        if(convertView==null){
            holderImage = new ViewHolderImage();
            convertView = getInflater().inflate(R.layout.item_fragment_feed, parent, false);
            holderImage.image = (ImageView) convertView.findViewById(R.id.image_feed_item);
            holderImage.description = (TextView) convertView.findViewById(R.id.description_feed_item);
            holderImage.comments = (ExpandableHeightListView) convertView.findViewById(R.id.comments_feed_item_user_fragment);
            holderImage.comments.setExpanded(true);

            holderImage.like_button = (Button) convertView.findViewById(R.id.like_image_item_user_fragment_feed);
            holderImage.ripple = (RippleBackground) convertView.findViewById(R.id.ripple_like_button_feed_adapter);

            convertView.setTag(holderImage);
        }
        else {
            holderImage = (ViewHolderImage) convertView.getTag();
        }

        holderImage.like_button.setOnTouchListener(new FragmentFeed.LikeListener(image.getImageId(), holderImage.like_button, getViewer(), holderImage.ripple));

        getImageLoader().displayImage(image.getImageUrl(), holderImage.image, getImageOptions());

        setTags(holderImage.description, " @" + image.getUsername() + " " + image.getDescription());

        if(image.getComments()==null){

        }
        else{

            holderImage.comments.setAdapter(new ArrayAdapterWithTags(getContext()
                    , R.layout.item_comment_frament_feed, image.getCommentsStringArray(3), image,getViewer()));//3 - because who cares about other comments.

            //setListViewHeightBasedOnItems(holderImage.comments);
        }


        holderImage.like_button.setText(convertToHumanLook(image.getTimelike()));

        return convertView;
    }


    //misc objects
    private class ViewHolderImage {
        ImageView image;
        TextView description;
        Button like_button;
        ExpandableHeightListView comments;
        RippleBackground ripple;
    }

    //Parcelable interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(getItems());
        dest.writeInt(getViewerId());
    }

    private static RecentAdapter2 getFeedAdapter(Parcel in) {
        ArrayList<ImageTimelike> items = new ArrayList<ImageTimelike>();
        in.readList(items, ImageTimelike.class.getClassLoader());
        int viewerId =  in.readInt();
        return new RecentAdapter2(TimelikeApp.getContext(), items, getViewer(viewerId));
    }

    private static FragmentFeed.SetTimelikeInterface getViewer(int viewerId) {
        FragmentFeed.SetTimelikeInterface viewer = null;

        switch (viewerId){
            case ViewerFeed.VIEWER_FEED_CLASS_ID:
                viewer = ViewerFeed.getInstance(null);
                break;
            case ViewerRecent.VIEWER_RECENT_CLASS_ID:
                viewer = ViewerRecent.getInstance();
                break;
            case ViewerUser.VIEWER_USER_CLASS_ID:
                viewer = ViewerUser.getInstance();
                break;
        }
        return viewer;
    }

    public static final Creator<RecentAdapter2> CREATOR = new Creator<RecentAdapter2>() {
        public RecentAdapter2 createFromParcel(Parcel source) {
            return getFeedAdapter(source);
        }

        public RecentAdapter2[] newArray(int size) {
            return new RecentAdapter2[size];
        }
    };
}
