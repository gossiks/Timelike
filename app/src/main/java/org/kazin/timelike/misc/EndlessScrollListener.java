package org.kazin.timelike.misc;

import android.widget.AbsListView;

import org.kazin.timelike.object.SimpleCallback;

/**
 * Created by Alexey on 17.07.2015.
 * src: http://benjii.me/2010/08/endless-scrolling-listview-in-android/
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private SimpleCallback callback;

    public EndlessScrollListener(SimpleCallback callback) {
        this.callback = callback;
    }
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading & (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            callback.success(currentPage); // this should fire upload
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

}