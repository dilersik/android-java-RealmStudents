package com.dilerdesenvolv.realmstudents.application;

import android.app.Application;

import com.dilerdesenvolv.realmstudents.domain.MigrationData;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by T-Gamer on 08/07/2016.
 */
public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("realm-students.realm")
                .schemaVersion(MigrationData.VERSION)
                .migration(new MigrationData())
                // .deleteRealmIfMigrationNeeded() // deletando a base sem migrar
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

}
