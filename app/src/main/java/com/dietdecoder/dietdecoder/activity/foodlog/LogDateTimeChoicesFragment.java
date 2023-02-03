package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
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

public class LogDateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    Integer mHour, mMinute, mDay, mMonth, mYear;

    LocalDateTime mNowDateTime, mEarlierTodayDateTime, mYesterdayDateTime, mDayBeforeYesterdayDateTime;
    Button mButtonJustNow, mButtonEarlierToday, mButtonAnotherDate, mButtonYesterday;

    // TODO set this to be a preference
    Integer hoursEarlierInt = 4;
    String foodLogIdString;

    Bundle mBundle;
    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;

    Instant mInstantConsumed;

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
        foodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
        // now get the food log associated with that UUID
        mFoodLog =
                mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(foodLogIdString));

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

                // they chose just now, so set our time to now
                // we came to this activity from setting the name
                // if we've made it here, then the food log has already been made
                // which means it's been set to the default, which was the moment they clicked
                // save on the name

                // we don't need to ask anything else about time or day
                // so just start asking details on what was consumed
                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), foodLogIdString,
                        Util.ARGUMENT_GO_TO_BRAND, Util.ARGUMENT_FROM_DATE_TIME_CHOICES));
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_earlier_today),
                        Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mEarlierTodayDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);

                //then set the values from the food log
                mInstantConsumed = Util.instantFromLocalDateTime(mEarlierTodayDateTime);
                mFoodLog.setMDateTimeConsumed(mInstantConsumed);
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), foodLogIdString,
                        Util.ARGUMENT_GO_TO_BRAND,
                        Util.ARGUMENT_FROM_DATE_TIME_CHOICES));
                break;

            case R.id.button_log_date_time_choices_yesterday:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_yesterday), Toast.LENGTH_SHORT).show();
                mYesterdayDateTime = LocalDateTime.now().minusDays(1);

                //then set the values from the food log
                mInstantConsumed = Util.instantFromLocalDateTime(mYesterdayDateTime);
                mFoodLog.setMDateTimeConsumed(mInstantConsumed);
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), foodLogIdString,
                        Util.ARGUMENT_GO_TO_PART_OF_DAY,
                        Util.ARGUMENT_FROM_DATE_TIME_CHOICES));
                break;

            case R.id.button_log_date_time_choices_another_date:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_another_date),
                        Toast.LENGTH_SHORT).show();

                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), foodLogIdString,
                        Util.ARGUMENT_GO_TO_SPECIFIC_DATE,
                        Util.ARGUMENT_FROM_DATE_TIME_CHOICES));
                break;

            default:
                break;
        }//end switch case

    }//end onClick

}//end fragment
