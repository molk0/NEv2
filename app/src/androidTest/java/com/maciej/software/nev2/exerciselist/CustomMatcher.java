package com.maciej.software.nev2.exerciselist;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class CustomMatcher {

    public static Matcher<View> withElement(final String itemText) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                return allOf(isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("With item element text: " + itemText);
            }
        };
    }

    public static int getCountFromRecyclerView(@IdRes int RecyclerViewId) {
        final int[] count = {0};

        Matcher matcher = new TypeSafeMatcher() {
            @Override
            protected boolean matchesSafely(Object item) {
                count[0] = ((RecyclerView)item).getAdapter().getItemCount();
                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Checking item count in Recycler View.");
            }
        };
        onView(allOf(withId(RecyclerViewId), isDisplayed())).check(matches(matcher));
        return count[0];
    }
}
