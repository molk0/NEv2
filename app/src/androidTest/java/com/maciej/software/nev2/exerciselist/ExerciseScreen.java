package com.maciej.software.nev2.exerciselist;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.maciej.software.nev2.R;
import com.maciej.software.nev2.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExerciseScreen {


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickAddExerciseFAB_openNewExerciseUi() {
        onView(allOf(isDisplayed(), withId(R.id.fab_add_exercise))).perform(click());
        onView(withId(R.id.input_name)).check(matches(isDisplayed()));
    }

    @Test
    public void addExerciseToList_confirmItIsDisplayed() {
        String name = "Name";
        String repRange = "3x10";
        String weight = "50";

        // open new exercise activity
        onView(allOf(isDisplayed(), withId(R.id.fab_add_exercise))).perform(click());

        // type in new exercise information and save
        onView(withId(R.id.input_name)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.input_rep_range)).perform(typeText(repRange), closeSoftKeyboard());
        onView(withId(R.id.input_weight)).perform(typeText(weight), closeSoftKeyboard());
        onView(withId(R.id.save_exercise_fab)).perform(click());

        // confirm new exercise has been added
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view)))
                .perform(scrollTo(hasDescendant(withText(name))));
        onView(CustomMatcher.withElement(name)).check(matches(isDisplayed()));
    }

    @Test
    public void clickItemButton_openBottomSheet() {
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        RecyclerItemViewAction.clickChildWithId(R.id.menu_ibtn)));
        onView(withId(R.id.bottom_sheet_update)).check(matches(isDisplayed()));
    }

    @Test
    public void updateWeightForAnExercise_confirmItIsUpdated() {
        String weight = "30";

        // select "Update weight" from the bottom sheet of the first element in the list
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        RecyclerItemViewAction.clickChildWithId(R.id.menu_ibtn)));
        onView(withId(R.id.bottom_sheet_update)).perform(click());

        // update the weight
        onView(withId(R.id.input_dialog_update_weight)).perform(typeText(weight));
        onView(withText(R.string.button_update)).perform(click());

        // confirm the weight has been updated
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view)))
                .perform(scrollTo(hasDescendant(withText(weight))));
        onView(CustomMatcher.withElement(weight)).check(matches(isDisplayed()));
    }

    @Test
    public void editExercise_confirmItIsEdited() {
        String newName = "New Name";

        // select "Edit exercise" from the bottom sheet of the first item in the list
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        RecyclerItemViewAction.clickChildWithId(R.id.menu_ibtn)));

        // update exercise name
        onView(withId(R.id.bottom_sheet_edit)).perform(click());
        onView(withId(R.id.edit_name)).perform(replaceText(newName), closeSoftKeyboard());
        onView(withId(R.id.edit_exercise_fab)).perform(click());

        // confirm the name has been updated
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view)))
                .perform(scrollTo(hasDescendant(withText(newName))));
        onView(CustomMatcher.withElement(newName)).check(matches(isDisplayed()));
    }


    /**
     * Uses a custom matcher that checks the list size before and after removal.
     *  Confirms that the list size has been decremented by one.
     */
    @Test
    public void deleteLastItemInTheList() {
        int countBeforeRemoval = CustomMatcher.getCountFromRecyclerView(R.id.exercise_list_view);
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        RecyclerItemViewAction.clickChildWithId(R.id.menu_ibtn)));
        onView(withId(R.id.bottom_sheet_remove)).perform(click());
        int newCount = CustomMatcher.getCountFromRecyclerView(R.id.exercise_list_view);
        assertEquals(countBeforeRemoval-1, newCount);
    }

    @Test
    public void clickOnExercise_openItsDetail () {
        onView(allOf(isDisplayed(), withId(R.id.exercise_list_view))).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.input_reps_done)).check(matches(isDisplayed()));

        // confirm it matches the exercise
    }
}
