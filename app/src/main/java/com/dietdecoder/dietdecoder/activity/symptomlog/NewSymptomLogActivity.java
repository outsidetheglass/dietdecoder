package com.dietdecoder.dietdecoder.activity.symptomlog;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.ListSymptomLogActivity;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.List;
import java.util.Objects;

public class NewSymptomLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = NewSymptomLogActivity.this;

    int mFragmentContainerView = Util.fragmentContainerViewAddSymptomLog;
    Bundle mBundle;

    String mWhichFragmentGoTo;

    private Fragment nextFragment = null;

    SymptomLogListAdapter mSymptomLogListAdapter;
    SymptomLogViewModel mSymptomLogViewModel;


    public NewSymptomLogActivity() {
        super(R.layout.activity_new_symptom_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_new_symptom_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        if (savedInstanceState == null) {

            // mate the view for listing the items in the log
            RecyclerView recyclerViewSymptomLogNameChoices =
                    findViewById(R.id.recyclerview_new_symptom_log_name_choices);
            // add horizontal lines between each recyclerview item
            recyclerViewSymptomLogNameChoices.addItemDecoration(new DividerItemDecoration(recyclerViewSymptomLogNameChoices.getContext(),
                    DividerItemDecoration.VERTICAL));


            mSymptomLogListAdapter = new SymptomLogListAdapter(new SymptomLogListAdapter.LogDiff());
            recyclerViewSymptomLogNameChoices.setAdapter(mSymptomLogListAdapter);
            recyclerViewSymptomLogNameChoices.setLayoutManager(new LinearLayoutManager(this));
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

            mSymptomLogViewModel.viewModelGetAllSymptomLogs().observe(this,
                    new Observer<List<SymptomLog>>() {
                        @Override
                        public void onChanged(List<SymptomLog> logs) {
                            // Update the cached copy of the words in the adapter.
                            mSymptomLogListAdapter.submitList(logs);

                        }
                    });


//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            Fragment newSymptomLogNameFragment = new NewSymptomLogNameFragment();
//
//            fragmentTransaction
//                    .replace(mFragmentContainerView, newSymptomLogNameFragment)
//                    .setReorderingAllowed(true)
//                    .addToBackStack(null)
//                    .commit();
        }
        // if there's an intent, it's the fragment passing info along
        if ( getIntent().getExtras() != null ) {
            mBundle = getIntent().getExtras();

            // put down we started activity in new symptom log
            mBundle.putString(Util.ARGUMENT_ACTIVITY_FROM,
                    Util.ARGUMENT_ACTIVITY_FROM_NEW_FOOD_LOG);

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
                        .replace(mFragmentContainerView, mNextFragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            } else {
                // it was null so go back to the symptom logs list
                startActivity(new Intent(thisActivity, ListSymptomLogActivity.class));
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

        // change which fragment starts based on which button was pressed
        if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_DATE_TIME_CHOICES)) {
            // we know the day but not the time
            // ask that before we can move on
//            nextFragment = new LogDateTimeChoicesFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_PART_OF_DAY)) {
            // we know the day but not the time
            // ask that before we can move on
//            nextFragment = new LogPartOfDayFragment();


        }  else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_TIME_FRAGMENT)) {
            // ask for specific time
//            nextFragment = new LogSpecificDateTimeFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT)) {
            // ask for specific time
//            nextFragment = new LogSpecificDateTimeFragment();

        } else if (Objects.equals(whichFragmentGoTo,
                Util.ARGUMENT_GO_TO_NAME)) {
            // we just got the specific time the user wants
            // so go to next, ask for name
//            nextFragment = new NewSymptomLogNameFragment();
        }
//        else if ( Objects.equals(whichFragmentGoTo,
//                Util.ARGUMENT_GO_TO_DESCRIPTION)) {
//
//
////            nextFragment = new NewSymptomLogDescriptionFragment();
//
//        }
//        else if ( Objects.equals(whichFragmentGoTo,
//                Util.ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY)) {
//            nextFragment = null;
//        }
        return nextFragment;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

