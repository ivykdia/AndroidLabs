package algonquin.cst2335.li000793;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Test case to check if the password requirement in general
     */
    @Test
    public void mainActivityTest() {

        ViewInteraction appCompatEditText = onView(withId(R.id.editText)); // tell somebody to go find something in some place.
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if at least one upper case is met for the password requirement
     */
    @Test
    public void testFindMissingUpperCase() {
        // find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editText)); // tell somebody to go find something in some place.
        // type in password123#$*
        appCompatEditText.perform(replaceText("password123#$*"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));

        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if at least one lower case is met for the password requirement
     */
    @Test
    public void testFindMissingLowerCase() {
        // find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editText)); // tell somebody to go find something in some place.
        // type in PASSWORD123#$*
        appCompatEditText.perform(replaceText("PASSWORD123#$*"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if at least one digit number is met for the password requirement
     */
    @Test
    public void testFindMissingDigit() {
        // find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editText)); // tell somebody to go find something in some place.
        // type in PASSword#$*
        appCompatEditText.perform(replaceText("PASSword#$*"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if at least one special character is met for the password requirement
     */
    @Test
    public void testFindMissingSpecialChar() {
        // find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editText)); // tell somebody to go find something in some place.
        // type in PASSword123
        appCompatEditText.perform(replaceText("PASSword123"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if all password requirements are met
     */
    @Test
    public void testMeetRequirement() {
        // find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editText)); // tell somebody to go find something in some place.
        // type in PASSword123#$*
        appCompatEditText.perform(replaceText("PASSword123#$*"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //click the button
        materialButton.perform(click());
        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView));
        //check the text do an assertion
        textView.check(matches(withText("Your password meets the requirements")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
