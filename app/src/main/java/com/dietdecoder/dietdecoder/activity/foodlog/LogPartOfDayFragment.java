package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class LogPartOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String foodLogIdString, mFragmentGoTo;

    //TODO set this to be preferences a different time
    Integer mEarlyHour = 8;
    Integer mMiddayHour = 12;
    Integer mAfternoonHour = 15;
    Integer mEveningHour = 19;
    Integer mMidnightHour = 23;

    LocalDateTime mFoodLogConsumedDateTime, mDateTime;
    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight, mButtonSpecificTime;
    Bundle mBundle;

    Instant mInstantConsumed;
    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;

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

        mBundle = getArguments();
        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        foodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
        // now get the food log associated with that UUID
        mFoodLog =
                mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(foodLogIdString));


        mButtonEarly = (Button) view.findViewById(R.id.button_log_part_of_day_early);
        mButtonEarly.setOnClickListener(this);

        mButtonMidday = (Button) view.findViewById(R.id.button_log_part_of_day_midday);
        mButtonMidday.setOnClickListener(this);

        mButtonAfternoon =
                (Button) view.findViewById(R.id.button_log_part_of_day_afternoon);
        mButtonAfternoon.setOnClickListener(this);

        mButtonEvening = (Button) view.findViewById(R.id.button_log_part_of_day_evening);
        mButtonEvening.setOnClickListener(this);

        mButtonMidnight =
                (Button) view.findViewById(R.id.button_log_part_of_day_midnight);
        mButtonMidnight.setOnClickListener(this);

        mButtonSpecificTime =
                (Button) view.findViewById(R.id.button_log_part_of_day_more_specific_time);
        mButtonSpecificTime.setOnClickListener(this);

    }//end onViewCreated


    @Override
    public void onClick(View view) {
        // set the date time to the one already set, ready to get changed it up
        mFoodLogConsumedDateTime = Util.localDateTimeFromInstant(mFoodLog.getMDateTimeConsumed());

        // most common next fragment is brand, so that's our default
        mFragmentGoTo = Util.ARGUMENT_GO_TO_BRAND;

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_part_of_day_early:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_early),
                        Toast.LENGTH_SHORT).show();

                // set the early date time to current time but with early hour and minute to 0
                mDateTime = mFoodLogConsumedDateTime.withHour(mEarlyHour).withMinute(0);

                break;

            case R.id.button_log_part_of_day_midday:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midday),
                        Toast.LENGTH_SHORT).show();
                mDateTime = mFoodLogConsumedDateTime.withHour(mMiddayHour).withMinute(0);

                break;
            case R.id.button_log_part_of_day_afternoon:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_afternoon),
                        Toast.LENGTH_SHORT).show();
                mDateTime = mFoodLogConsumedDateTime.withHour(mAfternoonHour).withMinute(0);

                break;

            case R.id.button_log_part_of_day_evening:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_evening),
                        Toast.LENGTH_SHORT).show();
                mDateTime = mFoodLogConsumedDateTime.withHour(mEveningHour).withMinute(0);

                break;

            case R.id.button_log_part_of_day_midnight:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midnight),
                        Toast.LENGTH_SHORT).show();
                mDateTime = mFoodLogConsumedDateTime.withHour(mMidnightHour).withMinute(0);
                break;

            case R.id.button_log_part_of_day_more_specific_time:
                // throw a toast with more specific time pulled from strings
                Toast.makeText(getContext(), getResources().getString(R.string.toast_more_specific_time),
                        Toast.LENGTH_SHORT).show();
                // only button that was different,
                // let's set the fragment to go to specific time
                mFragmentGoTo = Util.ARGUMENT_GO_TO_SPECIFIC_TIME;
                break;

            default:
                break;
        }//end switch case

        //then set the values from the food log
        mInstantConsumed = Util.instantFromLocalDateTime(mDateTime);
        mFoodLog.setMDateTimeConsumed(mInstantConsumed);
        mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

        // go back to parent activity and then to name with the early date time
        startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), foodLogIdString,
                mFragmentGoTo,
                Util.ARGUMENT_FROM_PART_OF_DAY));
    }//end onClick


}//end fragment
