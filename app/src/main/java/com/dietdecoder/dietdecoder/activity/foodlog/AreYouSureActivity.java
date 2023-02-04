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
import com.dietdecoder.dietdecoder.activity.Util;

import java.util.Objects;

public class AreYouSureActivity extends AppCompatActivity  {
    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = AreYouSureActivity.this;

    Bundle mBundle;

    String mWhichFragmentGoTo;

    private Fragment mNextFragment = null;

    int mFragmentContainerView = R.id.fragment_container_view_are_you_sure;

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
                    // it was null so go back to the food logs list
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
                Util.ARGUMENT_GO_TO_UPDATE_FOOD_LOG)) {
            //update food log
            //TODO uncomment this when delete is working
            //mNextFragment = new UpdateFoodLogFragment();

        }
        return mNextFragment;
    }
}

