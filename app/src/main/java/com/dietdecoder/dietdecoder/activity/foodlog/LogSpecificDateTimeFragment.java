package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class LogSpecificDateTimeFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick: mDatePickerDateTime = " + mDatePickerDateTime.getHour());

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mFoodLogIdString, mOnlySetDateOrTime, mWhatToChange, mWhereFrom;

    LocalDateTime mDatePickerDateTime;
    LocalDateTime mDateTime, mDateTimeSet;
    Button mDateButtonSave;
    DatePicker mDatePicker;
    EditText mEditTextTime, mEditTextDate;

    Bundle mBundle, mBundleNext;
    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;

    Instant mInstant, mInstantSet;

    Boolean isFromEditFoodLog;

    // IDs for which dialog to open
    static final int TIME_DIALOG_ID = 1111;
    static final int DATE_DIALOG_ID = 1112;
    TextView mTextViewTimePicker;
    int hour, minute;

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
            mBundle = getArguments();
            mFoodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
            mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
            mWhereFrom = mBundle.getString(Util.ARGUMENT_ACTIVITY_FROM);


            // if we're only supposed to set one or the other
//            if ( !!TextUtils.isEmpty(mBundle.getString(Util.ARGUMENT_ONLY_SET)) ) {
//                mOnlySetDateOrTime = mBundle.getString(Util.ARGUMENT_ONLY_SET);
//                if ( mOnlySetDateOrTime == Util.ARGUMENT_ONLY_SET_DATE ) {
//                    mEditTextTime.setVisibility(View.GONE);
//                } else {
//                    mEditTextDate.setVisibility(View.GONE);
//                }
//            }

            // now get the info associated with that UUID
            mFoodLog =
                    mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mFoodLogIdString));
            mDateTime = Util.getDateTimeConsumedAcquiredCooked(mWhatToChange, mFoodLog);
            // Current date and time
            mHour = mDateTime.getHour();
            mMinute = mDateTime.getMinute();
            mDay = mDateTime.getDayOfMonth();
            mMonth = mDateTime.getMonthValue()-1;
            mYear = mDateTime.getYear();
        } else {
            // no point in being here without it
            Toast.makeText(getContext(), "Can't set a date or time for a nonexistent log, going " +
                            "back" +
                            "...",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getActivity(), FoodLogActivity.class));
            // get current calendar if we don't have a date time
//            final Calendar c = Calendar.getInstance();
//            // Current date and time
//            mHour = c.get(Calendar.HOUR_OF_DAY);
//            mMinute = c.get(Calendar.MINUTE);
//            mDay = c.get(Calendar.DAY_OF_MONTH);
//            mMonth = c.get(Calendar.MONTH);
//            mYear = c.get(Calendar.YEAR);
        }

        // set time and date
        mEditTextTime.setText(Util.setTimeString(mHour, mMinute));
        mEditTextDate.setText(Util.setDateString(mDay, mMonth, mYear));

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

                // get the instant of the food log consumed
                // which here should be the instant the name was saved
                // or a specific time if they chose that first and then came here
                // set the early date time to current time but with early hour and minute to 0
                mDateTimeSet =
                        mDateTime.withDayOfMonth(mDay).withMonth(mMonth+1).withYear(mYear)
                                .withMinute(mMinute).withHour(mHour);

                // then set the values from the food log
                mFoodLog = Util.setFoodLogConsumedAcquiredCooked(mWhatToChange,
                        mFoodLog, mDateTimeSet);

                // update our food log with the new date consumed
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                // go back to activity to go to the next fragment
                // with our picked date

                // if next fragment has been set use that
                if ( !TextUtils.isEmpty(mOnlySetDateOrTime) ) {
                    // put which we're changing into the bundle
                    LogPartOfDayFragment logPartOfDayFragment = new LogPartOfDayFragment();
                    logPartOfDayFragment.setArguments(mBundleNext);
                    // actually go to the next place now
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(Util.fragmentContainerViewEditFoodLog, logPartOfDayFragment);
                    ft.commit();
                }
                // else it means go back to main activity or edit
                else {
                    // if we're from edit we have more to do
                    if (Objects.equals(
                                    mWhereFrom, Util.ARGUMENT_ACTIVITY_FROM_EDIT_FOOD_LOG)
                    ) {
                        Intent intent = new Intent(getActivity(), EditFoodLogActivity.class);
                        intent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, mFoodLogIdString);
                        intent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
                                Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT);
                        startActivity(intent);

                    } else {
                        // not from edit so we're done here
                        // go back to food log activity
                        startActivity(new Intent(getActivity(), FoodLogActivity.class));
                    }

                }

                break;

            default:
                break;
        }//end switch case

    }//end onClick


}//end fragment