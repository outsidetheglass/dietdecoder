package com.dietdecoder.dietdecoder.activity.foodlog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.symptomlog.ListSymptomLogActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class LogDateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    LocalDateTime mNowDateTime, mEarlierTodayDateTime, mYesterdayDateTime, mDayBeforeYesterdayDateTime;
    Button mButtonJustNow, mButtonEarlierToday, mButtonAnotherDate, mButtonYesterday;

    // TODO set this to be a preference
    Integer hoursEarlierInt = 4;
    String mLogIdString;
    int mFragmentContainer;

    Bundle mBundle;
    FoodLogViewModel mFoodLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    FoodLog mFoodLog;
    SymptomLog mSymptomLog;

    Instant mInstantConsumed, mInstantBegan;


    public LogDateTimeChoicesFragment() {
        super(R.layout.fragment_log_date_time_choices);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_date_time_choices, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set the food log id to add time to the bundle
        mBundle = getArguments();
        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

        // find out if we have a food log or symptom log to set the date time of
        // if food log ID was given then set that
        if (Objects.isNull(mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID)) ) {
            mLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
            // now get the food log associated with that UUID
            mFoodLog =
                    mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mLogIdString));
            // when we have to replace the fragment container, replace the right one
            mFragmentContainer = Util.fragmentContainerViewAddSymptomLog;

        } else if ( Objects.isNull(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID)) ) {
            mLogIdString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID);
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromId(UUID.fromString(mLogIdString));

            mFragmentContainer = Util.fragmentContainerViewAddFoodLog;
        }

        // set the listeners on the buttons
        // to run onClick method when they are clicked
        mButtonJustNow = (Button) view.findViewById(R.id.button_log_date_time_choices_just_now);
        mButtonJustNow.setOnClickListener(this);

        mButtonYesterday = (Button) view.findViewById(R.id.button_log_date_time_choices_yesterday);
        mButtonYesterday.setOnClickListener(this);

        mButtonAnotherDate =
                (Button) view.findViewById(R.id.button_log_date_time_choices_another_date);
        mButtonAnotherDate.setOnClickListener(this);

        mButtonEarlierToday = (Button) view.findViewById(R.id.button_log_date_time_choices_earlier_today);
        mButtonEarlierToday.setOnClickListener(this);

    }//end onViewCreated


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_date_time_choices_just_now:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_just_now),
                        Toast.LENGTH_SHORT).show();

                // they chose just now
                // if we've made it here, then the log has already been made
                // which means it's been set to the default
                if (!Objects.isNull(mSymptomLog)){
                    // if they are here from setting a symptom log
                    // and choosing just now, they probably are still experiencing the symptom
                    // so both end and begin are set to default for that symptom
                    // TODO set defaults for each symptom time duration if this is first time for
                    //  setting that symptom
                    // done setting symptom so go back to list
                    goToListSymptomLog();
                }
                else if (!Objects.isNull(mFoodLog)) {

                    // we don't need to ask anything else about time or day
                    // so just start asking details on what was consumed
                    // and which fragment to go to next
                    goToNextFragment(new NewFoodLogBrandFragment());
                }
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_earlier_today),
                        Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mEarlierTodayDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);

                if (!Objects.isNull(mSymptomLog)){
                    // set the date time the symptom began
                    setSymptomInstants(mEarlierTodayDateTime);
                }
                else if (!Objects.isNull(mFoodLog)) {
                    //then set the values from the food log
                    setFoodLogConsumedInstant(mEarlierTodayDateTime);
                    // and which fragment to go to next
                    goToNextFragment(new NewFoodLogBrandFragment());
                }
                break;

            case R.id.button_log_date_time_choices_yesterday:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_yesterday), Toast.LENGTH_SHORT).show();
                mYesterdayDateTime = LocalDateTime.now().minusDays(1);

                // if we're setting symptom log
                if (!Objects.isNull(mSymptomLog)){
                    // set the date time the symptom began
                    setSymptomInstants(mYesterdayDateTime);
                } else if (!Objects.isNull(mFoodLog)){
                    //then set the values from the food log
                    setFoodLogConsumedInstant(mYesterdayDateTime);
                }
                // and which fragment to go to next
                goToNextFragment(new LogPartOfDayFragment());
                break;

            case R.id.button_log_date_time_choices_another_date:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_another_date),
                        Toast.LENGTH_SHORT).show();

                // and which fragment to go to next
                goToNextFragment(new LogSpecificDateTimeFragment());
                break;

            default:
                break;
        }//end switch case


    }//end onClick

    private void goToListSymptomLog(){
        startActivity(new Intent(getActivity(), ListSymptomLogActivity.class));
    }

    private void goToNextFragment(Fragment nextFragment){

        // put which we're changing into the bundle
        nextFragment.setArguments(mBundle);
        // actually go to the next place now
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(mFragmentContainer, nextFragment);
        ft.commit();
    }

    //set the begin instant
    // and from recent symptoms of same type, calculate average duration
    // and set the instant changed to that
    private void setSymptomInstants(LocalDateTime localDateTime){

        mInstantBegan = Util.instantFromLocalDateTime(localDateTime);
        mSymptomLog.setInstantBegan(mInstantBegan);

        // find the average duration of the recent symptom logs of the same symptom
        mSymptomLogViewModel.viewModelGetAverageSymptomDuration(mSymptomLog.getSymptomName());
        mSymptomLogViewModel.viewModelUpdateSymptomLog(mSymptomLog);
    }

    private void setFoodLogConsumedInstant(LocalDateTime localDateTime){

        mInstantConsumed = Util.instantFromLocalDateTime(localDateTime);
        mFoodLog.setInstantConsumed(mInstantConsumed);
        mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);
    }
}//end fragment
