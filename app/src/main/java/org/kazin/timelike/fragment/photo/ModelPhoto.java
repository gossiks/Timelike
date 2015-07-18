package org.kazin.timelike.fragment.photo;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.kazin.timelike.MainActivity;
import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.misc.TimelikeApp;

import java.io.File;
import java.sql.Time;

/**
 * Created by Alexey on 17.07.2015.
 */
public class ModelPhoto {

    private static ModelPhoto model;
    private PresenterPhoto presenter;
    private ImageChooserManager mImageChooserManager;

    private void setMVP(PresenterPhoto presenter){
        this.presenter = presenter;
    }

    public static ModelPhoto getInstance(PresenterPhoto presenter){
        if(model == null){
            model = new ModelPhoto();
            model.setMVP(presenter);
        }
        return model;
    }

    public void onTakePhotoClick() {
         mImageChooserManager = new ImageChooserManager(
                MainActivity.getMainActivity(),
                ChooserType.REQUEST_CAPTURE_PICTURE);
        mImageChooserManager.setImageChooserListener(new ImageChooserLstn());
        try {
            mImageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChoosePhotoClick() {
        mImageChooserManager = new ImageChooserManager(
                MainActivity.getMainActivity(),
                ChooserType.REQUEST_PICK_PICTURE);
        mImageChooserManager.setImageChooserListener(new ImageChooserLstn());
        try {
            mImageChooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onImageChosen(int requestCode, Intent data) {
        mImageChooserManager.submit(requestCode, data);
    }


    private class ImageChooserLstn implements ImageChooserListener{
        //interface ImageChooserListener
        final String SHARE_TYPE_IMAGE = "image/*";

        @Override
        public void onImageChosen(ChosenImage chosenImage) {
            Intent toInstagram = new Intent(Intent.ACTION_SEND);
            toInstagram.setType(SHARE_TYPE_IMAGE);

            File file = new File(chosenImage.getFilePathOriginal());
            Uri uri = Uri.fromFile(file);

            toInstagram.putExtra(Intent.EXTRA_STREAM, uri);
            toInstagram.putExtra(Intent.EXTRA_TEXT, "#timelike");

            presenter.getFragmentContext().startActivity(Intent.createChooser(toInstagram, "PLEASE CHOOSE INSTAGRAM"));
        }

        @Override
        public void onError(String s) {
            Log.d("apkapk", "Error choosing image: " + s);
        }
    }
}
