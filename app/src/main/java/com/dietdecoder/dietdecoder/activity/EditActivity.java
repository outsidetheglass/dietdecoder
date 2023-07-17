package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.symptomlog.EditSymptomLogFragment;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

// control the fragments going back and forth
public class EditActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = EditActivity.this;

    int mFragmentContainerView = R.id.fragment_container_view_edit;
    Bundle mBundle;

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mWhichFragmentGoTo, mFromFragment, mWhichActivity;

    private Fragment mNextFragment = null;

    public EditActivity() {
        super(R.layout.activity_edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_edit);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        // if there's an intent, it's the fragment passing info along
        if ( getIntent().getExtras() != null ) {
            mBundle = getIntent().getExtras();
            // check why we're here
            Boolean isActionDuplicate = Objects.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                    Util.ARGUMENT_ACTION_DUPLICATE);

            // if it's a food log and we want to duplicate it
            if ( mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID_ARRAY) ) {
                // if coming from duplicate, copy it and put the new ID in the bundle
                if ( isActionDuplicate ) {
                    // we want to duplicate so do that given our existing ID
                    //String duplicatedFoodLogIdString =
                      //      duplicateFoodLog(mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID_ARRAY));
                    // then put our new duplicated food log in
                    //mBundle.putString(Util.ARGUMENT_FOOD_LOG_ID, duplicatedFoodLogIdString);

                    // start the next fragment
//                    Util.startNextFragment(getSupportFragmentManager().beginTransaction(),
//                            mFragmentContainerView, new EditFoodLogFragment());
                }

//                mNextFragment = new EditFoodLogFragment();
                // TODO remove this, I think this code is silly, just go into food log edit fragment
                //  and go
                //  straight to the right next fragment from there, don't come back here waste of
                //  energy

                //  mWhichFragmentGoTo = Util.ARGUMENT_ACTION_DUPLICATE;
                //
//                if ( mBundle.containsKey(Util.ARGUMENT_GO_TO)) {
//                    // get the string that tells us which button was pressed
//                    // so we know which fragment is next to start
//                    mWhichFragmentGoTo = mBundle.getString(Util.ARGUMENT_GO_TO);
//                }

            } else  if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {
                // we're here with symptom log
                mNextFragment = new EditSymptomLogFragment();
                // TODO add a duplicate in here same as for food log, or figure out how to do it
                //  with the dao the right way

            } //TODO make this for the rest of the symptom, ingredient, recipe edits

            // check which fragment we should start next based on
            // which button was pressed in the fragment we just came from
//            Fragment mNextFragment = whichFragmentNext(mWhichFragmentGoTo);

            if ( mNextFragment != null ) {

                // start the next fragment
                Util.startNextFragmentBundle(thisActivity,
                        getSupportFragmentManager().beginTransaction(),
                        mFragmentContainerView, mNextFragment, mBundle);
            } else {
                // it was null so go back to the main activity
                Util.goToMainActivity(null, thisActivity);
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
            Util.goToMainActivity(null, thisActivity);
        } else {
            // do something
        }

        return false;
    }

//    private Fragment whichFragmentNext(String whichFragmentGoTo) {
//
//        Log.d(TAG, whichFragmentGoTo);
//        nextFragment = null;
//        // change which fragment starts based on which button was pressed
//        if (Objects.equals(whichFragmentGoTo,
//                Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG)) {
//            // ask for specific time
//            nextFragment = new EditFoodLogFragment();
//
//        } else if (Objects.equals(whichFragmentGoTo,
//                Util.ARGUMENT_GO_TO_SPECIFIC_TIME_FRAGMENT)) {
//            // ask for specific time
//            //TODO is this where to put in only edit date or time maybe
//            nextFragment = new LogSpecificDateTimeFragment();
//
//        } else if (Objects.equals(whichFragmentGoTo,
//                Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT) ) {
//            // ask for specific date
//            //TODO is this where to put in only edit date or time maybe
//            nextFragment = new LogSpecificDateTimeFragment();
//
//        } else if (Objects.equals(whichFragmentGoTo,
//                Util.ARGUMENT_GO_TO_EDIT_SYMPTOM_LOG)) {
//            nextFragment = new EditSymptomLogFragment();
//
//        }
//        return nextFragment;
//    }


//    //TODO move this to the DAO and do it with SQLite the right way
//    // duplicate a food log given an id string and return the newly created duplicated food log's id
//    private String duplicateFoodLog(String foodLogIdString){
//        FoodLogViewModel mFoodLogViewModel =
//                new ViewModelProvider(this).get(FoodLogViewModel.class);
//
//        // turn it into its UUID
//        UUID mFoodLogId = UUID.fromString(foodLogIdString);
//        // use that to get the food log itself
//        FoodLog foodLog = mFoodLogViewModel.viewModelGetFoodLogFromId(mFoodLogId);
//
//
//        // get the info to duplicate
//        UUID duplicatedFoodLogId = foodLog.getIngredientId();
//        String duplicatedFoodLogName = foodLog.getIngredientName();
//
//        // make the new food log from that name, it will set the date consumed to be now
//        FoodLog duplicatedFoodLog = new FoodLog(duplicatedFoodLogId, duplicatedFoodLogName);
//        mFoodLogViewModel.viewModelInsertFoodLog(duplicatedFoodLog);
//
//        // get the other info to copy over from the original food log
//        Instant duplicatedFoodLogAcquired = foodLog.getInstantAcquired();
//        Instant duplicatedFoodLogCooked = foodLog.getInstantCooked();
////        String duplicatedFoodLogBrand = foodLog.getBrand();
//        // now update it with those values
////        duplicatedFoodLog.setBrand(duplicatedFoodLogBrand);
//        duplicatedFoodLog.setInstantAcquired(duplicatedFoodLogAcquired);
//        duplicatedFoodLog.setInstantCooked(duplicatedFoodLogCooked);
//        mFoodLogViewModel.viewModelUpdateFoodLog(duplicatedFoodLog);
//
//        return duplicatedFoodLog.getFoodLogId().toString();
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

