package org.kazin.timelike.main.photo;


import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ViewerPhoto {
    private static ViewerPhoto viewer;
    private PresenterPhoto presenter;
    private Fragment fragment;

    private void setMVP(FragmentPhoto fragment, ViewerPhoto viewer){
        this.fragment = fragment;
        presenter = PresenterPhoto.getInstance(viewer);
    }

    public static ViewerPhoto getInstance(FragmentPhoto fragment){
        if(viewer==null){
            viewer = new ViewerPhoto();
            viewer.setMVP(fragment, viewer);
        }
        return viewer;
    }

    public void onTakePhotoClick(){
        presenter.onTakePhotoClick();
    }

    public void onChoosePhotoClick(){
        presenter.onChoosePhotoClick();
    }

    public Fragment getFragmentContext() {
        return fragment;
    }

    public void onImageChosen(int requestCode, Intent data) {
        presenter.onImageChosen(requestCode,data);
    }
}