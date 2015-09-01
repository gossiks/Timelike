package org.kazin.timelike.main.photo;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Alexey on 17.07.2015.
 */
public class PresenterPhoto {

    private static PresenterPhoto presenter;
    private ViewerPhoto viewer;
    private ModelPhoto model;

    private void setMVP(ViewerPhoto viewer, PresenterPhoto presenter){
        this.viewer = viewer;
        model = ModelPhoto.getInstance(presenter);
    }

    public static PresenterPhoto getInstance(ViewerPhoto viewer){
        if(presenter == null){
            presenter = new PresenterPhoto();
            presenter.setMVP(viewer,presenter);
        }
        return presenter;
    }

    public void onTakePhotoClick() {
        model.onTakePhotoClick();
    }

    public void onChoosePhotoClick() {
        model.onChoosePhotoClick();
    }

    public Fragment getFragmentContext() {
        return viewer.getFragmentContext();
    }

    public void onImageChosen(int requestCode, Intent data) {
        model.onImageChosen(requestCode,data);
    }
}
