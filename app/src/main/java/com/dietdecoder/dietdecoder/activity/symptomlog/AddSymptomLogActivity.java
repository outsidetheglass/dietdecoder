package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AddSymptomLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {


    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = AddSymptomLogActivity.this;
    private Context thisContext;

    int mFragmentContainerView = Util.fragmentContainerViewAddSymptomLog;
    Bundle mBundle, mBundleNext;

    String mWhichFragmentGoTo;
    ArrayList<String> mSymptomsToAddArrayListIdStrings, mSymptomLogsArrayListIdStrings;

    private Fragment mNextFragment = null;

    SymptomLog mSymptomLog;
    SymptomLogViewModel mSymptomLogViewModel;
    SymptomViewModel mSymptomViewModel;


    public AddSymptomLogActivity() {
        super(R.layout.activity_add_symptom_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_add_symptom_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);


        // if we had no view made, set the variables
        if (savedInstanceState == null) {

            // declare and set variables
            mNextFragment = new SymptomIntensityFragment();
            thisContext = thisActivity.getApplicationContext();
            mSymptomLogViewModel =
                    new ViewModelProvider(this).get(SymptomLogViewModel.class);
            mSymptomViewModel =
                    new ViewModelProvider(this).get(SymptomViewModel.class);
            mSymptomsToAddArrayListIdStrings = new ArrayList<>();
            mSymptomLogsArrayListIdStrings = new ArrayList<>();
        // if we had a savedinstancestate, like user turned the phone to horizontal or vice versa
        // or if we've made the variables just like normal
        // both ways, next thing is to check the bundle has the info we need

        // get the info about which symptom we're logging
        // check if info passed in exists, if not then go home
        Util.checkValidFragment(getIntent().getExtras(), thisActivity);

        // get the info now we know it's valid
        mBundle = getIntent().getExtras();
        mBundleNext = mBundle;

        // get the array of Id's of symptoms to add
        String mSymptomIdsToAddString = mBundle.getString(Util.ARGUMENT_SYMPTOM_ID_ARRAY);
        // clean the array string
        mSymptomIdsToAddString = Util.cleanArrayString(mSymptomIdsToAddString);

        // go through the string and at the comma's add that ID to the array
        for (String mSymptomIdString : mSymptomIdsToAddString.split(",")) {
            //Log.d(TAG, mSymptomIdString);
            mSymptomsToAddArrayListIdStrings.add(mSymptomIdString);

            //get info on the symptom to make the log based on defaults
            SymptomLog symptomLog = new SymptomLog(UUID.fromString(mSymptomIdString));
            // make the symptom log
            mSymptomLogViewModel.viewModelInsert(symptomLog);
            // put its ID in array to send to fragment
            mSymptomLogsArrayListIdStrings.add(symptomLog.getLogId().toString());
        }
        // putting set bundle in Util so it's easier for me to see what exactly is in
        // each bundle
        mBundleNext = Util.setNewSymptomLogBundleFromLogIdStringArray(
                mSymptomLogsArrayListIdStrings);

    }
        // then go to the specific fragments to change away from the defaults
        Util.startNextFragmentBundle(thisActivity,
                getSupportFragmentManager().beginTransaction(),
                mFragmentContainerView,
                mNextFragment, mBundleNext);





        // leaving this here in case I want this activity to do more than just intensity
        //TODO ask when it started and ended

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
//                // start the next fragment
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction
//                        .replace(mFragmentContainerView, mNextFragment)
//                        .setReorderingAllowed(true)
//                        .addToBackStack(null)
//                        .commit();
//            } else {
//                // it was null so go back to the logs list
//                startActivity(new Intent(thisActivity, ListSymptomLogActivity.class));
//            }
//
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

            // do something
        } else if (item.getItemId() == R.id.action_go_home) {
            // do something
            Util.goToMainActivity(null, thisActivity);
        }   else if (item.getItemId() == R.id.action_more) {

            // Initializing the popup menu and giving the reference as current logContext
            PopupMenu popupMenu = new PopupMenu(thisContext, findViewById(R.id.action_more));
            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.item_more_menu, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.END);
            // if an option in the menu is clicked
            popupMenu.setOnMenuItemClickListener(moreMenuItem -> {
                // which button was clicked
                switch (moreMenuItem.getItemId()) {

                    // go to the right activity
                    case R.id.more_all_symptoms:
                        Util.goToListSymptomActivity(null, thisActivity, null);
                        break;

                    case R.id.more_all_ingredients:
                        Util.goToListIngredientActivity(thisContext, thisActivity, null);
                        break;

                    case R.id.more_export_activity:
                        Util.goToExportActivity(thisContext, thisActivity);
                        break;

                    default:
                        break;
                }//end switch case for which menu item was chosen

                return true;
            });
            // Showing the popup menu
            popupMenu.show();

        }

        return false;
    }


    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

