package com.maciej.software.nev2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity
public class Workout {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String type;
    private String version;

    public Workout(@Nullable String type, @Nullable String version) {
        this.type = type;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
