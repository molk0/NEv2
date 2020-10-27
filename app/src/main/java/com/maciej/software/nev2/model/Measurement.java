package com.maciej.software.nev2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Measurement {

    @PrimaryKey(autoGenerate = true)
    long id;
    String name;
    double valueIn;
    double valueCm;

    public Measurement(@NonNull String name, double valueIn, double valueCm) {
        this.name = name;
        this.valueIn = valueIn;
        this.valueCm = valueCm;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValueIn() {
        return valueIn;
    }

    public void setValueIn(double valueIn) {
        this.valueIn = valueIn;
    }

    public double getValueCm() {
        return valueCm;
    }

    public void setValueCm(double valueCm) {
        this.valueCm = valueCm;
    }

    public double convertInToCm(double value) {
        return value * 2.54;
    }

    public double convertCmToIn(double value) {
        return value * (1/2.54);
    }
}
