package org.kazin.timelike.misc;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.londatiga.android.instagram.util.StringUtil;

import org.kazin.timelike.R;
import org.kazin.timelike.main.feed.FragmentFeed;
import org.kazin.timelike.object.ImageTimelike;

/**
 * Created by Alexey on 03.08.2015.
 */
public class ArrayAdapterWithTags extends ArrayAdapter{
    private final LayoutInflater mInflater;
    private int mTagColor;
    private int mResource;
    private FragmentFeed.SetTimelikeInterface mViewer;
    private ImageTimelike mImage;
    private View.OnClickListener mOnClickListener;


    public ArrayAdapterWithTags(Context context, int resource, Object[] objects, ImageTimelike image, FragmentFeed.SetTimelikeInterface viewer) {
        super(context, resource, objects);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
        mTagColor = getTagColor(context);
        mViewer = viewer;
        mImage = image;


        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewer.navigateToComments(mImage);
            }
        };
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }




    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
                //  Work only if the whole resource is a TextView
                text = (TextView) view;
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        String item = (String) getItem(position);

        setTags(text, item);
        view.setOnClickListener(mOnClickListener);
        return view;
    }




    //misc
    private int getTagColor(Context context){
        return context.getResources().getColor(R.color.blue_medium_timelike);
    }

    public void setTags(TextView pTextView, final String pTagString) {
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

                    //final String tag = pTagString.substring(start+1, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            //Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(mTagColor);
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


                    try{
                        final String tag = pTagString.substring(start+1,i); //very rarely involves StringIndexOutOfBoundsException.

                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            mViewer.navigateToUserActivity(mImage.getIdByUsername(tag));
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(mTagColor);
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    start = -1;
                }
            }
        }

        pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }
}
