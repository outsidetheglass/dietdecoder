package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import org.checkerframework.checker.units.qual.A;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class LogPartOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;
    String mFragmentFrom = Util.ARGUMENT_FROM_PART_OF_DAY;

    int mFragmentContainer;

    //TODO set this to be preferences a different time
    Integer mEarlyHour = 8;
    // personally 8am and 11pm work for me, but preferences should be set in case they want 1am
    // or 9pm or something else
    Integer mEarliestHour = mEarlyHour;
    Integer mMiddayHour = 12;
    Integer mAfternoonHour = 15;
    Integer mEveningHour = 19;
    Integer mMidnightHour = 23;
    Integer mLatestHour = mMidnightHour;
    ArrayList<Integer> mHourChoicesArrayList;


    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight, mButtonSpecificTime, mButtonAllDay;
    TextView questionTextView;

    Bundle mBundle, mButtonBundle;
    Fragment mDefaultNextFragment, mNextFragment;

    Instant mInstantToChange, mInstantConsumed;
    Boolean mSettingIngredientLog = Boolean.FALSE, mSettingSymptomLog = Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE, mSetBoth = Boolean.FALSE;
    UUID mCurrentLogId;
    ArrayList<String> mSymptomLogIdsToAddStringArray, mIngredientLogIdsToAddStringArray, mLogIdToAddStringArray;
    ArrayList<Button> mButtonsArrayList;
    LocalDateTime mIngredientLogDateTime, mDateTime;
    Integer mHourToSet;
    String mCurrentLogIdToAddString, ingredientLogIdString, mFragmentGoTo, mWhatToChange,
            mSymptomLogIdsToAddString
            , mIngredientLogIdsToAddString;

    IngredientLogViewModel mIngredientLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    IngredientLog mIngredientLog;
    SymptomLog mCurrentSymptomLog, mSymptomLog;
    ArrayList<IngredientLog> mIngredientLogArray;
    ArrayList<SymptomLog> mSymptomLogArray;

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

        thisActivity = getActivity();
        mBundle = getArguments();
        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
        mSymptomLogArray = new ArrayList<>();
        mIngredientLogArray = new ArrayList<>();

        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

        questionTextView = view.findViewById(R.id.textview_question_log_part_of_day_specific_time_range);
        mButtonEarly = view.findViewById(R.id.button_log_part_of_day_early);
        mButtonEarly.setOnClickListener(this);
        //TODO make util hold times of part of day and which button it is, then put this in
        // function to find what time and which button to set invisible
//        mButtonBundle = new Bundle();
        //mButtonBundle.putInt(Util.PART_OF_DAY_EARLY, R.id.button_log_part_of_day_early);
        mButtonMidday = view.findViewById(R.id.button_log_part_of_day_midday);
        mButtonMidday.setOnClickListener(this);
        mButtonAfternoon = view.findViewById(R.id.button_log_part_of_day_afternoon);
        mButtonAfternoon.setOnClickListener(this);
        mButtonEvening = view.findViewById(R.id.button_log_part_of_day_evening);
        mButtonEvening.setOnClickListener(this);
        mButtonMidnight = view.findViewById(R.id.button_log_part_of_day_midnight);
        mButtonMidnight.setOnClickListener(this);
        mButtonSpecificTime = view.findViewById(R.id.button_log_part_of_day_more_specific_time);
        mButtonSpecificTime.setOnClickListener(this);
        // all day button only for symptom log adding
        mButtonAllDay = view.findViewById(R.id.button_log_part_of_day_all_day);

        mHourChoicesArrayList = new ArrayList<Integer>();
        mButtonsArrayList = new ArrayList<>();

        mHourChoicesArrayList.add(mEarlyHour);
        mButtonsArrayList.add(mButtonEarly);

        mHourChoicesArrayList.add(mMiddayHour);
        mButtonsArrayList.add(mButtonMidday);

        mHourChoicesArrayList.add(mAfternoonHour);
        mButtonsArrayList.add(mButtonAfternoon);

        mHourChoicesArrayList.add(mEveningHour);
        mButtonsArrayList.add(mButtonEvening);

        mHourChoicesArrayList.add(mMidnightHour);
        mButtonsArrayList.add(mButtonMidnight);


        // find out if we have a food log or symptom log to set the date time of
        if ( mBundle.containsKey(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) ) {

            setDependentValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY),
                    Boolean.FALSE, Boolean.TRUE, Util.fragmentContainerViewAddIngredientLog, View.INVISIBLE);

            //TODO get button visibility to work for all all logs
