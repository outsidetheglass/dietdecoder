package com.dietdecoder.dietdecoder.activity.ingredientlog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;

import java.util.ArrayList;
import java.util.UUID;

public class AddIngredientLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {


    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = AddIngredientLogActivity.this;
    private Context thisContext;

//    int mFragmentContainerView = ;
    Bundle mBundle, mBundleNext;

    String mWhichFragmentGoTo;
    ArrayList<String> mIngredientsToAddArrayListIdStrings, mIngredientLogsToAddArrayListIdStrings;

    private Fragment mNextFragment = null;

    IngredientLog mIngredientLog;
    IngredientLogViewModel mIngredientLogViewModel;
    IngredientViewModel mIngredientViewModel;


    public AddIngredientLogActivity() {
        super(R.layout.activity_new_ingredient_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_ingredient_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        //TODO Ingredient log will need to search ingredients for the ingredient being added,
        // and if it doesn’t already exist add it in,
        // if it does then use existing ingredient ID.

        // if we had no view made, go straight to asking user for intensity
        if (savedInstanceState == null) {

            // declare and set variables
            mNextFragment = new IngredientAmountFragment();
            thisContext = thisActivity.getApplicationContext();
            mIngredientLogViewModel =
                    new ViewModelProvider(this).get(IngredientLogViewModel.class);
            mIngredientViewModel =
                    new ViewModelProvider(this).get(IngredientViewModel.class);
            mIngredientsToAddArrayListIdStrings = new ArrayList<>();
            mIngredientLogsToAddArrayListIdStrings = new ArrayList<>();

            // give the intensity fragment which ingredient we're setting

            // get the info about which ingredient we're logging
            // check if info passed in exists, if not then go home
            if ( getIntent().getExtras() != null ) {
                // get the info
                mBundle = getIntent().getExtras();
                mBundleNext = mBundle;

                // get the array of Id's of ingredients to add
                String mIngredientIdsToAddString =
                        mBundle.getString(Util.ARGUMENT_INGREDIENT_ID_ARRAY);
                // clean the array string
                mIngredientIdsToAddString = Util.cleanArrayString(mIngredientIdsToAddString);
                //Log.d(TAG, mIngredientIdsToAddString);
                // go through the string and at the comma's add that ID to the array
                for (String mIngredientIdString : mIngredientIdsToAddString.split(",")) {
                    //Log.d(TAG, mIngredientIdString);
                    mIngredientsToAddArrayListIdStrings.add(mIngredientIdString);

                    //get info on the ingredient to make the log based on defaults
                    UUID ingredientId = UUID.fromString(mIngredientIdString);
                    // make the ingredient log
                    IngredientLog ingredientLog = new IngredientLog(ingredientId);
                    mIngredientLogViewModel.viewModelInsert(ingredientLog);
                    // put its ID in array to send to fragment
                    mIngredientLogsToAddArrayListIdStrings.add(ingredientLog.getLogId().toString());
                }
                // putting set bundle in Util so it's easier for me to see what exactly is in
                // each bundle
                mBundleNext =
                        Util.setNewIngredientLogBundleFromArray(mIngredientLogsToAddArrayListIdStrings);

                Log.d(TAG, mBundleNext.toString());
                // then go to the specific fragments to change away from the defaults
                Util.startNextFragmentBundle(thisActivity, getSupportFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewAddIngredientLog,
                        mNextFragment, mBundleNext);

            } else {
                // there's no information about which ingredient to add, so
                // tell the user that they got here by mistake, it's a bug
                // must choose which ingredient before this activity
                String mWrongPlaceLetsGoHome =
                        getResources().getString(R.string.wrong_place_lets_go_home);
                Toast.makeText(getApplicationContext(), mWrongPlaceLetsGoHome,
                        Toast.LENGTH_SHORT).show();
                Util.goToMainActivity( null, thisActivity);
            }

        }

        // leaving this here in case I want this activity to do more than just intensity
        //TODO ask when it started and ended
//        // if there's an intent, it's the fragment passing info along
//        if ( getIntent().getExtras() != null ) {
//            mBundle = getIntent().getExtras();
//
//            // get the string that tells us which button was pressed
//            // so we know which fragment is next to start
//            mWhichFragmentGoTo = mBundle.getString(Util.ARGUMENT_FRAGMENT_GO_TO);
//
//            Log.d(TAG, String.valueOf(mBundle.getClass()));
//            // check which fragment we should start next based on
//            // which button was pressed in the fragment we just came from
//            Fragment mNextFragment = whichFragmentNext(mWhichFragmentGoTo);
//
//            if ( mNextFragment != null ) {
//                // set the data to pass along info
//                // given from the previous fragment
//                mNextFragment.setArguments(mBundle);
//
//            } else {
//                // it was null so go back to the logs list
//                startActivity(new Intent(thisActivity, ListIngredientLogActivity.class));
//            }
//        }
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

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

