package com.dietdecoder.dietdecoder.activity.foodlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;

import java.time.LocalDateTime;

public class LogDateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    Integer mHour, mMinute, mDay, mMonth, mYear;

    LocalDateTime mNowDateTime, mEarlierTodayDateTime, mYesterdayDateTime, mDayBeforeYesterdayDateTime;
    Button mButtonJustNow, mButtonEarlierToday, mButtonAnotherTime, mButtonYesterday;

    // TODO set this to be a preference
    Integer hoursEarlierInt = 4;

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
        // set the listeners on the buttons
        // to run onClick method when they are clicked
        mButtonJustNow = (Button) view.findViewById(R.id.button_log_date_time_choices_just_now);
        mButtonJustNow.setOnClickListener(this);

        mButtonYesterday = (Button) view.findViewById(R.id.button_log_date_time_choices_yesterday);
        mButtonYesterday.setOnClickListener(this);

        mButtonAnotherTime = (Button) view.findViewById(R.id.button_log_date_time_choices_another_time);
        mButtonAnotherTime.setOnClickListener(this);

        mButtonEarlierToday = (Button) view.findViewById(R.id.button_log_date_time_choices_earlier_today);
        mButtonEarlierToday.setOnClickListener(this);

    }//end onViewCreated


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_date_time_choices_just_now:
                Toast.makeText(getContext(), "Just now", Toast.LENGTH_SHORT).show();

                // they chose just now, so set our time to now
                // set the arguments for which button and times
                // to put in the intent to pass the info to the main activity
                mNowDateTime = LocalDateTime.now();

                // start the activity with that time and the string saying which button was pressed
                startActivity(Util.intentWithDateTimeButton(getActivity(), mNowDateTime,
                        Util.ARGUMENT_JUST_NOW));
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), "Earlier today", Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mEarlierTodayDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);
                startActivity(Util.intentWithDateTimeButton(getActivity(), mEarlierTodayDateTime,
                        Util.ARGUMENT_EARLIER_TODAY));
                break;

            case R.id.button_log_date_time_choices_yesterday:
                Toast.makeText(getContext(), "Yesterday... what time?", Toast.LENGTH_SHORT).show();
                mYesterdayDateTime = LocalDateTime.now().minusDays(1);
                startActivity(Util.intentWithDateTimeButton(getActivity(), mYesterdayDateTime,
                        Util.ARGUMENT_YESTERDAY));
                break;

            case R.id.button_log_date_time_choices_another_time:
                Toast.makeText(getContext(), "Another time, so let's select when...",
                        Toast.LENGTH_SHORT).show();
                mDayBeforeYesterdayDateTime = LocalDateTime.now().minusDays(2);
                startActivity(Util.intentWithDateTimeButton(getActivity(),
                        mDayBeforeYesterdayDateTime, Util.ARGUMENT_ANOTHER_TIME ));
                break;

            default:
                break;
        }//end switch case

    }//end onClick

}//end fragment
