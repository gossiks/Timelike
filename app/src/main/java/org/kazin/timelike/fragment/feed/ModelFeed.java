package org.kazin.timelike.fragment.feed;

import org.kazin.timelike.backend.BackendManager;
import org.kazin.timelike.object.ImageTimelike;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ModelFeed {

    private static ModelFeed model;
    private PresenterFeed presenter;
    private BackendManager mBackend;

    public ModelFeed() {
        mBackend = new BackendManager();
    }

    private void setMVP(PresenterFeed presenter){
        this.presenter = presenter;
    }

    public static ModelFeed getInstance(PresenterFeed presenter) {
        if(model == null){
            model = new ModelFeed();
            model.setMVP(presenter);
            return model;
        }
        else{
            return model;
        }
    }

    public void onLaunch() {
        mBackend.initialize(new BackendManager.BackendInitializeClk() {
            @Override
            public void success() {
                presenter.setMessage("logged in");
                loadFeed();
            }
        });
    }

    public void onClickReload(){
        loadFeed();
    }

    private void loadFeed(){
        mBackend.getFeedInst(new BackendManager.BackendGetFeedClk() {
            @Override
            public void successInst(ArrayList<ImageTimelike> feed) {
                mBackend.getFeedParse(feed, new BackendManager.BackendGetFeedClk() {
                    @Override
                    public void successInst(ArrayList<ImageTimelike> feed) {

                    }
                });
                presenter.setFeed(feed);
            }

        });
    }
    public void onLikeReceived(String imageid, long timelike){
        //TODO
    }


}
