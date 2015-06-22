package org.kazin.timelike;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.kazin.timelike.backend.BackendManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void getFeedAndShow(){
            BackendManager backendManager = BackendManager.getInstance();

            backendManager.initialize(new BackendManager.BackendInitializeClk() {
                @Override
                public void success() {

                }
            });
    }
}