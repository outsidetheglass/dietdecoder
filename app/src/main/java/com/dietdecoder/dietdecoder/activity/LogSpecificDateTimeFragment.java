package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.LogDateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogBrandFragment;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LogSpecificDateTimeFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;
    //Log.d(TAG, "onClick: mDatePickerDateTime = " + mDatePickerDateTime.getHour());

    Button mDateButtonSave;
    DatePicker mDatePicker;
    EditText mEditTextTime, mEditTextDate;
    TextView titleTextView, mTextViewTimePicker;

    Bundle mBundle, mBundleNext;
    FoodLogViewModel mFoodLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    FoodLog mFoodLog;
    SymptomLog mSymptomLog;

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mFoodLogIdString, mOnlySetDateOrTime, mWhatToChange, mWhereFrom, mLogIdToAddString, mCurrentLogIdToAddString;
    int mFragmentContainer, hour, minute;
    Instant mInstant, mInstantSet;
    LocalDateTime mDatePickerDateTime, mDateTime, mDateTimeSet;
    Boolean isFromEditFoodLog, settingFoodLog = Boolean.FALSE, settingSymptomLog = Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE;
    ArrayList<String> mLogIdToAddStringArray;

    // IDs for which dialog to open
    static final int TIME_DIALOG_ID = 1111;
    static final int DATE_DIALOG_ID = 1112;
    Fragment mDefaultNextFragment;


    public LogSpecificDateTimeFragment() {
        super(R.layout.fragment_log_specific_date_time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_specific_date_time, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        thisActivity = getActivity();
        //TODO finish setting this up for food vs symptom
        titleTextView = view.findViewById(R.id.textview_title_log_specific_date);

        // set the listener on the save button
        mDateButtonSave = view.findViewById(R.id.button_log_specific_date_save);
        mDateButtonSave.setOnClickListener(this);

        // set the time and date edit texts
        mEditTextTime = view.findViewById(R.id.edittext_log_specific_date_time);
        // set a listener on it to open the picker
        // to run onClick method when they are clicked
        mEditTextTime.setOnClickListener(this);
        // do the same for date
        mEditTextDate = view.findViewById(R.id.edittext_log_specific_date_date);
        mEditTextDate.setOnClickListener(this);

        // if there was data passed use that to get date time to change, else use current or go
        // back to food log activity
        if ( !getArguments().isEmpty() ) {
            // get info about what date time we're starting from
            mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
            mBundle = getArguments();
            // find out if we have a food log or symptom log to set the date time of
            // if food log ID was given then set that
            if ( mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID) || mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID_ARRAY) ) {

                settingFoodLog = Boolean.TRUE;
                //TODO change this when food log is an array
//                mLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
//                // now get the food log associated with that UUID
//                mFoodLog =
//                        mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mLogIdString));
                // when we have to replace the fragment container, replace the right one
                mFragmentContainer = Util.fragmentContainerViewAddFoodLog;
                mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
                mWhereFrom = mBundle.getString(Util.ARGUMENT_ACTIVITY_FROM);
                // now get the info associated with that UUID
                mFoodLog =
                        mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mFoodLogIdString));
                mDateTime = Util.getDateTimeConsumedAcquiredCooked(mWhatToChange, mFoodLog);

                // so check what we've added so far and then go to the next info needed
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
            } else if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID) || mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {
                settingSymptomLog = Boolean.TRUE;
                mFragmentContainer = Util.fragmentContainerViewAddSymptomLog;

                //if it's an array, setting all the arrays passed in to be the same as single
                //TODO remove non array as an option
                if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {
                    mLogIdToAddString =
                            mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
                } else {
                    // if it isn't an array just set the one
                    mLogIdToAddString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID);
                }
                // parse the ids from the string into an array
                mLogIdToAddStringArray =
                        Util.cleanBundledStringIntoArrayList(mLogIdToAddString);

                mCurrentLogIdToAddString = mLogIdToAddStringArray.get(0);
                // now get the info associated with that UUID
                mSymptomLog =
                        mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(mCurrentLogIdToAddString));

                // get the date time of current symptom log so we can set the default view with it
                mDateTime = Util.getDateTimeBeganChanged(mWhatToChange, mSymptomLog);

                // if begin hasn't been set yet, then that's what we're changing
                if ( mBundle.getString(Util.ARGUMENT_CHANGE) == Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN ){
                    titleTextView.setText(getResources().getString(R.string.title_log_begin));
                }

            }


            // if we're only supposed to set one or the other