//            setButtonVisibilityByInstantAlreadySet(mCurrentIngredientLog.getInstantAcquired(),
//                    mCurrentIngredientLog.getInstantConsumed());
        } else if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {

            // set the defaults for symptom log changes
            setDependentValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY),
                    Boolean.TRUE, Boolean.FALSE, Util.fragmentContainerViewAddSymptomLog, View.VISIBLE);

            mButtonAllDay.setOnClickListener(this);

            // first time through this we're setting begin so leave second one null
            setButtonVisibilityByInstantAlreadySet(Instant.now(), null);

        }


    }//end onViewCreated
    private void setDependentValues(String logIdToAddString, Boolean settingSymptomLog,
                                    Boolean settingIngredientLog, int fragmentContainer,
                                    int visibleOrNot){
        // one of these will be false and the other true
        mSettingSymptomLog = settingSymptomLog;
        mSettingIngredientLog = settingIngredientLog;
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;
        // parse the ids from the string into an array
        mLogIdToAddStringArray =
                Util.cleanBundledStringIntoArrayList(logIdToAddString);
        mCurrentLogIdToAddString = mLogIdToAddStringArray.get(0);
        mCurrentLogId = UUID.fromString(mCurrentLogIdToAddString);
        if ( mSettingIngredientLog ){
            // now get the info associated with that UUID
            mIngredientLog =
                    mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mCurrentLogId);
            mSymptomLog = null;

            // if consumed hasn't been set yet, then that's what we're changing
            if ( mWhatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_CONSUMED ){
//                questionTextView.setText(R.string.question_textview_new_ingredient_log_consumed_time);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_COOKED) {
                questionTextView.setText(R.string.question_textview_new_ingredient_log_cooked_time);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_INGREDIENT_ACQUIRED) {
                questionTextView.setText(R.string.question_textview_new_ingredient_log_acquired_time);
//                mDefaultNextFragment = new IngredientLogBrandFragment();
            }

            // for each string in array update that log's instant began
            for (String ingredientLogIdString: mLogIdToAddStringArray) {
                // now get the food log associated with each UUID
                mIngredientLogArray.add(
                        mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                                UUID.fromString(ingredientLogIdString)
                        ));
            }

        } else if ( mSettingSymptomLog ){
            // now get the info associated with that UUID
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(mCurrentLogIdToAddString));

            // for each string in array update that log's instant began
            for (String logIdString: mLogIdToAddStringArray) {
                // now get the log associated with each UUID
                mSymptomLogArray.add(
                        mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(logIdString))
                );
            }

            mIngredientLog = null;

            // if begin hasn't been set yet, then that's what we're changing
            if ( mWhatToChange == Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN ){

//                mTitleString = getResources().getString(R.string.title_log_begin);
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED) {
//                mTitleString = getResources().getString(R.string.title_log_change);
            }

        }
        // get the date time of current log so we can set the default view with it
        mDateTime = Util.getDateTimeFromChange(mWhatToChange, mIngredientLog, mSymptomLog);


        //remove the all day button if it's a food log, since there isn't an end time option
        // for food
        // or start the listener for the all day symptom log button
        mButtonAllDay.setVisibility(visibleOrNot);

    }

    public void setButtonVisibilityByInstantAlreadySet(Instant firstInstant, Instant secondInstant){

        // TODO get this working for ingredient log as well, with acquired coming before cooked
        //  and cooked coming before consumed

        // if we are here to set when symptom began
        // it can't have began before now,
        // or else we're not recording our actual symptoms just predicting them which is wrong.
        Instant validWhenBeforeOrOnThisInstant = null;
        // so set our valid instants
        // is if it's begin, the valid times are hours before now (same for changed)

        // there is no too early to be valid time for began
        Instant validWhenAfterOrOnThisInstant = null;
        Integer validWhenAfterOrOnThisHour = 0;

        if ( firstInstant == null ) {
            // we don't have a time for before
            validWhenBeforeOrOnThisInstant = Instant.now();
        } else {
            // not null so set to that
            validWhenBeforeOrOnThisInstant = firstInstant;
        }
        Integer validBeforeOrOnThisHour =
                Util.localDateTimeFromInstant(validWhenBeforeOrOnThisInstant).getHour();

        if ( secondInstant == null ){
            // all hours 0 or greater will be valid, so this works for began
            validWhenAfterOrOnThisInstant = null;
            validWhenAfterOrOnThisHour = 0;
        } else {
            validWhenAfterOrOnThisInstant = secondInstant;
            validWhenAfterOrOnThisHour =
                    Util.localDateTimeFromInstant(validWhenAfterOrOnThisInstant).getHour();
        }



        // if began has been set then we're here to set changed/ended
//        if ( isSymptomLogBeginInstantSet ) {
//            // if it's changed, the valid times are after the begin time and before now or now
//            validWhenAfterOrOnThisInstant = firstInstant;
//            validWhenAfterOrOnThisHour =
//                    Util.localDateTimeFromInstant(validWhenAfterOrOnThisInstant).getHour();
//        }

        // TODO change this to cases for the int in the arraylist from util
        int i = 0;
        // we don't care about same day or not, this fragment is just about changing part of day
        // if it's a different day then changed will get set to began's day when we set the hour
        // for each hour we have a choice button for, look through them
        for ( Button button : mButtonsArrayList) {
            // check that the hour the button corresponds to is
            Integer hourOfButton = mHourChoicesArrayList.get(i);
            // less than now (so if it's noon, hide the afternoon, evening and midnight buttons
            // which are larger numbers than our valid hour)
            // also later or same time than when it began
            if ( hourOfButton > validBeforeOrOnThisHour || validWhenAfterOrOnThisHour > hourOfButton ) {
                // if it is less, then set that corresponding button to invisible so the user isn't
                // distracted by illogical buttons
//                getView().findViewById(case 1 button)
                button.setVisibility(View.INVISIBLE);
            }
            i++;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_part_of_day_all_day:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_all_day),
                        Toast.LENGTH_SHORT).show();
                // set begin to be earliest and changed/ended to be latest times
                mSetBoth = Boolean.TRUE;
                setLogInstantsThenGoToNext(mEarliestHour, mWhatToChange);

                break;

            case R.id.button_log_part_of_day_early:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_early),
                        Toast.LENGTH_SHORT).show();
                // set the early date time to current time but with early hour and minute to 0
                setLogInstantsThenGoToNext(mEarlyHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_midday:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midday),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(mMiddayHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_afternoon:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_afternoon),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(mAfternoonHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_evening:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_evening),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(mEveningHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_midnight:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midnight),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(mMidnightHour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_more_specific_time:
                // throw a toast with more specific time pulled from strings
                Toast.makeText(getContext(), getResources().getString(R.string.toast_more_specific_time),
                        Toast.LENGTH_SHORT).show();
                // only button that was different,
                // let's set the fragment to go to specific time
                Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                        mFragmentContainer,
                        new LogSpecificDateTimeFragment(), mBundle);
                break;

            default:
                break;
        }//end switch case




    }//end onClick



    private void resetUIOrGoHome(Instant firstInstant){

        if (TextUtils.equals(mWhatToChange, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN)) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_intensity_log_changed_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED;
            // reset which buttons to be invisible
            setButtonVisibilityByInstantAlreadySet(Instant.now(),
                    firstInstant);
        }else  if (TextUtils.equals(mWhatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_CONSUMED)) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_ingredient_log_cooked_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_INGREDIENT_COOKED;
            // reset which buttons to be invisible
            setButtonVisibilityByInstantAlreadySet(Instant.now(),
                    firstInstant);
        } else  if (TextUtils.equals(mWhatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_COOKED)) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_ingredient_log_acquired_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_INGREDIENT_ACQUIRED;
            // reset which buttons to be invisible
            setButtonVisibilityByInstantAlreadySet(Instant.now(),
                    firstInstant);
        }  else {
            // done so lets go home
            Util.goToListSymptomLogActivity(null, thisActivity, mSymptomLogIdsToAddString, mBundle.getString(Util.ARGUMENT_ACTION));
        }
    }




    private void setLogInstantsThenGoToNext(Integer hourToSet, String whatToChange){

        Bundle bundleHourToSet = new Bundle();
        bundleHourToSet.putInt(Util.ARGUMENT_HOUR, hourToSet);
        Instant firstInstant = null;
        Bundle mBundleNext = mBundle;

        // we have the time to set now
        if (mSettingSymptomLog){
            //TODO fix, somewhere in here it's breaking on set changed
            if ( mSetBoth ) {
                // run it twice
                Bundle mBundleFirst = mBundle;
                // get the date and time user picked and put them in a bundle and set log with them
                mBundleFirst = Util.setSymptomLogInstants(whatToChange, mSymptomLogArray,
                        mSymptomLogViewModel,
                        bundleHourToSet, mBundleNext);

                // now run it again with latest hour to set as changed instant
                Bundle bundleHourToSetSecond = new Bundle();
                bundleHourToSetSecond.putInt(Util.ARGUMENT_HOUR, mLatestHour);

                // get the date and time user picked and put them in a bundle and set log with them
                mBundleNext = Util.setSymptomLogInstants(whatToChange, mSymptomLogArray,
                        mSymptomLogViewModel,
                        bundleHourToSetSecond, mBundleFirst);
            } else {
                // only setting one
                // get the date and time user picked and put them in a bundle and set log with them
                mBundleNext = Util.setSymptomLogInstants(whatToChange, mSymptomLogArray,
                        mSymptomLogViewModel,
                        bundleHourToSet, mBundleNext);

                // return the began instant in order to reset the UI for changed
                // get the new first instant corresponding to our newly set log instant
                firstInstant =
                        Util.getFirstInstant(null,
                                mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(
                                        UUID.fromString(Util.cleanBundledStringIntoArrayList(
                                                mBundleNext.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY)).get(0)
                                        )
                                ));

                // reset our UI with setting the buttons invisible based on what the first instant is
                resetUIOrGoHome(firstInstant);
            }
        }
        else if (mSettingIngredientLog) {

            // get the date and time user picked and set the ingredient logs to that
            mBundleNext = Util.setIngredientLogInstants(whatToChange, mIngredientLogArray,
                    mIngredientLogViewModel, bundleHourToSet, mBundleNext
            );


            // if we're at cooked then use that instant to set for acquired's UI view, else use
            // consumed
            if ( TextUtils.equals(mWhatToChange, Util.ARGUMENT_CHANGE_INGREDIENT_COOKED) ) {
                // get the new first instant corresponding to our newly set ingredient log instant
                firstInstant = mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                        UUID.fromString( Util.cleanBundledStringIntoArrayList(mBundleNext.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY)).get(0)
                        )
                ).getInstantCooked();
            } else {
                // get the new first instant corresponding to our newly set ingredient log instant
                firstInstant =
                        Util.getFirstInstant(mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                                UUID.fromString(Util.cleanBundledStringIntoArrayList(mBundleNext.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY)).get(0)
                                )
                        ), null);
            }

            // reset our UI with setting the buttons invisible based on what the first instant is
            resetUIOrGoHome(firstInstant);



            // set the food log id we're changing in to be accessible in next fragment
            Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                    mFragmentContainer,
                    mDefaultNextFragment, mBundle);
        }

    }



}//end fragment
