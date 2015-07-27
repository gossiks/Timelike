package org.kazin.timelike.main.photo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

import net.grobas.view.MovingImageView;

import org.kazin.timelike.R;


public class FragmentPhoto extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


   private static FragmentPhoto fragment;
   private ViewerPhoto viewer;

   private void setMVP(FragmentPhoto fragment){
       viewer = ViewerPhoto.getInstance(fragment);
   }

   public static Fragment getInstance(){
       if(fragment == null){
           fragment = new FragmentPhoto();
           fragment.setMVP(fragment);
       }
       return fragment;
   }

    public FragmentPhoto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_photo, container, false);
        MovingImageView chooseImageView = (MovingImageView) convertView.findViewById(R.id.choosephoto_photo_fragment);
        MovingImageView takePhotoView = (MovingImageView) convertView.findViewById(R.id.takephoto_photo_fragment);

        //setAnimationProperties(chooseImageView);
        //setAnimationProperties(takePhotoView);

        if(viewer==null){
            viewer = ViewerPhoto.getInstance(fragment);
        }

        takePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewer.onTakePhotoClick();
            }
        });
        chooseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewer.onChoosePhotoClick();
            }
        });

        return convertView;
    }


    //misc

    private void setAnimationProperties(MovingImageView image){
        image.getMovingAnimator().setInterpolator(new BounceInterpolator());
        image.getMovingAnimator().setSpeed(100);
        image.getMovingAnimator().addCustomMovement().
                addDiagonalMoveToDownRight().
                addHorizontalMoveToLeft().
                addDiagonalMoveToUpRight().
                addVerticalMoveToDown().
                addHorizontalMoveToLeft().
                addVerticalMoveToUp().
                start();
    }
}
