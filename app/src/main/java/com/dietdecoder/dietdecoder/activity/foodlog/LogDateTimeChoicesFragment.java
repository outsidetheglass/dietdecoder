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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class LogDateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    LocalDateTime mNowDateTime = LocalDateTime.now(), mEarlierTodayDateTime, mYesterdayDateTime,
            mDayBeforeYesterdayDateTime;
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
    Boolean settingFoodLog = Boolean.FALSE;
    Boolean settingSymptomLog = Boolean.FALSE;
    Boolean isSymptomLogBeginInstantSet = Boolean.FALSE;
    Boolean isSymptomLogChangedInstantSet = Boolean.FALSE;
    TextView questionTextView;

    Instant mInstantConsumed, mInstantBegan, mInstant;



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
        questionTextView = view.findViewById(R.id.textview_question_log_date_time_choices_time);

        // find out if we have a food log or symptom log to set the date time of
        // if food log ID was given then set that
        if ( mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID) ) {
            settingFoodLog = Boolean.TRUE;
            mLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
            // now get the food log associated with that UUID
            mFoodLog =
                    mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mLogIdString));
            // when we have to replace the fragment container, replace the right one
            mFragmentContainer = Util.fragmentContainerViewAddSymptomLog;

        } else if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID) ) {
            settingSymptomLog = Boolean.TRUE;
            mLogIdString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID);
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromId(UUID.fromString(mLogIdString));

            mFragmentContainer = Util.fragmentContainerViewAddFoodLog;
            if ( mBundle.containsKey(Util.ARGUMENT_CHANGE) ){
                if (mBundle.getString(Util.ARGUMENT_CHANGE).equals(Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN)) {
                    isSymptomLogBeginInstantSet = Boolean.FALSE;
                    isSymptomLogChangedInstantSet = Boolean.FALSE;
                } else {
                    isSymptomLogBeginInstantSet = Boolean.TRUE;
                }
            }
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
                if (settingSymptomLog){
                    // still set the instants because we need to check the user is fine with the
                    // default for ended/changed time
                    setSymptomInstants(mNowDateTime, null);
                }
                else if (settingFoodLog) {
                    // just now is the default when making a food log
                    // so we don't need to ask anything else about time or day
                    goToNextFragment(null);
                }
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_earlier_today),
                        Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mEarlierTodayDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);

                if (settingSymptomLog){
                    setSymptomInstants(mEarlierTodayDateTime, null);
                }
                else if (settingFoodLog) {
                    //then set the values from the food log
                    setFoodLogConsumedInstant(mEarlierTodayDateTime, null);
                }
                break;

            case R.id.button_log_date_time_choices_yesterday:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_yesterday), Toast.LENGTH_SHORT).show();
                mYesterdayDateTime = LocalDateTime.now().minusDays(1);

                // if we're setting symptom log
                if (settingSymptomLog){
                    // set the date time the symptom began
                    setSymptomInstants(mYesterdayDateTime, new LogPartOfDayFragment());
                } else if (settingFoodLog){
                    //then set the values from the food log
                    setFoodLogConsumedInstant(mYesterdayDateTime, new LogPartOfDayFragment());
                }
                // and which fragment to go to next
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

        // reset to setting begin time date for the next fragment
        mBundle.remove(Util.ARGUMENT_CHANGE);
        mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN);

        // and now which fragment to go to next
        // if nothing given use the default fragment
        if ( Objects.isNull(nextFragment)) {
            // so just start asking details on what was consumed
            nextFragment = new NewFoodLogBrandFragment();
        }
        // put which we're changing into the bundle
        nextFragment.setArguments(mBundle);
        // actually go to the next place now
        getParentFragmentManager().beginTransaction()
                .replace(mFragmentContainer, nextFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    //set the begin instant
    // and from recent symptoms of same type, calculate average duration
    // and set the instant changed to that
    private void setSymptomInstants(LocalDateTime localDateTime, Fragment whereTo){
        mInstant = Util.instantFromLocalDateTime(localDateTime);

        // check if this is the first run through of the fragment, i.e. should we set begin
        // or when the symptom changed/ended
        // if begin has already been set then we set the changed time
        if ( isSymptomLogBeginInstantSet ) {
            // we don't set begin because it already was
            // so set the end instant
            mSymptomLog.setInstantChanged(mInstant);
            // set the database with the updated symptom log times
            mSymptomLogViewModel.viewModelUpdateSymptomLog(mSymptomLog);
            isSymptomLogChangedInstantSet = Boolean.TRUE;
            // we're done so go back to list symptoms log
            goToListSymptomLog();
        }
        else {
            // begin has not been set yet, so set that first
            mSymptomLog.setInstantBegan(mInstant);
            isSymptomLogBeginInstantSet = Boolean.TRUE;
            mBundle.remove(Util.ARGUMENT_CHANGE);
            mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);

            // and then that means the end should be set to the default for now
            // so find the average duration of the recent symptom logs of the same symptom
            Duration averageDuration =
                    mSymptomLogViewModel.viewModelGetAverageSymptomDuration(mSymptomLog.getSymptomName());
            // and set default for changed time to be from that average
            mSymptomLog.setInstantChanged(Util.instantFromDurationAndStartInstant(mInstantBegan,
                    averageDuration));
            // set the database with the updated symptom log times
            mSymptomLogViewModel.viewModelUpdateSymptomLog(mSymptomLog);

            // if they clicked yesterday
            // then it should set defaults to yesterday at same time as now
            // but now go to part of day for real time
            if ( !Objects.isNull(whereTo)){
                goToNextFragment(whereTo);
            } else {
                // else they did not click yesterday and time has been set, so we need to set
                // question and get another user input for when time changed
                // reset question to ask about end then
                questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_intensity_log_changed_time));
                // if they did not choose yesterday or another date for begin
                // future time for symptom ended doesn't make sense
                // so make those buttons vanish
                mButtonAnotherDate.setVisibility(View.INVISIBLE);
                mButtonYesterday.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setFoodLogConsumedInstant(LocalDateTime localDateTime, Fragment whereTo){
        mInstantConsumed = Util.instantFromLocalDateTime(localDateTime);
        mFoodLog.setInstantConsumed(mInstantConsumed);
        mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);
        // and which fragment to go to next
        goToNextFragment(whereTo);
    }

}//end fragment
