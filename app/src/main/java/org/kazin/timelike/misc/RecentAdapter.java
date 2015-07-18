package org.kazin.timelike.misc;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.squareup.picasso.Picasso;

import org.kazin.timelike.R;
import org.kazin.timelike.fragment.feed.FragmentFeed;
import org.kazin.timelike.object.ImageTimelike;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Alexey on 18.07.2015.
 */
public class RecentAdapter extends BaseAdapter{

    private ArrayList<ImageTimelike> mItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public RecentAdapter(ArrayList<ImageTimelike> images) {
        mContext = TimelikeApp.getContext();
        mInflater = LayoutInflater.from(mContext);
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
            holderImage.comments = (ExpandableHeightListView) convertView.findViewById(R.id.comments_feed_item_user_fragment);
            holderImage.comments.setExpanded(true);

            holderImage.like_button = (Button) convertView.findViewById(R.id.like_image_item_user_fragment_feed);

            convertView.setTag(holderImage);
        }
        else {
            holderImage = (ViewHolderImage) convertView.getTag();
        }

        holderImage.like_button.setOnTouchListener(new FragmentFeed.LikeListener(image.getImageId(), holderImage.like_button));

        Picasso.with(mContext).load(image.getImageUrl())
                .into(holderImage.image);
        //holderImage.description.setText(" @"+image.getUsername() + " " + image.getDescription());
        setTags(holderImage.description, " @" + image.getUsername() + " " + image.getDescription());

        if(image.getComments()==null){

        }
        else{

            holderImage.comments.setAdapter(new ArrayAdapter<>(mContext
                    , R.layout.item_comment_frament_feed, image.getCommentsStringArray(3)));//3 - because who cares about other comments.

            setListViewHeightBasedOnItems(holderImage.comments);
        }


        holderImage.like_button.setText(convertToHumanLook(image.getTimelike()));

        return convertView;
    }

    //misc
    private class ViewHolderImage {
        ImageView image;
        TextView description;
        Button like_button;
        ExpandableHeightListView comments;
    }

    private void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '#') {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(Color.parseColor("#33b5e5"));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '@') {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(Color.parseColor("#33b5e5"));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }

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
            listView.setVisibility(View.GONE);
            return false;
        }

    }

    private String convertToHumanLook(long timelike1){
        String timelikeCrop = null;
        double timelike = (double)timelike1;
        DecimalFormat noDigit = new DecimalFormat("#");
        DecimalFormat twoDigit = new DecimalFormat("#.##");


        if(timelike<1*1000){
            timelikeCrop = noDigit.format(timelike)+" sec";
        } else
        if (timelike<1000*1000){
            timelikeCrop = twoDigit.format(timelike / 1000)+"k";
        } else
        if(timelike<1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000))+"M";
        }else
        if(timelike<1000*1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000 * 1000))+"G";
        } else
        if(timelike<1000*1000*1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000 * 1000 * 1000))+"T";
        } else
        if(timelike<1000*1000*1000*1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000 * 1000 * 1000 * 1000))+"P";
        } else
        if(timelike<1000*1000*1000*1000*1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000 * 1000 * 1000 * 1000 * 1000))+"E";
        } else
        if(timelike<1000*1000*1000*1000*1000*1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))+"Z";
        } else
        if(timelike<1000*1000*1000*1000*1000*1000*1000*1000*1000){
            timelikeCrop = twoDigit.format(timelike / (1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000 * 1000))+"Y";
        } else{
            timelikeCrop = "googol";
        }

        return timelikeCrop;
    }
}
