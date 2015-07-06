package org.kazin.timelike.backend;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.kazin.timelike.R;
import org.kazin.timelike.misc.Const;
import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexey on 16.06.2015.
 */
public class FireManager {

    private static FireManager manager;
    private Context mContext;
    private Firebase mFireBase;

    public FireManager() {
        createConnection();
    }

    private void createConnection(){
        mContext = TimelikeApp.getContext();
        Firebase.setAndroidContext(mContext);
        mFireBase = new Firebase("https://timelike.firebaseio.com/");
    }

    public static FireManager getInstance(){
        if(manager == null){
            return manager = new FireManager();
        }
        else{
            return manager;
        }
    }

    public void initializeConnection(){
        createConnection();
    }



    public String getFireUsername() {
            return "n.o.u.s.e.r";
    }

    public void loginFire(final UserTimelike user, final BackendManager.ParseLoginClk fireLoginClk){
        Firebase users = mFireBase.child("users").child("tempUser");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("apkapk","users data snapshot"+dataSnapshot+". getValue: "+dataSnapshot.getValue());
                if(dataSnapshot.getValue()==null){
                    signUp(user, fireLoginClk);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void signUp(UserTimelike user, final BackendManager.ParseLoginClk fireLoginClk){
        mFireBase.createUser(user.username + "@unknownemail.com", generatePassword(user), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                fireLoginClk.success();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.d("apkapk","Firebase user create error: "+firebaseError);
                fireLoginClk.error(firebaseError.toString());
            }
        });
    }


    public void getFeed(final ArrayList<ImageTimelike> images, final BackendManager.BackendGetFeedClk callback){


        /*ParseQuery<ParseObject> queryGetFeed = ParseQuery.getQuery(Const.IMAGE_CLASS_PARSE);
        Collection<String> idImageInst = new ArrayList<>(images.size());
        for(ImageTimelike image: images){
            idImageInst.add(image.getImageId());
        }
        queryGetFeed.whereContainedIn(Const.IMAGE_ID_INST_PARSE, idImageInst);
        queryGetFeed.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    String idImage = list.get(i).getString(Const.IMAGE_ID_INST_PARSE);
                    ImageTimelike image = getImage(idImage, images);
                    if (image != null) {
                        image.setTimelike(list.get(i).getInt(Const.IMAGE_TIMELIKE_PARSE));

                        //TODO check if actual object ImageTimelike in ArrayList changes
                    }
                    callback.successInst(images);
                }

            }
        });*/
    }




    //misc

    private ImageTimelike getImage(String idImage, ArrayList<ImageTimelike> images){
        for (int i=0; i<images.size();i++){
            if(images.get(i).getImageId().equals(idImage)){
                return images.get(i);
            }
        }
        return null;
    }
    private String generatePassword(UserTimelike user){
        String[] username = user.username.split("");
        String[] userid = user.id.split("");

        String pass = new String();
        int usernameLength = username.length;
        String tempPartUsername;

        for(int i=0; i<userid.length; i++){
            pass = pass + userid[i];
            if(i<usernameLength){
                pass = pass + username[i];
            }
        }
        return pass;
    }


}
