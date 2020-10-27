package com.maciej.software.nev2.detail;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.db.ExerciseDao;
import com.maciej.software.nev2.db.NEDatabase;
import com.maciej.software.nev2.model.Exercise;
import com.maciej.software.nev2.ui.ExerciseDetail.ExerciseDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailScreen {

    private Exercise exercise;
    private final String intentWeight = "50";

    @Rule
    public ActivityTestRule<ExerciseDetailActivity> activityRule =
            new ActivityTestRule<ExerciseDetailActivity>(ExerciseDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(context, ExerciseDetailActivity.class);
                    exercise = new Exercise("Test", "Test", Double.valueOf(intentWeight), 1);
                    exercise.setId(1);
                    result.putExtra("exercise", exercise);
                    return result;
                }
            };

    @Before
    public void init() {
        NEDatabase db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getTargetContext(),
                NEDatabase.class)
                .build();
        ExerciseDao dao = db.getExerciseDao();
        dao.insert(exercise);
    }

    @Test
    public void shouldShowExerciseWeight_inTheRepRangeEditText() {
        String repRangePrefix = intentWeight + "x";
        onView(withId(R.id.input_reps_done)).check(matches(withText(repRangePrefix)));
    }

    @Test
    public void clickOnAddNoteButton_showEditText() {
        onView(withId(R.id.add_note_text_button)).perform(click());
        onView(withId(R.id.input_note)).check(matches(isDisplayed()));
    }

    /**
     * Tested in ExerciseScreen because otherwise foreign key constraint is violated
     */
    @Test
    public void addNewNote_showItInTheRecentHistory() {
/*        //violates foreign key constraint -- exercise needs to be added to the db first

        String reps = "test was edited";
        onView(withId(R.id.input_reps_done)).perform(typeText(reps), closeSoftKeyboard());
        onView(withId(R.id.detail_save_button)).perform(click());

        // confirm the note has been added
        onView(withId(R.id.recent_reps)).check(matches(withText(reps)));*/
    }

    @Test
    public void clickEdit_loadDetailInfoIntoEditTexts() {
    }

    @Test
    public void clickOnMoreDetailsButton_showMoreDetails() {
        onView(withId(R.id.show_more_button)).perform(click());
    }
}
