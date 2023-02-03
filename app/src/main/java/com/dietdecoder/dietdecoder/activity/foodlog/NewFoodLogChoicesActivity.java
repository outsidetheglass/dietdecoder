package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.util.Objects;
import java.util.UUID;

public class NewFoodLogChoicesActivity extends AppCompatActivity  {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = NewFoodLogChoicesActivity.this;

    Bundle mBundle;


    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mWhichFragmentGoTo;

    private Fragment nextFragment = null;

    public NewFoodLogChoicesActivity() {
        super(R.layout.activity_new_food_log_choices);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment newFoodLogNameFragment = new NewFoodLogNameFragment();

            fragmentTransaction
                    .replace(R.id.fragment_container_view_choices, newFoodLogNameFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
        // if there's an intent, it's the fragment passing info along
        if ( getIntent().getExtras() != null ) {
            mBundle = getIntent().getExtras();
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
                        .replace(R.id.fragment_container_view_choices, mNextFragment)
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
                Util.ARGUMENT_GO_TO_SPECIFIC_TIME)) {
            // ask for specific time
            nextFragment = new LogSpecificTimeFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_DATE)) {
            // ask for specific time
            nextFragment = new LogSpecificDateFragment();

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
                Util.ARGUMENT_DONE)) {
            nextFragment = null;
        }
        return nextFragment;
    }
}
