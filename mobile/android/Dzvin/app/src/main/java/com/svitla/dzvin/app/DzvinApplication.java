package com.svitla.dzvin.app;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.svitla.dzvin.app.activity.AlertDetailsActivity;
import com.svitla.dzvin.app.model.Alert;

public class DzvinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Alert.class);

        Parse.initialize(this, "95kkc2ylHJWBLk7Uhm98SH7kyQ2e9Y9elU6sBDgS", "TEMbOtXCORsSI8h7QIKbHomsaTVEj9scVgdNaz6Q");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        PushService.setDefaultPushCallback(this, AlertDetailsActivity.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
