package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.Objects;
import java.util.UUID;

public class NewSymptomLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {


    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = NewSymptomLogActivity.this;

    int mFragmentContainerView = Util.fragmentContainerViewAddSymptomLog;
    Bundle mBundle;

    String mWhichFragmentGoTo;

    private Fragment nextFragment = null;


    public NewSymptomLogActivity() {
        super(R.layout.activity_new_symptom_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_symptom_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        SymptomLogViewModel mSymptomLogViewModel =
                new ViewModelProvider(this).get(SymptomLogViewModel.class);


        // if we had no view made, go straight to asking user for intensity
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment newSymptomIntensityFragment = new NewSymptomIntensityFragment();

            // give the intensity fragment which symptom we're setting
            mBundle = getIntent().getExtras();

            // get the ID of the symptom chosen to make a log of
            UUID symptomId = UUID.fromString(mBundle.getString(Util.ARGUMENT_SYMPTOM_ID));
            String symptomName = mBundle.getString(Util.ARGUMENT_SYMPTOM_NAME);
            // make the symptom log and set its intensity and put its ID to send to fragment
            SymptomLog symptomIntensityLog = new SymptomLog(symptomId, symptomName);
            mSymptomLogViewModel.viewModelInsertSymptomLog(symptomIntensityLog);
            mBundle.putString(Util.ARGUMENT_SYMPTOM_LOG_ID,
                    symptomIntensityLog.getSymptomLogId().toString());

            newSymptomIntensityFragment.setArguments(mBundle);


            fragmentTransaction
                    .replace(mFragmentContainerView,
                            newSymptomIntensityFragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
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
//        }
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

        // TODO add symptom log for all symptom IDs chosen
        // TODO fix, right now it only will work for first symptom and maybe not even that
        // TODO make it work for intensity for all symptoms

        // change which fragment starts based on which button was pressed
        if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SYMPTOM_INTENSITY)) {
            // we know the day but not the time
            // ask that before we can move on
            nextFragment = new NewSymptomIntensityFragment();

        }
        return nextFragment;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

