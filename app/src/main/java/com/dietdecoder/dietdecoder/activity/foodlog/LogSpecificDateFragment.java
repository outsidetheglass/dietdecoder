package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

public class LogSpecificDateFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick: mDatePickerDateTime = " + mDatePickerDateTime.getHour());

    Integer mDay, mMonth, mYear;
    String mFoodLogIdString, mPreviousFragment, mWhatToChange;

    LocalDateTime mDatePickerDateTime;
    LocalDateTime mDateTime, mDateTimeSet;
    Button mDateButtonSave;
    DatePicker mDatePicker;
    EditText mEditTextTime, mEditTextDate;

    Bundle mBundle;
    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;

    Instant mInstant, mInstantSet;
    static final int TIME_DIALOG_ID = 1111;
    public Button btnClick;
    TextView STtime;
    int hour,minute;
    public LogSpecificDateFragment() {
        super(R.layout.fragment_log_specific_date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_specific_date, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        STtime = (TextView) getActivity().findViewById(R.id.textView2);
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);

//        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
//
//        // no point in checking if it's empty, this entire fragment is useless without
//        mBundle = getArguments();
//        mFoodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
//        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
//        // now get the food log associated with that UUID
//        mFoodLog =
//                mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mFoodLogIdString));

        //mEditTextTime = view.findViewById(R.id.edittext_log_specfic_date_pick_time);
        // set the listeners on the buttons
        // to run onClick method when they are clicked
//        mDateButtonSave = view.findViewById(R.id.button_log_specific_date_save);
//
//        mDateButtonSave.setOnClickListener(this);
//        mDatePicker = (DatePicker) view.findViewById(R.id.datepicker_log_specific_date);

        Button mDialogButton = view.findViewById(R.id.button_log_specific_date_dialog);
        mDialogButton.setOnClickListener(this);


    }//end onViewCreated


    public Dialog createDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(getActivity(), timePickerListener, hour, minute, false);

        }
        return null;
    }



    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);

        }

    };



    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        STtime.setText(aTime);
    }

//
    @Override
    public void onClick(View view) {
//
//        //int id = view.getId();
        switch (view.getId()) {
            // which button was clicked

            case R.id.button_log_specific_date_dialog:
                createDialog(TIME_DIALOG_ID).show();

                break;
            case R.id.button_log_specific_date_save:
                Toast.makeText(getContext(), getResources().getString(R.string.saving),
                        Toast.LENGTH_SHORT).show();

                // get the current date of the picker
                mDay = mDatePicker.getDayOfMonth();
                // month not zero indexed
                mMonth = mDatePicker.getMonth()+1;
                mYear = mDatePicker.getYear();

                // set the instant we're setting now to be based on consumed or acquired or
                // cooked date times
                // based on which fragment's button we're coming from
                mDateTime = Util.getDateTimeConsumedAcquiredCooked(mWhatToChange,
                        mFoodLog);

                // get the instant of the food log consumed
                // which here should be the instant the name was saved
                // or a specific time if they chose that first and then came here
                // set the early date time to current time but with early hour and minute to 0
                mDateTimeSet = mDateTime.withDayOfMonth(mDay).withMonth(mMonth).withYear(mYear);

                // then set the values from the food log
                mFoodLog = Util.setFoodLogConsumedAcquiredCooked(mWhatToChange,
                        mFoodLog, mDateTimeSet);

                // update our food log with the new date consumed
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                // go back to activity to go to the next fragment
                // with our picked date
                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), mFoodLogIdString,
                        Util.ARGUMENT_GO_TO_PART_OF_DAY,
                        Util.ARGUMENT_FROM_SPECIFIC_DATE));
                break;

            default:
                break;
        }//end switch case

    }//end onClick
//
//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getContext(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//        }
//    }
//


}//end fragment
