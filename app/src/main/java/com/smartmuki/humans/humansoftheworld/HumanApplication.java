package com.smartmuki.humans.humansoftheworld;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by abmitra on 7/23/2015.
 */
public class HumanApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
