package org.kazin.timelike.backend;

import android.content.Context;

import org.kazin.timelike.misc.TimelikeApp;
import org.kazin.timelike.object.ImageTimelike;
import org.kazin.timelike.object.UserTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 06.07.2015.
 */
public class BackendManager2 {

    Context mContext;
    BackendManager2 mManager;
    FireManager2 mFireManager2;
    InstagramManager2 mInstagramManager2;

    public BackendManager2() {
        mContext = TimelikeApp.getContext();
        mFireManager2 = new FireManager2();
        mInstagramManager2 = new InstagramManager2();
    }

    public BackendManager2 getInstance(){
        if(mManager==null){
            mManager = new BackendManager2();
        }
        return mManager;
    }

    //misc for other
    interface GetFeedClk{
        void success(ArrayList<ImageTimelike> feed);
        void error(String error);
    }

    interface LoginInstClk{
        void success(UserTimelike user);
        void error(String error);
    }

    interface LoginFireClk{
        void success();
    }
}
