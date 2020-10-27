package com.maciej.software.nev2.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.maciej.software.nev2.model.Detail;

import java.util.List;

@Dao
public interface DetailDao {

    @Insert
    void insert(Detail detail);

    @Insert
    long insertAndReturnId(Detail detail);

    @Update
    void update(Detail detail);

    @Delete
    void delete(Detail detail);

    @Query("SELECT COUNT(*) FROM detail WHERE exerciseId=:exerciseId")
    int getDetailCount(long exerciseId);

    @Query("SELECT * FROM detail WHERE id=:id")
    Detail findDetailWithId(long id);

    @Query("SELECT * FROM detail WHERE exerciseId=:exerciseId")
    List<Detail> findDetailsForExercise(long exerciseId);

/*    @Query("SELECT * FROM detail WHERE exerciseId=:exerciseId AND id = (SELECT MAX(id) FROM detail)")
    Detail findRecentDetailForExercise(long exerciseId);*/

    @Query("SELECT * FROM detail WHERE exerciseId=:exerciseId ORDER BY id DESC LIMIT 1")
    Detail findRecentDetailForExercise(long exerciseId);

}
