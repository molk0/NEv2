package com.maciej.software.nev2.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.maciej.software.nev2.model.Detail;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.model.ExercisesInWorkout;
import com.maciej.software.nev2.model.Workout;

@Database(entities = {Exercise.class, Detail.class, Workout.class, ExercisesInWorkout.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class NEDatabase extends RoomDatabase {

    public abstract ExerciseDao getExerciseDao();

    public abstract WorkoutDao getWorkoutDao();

    public abstract DetailDao getDetailDao();

    public abstract ExercisesInWorkoutDeprecatedDao getExercisesInWorkoutDao_DEPR();

    public abstract ExercisesInWorkoutDao getExercisesInWorkoutDao();

    private static NEDatabase exerciseDb;


    public static NEDatabase getInstance(Context context) {
        if (exerciseDb == null)
            exerciseDb = buildDatabaseInstance(context);

        return exerciseDb;
    }

    @NonNull
    private static NEDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                NEDatabase.class,
                "NE-Room")
                .allowMainThreadQueries()
                .build();
    }

    public void cleanUp() {
        exerciseDb = null;
    }

/*    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };*/
}