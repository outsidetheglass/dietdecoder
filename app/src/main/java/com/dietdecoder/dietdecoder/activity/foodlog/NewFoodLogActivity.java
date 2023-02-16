package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;

import java.util.Objects;

public class NewFoodLogActivity extends AppCompatActivity  {

    //TODO make recipe dropdown, listed in order of frequency made
    // the dropdown should be typeable, which selects for that recipe
    // if chosen, auto put the ingredients of that recipe into the view
    // beside each ingredient, edittext dropdown for common replacements, can search select for by typing
    // Add new ingredient at bottom, adds another row of edittext
    // Add option for unknown ingredients were in this
    // add option next to each to select if it was "this" or maybe "that", like on chips where it says Canola Oil or Sunflower Oil
    // if add new ingredient was clicked, when save is pressed add that in as a variant of the recipe
    // add view for now or earlier, which then allows user to say today, yesterday, earlier,
    // earlier selected then option shows days of the month,
    // then clicks broad times of day like morning, midday, evening, overnight, then specific times as an optional selection



    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = NewFoodLogActivity.this;

    int mFragmentContainerView = Util.fragmentContainerViewAddFoodLog;
    Bundle mBundle;

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mWhichFragmentGoTo;

    private Fragment nextFragment = null;

    public NewFoodLogActivity() {
        super(R.layout.activity_new_food_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment newFoodLogNameFragment = new NewFoodLogNameFragment();

            fragmentTransaction
                    .replace(mFragmentContainerView, newFoodLogNameFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
        // if there's an intent, it's the fragment passing info along
        if ( getIntent().getExtras() != null ) {
            mBundle = getIntent().getExtras();

            // put down we started activity in new food log
            mBundle.putString(Util.ARGUMENT_ACTIVITY_FROM,
                    Util.ARGUMENT_ACTIVITY_FROM_NEW_FOOD_LOG);

            // get the string that tells us which button was pressed
            // so we know which fragment is next to start
            mWhichFragmentGoTo = mBundle.getString(Util.ARGUMENT_FRAGMENT_GO_TO);

            Log.d(TAG, String.valueOf(mBundle.getClass()));
            // check which fragment we should start next based on
            // which button was pressed in the fragment we just came from
            Fragment mNextFragment = whichFragmentNext(mWhichFragmentGoTo);

            if ( mNextFragment != null ) {
                // set the data to pass along info
                // given from the previous fragment
                mNextFragment.setArguments(mBundle);

                // start the next fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .replace(mFragmentContainerView, mNextFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            } else {
                // it was null so go back to the food logs list
                startActivity(new Intent(thisActivity, FoodLogActivity.class));
            }
        }
    }

    private Fragment whichFragmentNext(String whichFragmentGoTo) {

        // change which fragment starts based on which button was pressed
        if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_DATE_TIME_CHOICES)) {
            // we know the day but not the time
            // ask that before we can move on
            nextFragment = new LogDateTimeChoicesFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_PART_OF_DAY)) {
            // we know the day but not the time
            // ask that before we can move on
            nextFragment = new LogPartOfDayFragment();

            if (Objects.equals(whichFragmentGoTo,
                    Util.ARGUMENT_FROM_SPECIFIC_DATE)) {
                // add to the data the new fragment will see that we already know our date
                // TODO remove this when confirm that update into database after name inserts it
                //  is working
                this.mBundle.putString(Util.ARGUMENT_FRAGMENT_FROM, Util.ARGUMENT_FROM_SPECIFIC_DATE);

            }

        }  else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_TIME_FRAGMENT)) {
            // ask for specific time
            nextFragment = new LogSpecificDateTimeFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT)) {
            // ask for specific time
            nextFragment = new LogSpecificDateTimeFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_NAME)) {
            // we just got the specific time the user wants
            // so go to next, ask for name
            nextFragment = new NewFoodLogNameFragment();
        } else if ( Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_BRAND)) {

            // go to the brand
            nextFragment = new NewFoodLogBrandFragment();

        } else if ( Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY)) {
            nextFragment = null;
        }
        return nextFragment;
    }
}

