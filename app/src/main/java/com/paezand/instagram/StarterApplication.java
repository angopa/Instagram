package com.paezand.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class StarterApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();

        //Enable Local Datastore
        Parse.enableLocalDatastore(this);

        //Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("202ac01cf6d45f7e7ba41d1136bc8a86213328b4")
                .clientKey("76b883b596195a568480f60f9352fdb926dde46e")
                .server("http://ec2-52-14-121-59.us-east-2.compute.amazonaws.com:80/parse/")
        .build());

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        //Optional enable public read access
        //defaultAC.setPublicReadAccess(true)
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
