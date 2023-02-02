package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dietdecoder.dietdecoder.R;

import java.util.Objects;

public class NewFoodLogChoicesActivity extends AppCompatActivity  {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private final Activity thisActivity = NewFoodLogChoicesActivity.this;

    Bundle mBundle;

    private static final String ARGUMENT_HOUR = "hour";
    private static final String ARGUMENT_MINUTE = "minute";
    private static final String ARGUMENT_DAY = "day";
    private static final String ARGUMENT_MONTH = "month";
    private static final String ARGUMENT_YEAR = "year";
    private static final String ARGUMENT_WHICH_BUTTON = "which_button";

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mWhichButton;

    private static final String mJustNowString = "just_now";
    private static final String mYesterdayString = "yesterday";
    private static final String mEarlierTodayString = "earlier_today";
    private static final String mAnotherTimeString = "another_time";


    public NewFoodLogChoicesActivity() {
        super(R.layout.activity_new_food_log_choices);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment logDateTimeChoicesFragment = new LogDateTimeChoicesFragment();

            fragmentTransaction
                    .replace(R.id.fragment_container_view_choices, logDateTimeChoicesFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
        // if there's an intent, it's the fragment passing hour and minute along
        if ( getIntent().getExtras() != null ) {
            mBundle = getIntent().getExtras();
            // get the string that tells us which button was pressed
            // so we know which fragment is next to start
            mWhichButton = mBundle.getString(ARGUMENT_WHICH_BUTTON);
            Log.d(TAG, " onCreate, mWhichButton: " + mWhichButton);

            Fragment mNextFragment = whichFragmentNext(mWhichButton);

            // set the data to pass along to be the hour and minute
            // given from the previous fragment
            mNextFragment.setArguments(mBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .replace(R.id.fragment_container_view_choices, mNextFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private Fragment whichFragmentNext(String whichButton) {

        // TODO move name of ingredient first
        // so we can then ask about time and then timepicker can directly save
        Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);

        Fragment nextFragment = null;

        // change which fragment starts based on which button was pressed
        // if it was the just now button
        if (Objects.equals(whichButton, mJustNowString)) {
            Log.d(TAG, " whichFragmentNext, mWhichButton: " + mWhichButton);
            // we don't need to ask anything else about time or day
            // so just start asking what was consumed
            nextFragment = new NewFoodLogNameFragment();
        } else if (Objects.equals(whichButton, mYesterdayString)) {
            // we know the day but not the time
            // ask that before we can move on
            nextFragment = new LogWhatTimeOfDayFragment();
        } else if (Objects.equals(whichButton, mEarlierTodayString)) {
            // just set it to preferred amount earlier and go for the name
            nextFragment = new NewFoodLogNameFragment();
        } else if (Objects.equals(whichButton, mAnotherTimeString)) {
            // just set it to preferred amount earlier and go for the name
            nextFragment = new LogSpecificTimeFragment();
        }
        //        private static final String mEarlierTodayString = "earlier_today";
//        private static final String mAnotherTimeString = "another_time";
        return nextFragment;
    }
}

