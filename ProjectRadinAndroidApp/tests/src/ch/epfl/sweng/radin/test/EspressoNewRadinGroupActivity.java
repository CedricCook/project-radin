package ch.epfl.sweng.radin.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.sweng.radin.NewRadinGroupActivity;
import ch.epfl.sweng.radin.R;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

/**
 * A class for testing everything that concerns RadinGroups
 * @author CedricCook
 *
 */
public class EspressoNewRadinGroupActivity extends ActivityInstrumentationTestCase2<NewRadinGroupActivity> {
	public EspressoNewRadinGroupActivity() {
		super(NewRadinGroupActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		Intent myIntent = new Intent();
		myIntent.putExtra("key", "My example list");
		setActivityIntent(myIntent);
		getActivity();
	}
	
	
	public void testNewRadinGroup() {	
		//TODO add restriction on special characters
		Espresso.onView(ViewMatchers.withId(R.id.edit_name)).perform(ViewActions.typeText("Ma super liste!"))
		.check(ViewAssertions.matches(ViewMatchers.withText("Ma super liste!")));
		
		Espresso.onView(ViewMatchers.withId(R.id.people)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("julie")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.create)).perform(ViewActions.click());
		//should close activity and toast list created
	}
	
	public void testNewRadinGroupWithoutName() {
		Espresso.onView(ViewMatchers.withId(R.id.people)).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("julie")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click());
		Espresso.onView(ViewMatchers.withId(R.id.create)).perform(ViewActions.click());
		// should toast no name and stay on activity
	}
	public void testNewRadinGroupWithoutParticipant() {
		Espresso.onView(ViewMatchers.withId(R.id.edit_name)).perform(ViewActions.typeText("Ma super liste!"));
		Espresso.onView(ViewMatchers.withId(R.id.create)).perform(ViewActions.click());
		// should toast no people on list and stay on activity
	}
	
}
