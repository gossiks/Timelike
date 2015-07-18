package org.kazin.timelike.fragment.recent;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.kazin.timelike.MainActivity;
import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.fragment.feed.ModelFeed;
import org.kazin.timelike.fragment.photo.ModelPhoto;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ModelRecent {

    private static ModelRecent model;
    private PresenterRecent presenter;
    private ImageChooserManager mImageChooserManager;

    private BackendManager mBackend;

    private ModelRecent(){
        mBackend = ModelFeed.getInstance(null).getBackend();
    }

    private void setMVP(PresenterRecent presenter){
        this.presenter = presenter;
    }

    public static ModelRecent getInstance(PresenterRecent presenter){
        if(model == null){
            model = new ModelRecent();
            model.setMVP(presenter);
        }
        return model;
    }

    public void onLaunch() {
        loadFeed();
    }

    private void loadFeed(){
        if(mBackend.checkInstLoggedIn()){
            Log.d("apkapk","Instagram is logged!");
            mBackend.getRecentFeed(new BackendManager.GetFeedClk() {
                @Override
                public void success(ArrayList<ImageTimelike> feed) {
                    presenter.setRecentFeed(feed);
                    mBackend.getFeedTimeLikes(feed, new BackendManager.GetFeedTimelikes() {
                        @Override
                        public void success(ImageTimelike image) {
                            setTimelike(image);
                        }

                        @Override
                        public void error(String error) {
                            Log.d("apkapk", "GetFeedTimeLikes error: "+error);
                        }
                    });
                }

                @Override
                public void error(String error) {
                    Log.d("apkapk","Error Logging instagram: "+error);
                }
            });
        }
    }

    public void setTimelike(ImageTimelike image){
        presenter.setTimelike(image);
    }
}