//            if ( !!TextUtils.isEmpty(mBundle.getString(Util.ARGUMENT_ONLY_SET)) ) {
//                mOnlySetDateOrTime = mBundle.getString(Util.ARGUMENT_ONLY_SET);
//                if ( mOnlySetDateOrTime == Util.ARGUMENT_ONLY_SET_DATE ) {
//                    mEditTextTime.setVisibility(View.GONE);
//                } else {
//                    mEditTextDate.setVisibility(View.GONE);
//                }
//            }


            // Current date and time
            mHour = mDateTime.getHour();
            mMinute = mDateTime.getMinute();
            mDay = mDateTime.getDayOfMonth();
            mMonth = mDateTime.getMonthValue()-1;
            mYear = mDateTime.getYear();

            // set time and date
            mEditTextTime.setText(Util.setTimeString(mHour, mMinute));
            mEditTextDate.setText(Util.setDateString(mDay, mMonth, mYear));

        } else {
            // no point in being in a set date time fragment without which log to update
            Toast.makeText(getContext(), "Can't set a date or time for a nonexistent log, going " +
                            "back...", Toast.LENGTH_SHORT).show();
            Util.goToMainActivity(thisActivity);
        }


    }//end onViewCreated


    // create time picker dialog
    public Dialog createDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set to true for getting 24 hour time or not
                // if switched, also switch the pretty string print for set Time in Util
                Boolean setTo24HourTime = Boolean.TRUE;

                // set time picker as given time
                return new TimePickerDialog(getActivity(), timePickerListener, mHour, mMinute,
                    setTo24HourTime);

            case DATE_DIALOG_ID:

                // set date picker as given date
                return new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth,
                        mDay);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            mHour = hour;
            mMinute = minute;
            mEditTextTime.setText(Util.setTimeString(hour, minute));
        }

    };

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            mEditTextDate.setText(Util.setDateString(day, month, year));
        }

    };


    //
    @Override
    public void onClick(View view) {
//
        // set our relevant data to use in new location
        mBundleNext = new Bundle();
        mBundleNext.putString(Util.ARGUMENT_FOOD_LOG_ID, mFoodLogIdString);
//        //int id = view.getId();
        switch (view.getId()) {
            // which button was clicked

            case R.id.edittext_log_specific_date_time:
                Toast.makeText(getContext(), getResources().getString(R.string.pick_time),
                        Toast.LENGTH_SHORT).show();
                createDialog(TIME_DIALOG_ID).show();

                break;
            case R.id.edittext_log_specific_date_date:
                createDialog(DATE_DIALOG_ID).show();

                break;
            case R.id.button_log_specific_date_save:
                Toast.makeText(getContext(), getResources().getString(R.string.saving),
                        Toast.LENGTH_SHORT).show();

                setLogInstants(mWhatToChange, mLogIdToAddStringArray);


                // if next fragment has been set use that
//                if ( !TextUtils.isEmpty(mOnlySetDateOrTime) ) {
//                    // put which we're changing into the bundle
//                    LogPartOfDayFragment logPartOfDayFragment = new LogPartOfDayFragment();
//                    logPartOfDayFragment.setArguments(mBundleNext);
//                    // actually go to the next place now
//                    getParentFragmentManager().beginTransaction()
//                            .replace(Util.fragmentContainerViewEditFoodLog, logPartOfDayFragment)
//                            .setReorderingAllowed(true)
//                            .addToBackStack(null)
//                            .commit();
//                }
                // else it means go back to main activity or edit
                //else {
                //}


                break;

            default:
                break;
        }//end switch case

    }//end onClick

    private void setSymptomLogInstants(String whatToChange,
                                       ArrayList<String> symptomLogIdsToAddStringArray){

        // for each string in array update that log's instant began
        for (String symptomLogIdString: symptomLogIdsToAddStringArray){
            // now get the food log associated with each UUID
            SymptomLog symptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(symptomLogIdString));
            // get the date and time user picked and put them in a bundle
            Bundle integerBundleToSetDateTime = Util.setBundleGivenIntegersOfDateTime(mMinute, mHour, mDay, mMonth, mYear);
            // set that date and time to either began or changed (determined by whatToChange)
            symptomLog = Util.setSymptomLogBeganChanged(whatToChange, symptomLog,
                    integerBundleToSetDateTime);
            // put our updated log into the database
            mSymptomLogViewModel.viewModelUpdateSymptomLog(symptomLog);
        }

        if (android.text.TextUtils.equals(whatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN)) {
            // we're done with begin so reset UI for changed/ended
            titleTextView.setText(getResources().getString(R.string.title_log_change));
            // we have changed begin so now set changed/ended instead
            mBundle.remove(Util.ARGUMENT_CHANGE);
            mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
            mWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED;
        } else {
            // done so lets go home
            Util.goToListSymptomLog(thisActivity);
        }

    }


    private void setFoodLogInstants(String whatToChange,
                                    ArrayList<String> foodLogIdsToAddStringArray){
        // for each string in array update that log's instant began
        for (String foodLogIdString: foodLogIdsToAddStringArray){
            // now get the food log associated with each UUID
            FoodLog foodLog =
                    mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(foodLogIdString));
            // get the date and time user picked and put them in a bundle
            Bundle integerBundleToSetDateTime = Util.setBundleGivenIntegersOfDateTime(mMinute, mHour, mDay, mMonth, mYear);
            //then set the values from the food log
            foodLog = Util.setFoodLogConsumedAcquiredCooked(whatToChange, foodLog, integerBundleToSetDateTime);
            mFoodLogViewModel.viewModelUpdateFoodLog(foodLog);
        }
        //done with for loop, set that we've changed what we needed to

        // we have changed consumed so now set acquired/cooked instead
        if ( whatToChange == Util.ARGUMENT_CHANGE_CONSUMED ) {
            // when consumed is done being set,
            // we'll circle back around and ask user for acquired and cooked
            whatToChange = Util.ARGUMENT_CHANGE_ACQUIRED;
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
        } else if (mWhatToChange == Util.ARGUMENT_CHANGE_ACQUIRED) {
            whatToChange = Util.ARGUMENT_CHANGE_COOKED;
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
        } else if (mWhatToChange == Util.ARGUMENT_CHANGE_COOKED) {
            // after cooked is set go to the next info needed
            mDefaultNextFragment = new NewFoodLogBrandFragment();
        }

        //TODO either here or don't go in to set bundle, ask user if food was cooked and acquired
        // at same time as most recent food log
        // (i.e. if they're putting sushi in and this is setting fish,
        // set rice acquired and cooked to same as fish food log)
        mBundle = Util.setBundleFoodLogInstants(mBundle, whatToChange);

    }


    private void setLogInstants(String whatToChange,
                                ArrayList<String> logIdToAddStringArray){
        // we have the date time to set now
        if (settingSymptomLog){
            setSymptomLogInstants(whatToChange, logIdToAddStringArray);
        }
        else if (settingFoodLog) {
            // TODO add check for if acquired and cooked are same as an existing food log
            // TODO then use whatToChange or a new argument
            //  that's for set them all the same as another food log
            setFoodLogInstants(whatToChange, logIdToAddStringArray);

            // go back to activity to go to the next fragment
            // with our picked date
            // if we're from edit we have more to do
            if ( Objects.equals(mWhereFrom, Util.ARGUMENT_ACTIVITY_FROM_EDIT_FOOD_LOG) ) {
                // go to edit food log activity with all the needed info
                Util.goToEditFoodLogActivity(thisActivity, mFoodLogIdString,
                        Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG);
            } else {
                // we didn't come from edit food log
                // so we're adding a food log

                // set the food log id we're changing in to be accessible in next fragment
                mDefaultNextFragment.setArguments(mBundle);
                // and go to the next info needed
                //TODO this probably will break when needing multiple instants
                Util.startNextFragment(getParentFragmentManager().beginTransaction(), mFragmentContainer,
                        mDefaultNextFragment);
            }
        }
    }

}//end fragment
