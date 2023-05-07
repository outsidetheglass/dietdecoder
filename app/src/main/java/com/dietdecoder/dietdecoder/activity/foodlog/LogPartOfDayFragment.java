package com.dietdecoder.dietdecoder.activity.foodlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.symptomlog.ListSymptomLogActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class LogPartOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    String mFragmentFrom = Util.ARGUMENT_FROM_PART_OF_DAY;

    Integer mHourToSet;
    String foodLogIdString, mFragmentGoTo, mWhatToChange;

    //TODO set this to be preferences a different time
    Integer mEarlyHour = 8;
    Integer mMiddayHour = 12;
    Integer mAfternoonHour = 15;
    Integer mEveningHour = 19;
    Integer mMidnightHour = 23;

    LocalDateTime mFoodLogDateTime, mDateTime;
    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight, mButtonSpecificTime;
    Bundle mBundle;
    Instant mInstantToChange;

    Instant mInstantConsumed;
    FoodLogViewModel mFoodLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    FoodLog mFoodLog;
    SymptomLog mSymptomLog;
    Boolean settingFoodLog = Boolean.FALSE;
    Boolean settingSymptomLog = Boolean.FALSE;
    Boolean isSymptomLogBeginInstantSet = Boolean.FALSE;
    Boolean isSymptomLogChangedInstantSet = Boolean.FALSE;
    String mLogIdString;
    TextView questionTextView;
    int mFragmentContainer;

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
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
        questionTextView = view.findViewById(R.id.textview_question_log_part_of_day_specific_time_range);

        // find out if we have a food log or symptom log to set the date time of
        // if food log ID was given then set that
        if ( mBundle.containsKey(Util.ARGUMENT_FOOD_LOG_ID) ) {
            settingFoodLog = Boolean.TRUE;
            mLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
            // now get the food log associated with that UUID
            mFoodLog =
                    mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(mLogIdString));
            // when we have to replace the fragment container, replace the right one
            mFragmentContainer = Util.fragmentContainerViewAddSymptomLog;

        } else if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID) ) {
            settingSymptomLog = Boolean.TRUE;
            mLogIdString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID);
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromId(UUID.fromString(mLogIdString));

            mFragmentContainer = Util.fragmentContainerViewAddFoodLog;
            if ( !isSymptomLogBeginInstantSet ){
                // if begin hasn't been set yet, then that's what we're changing
                mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN);
            } else {
                // we have changed begin so now set changed/ended instead
                mBundle.remove(Util.ARGUMENT_CHANGE);
                mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
            }
        }

        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);

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
        // set a boolean so we can know to set the part of day
        // doing this because specific time starts a different fragment
        Boolean doSetTime = Boolean.FALSE;

        // set the date time to the one already set, ready to get changed it up
        mFoodLogDateTime = Util.getDateTimeConsumedAcquiredCooked(mWhatToChange, mFoodLog);

        // most common next fragment is brand, so that's our default
        mFragmentGoTo = Util.ARGUMENT_GO_TO_BRAND;

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_part_of_day_early:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_early),
                        Toast.LENGTH_SHORT).show();

                // set the early date time to current time but with early hour and minute to 0
                // TODO use ARGUMENT_CHANGE to get Util function to set that
                mHourToSet = mEarlyHour;
                doSetTime = Boolean.TRUE;

                break;

            case R.id.button_log_part_of_day_midday:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midday),
                        Toast.LENGTH_SHORT).show();
                mHourToSet = mMiddayHour;
                doSetTime = Boolean.TRUE;

                break;
            case R.id.button_log_part_of_day_afternoon:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_afternoon),
                        Toast.LENGTH_SHORT).show();
                mHourToSet = mAfternoonHour;
                doSetTime = Boolean.TRUE;

                break;

            case R.id.button_log_part_of_day_evening:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_evening),
                        Toast.LENGTH_SHORT).show();
                mHourToSet = mEveningHour;
                doSetTime = Boolean.TRUE;

                break;

            case R.id.button_log_part_of_day_midnight:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midnight),
                        Toast.LENGTH_SHORT).show();
                mHourToSet = mMidnightHour;
                doSetTime = Boolean.TRUE;

                break;

            case R.id.button_log_part_of_day_more_specific_time:
                // throw a toast with more specific time pulled from strings
                Toast.makeText(getContext(), getResources().getString(R.string.toast_more_specific_time),
                        Toast.LENGTH_SHORT).show();
                // only button that was different,
                // let's set the fragment to go to specific time
                goToNextFragment(new LogSpecificDateTimeFragment());

                break;

            default:
                break;
        }//end switch case


        if ( doSetTime ) {
            // TODO use ARGUMENT_CHANGE to get Util function to set that

            if (settingSymptomLog){
                if (mWhatToChange == Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN) {
                    mInstantToChange = mSymptomLog.getInstantBegan();
                    mDateTime =
                            Util.localDateTimeFromInstant(mInstantToChange)
                                    .withHour(mHourToSet).withMinute(0);
                    mSymptomLog.setInstantBegan(Util.instantFromLocalDateTime(mDateTime));
                    //then set the values from the symptom log
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(mSymptomLog);
                    isSymptomLogBeginInstantSet = Boolean.TRUE;
                    // TODO add different date option for end time
                    //TODO add all day as option
                    questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_intensity_log_changed_time));
                } else {
                    mInstantToChange = mSymptomLog.getInstantChanged();
                    mDateTime =
                            Util.localDateTimeFromInstant(mInstantToChange)
                                    .withHour(mHourToSet).withMinute(0);
                    mSymptomLog.setInstantChanged(Util.instantFromLocalDateTime(mDateTime));
                    //then set the values from the symptom log
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(mSymptomLog);
                    goToListSymptomLog();
                }
            }
            else if (settingFoodLog) {
                mDateTime = mFoodLogDateTime.withHour(mHourToSet).withMinute(0);
                //then set the values from the food log
                mFoodLog = Util.setFoodLogConsumedAcquiredCooked(mWhatToChange, mFoodLog, mDateTime);
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);
                goToNextFragment(null);
            }

        }
    }//end onClick


    private void goToListSymptomLog(){
        startActivity(new Intent(getContext(), ListSymptomLogActivity.class));
    }

    private void goToNextFragment(Fragment nextFragment){
        // and which fragment to go to next
        if ( Objects.isNull(nextFragment)) {
            // so just start asking details on what was consumed
            nextFragment = new NewFoodLogBrandFragment();
        }
        // put which we're changing into the bundle
        nextFragment.setArguments(mBundle);
        // actually go to the next place now
        getParentFragmentManager().beginTransaction()
                .replace(mFragmentContainer, nextFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

}//end fragment
