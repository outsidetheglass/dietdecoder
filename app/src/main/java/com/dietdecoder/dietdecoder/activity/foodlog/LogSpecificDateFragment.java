package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class LogSpecificDateFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick: mDatePickerDateTime = " + mDatePickerDateTime.getHour());

    Integer mDay, mMonth, mYear;
    String foodLogIdString;

    LocalDateTime mDatePickerDateTime;
    Button mDateButtonSave;
    DatePicker mDatePicker;

    Bundle mBundle;
    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;

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

        mBundle = getArguments();
        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        foodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
        // now get the food log associated with that UUID
        mFoodLog =
                mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(foodLogIdString));

        // set the listeners on the buttons
        // to run onClick method when they are clicked
        mDateButtonSave = view.findViewById(R.id.button_log_specific_date_save);

        mDateButtonSave.setOnClickListener(this);
        mDatePicker = (DatePicker) view.findViewById(R.id.datepicker_log_specific_date);


    }//end onViewCreated


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_specific_date_save:
                Toast.makeText(getContext(), getResources().getString(R.string.saving),
                        Toast.LENGTH_SHORT).show();

                // get the current date of the picker
                mDay = mDatePicker.getDayOfMonth();
                mMonth = mDatePicker.getMonth();
                mYear = mDatePicker.getYear();

                // get the instant of the food log consumed
                // which here should be the instant the name was saved
                // or a specific time if they chose that first and then came here
                mDatePickerDateTime =
                        Util.localDateTimeFromInstant( mFoodLog.getMDateTimeConsumed() );

                // and now set it with the date chosen
                mDatePickerDateTime =
                        mDatePickerDateTime.withDayOfMonth(mDay).withMonth(mMonth).withYear(mYear);
                // TODO put how to set this in the time fragments
                // get that turned into an instant now
                Instant mDatePickerInstant = Util.instantFromLocalDateTime(mDatePickerDateTime);
                // set it in the food log
                mFoodLog.setMDateTimeConsumed(mDatePickerInstant);
                // update our food log with the new date consumed
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                // go back to activity to go to the next fragment
                // with our picked date
                // TODO move the above I just made into Util, or Model
                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), foodLogIdString,
                        Util.ARGUMENT_GO_TO_PART_OF_DAY,
                        Util.ARGUMENT_FROM_SPECIFIC_DATE));
                break;

            default:
                break;
        }//end switch case

    }//end onClick

}//end fragment
