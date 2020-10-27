package com.maciej.software.nev2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.maciej.software.nev2.util.TypeFormatter;

@Entity
public class Exercise implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String repRange;
    private double weight;
    public long workoutId = 0;

    @Ignore
    public Exercise(String name, String repRange, double weight) {
        this.name = name;
        this.repRange = repRange;
        this.weight = weight;
    }

    @Ignore
    public Exercise(long id, String name, String repRange, double weight) {
        this.id = id;
        this.name = name;
        this.repRange = repRange;
        this.weight = weight;
    }

    public Exercise(String name, String repRange, double weight, long workoutId) {
        this.name = name;
        this.repRange = repRange;
        this.weight = weight;
        this.workoutId = workoutId;
    }

    public String getName() {
        return this.name;
    }

    public String getRepRange() {
        return this.repRange;
    }

    public double getWeight() {
        return this.weight;
    }

    public String getFormattedWeight() {
        return TypeFormatter.getFormattedWeight(this.weight);
    }

    public long getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setRepRange(String repRange) {
        this.repRange = repRange;
    }

    public void setId(long id) {
        this.id = id;
    }


    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public Exercise(Parcel in) {
        id = in.readLong();
        name = in.readString();
        repRange = in.readString();
        weight = in.readDouble();
        workoutId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(repRange);
        parcel.writeDouble(weight);
        parcel.writeLong(workoutId);
    }
}

