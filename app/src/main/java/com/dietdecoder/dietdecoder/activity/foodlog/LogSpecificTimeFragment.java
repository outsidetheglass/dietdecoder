package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;

import java.time.LocalDateTime;
import java.util.Calendar;

public class LogSpecificTimeFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    Integer mHour, mMinute, mDay, mMonth, mYear;

    private static final String mJustNowString = "just_now";
    private static final String mYesterdayString = "yesterday";

    LocalDateTime mNowDateTime, mEarlierTodayDateTime, mYesterdayDateTime, mDayBeforeYesterdayDateTime;
    Button mButtonSave;
    TimePicker mTimePicker;

    public LogSpecificTimeFragment() {
        super(R.layout.fragment_log_specific_time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_specific_time, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set the listeners on the buttons
        // to run onClick method when they are clicked
        mButtonSave = (Button) view.findViewById(R.id.button_log_specific_time_save);
        mButtonSave.setOnClickListener(this);
        mTimePicker =
                (TimePicker) view.findViewById(R.id.timepicker_log_specific_time);


    }//end onViewCreated


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_specific_time_save:
                Toast.makeText(getContext(), getResources().getString(R.string.saving),
                        Toast.LENGTH_SHORT).show();

                // change this to get date time if it can
                LocalDateTime mTimePickerDateTime = null;
                startActivity(Util.intentWithDateTimeButton(getActivity(), mTimePickerDateTime,
                        Util.ARGUMENT_SPECIFIC_TIME));
                break;

            default:
                break;
        }//end switch case

    }//end onClick


    // here's the timepicker fragment
    // set it in an activity with:
    // start the time picker fragment display
    // when changed, in TimeSet in the fragment, save the new time in
//    DialogFragment timePickerFragment = new com.dietdecoder.dietdecoder.activity.foodlog.TimePickerFragment();
//        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    //TODO here, reset the views based on the new times in the saved database
    // or start a new activity for each value,
    // or go back to the main view each time, then
    // back to this activity
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public final String TAG = "TAG: " + getClass().getSimpleName();

        Bundle myBundle;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
            // Do something with the time chosen by the user
            this.myBundle = new Bundle();
            this.myBundle.putInt("mMinute", minuteOfHour);
            this.myBundle.putInt("mHour", hourOfDay);
            Log.d(TAG, String.valueOf(hourOfDay));
            // TODO this is where to save the values in to the database
            // move name of ingredient first so we can then ask about time and then it could save
            // from here

        }

    }

}//end fragment
