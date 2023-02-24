package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

// control the fragments going back and forth
public class EditFoodLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_edit_food_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);
        if (savedInstanceState == null ) {

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

            Log.d(TAG, mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID));
            // if coming from duplicate, copy it and put the new ID in the bundle
            if (Objects.equals(mBundle.getString(Util.ARGUMENT_ACTION), Util.ARGUMENT_DUPLICATE)) {
                String foodLogIdString =
                        duplicateFoodLog(mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID));
                mBundle.putString(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);

                // start the next fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .replace(mFragmentContainerView, new EditFoodLogFragment())
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }

            // get the string that tells us which button was pressed
            // so we know which fragment is next to start
            mWhichFragmentGoTo = mBundle.getString(Util.ARGUMENT_FRAGMENT_GO_TO);


            Log.d(TAG, "after get string go to " + mWhichFragmentGoTo);
            // check which fragment we should start next based on
            // which button was pressed in the fragment we just came from
            Fragment mNextFragment = whichFragmentNext(mWhichFragmentGoTo);

            Log.d(TAG, "after method which fragment " + mNextFragment.toString());
            if ( mNextFragment != null ) {
                // put down the activity parent was edit food log

                mBundle.putString(Util.ARGUMENT_ACTIVITY_FROM, Util.ARGUMENT_ACTIVITY_FROM_EDIT_FOOD_LOG);
                Log.d(TAG, mBundle.toString());
                // set the data to pass along info
                // given from the previous fragment
                mNextFragment.setArguments(mBundle);

                Log.d(TAG, mNextFragment.getArguments().toString());
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

            // do something
        } else if (item.getItemId() == R.id.action_go_home) {
            // do something
            startActivity(new Intent(thisActivity, MainActivity.class));
        } else {
            // do something
        }

        return false;
    }

    private Fragment whichFragmentNext(String whichFragmentGoTo) {

        Log.d(TAG, whichFragmentGoTo);
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


    //TODO move this to the DAO and do it with SQLite the right way
    private String duplicateFoodLog(String foodLogIdString){
        FoodLogViewModel mFoodLogViewModel =
                new ViewModelProvider(this).get(FoodLogViewModel.class);

        // turn it into its UUID
        UUID mFoodLogId = UUID.fromString(foodLogIdString);
        // use that to get the food log itself
        FoodLog foodLog = mFoodLogViewModel.viewModelGetFoodLogFromId(mFoodLogId);


        // get the info to duplicate
        String duplicatedFoodLogName = foodLog.getIngredientId();

        // make the new food log from that name, it will set the date consumed to be now
        FoodLog duplicatedFoodLog = new FoodLog(duplicatedFoodLogName);
        mFoodLogViewModel.viewModelInsertFoodLog(duplicatedFoodLog);

        // get the other info to copy over from the original food log
        Instant duplicatedFoodLogAcquired = foodLog.getInstantAcquired();
        Instant duplicatedFoodLogCooked = foodLog.getInstantCooked();
        String duplicatedFoodLogBrand = foodLog.getBrand();
        // now update it with those values
        duplicatedFoodLog.setBrand(duplicatedFoodLogBrand);
        duplicatedFoodLog.setInstantAcquired(duplicatedFoodLogAcquired);
        duplicatedFoodLog.setInstantCooked(duplicatedFoodLogCooked);
        mFoodLogViewModel.viewModelUpdateFoodLog(duplicatedFoodLog);

        return duplicatedFoodLog.getFoodLogId().toString();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

