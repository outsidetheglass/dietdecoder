package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LogPartOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;
    String mFragmentFrom = Util.ARGUMENT_FROM_PART_OF_DAY;

    int mFragmentContainer;

    //TODO set this to be preferences a different time
    Integer mEarlyHour = 8;
    Integer mMiddayHour = 12;
    Integer mAfternoonHour = 15;
    Integer mEveningHour = 19;
    Integer mMidnightHour = 23;
    // personally 8am and 11pm work for me, but preferences should be set in case they want 1am
    // or 9pm or something else
    Integer mEarliestHour = mEarlyHour;
    Integer mLatestHour = mMidnightHour;

    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight, mButtonSpecificTime, mButtonAllDay;
    TextView questionTextView;

    Bundle mBundle;
    Fragment mDefaultNextFragment, mNextFragment;

    Instant mInstantToChange, mInstantConsumed;
    Boolean settingFoodLog = Boolean.FALSE, settingSymptomLog = Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE;
    ArrayList<String> mSymptomLogIdsToAddStringArray, mFoodLogIdsToAddStringArray;
    LocalDateTime mFoodLogDateTime, mDateTime;
    Integer mHourToSet;
    String mLogIdString, foodLogIdString, mFragmentGoTo, mWhatToChange, mSymptomLogIdsToAddString, mFoodLogIdsToAddString;

    FoodLogViewModel mFoodLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    FoodLog mFoodLog;
    SymptomLog mSymptomLog;

    public LogPartOfDayFragment() {
        super(R.layout.fragment_log_part_of_day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_part_of_day, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set the listeners on the buttons
        // to run onClick method when they are clicked

        thisActivity = getActivity().getParent();
        mBundle = getArguments();
        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

        questionTextView = view.findViewById(R.id.textview_question_log_part_of_day_specific_time_range);
        mButtonEarly = view.findViewById(R.id.button_log_part_of_day_early);
        mButtonEarly.setOnClickListener(this);
        mButtonMidday = view.findViewById(R.id.button_log_part_of_day_midday);
        mButtonMidday.setOnClickListener(this);
        mButtonAfternoon = view.findViewById(R.id.button_log_part_of_day_afternoon);
        mButtonAfternoon.setOnClickListener(this);
        mButtonEvening = view.findViewById(R.id.button_log_part_of_day_evening);
        mButtonEvening.setOnClickListener(this);
        mButtonMidnight = view.findViewById(R.id.button_log_part_of_day_midnight);
        mButtonMidnight.setOnClickListener(this);
        mButtonSpecificTime = view.findViewById(R.id.button_log_part_of_day_more_specific_time);
        mButtonSpecificTime.setOnClickListener(this);
        // all day button only for symptom log adding
        mButtonAllDay = view.findViewById(R.id.button_log_part_of_day_all_day);


        // find out if we have a food log or symptom log to set the date time of
        if ( mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID) || mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID_ARRAY_TO_ADD) ) {
            // we're setting food logs
            settingFoodLog = Boolean.TRUE;
            // when we have to replace the fragment container, replace the right one
            mFragmentContainer = Util.fragmentContainerViewAddFoodLog;
            //remove the all day button if it's a food log, since there isn't an end time option
            // for food
            mButtonAllDay.setVisibility(View.INVISIBLE);

            // change which fragment we'll go to next based on what we're changing
            //TODO go into logdatetimechoices and make it work with which food log it has
            if (mWhatToChange == Util.ARGUMENT_CHANGE_CONSUMED) {
                // when consumed is done being set,
                // we'll circle back around and ask user for acquired and cooked
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if (mWhatToChange == Util.ARGUMENT_CHANGE_ACQUIRED) {
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if (mWhatToChange == Util.ARGUMENT_CHANGE_COOKED) {
                // after cooked is set go to the next info needed
                mDefaultNextFragment = new NewFoodLogBrandFragment();
            }

            // set our array from the strings passed in
            if ( mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID_ARRAY_TO_ADD) ) {
                mFoodLogIdsToAddString =
                        mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID_ARRAY_TO_ADD);
            } else {
                // if it isn't an array just set the one
                mFoodLogIdsToAddString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
            }
            // parse the ids from the string into an array
            mFoodLogIdsToAddStringArray =
                    Util.cleanBundledStringIntoArrayList(mFoodLogIdsToAddString);


        } else if (  mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID) || mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD) ) {
            //TODO just remove single symptom log id, it just should always be an array
            //we're setting symptom logs
            settingSymptomLog = Boolean.TRUE;
            // when we have to replace the fragment container, replace the right one
            mFragmentContainer = Util.fragmentContainerViewAddSymptomLog;
            // start the listener for the all day button
            mButtonAllDay.setVisibility(View.VISIBLE);
            mButtonAllDay.setOnClickListener(this);

            //if it's an array, setting all the arrays passed in to be the same as single
            if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD) ) {
                mSymptomLogIdsToAddString =
                        mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD);
            } else {
                // if it isn't an array just set the one
                mSymptomLogIdsToAddString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID);
            }
            // parse the ids from the string into an array
            mSymptomLogIdsToAddStringArray = Util.cleanBundledStringIntoArrayList(mSymptomLogIdsToAddString);

        }


    }//end onViewCreated


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_part_of_day_all_day:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_all_day),
                        Toast.LENGTH_SHORT).show();
                // set begin to be earliest and changed/ended to be latest times
                setLogInstants(mEarliestHour, mWhatToChange);
                setLogInstants(mLatestHour, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
                break;

            case R.id.button_log_part_of_day_early:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_early),
                        Toast.LENGTH_SHORT).show();
                // set the early date time to current time but with early hour and minute to 0
                setLogInstants(mEarlyHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_midday:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midday),
                        Toast.LENGTH_SHORT).show();
                setLogInstants(mMiddayHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_afternoon:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_afternoon),
                        Toast.LENGTH_SHORT).show();
                setLogInstants(mAfternoonHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_evening:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_evening),
                        Toast.LENGTH_SHORT).show();
                setLogInstants(mEveningHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_midnight:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midnight),
                        Toast.LENGTH_SHORT).show();
                setLogInstants(mMidnightHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_more_specific_time:
                // throw a toast with more specific time pulled from strings
                Toast.makeText(getContext(), getResources().getString(R.string.toast_more_specific_time),
                        Toast.LENGTH_SHORT).show();
                // only button that was different,
                // let's set the fragment to go to specific time
                mNextFragment = new LogSpecificDateTimeFragment();
                mNextFragment.setArguments(mBundle);
                Util.startNextFragment(getParentFragmentManager().beginTransaction(), mFragmentContainer,
                        mNextFragment);
                break;

            default:
                break;
        }//end switch case




    }//end onClick


    private void setSymptomLogInstants(ArrayList<String> symptomLogIdsToAddStringArray,
                                       Bundle bundleHourToSet){

        // for each string in array update that log's instant began
        for (String symptomLogIdString: symptomLogIdsToAddStringArray){
            // now get the food log associated with each UUID
            SymptomLog symptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromId(UUID.fromString(symptomLogIdString));
            symptomLog = Util.setSymptomLogBeganChanged(mWhatToChange, symptomLog,
                    bundleHourToSet);
            mSymptomLogViewModel.viewModelUpdateSymptomLog(symptomLog);
        }

        if (TextUtils.equals(mWhatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN)) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_intensity_log_changed_time));
            // we have changed begin so now set changed/ended instead
            mBundle.remove(Util.ARGUMENT_CHANGE);
            mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
            mWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED;
        } else {
            // done so lets go home
            Util.goToListSymptomLog(thisActivity);
        }

    }


    private void setFoodLogInstants(ArrayList<String> foodLogIdsToAddStringArray,
                                              Bundle bundleHourToSet){
        // for each string in array update that log's instant began
        for (String foodLogIdString: foodLogIdsToAddStringArray){
            // now get the food log associated with each UUID
            FoodLog foodLog =
                    mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(foodLogIdString));
            //then set the values from the food log
            foodLog = Util.setFoodLogConsumedAcquiredCooked(mWhatToChange, foodLog, bundleHourToSet);
            mFoodLogViewModel.viewModelUpdateFoodLog(foodLog);
        }
        //done with for loop, set that we've changed what we needed to
        // we have changed begin so now set changed/ended instead
        //TODO either here or don't go in to set bundle, ask user if food was cooked and acquired
        // at same time as most recent food log
        // (i.e. if they're putting sushi in and this is setting fish,
        // set rice acquired and cooked to same as fish food log)
        mBundle = Util.setBundleFoodLogInstants(mBundle, mWhatToChange);
    }


    private void setLogInstants(Integer hourToSet, String whatToChange){

        Bundle bundleHourToSet = new Bundle();
        bundleHourToSet.putInt(Util.ARGUMENT_HOUR, hourToSet);

        // we have the time to set now
        if (settingSymptomLog){
            setSymptomLogInstants(mSymptomLogIdsToAddStringArray, bundleHourToSet);
        }
        else if (settingFoodLog) {

            setFoodLogInstants(mFoodLogIdsToAddStringArray, bundleHourToSet);

            // set the food log id we're changing in to be accessible in next fragment
            mDefaultNextFragment.setArguments(mBundle);
            Util.startNextFragment(getParentFragmentManager().beginTransaction(), mFragmentContainer,
                    mDefaultNextFragment);
        }
    }



}//end fragment
