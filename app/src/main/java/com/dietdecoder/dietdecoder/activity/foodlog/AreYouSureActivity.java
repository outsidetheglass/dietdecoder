package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.util.Objects;
import java.util.UUID;

public class AreYouSureActivity extends AppCompatActivity  {
    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = AreYouSureActivity.this;

    Bundle mBundle;

    String mWhichFragmentGoTo;

    private Fragment mNextFragment = null;

    int mFragmentContainerView = R.id.fragment_container_view_are_you_sure;

    FoodLogViewModel mFoodLogViewModel;

    public AreYouSureActivity() {
        super(R.layout.activity_are_you_sure);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // if there's an intent, it's the fragment passing info along
            if ( getIntent().getExtras() != null ) {
                mBundle = getIntent().getExtras();

                // get the string that tells us which button was pressed
                // so we know which fragment is next to start
                mWhichFragmentGoTo = mBundle.getString(Util.ARGUMENT_FRAGMENT_GO_TO);

                // check which fragment we should start next based on
                // which button was pressed in the fragment we just came from
                mNextFragment = whichFragmentNext(mWhichFragmentGoTo);

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
                    // no next fragment to go to was found
                    // so check if we're here to duplicate and do that if we are
                    if ( Objects.equals(mBundle.get(Util.ARGUMENT_ACTION),
                            Util.ARGUMENT_DUPLICATE)) {

                        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);


                        // get the food log from the UUID in the previous activity
                        FoodLog foodLogToCopy = mFoodLogViewModel.viewModelGetFoodLogFromId(
                                UUID.fromString(mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID)) );
                        // copy it with current datetime consumed
                        //TODO fix this with name
                        String foodLogToCopyName = foodLogToCopy.getIngredientId().toString();
//                        FoodLog newFoodLog = new FoodLog(foodLogToCopyName);
                        // TODO put this somewhere else, like in the dao itself
//                        newFoodLog.setBrand(foodLogToCopy.getBrand());
//                        newFoodLog.setInstantCooked(foodLogToCopy.getInstantCooked());
//                        newFoodLog.setInstantAcquired(foodLogToCopy.getInstantAcquired());
//                        mFoodLogViewModel.viewModelUpdateFoodLog(newFoodLog);
//                        String newId = newFoodLog.getFoodLogId().toString();
                        String newId = foodLogToCopy.getFoodLogId().toString();

                        // go to edit log with our new food log
                        Intent intent = Util.intentWithFoodLogIdStringButtonActivity(thisActivity,
                                newId,
                                Util.ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY, Util.ARGUMENT_ACTIVITY_FROM_ARE_YOU_SURE);
                        startActivity(intent);

                    }

                    // so go back to the food logs list
                    startActivity(new Intent(thisActivity, FoodLogActivity.class));
                }
            }

        }
    }

    private Fragment whichFragmentNext(String whichFragmentGoTo) {

        // change which fragment starts based on which button was pressed
        if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_DELETE_FOOD_LOG)) {
            // delete the food log
            mNextFragment = new DeleteFoodLogFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT)) {
            //update food log
//            mNextFragment = new EditFoodLogActivity();

        } return mNextFragment;
    }
}

