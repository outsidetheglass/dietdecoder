package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class LogSpecificTimeFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick: mTimePickerDateTime = " + mTimePickerDateTime.getHour());

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mFoodLogIdString, mPreviousFragment, mWhatToChange, mGoTo, mActivityFrom;

    LocalDateTime mTimePickerDateTime;
    Button mButtonSave;
    TimePicker mTimePicker;

    Bundle mBundle;
    Instant mInstantConsumed;
    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;

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

        mBundle = getArguments();

        // get previous fragment so we know which time to set in the food log
        mPreviousFragment = mBundle.getString(Util.ARGUMENT_FRAGMENT_FROM);
        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        mFoodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
        // now get the food log associated with that UUID
        mFoodLog =
                mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mFoodLogIdString));


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

                // get the current hour and minute of the time picker
                mHour = mTimePicker.getHour();
                mMinute = mTimePicker.getMinute();

                // start an instance of LocalDateTime based on which value we're changing
                // get and reset the instant we're setting now to be based on consumed or
                // acquired or cooked date times
                // based on which fragment's button we're coming from
                mTimePickerDateTime = Util.getDateTimeConsumedAcquiredCooked(mWhatToChange,
                        mFoodLog);
                // and set it with the hour and minute chosen
                mTimePickerDateTime = mTimePickerDateTime.withHour(mHour).withMinute(mMinute);
                mFoodLog = Util.setFoodLogConsumedAcquiredCooked(mWhatToChange,
                        mFoodLog, mTimePickerDateTime);

                // update our food log with the new date consumed
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                // go back to activity to go to the next fragment
                // with our picked time
                // get which activity we came from and work from that
                //TODO put this in other relevant fragments
                mActivityFrom =  mBundle.getString(Util.ARGUMENT_ACTIVITY_FROM);
                if ( mActivityFrom == Util.ARGUMENT_ACTIVITY_FROM_EDIT_FOOD_LOG) {
                    mGoTo = Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT;
                } else if ( mActivityFrom == Util.ARGUMENT_ACTIVITY_FROM_NEW_FOOD_LOG) {
                    mGoTo = Util.ARGUMENT_GO_TO_BRAND;
                }
                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), mFoodLogIdString,
                        mGoTo,
                        Util.ARGUMENT_FROM_SPECIFIC_TIME));

                break;

            default:
                break;
        }//end switch case

    }//end onClick

}//end fragment
