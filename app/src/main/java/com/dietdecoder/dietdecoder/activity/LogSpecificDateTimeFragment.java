package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
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
    IngredientLogViewModel mIngredientLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    IngredientLog mIngredientLog;
    SymptomLog mSymptomLog;

    Integer mHour, mMinute, mDay, mMonth, mYear;
    UUID mCurrentLogId;
    String mIngredientLogIdString, mOnlySetDateOrTime, mWhatToChange, mWhereFrom, mLogIdToAddString,
            mCurrentLogIdToAddString, mTitleString;
    int mFragmentContainer, hour, minute;
    Instant mInstant, mInstantSet;
    LocalDateTime mDatePickerDateTime, mDateTime, mDateTimeSet;
    Boolean mGoBackToEdit, mSettingIngredientLog = Boolean.FALSE, mSettingSymptomLog =
            Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE;
    ArrayList<String> mLogIdToAddStringArray;
    ArrayList<SymptomLog> mSymptomLogArray;
    ArrayList<IngredientLog> mIngredientLogArray;

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
            mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
            mBundle = getArguments();
            mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
            // if we came from edit this will tell us
            mGoBackToEdit = Util.setGoToEditFromBundle(mBundle);

            // find out if we have a food log or symptom log to set the date time of
            // if food log ID was given then set that
            if ( mBundle.containsKey(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) ) {
                // set the defaults for ingredient log changes
                // the id array as a string, setting symptom log is false, setting ingredient log
                // is true, and the fragment container to modify
                setDependentValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY),
                        Boolean.FALSE, Boolean.TRUE, Util.fragmentContainerViewAddIngredientLog);

            } else if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {
                // set the defaults for symptom log changes
                Log.d(TAG, mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
                setDependentValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY),
                        Boolean.TRUE, Boolean.FALSE, Util.fragmentContainerViewAddSymptomLog);
            }

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
            Util.goToMainActivity(null, thisActivity);
        }


    }//end onViewCreated

    private void setDependentValues(String logIdToAddString, Boolean settingSymptomLog,
                           Boolean settingIngredientLog, int fragmentContainer){
        // one of these will be false and the other true
        mSettingSymptomLog = settingSymptomLog;
        mSettingIngredientLog = settingIngredientLog;
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;
        // parse the ids from the string into an array
        mLogIdToAddStringArray =
                Util.cleanBundledStringIntoArrayList(logIdToAddString);
        Log.d(TAG, mLogIdToAddStringArray.toString());
        Log.d(TAG, mLogIdToAddStringArray.get(0));
        mCurrentLogIdToAddString = mLogIdToAddStringArray.get(0);
        mCurrentLogId = UUID.fromString(mCurrentLogIdToAddString);
        if ( mSettingIngredientLog ){
            mIngredientLogArray = new ArrayList<>();
            // now get the info associated with that UUID
            mIngredientLog =
                    mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mCurrentLogId);
            mSymptomLog = null;

            // if consumed hasn't been set yet, then that's what we're changing
            if ( mWhatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_CONSUMED ){
                mTitleString = getResources().getString(R.string.title_log_consumed);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_COOKED) {
                mTitleString = getResources().getString(R.string.title_log_cooked);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_ACQUIRED) {
                mTitleString = getResources().getString(R.string.title_log_acquired);
//                mDefaultNextFragment = new IngredientLogBrandFragment();
            }

            // for each string in array update that log's instant began
            for (String ingredientLogIdString: mLogIdToAddStringArray) {
                // now get the food log associated with each UUID
                mIngredientLogArray.add(
                        mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                                UUID.fromString(ingredientLogIdString)
                        ));
            }

        } else if ( mSettingSymptomLog ){
            mSymptomLogArray = new ArrayList<>();
            // now get the info associated with that UUID
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(mCurrentLogIdToAddString));

            Log.d(TAG, mLogIdToAddStringArray.toString());
            // for each string in array update that log's instant began
            for (String logIdString: mLogIdToAddStringArray) {
                // now get the log associated with each UUID
                Log.d(TAG, logIdString);
                mSymptomLogArray.add(
                        mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(logIdString))
                );
            }

            mIngredientLog = null;

            // if begin hasn't been set yet, then that's what we're changing
            if ( mWhatToChange == Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN ){
                mTitleString = getResources().getString(R.string.title_log_begin);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED) {
                mTitleString = getResources().getString(R.string.title_log_change);
            }

        }

        Log.d(TAG, mWhatToChange.toString());

        Log.d(TAG, String.valueOf(Objects.isNull(mIngredientLog)));
        Log.d(TAG, mSymptomLog.toString());
        // get the date time of current log so we can set the default view with it
        mDateTime = Util.getDateTimeFromChange(mWhatToChange, mIngredientLog, mSymptomLog);

        // set the title of the view to be what we're changing
        titleTextView.setText(mTitleString);



        // if we're only supposed to set one or the other
//            if ( !!TextUtils.isEmpty(mBundle.getString(Util.ARGUMENT_ONLY_SET)) ) {
//                mOnlySetDateOrTime = mBundle.getString(Util.ARGUMENT_ONLY_SET);
//                if ( mOnlySetDateOrTime == Util.ARGUMENT_ONLY_SET_DATE ) {
//                    mEditTextTime.setVisibility(View.GONE);
//                } else {
//                    mEditTextDate.setVisibility(View.GONE);
//                }
//            }

    }


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
        mBundleNext = Util.setBundleLogToNextInstant(mBundle);

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
//                            .replace(Util.fragmentContainerViewEditIngredientLog,
//                            logPartOfDayFragment)
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




    private void setLogInstants(String whatToChange,
                                ArrayList<String> logIdToAddStringArray){

        // we have the date time to set now
        if (mSettingSymptomLog){

            // get the date and time user picked and put them in a bundle and set log with them
            mBundleNext = Util.setSymptomLogInstants(whatToChange, mSymptomLogArray,
                    mSymptomLogViewModel,
                    Util.setBundleGivenIntegersOfDateTime(mMinute, mHour, mDay, mMonth, mYear), mBundleNext);


            // go back to activity to go to the next fragment
            // with our picked date
            if (TextUtils.equals(mWhatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED)) {
                // done so lets go home
                // TODO moke action argument into create view with if null
                Util.goToListSymptomLogActivity(null, thisActivity,
                        mLogIdToAddStringArray.toString(), mBundle.getString(Util.ARGUMENT_ACTION));
            } else {
                // begin instant was set now go back to choices to select for changed instant
                // set the ingredient log id we're changing in to be accessible in next fragment
                // and go to the next info needed
                //TODO this probably will break when needing multiple instants
                Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                        mFragmentContainer,
                        mDefaultNextFragment, mBundleNext);
            }

        }
        else if (mSettingIngredientLog) {
            // TODO add check for if acquired and cooked are same as an existing food log
            // TODO then use whatToChange or a new argument
            //  that's for set them all the same as another food log

            // get the date and time user picked and set the ingredient logs to that
            mBundleNext = Util.setIngredientLogInstants(mWhatToChange, mIngredientLogArray,
                    mIngredientLogViewModel, Util.setBundleGivenIntegersOfDateTime(mMinute,
                            mHour, mDay, mMonth, mYear), mBundleNext
            );

            // go back to activity or go to the next fragment
            // with our picked date
            // set the ingredient log id we're changing in to be accessible in next fragment
            // and go to the next info needed
            //TODO this probably will break when needing multiple instants
            Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                    mFragmentContainer,
                    mDefaultNextFragment, mBundleNext);

        }
    }

}//end fragment
