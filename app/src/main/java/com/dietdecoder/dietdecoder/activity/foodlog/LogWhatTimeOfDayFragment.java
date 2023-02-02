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

public class LogWhatTimeOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    Integer mHour, mMinute, mDay, mMonth, mYear;

    LocalDateTime mNowDateTime, mEarlierTodayDateTime, mYesterdayDateTime, mDayBeforeYesterdayDateTime;
    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight,
            mButtonSpecificTime;



    public LogWhatTimeOfDayFragment() {
        super(R.layout.fragment_log_what_time_of_day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_what_time_of_day, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set the listeners on the buttons
        // to run onClick method when they are clicked

        mButtonEarly = (Button) view.findViewById(R.id.button_log_what_time_of_day_early);
        mButtonEarly.setOnClickListener(this);

        mButtonMidday = (Button) view.findViewById(R.id.button_log_what_time_of_day_midday);
        mButtonMidday.setOnClickListener(this);

        mButtonAfternoon = (Button) view.findViewById(R.id.button_log_what_time_of_day_afternoon);
        mButtonAfternoon.setOnClickListener(this);

        mButtonEvening = (Button) view.findViewById(R.id.button_log_what_time_of_day_evening);
        mButtonEvening.setOnClickListener(this);

        mButtonMidnight =
                (Button) view.findViewById(R.id.button_log_what_time_of_day_midnight);
        mButtonMidnight.setOnClickListener(this);

        mButtonSpecificTime =
                (Button) view.findViewById(R.id.button_log_what_time_of_day_more_specific_time);
        mButtonSpecificTime.setOnClickListener(this);

    }//end onViewCreated


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_what_time_of_day_early:
                Toast.makeText(getContext(), "button_log_what_time_of_day_early", Toast.LENGTH_SHORT).show();
                //startActivity(Util.intentWithDateTimeButton(getActivity(), mNowDateTime,mButtonEarly);
                break;

            case R.id.button_log_what_time_of_day_midday:
                Toast.makeText(getContext(), "button_log_what_time_of_day_midday",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_log_what_time_of_day_afternoon:
                Toast.makeText(getContext(), "button_log_what_time_of_day_afternoon",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.button_log_what_time_of_day_evening:
                Toast.makeText(getContext(), "button_log_what_time_of_day_evening",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_log_what_time_of_day_midnight:
                Toast.makeText(getContext(), "midnight", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_log_what_time_of_day_more_specific_time:
                // throw a toast with more specific time pulled from strings
                Toast.makeText(getContext(), getResources().getString(R.string.toast_more_specific_time),
                        Toast.LENGTH_SHORT).show();
                startActivity(Util.intentWithDateTimeButton(getActivity(), mNowDateTime,
                        Util.ARGUMENT_SPECIFIC_TIME));

                break;

            default:
                break;
        }//end switch case

    }//end onClick


}//end fragment
