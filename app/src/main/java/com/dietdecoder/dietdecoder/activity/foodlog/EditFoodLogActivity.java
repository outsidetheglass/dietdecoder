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

// control the fragments going back and forth
public class EditFoodLogActivity extends AppCompatActivity  {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = EditFoodLogActivity.this;

    int mFragmentContainerView = R.id.fragment_container_view_edit_food_log;
    Bundle mBundle;

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mWhichFragmentGoTo, mFromFragment, mWhichActivity;

    private Fragment nextFragment = null;

    public EditFoodLogActivity() {
        super(R.layout.activity_edit_food_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment defaultFragment = new EditFoodLogFragment();

            fragmentTransaction
                    .replace(mFragmentContainerView, defaultFragment)
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
                // put down the activity parent was edit food log
                mBundle.putString(Util.ARGUMENT_ACTIVITY_FROM,
                        Util.ARGUMENT_ACTIVITY_FROM_EDIT_FOOD_LOG);
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

        nextFragment = null;
        // change which fragment starts based on which button was pressed
        if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT)) {
            // ask for specific time
            nextFragment = new EditFoodLogFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_TIME_FRAGMENT)) {
            // ask for specific time
            nextFragment = new LogSpecificDateTimeFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT) ) {
            // ask for specific time
            nextFragment = new LogSpecificDateTimeFragment();

        }  else if ( Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY)) {
            nextFragment = null;
        }
        return nextFragment;
    }
}

