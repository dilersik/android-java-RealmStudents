package com.dilerdesenvolv.realmstudents.domain;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Discipline extends RealmObject {
    public static final String ID = "com.dilerdesenvolv.domain.RealmObject.ID";

    @PrimaryKey
    private long id;
    private String name;
    // @Ignore : nao cria coluna na tabela

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
