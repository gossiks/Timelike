package org.kazin.timelike.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.kazin.timelike.R;
import org.kazin.timelike.fragment.feed.FragmentFeed;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;
import java.util.Arrays;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Alexey on 19.06.2015.
 */
public class FeedAdapter2 extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private ArrayList<ImageTimelike> mItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public FeedAdapter2(Context context, ArrayList<ImageTimelike> images) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        ArrayList<ImageTimelike> items = new ArrayList<>(images.size()*2);
        //ArrayList<Integer> header
        /*ImageTimelike tempImage;
        for(int i = 0; i<images.size();i++){
            tempImage = images.get(i);
            items.add(tempImage);
        }
        mItems = items;*/
        mItems = images;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderImage holderImage;
        ImageTimelike image = mItems.get(position);

        if(convertView==null){
            holderImage = new ViewHolderImage();
            convertView = mInflater.inflate(R.layout.item_fragment_feed, parent, false);
            holderImage.image = (ImageView) convertView.findViewById(R.id.image_feed_item);
            holderImage.description = (TextView) convertView.findViewById(R.id.description_feed_item);
            holderImage.comments = (ListView) convertView.findViewById(R.id.comments_feed_item_user_fragment);

            holderImage.like_button = (Button) convertView.findViewById(R.id.like_image_item_user_fragment_feed);

            convertView.setTag(holderImage);
        }
        else {
            holderImage = (ViewHolderImage) convertView.getTag();
        }

        holderImage.like_button.setOnTouchListener(new FragmentFeed.LikeListener(image.getImageId(), holderImage.like_button));

        Picasso.with(mContext).load(image.getImageUrl())
                .into(holderImage.image);
        holderImage.description.setText(image.getUsername() + " " + image.getDescription());

        holderImage.comments.setAdapter(new ArrayAdapter<>(mContext
                , android.R.layout.simple_list_item_1, image.getCommentsStringArray(3)));//3 - because who cares about other comments.
        setListViewHeightBasedOnItems(holderImage.comments);
        holderImage.like_button.setText(Long.toString(image.getTimelike()));

        return convertView;
    }



    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        ViewHolderHeader holderHeader;
        if(convertView==null){
            holderHeader = new ViewHolderHeader();
            convertView = mInflater.inflate(R.layout.item_user_fragment_feed, parent, false);
            holderHeader.avatar = (SelectableRoundedImageView) convertView.findViewById(R.id.avatar_user_item_fragment_feed);
            holderHeader.username = (TextView) convertView.findViewById(R.id.username_item_user_fragment_feed);
            convertView.setTag(holderHeader);
        }
        else {
            holderHeader = (ViewHolderHeader) convertView.getTag();
        }

        ImageTimelike user = mItems.get(position);
        Picasso.with(mContext).load(user.getAvatarUrl()).into(holderHeader.avatar);
        holderHeader.username.setText(user.getUsername());

        return convertView;
    }

    @Override
    public long getHeaderId(int position ) {
        return position; //
    }

    @Override
    public Object[] getSections() {
        return mItems.toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }


    //misc for update on the fly for certain list item

    public void setTimelike(ImageTimelike imageTimelike){
        String imageId = imageTimelike.getImageId();
        long timelike = imageTimelike.getTimelike();
        for(ImageTimelike image: mItems){
            if(image.getImageId().equals(imageId)){
                image.setTimelike(timelike);
            }
        }
        notifyDataSetChanged();
    }

    //misc for FeedAdapter
    private class ViewHolderImage {
        ImageView image;
        TextView description;
        Button like_button;
        ListView comments;
    }

    private class ViewHolderHeader {
        SelectableRoundedImageView avatar;
        TextView username;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
