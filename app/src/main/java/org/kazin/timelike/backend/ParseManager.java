package org.kazin.timelike.backend;

import android.content.Context;

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

/**
 * Created by Alexey on 16.06.2015.
 */
public class ParseManager {

    private static ParseManager manager;
    private Context mContext;

    public ParseManager() {
        createConnection();
    }

    private void createConnection(){
        mContext = TimelikeApp.getContext();
        ParseCrashReporting.enable(mContext);
        Parse.initialize(mContext,
                mContext.getString(R.string.parse_client_key),
                mContext.getString(R.string.parse_client_secret));
    }

    public static ParseManager getInstance(){
        if(manager == null){
            return manager = new ParseManager();
        }
        else{
            return manager;
        }
    }

    public void initialize(){
        createConnection();
    }

    public String getParseUsername() {
        if (ParseUser.getCurrentUser() == null) {
            return "n.o.u.s.e.r";
        }
        return ParseUser.getCurrentUser().getUsername();
    }

    public void loginParse(final UserTimelike user, final BackendManager.ParseLoginClk parseLoginClk){
        ParseQuery<ParseUser> queryCheckUserAvaliable = ParseUser.getQuery();
        queryCheckUserAvaliable.whereEqualTo(
                mContext.getString(R.string.parse_username),
                user.username
        );
        queryCheckUserAvaliable.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser == null) {
                    signUp(user, parseLoginClk);
                } else {
                    login(user, parseLoginClk);
                }
            }
        });
    }

    private void signUp(UserTimelike user, final BackendManager.ParseLoginClk parseLoginClk){
        ParseUser userParse = new ParseUser();
        userParse.setUsername(user.username);
        userParse.setPassword(generatePassword(user));

        userParse.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    parseLoginClk.success();
                } else {
                    parseLoginClk.error(e.toString());
                }
            }
        });
    }

    private void login(final UserTimelike user, final BackendManager.ParseLoginClk parseLoginClk){
        ParseUser userParse = new ParseUser();
        userParse.setUsername(user.username);
        userParse.setPassword(generatePassword(user));

        ParseUser.logInInBackground(user.username, generatePassword(user), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    parseLoginClk.success();
                } else {
                    parseLoginClk.error(e.toString());
                }
            }
        });
    }

    public void getFeed(final ArrayList<ImageTimelike> images, final BackendManager.BackendGetFeedClk callback){
        ParseQuery<ParseObject> queryGetFeed = ParseQuery.getQuery(Const.IMAGE_CLASS_PARSE);
        Collection<String> idImageInst = new ArrayList<>(images.size());
        int i = 0;
        for(ImageTimelike image: images){
            idImageInst.add(image.getImageId());
            i++;
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
                }

            }
        });
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
