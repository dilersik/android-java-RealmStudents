package com.dilerdesenvolv.realmstudents.domain;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MigrationData implements RealmMigration {

    public static final int VERSION = 3;

    // oldVersion version = versao atual da parada
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            schema.get("Student") // DEVE ser passado nome exato da classe
                    .addField("age", int.class);

            oldVersion ++;
        }

        if (oldVersion == 1) {
            schema.get("Student")
                    .addField("teste", String.class);

            oldVersion ++;
        }

        if (oldVersion == 2) {
            schema.create("Address")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addField("street", String.class)
                    .addField("neighborhood", String.class)
                    .addField("city", String.class)
                    .addField("state", String.class)
                    .addField("country", String.class);

            schema.get("Student")
                    .addRealmObjectField("address", schema.get("Address"))
                    .removeField("age")
                    .removeField("teste");

            oldVersion ++;
        }
    }

}
