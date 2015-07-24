package org.kazin.timelike.backend;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexey on 06.07.2015.
 */
public class FireManager {
    private static FireManager manager;
    private Context mContext;
    private Firebase mFirebase;

    private HashMap<String,ValueEventListener> mListenersToRemove = new HashMap<String,ValueEventListener>();

    public FireManager() {
        mContext = TimelikeApp.getContext();
        createConnection();
    }

    private void createConnection(){
        Firebase.setAndroidContext(mContext);
        mFirebase = new Firebase("https://timelike.firebaseio.com/");
    }

    public static FireManager getInstance(){
        if(manager == null){
            return manager = new FireManager();
        }
        else{
            return manager;
        }
    }

    public void loginAnon(final BackendManager.LoginFireAnonClk callback) {
        mFirebase.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                callback.success();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                callback.error(firebaseError.toString());
            }
        });
    }

    public void getTimelikes(ArrayList<String> imageIds, final BackendManager.GetFeedTimelikes callback) {
        Firebase tempImageRef;
        removeAllImageListeners();
        for(final String imageId:imageIds){
            tempImageRef = mFirebase.child("image").child(imageId);
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("apkapk","onDataChangeImage fired for imageId: "+imageId+" With timelike value: "+dataSnapshot.getValue());
                    if(dataSnapshot.getValue()==null){
                        callback.error("no FireData for imageId");
                        return;
                    }
                    ImageTimelike image = new ImageTimelike();
                    image.setImageId(imageId);
                    image.setTimelike((Long) dataSnapshot.getValue());
                    callback.success(image);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    callback.error(firebaseError.toString());
                }
            };
            tempImageRef.addValueEventListener(valueEventListener);
            saveImageListenerToRemove(imageId, valueEventListener);
        }
    }

    public void onReloadFeed(){
        removeAllImageListeners();
    }

    public void saveTimelike(String imageid, final long timelike) {
        Firebase tempImageRef = mFirebase.child("image").child(imageid);
        tempImageRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if(currentData.getValue()==null){
                    currentData.setValue(timelike);
                } else {
                    currentData.setValue((long)currentData.getValue()+timelike);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("apkapk","Save Timelike errorcode: "+ firebaseError);
            }
        });
    }


    //misc for FireManager
    private void saveImageListenerToRemove(String imageId, ValueEventListener listener){
        mListenersToRemove.put(imageId, listener);
    }

    private void removeAllImageListeners(){
        Firebase imageRef = mFirebase.child("image");
        Firebase tempImageRef;
        for(HashMap.Entry<String,ValueEventListener> entry: mListenersToRemove.entrySet()){
            tempImageRef = imageRef.child(entry.getKey());
            tempImageRef.removeEventListener(entry.getValue());
        }
        mListenersToRemove.clear();
    }
}
