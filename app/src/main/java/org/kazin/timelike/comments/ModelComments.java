package org.kazin.timelike.comments;

import android.content.Intent;

import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 03.08.2015.
 */
public class ModelComments {
    private static ModelComments model;
    private ViewerComments viewer;

    private String mUsername;
    private ImageTimelike mImage;

    private BackendManager mBackend;

   public ModelComments(){
       super();
       mBackend = BackendManager.getInstance();
   }

    public ViewerComments getViewer() {
        return viewer;
    }

    public void setViewer(ViewerComments viewer) {
        this.viewer = viewer;
    }

    public static ModelComments getInstance(ViewerComments viewer) {
        if(model==null){
            model = new ModelComments();
        }

        if(model.getViewer()!=viewer & viewer!=null){
            model.setViewer(viewer);
        }
        return model;
    }


    //"on" methods
    public void onStart(Intent intent) {
        ImageTimelike image = intent.getParcelableExtra(UserCommentsActivity.IMAGE_USERCOMMENTSACTIVITY_EXTRAS);

        if(!image.getUsername().equals(mUsername)){
            mUsername = image.getUsername();
            mImage = image;

            viewer.setComments(
                    commentsToStringArrayList(image.getComments()), image);
        }
    }



    public void onNavigateToUserActivity(String userId) {
        //TODO check if userId is avaliable (notfriend and private account)
        viewer.navigateToUserActivityFromModel(userId);
    }

    public void onRefresh() {

        mBackend.getComments(mImage.getImageId(), new SimpleCallback() {
            @Override
            public void success(Object object) {
                viewer.setComments(
                        commentsToStringArrayList((ArrayList<ImageTimelike.Comment>) object)
                        , mImage);
            }

            @Override
            public void fail(Object object) {
                TimelikeApp.showToast("Fail retrieving comments. Error: " + object.toString());
            }
        });
    }

    private ArrayList<String> commentsToStringArrayList(ArrayList<ImageTimelike.Comment> comments){
        ArrayList<String> commentsString = new ArrayList<>();
        for (ImageTimelike.Comment comm : comments) {
            commentsString.add(comm.toString());
        }
        return commentsString;
    }
}
