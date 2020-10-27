package com.maciej.software.nev2.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.maciej.software.nev2.util.TypeFormatter;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Exercise.class,
        parentColumns = "id",
        childColumns = "exerciseId",
        onDelete = CASCADE))
public class Detail {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String repsDone;
    private String note;
    private Date dateAdded;
    private long exerciseId;

    public Detail(String repsDone, @Nullable String note, Date dateAdded, long exerciseId) {
        this.repsDone = repsDone;
        this.note = note;
        this.exerciseId = exerciseId;
        this.dateAdded = dateAdded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRepsDone() {
        return repsDone;
    }

    public void setRepsDone(String repsDone) {
        this.repsDone = repsDone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String getFormattedDate() {
        return TypeFormatter.formatDate(this.dateAdded);
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
