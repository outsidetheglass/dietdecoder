package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LogPartOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;
    String mFragmentFrom = Util.ARGUMENT_FROM_PART_OF_DAY;

    int mFragmentContainer;

    ArrayList<Integer> mHourChoicesArrayList;


    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight, mButtonSpecificTime, mButtonAllDay;
    TextView questionTextView;

    Bundle mBundle, mButtonBundle, mBundleNext;
    Fragment mDefaultNextFragment, mNextFragment;

    Instant mInstantToChange, mInstantConsumed;
    Boolean mSettingIngredientLog = Boolean.FALSE, mSettingSymptomLog = Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE, mSetBoth = Boolean.FALSE, mIsToday = Boolean.TRUE;
    UUID mCurrentLogId;
    ArrayList<String> mSymptomLogIdsToAddStringArray, mIngredientLogIdsToAddStringArray, mLogIdToAddStringArray;
    ArrayList<Button> mButtonsArrayList;
    LocalDateTime mIngredientLogDateTime, mDateTime;
    Integer mHourToSet, mCurrentLogIdIndex;
    String mCurrentLogIdToAddString, ingredientLogIdString, mFragmentGoTo, mWhatToChange,
            mSymptomLogIdsToAddString
            , mIngredientLogIdsToAddString;

    IngredientLogViewModel mIngredientLogViewModel = null;
    SymptomLogViewModel mSymptomLogViewModel = null;
    IngredientViewModel mIngredientViewModel = null;
    SymptomViewModel mSymptomViewModel = null;

    IngredientLog mIngredientLog = null;
    SymptomLog mSymptomLog = null;
    ArrayList<IngredientLog> mIngredientLogArray = null;
    ArrayList<SymptomLog> mSymptomLogArray = null;

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
        Log.d(TAG, " \nmBundle in log part of day: \n" + getArguments().toString());
        // set the listeners on the buttons
        // to run onClick method when they are clicked

        thisActivity = getActivity();
        mBundle = getArguments();
        mBundleNext = mBundle;
        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
        mSymptomLogArray = new ArrayList<>();
        mIngredientLogArray = new ArrayList<>();


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

        mHourChoicesArrayList.add(Util.early_hour);
        mButtonsArrayList.add(mButtonEarly);

        mHourChoicesArrayList.add(Util.midday_hour);
        mButtonsArrayList.add(mButtonMidday);

        mHourChoicesArrayList.add(Util.afternoon_hour);
        mButtonsArrayList.add(mButtonAfternoon);

        mHourChoicesArrayList.add(Util.evening_hour);
        mButtonsArrayList.add(mButtonEvening);

        mHourChoicesArrayList.add(Util.midnight_hour);
        mButtonsArrayList.add(mButtonMidnight);


        // find out if we have a food log or symptom log to set the date time of
        if ( Util.isIngredientLogBundle(mBundle) ) {

            setDependentValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY),
                    Boolean.FALSE, Boolean.TRUE, Util.fragmentContainerViewAddIngredientLog, View.INVISIBLE);

            //TODO get button visibility to work for all all logs
//            setButtonVisibilityByInstantAlreadySet(mCurrentIngredientLog.getInstantAcquired(),
//                    mCurrentIngredientLog.getInstantConsumed());
        } else if ( Util.isSymptomLogBundle(mBundle) ) {

            // set the defaults for symptom log changes
            setDependentValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY),
                    Boolean.TRUE, Boolean.FALSE, Util.fragmentContainerViewAddSymptomLog, View.VISIBLE);

            mButtonAllDay.setOnClickListener(this);


        }


    }//end onViewCreated
    private void setDependentValues(String logIdToAddString, Boolean settingSymptomLog,
                                    Boolean settingIngredientLog, int fragmentContainer,
                                    int visibleOrNot){
        Instant longestAgoInstant = null;
        Instant middleInstant = null;
        Instant mostRecentInstant = null;

        // one of these will be false and the other true
        mSettingSymptomLog = settingSymptomLog;
        mSettingIngredientLog = settingIngredientLog;
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;

        //TODO put this in startnextfragmentbundle properly set finished previous fragment
        if (!Objects.isNull(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY))) {
            mCurrentLogIdIndex =
                    Integer.parseInt(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
        } else {
            mCurrentLogIdIndex = 0;
        }

        if ( logIdToAddString.contains(",")) {
            // parse the ids from the string into an array
            mLogIdToAddStringArray =
                    Util.cleanBundledStringIntoArrayList(logIdToAddString);
            mCurrentLogIdToAddString = mLogIdToAddStringArray.get(mCurrentLogIdIndex);
        } else {
            mLogIdToAddStringArray = new ArrayList<String>();
            mCurrentLogIdToAddString = Util.cleanArrayString(logIdToAddString);
            mLogIdToAddStringArray.add(mCurrentLogIdToAddString);
        }
        mCurrentLogId = UUID.fromString(mCurrentLogIdToAddString);

        // instant to check if the date is today
        Instant instantToCheck = Instant.now();

        if ( mSettingIngredientLog ){
            mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
            mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
            // now get the info associated with that UUID
            mIngredientLog =
                    mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mCurrentLogId);


            // if consumed hasn't been set yet, then that's what we're changing
            if ( Util.isIngredientLogConsumed(mWhatToChange) ){
//                questionTextView.setText(R.string.question_textview_new_ingredient_log_consumed_time);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
                instantToCheck = mIngredientLog.getInstantConsumed();
                mIsToday = Util.isTodayFromInstant(instantToCheck);
                mostRecentInstant = instantToCheck;
                // if they haven't already been set then cooked is auto set to same as begin
                longestAgoInstant = mIngredientLog.getInstantCooked();
            }
            else if ( Util.isIngredientLogCooked(mWhatToChange) ) {
                questionTextView.setText(R.string.question_textview_new_ingredient_log_cooked_time);
                mDefaultNextFragment = new LogDateTimeChoicesFragment();
                instantToCheck = mIngredientLog.getInstantCooked();
                mIsToday = Util.isTodayFromInstant(instantToCheck);
                mostRecentInstant = instantToCheck;
                middleInstant = mIngredientLog.getInstantCooked();
                longestAgoInstant = mIngredientLog.getInstantAcquired();
            }
            else if ( Util.isIngredientLogAcquired(mWhatToChange) ) {
                questionTextView.setText(R.string.question_textview_new_ingredient_log_acquired_time);
//                mDefaultNextFragment = new IngredientLogBrandFragment();
                instantToCheck = mIngredientLog.getInstantAcquired();
                mIsToday = Util.isTodayFromInstant(instantToCheck);
                mostRecentInstant = instantToCheck;
            }

            // for each string in array update that log's instant began
            for (String ingredientLogIdString: mLogIdToAddStringArray) {
                // now get the food log associated with each UUID
                mIngredientLogArray.add(
                        mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                                UUID.fromString(ingredientLogIdString)
                        ));
            }
        }
        else if ( mSettingSymptomLog ){
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            // now get the info associated with that UUID
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(
                            UUID.fromString(mCurrentLogIdToAddString)
                    );

            // for each string in array update that log's instant began
            for (String logIdString: mLogIdToAddStringArray) {
                // now get the log associated with each UUID
                mSymptomLogArray.add(
                        mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(logIdString))
                );
            }

            // is begin and not today, then all times are valid
            // if is changed then valid time has to be after begin time and a button to specific date
            // in case it's the next day

            // is longestAgoInstant and not today, then valid

            // if is mostRecentInstant, then mostRecentInstant after longestAgoInstant, and make
            // specific date button visible

            // if begin hasn't been set yet, then that's what we're changing
            if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ){
                instantToCheck = mSymptomLog.getInstantBegan();
                // get if the instant to change is today, we'll change button visibility from this
                mIsToday = Util.isTodayFromInstant(instantToCheck);
                if ( mIsToday ) {
                    mostRecentInstant = Instant.now();
                    // if it's today and we're setting begin, earliest is midnight of today
                    longestAgoInstant = Util.instantFromLocalDateTime(
                            Util.localDateTimeFromInstant(Instant.now())
                                    .withHour(0).withMinute(0)
                    );
                }
//                mTitleString = getResources().getString(R.string.title_log_begin);
            } else if ( Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange) ) {
                instantToCheck = mSymptomLog.getInstantChanged();
                // get if the instant to change is today, we'll change button visibility from this
                mIsToday = Util.isTodayFromInstant(instantToCheck);
                longestAgoInstant = mSymptomLog.getInstantBegan();
                if ( mIsToday ) {
                    mostRecentInstant = Instant.now();
                    // if it's today and we're setting begin, earliest is midnight of today
                    // TODO converting twice is very inefficient
                    longestAgoInstant = Util.instantFromLocalDateTime(
                            Util.localDateTimeFromInstant(Instant.now())
                                    .withHour(0).withMinute(0)
                    );
                } else {
                    // then changed is not set to today
                    // so the most recent would be the latest time of the day in question
                    // because it's a fair assumption they got to this menu after setting the
                    // date itself first, or, the other date button should be an option
                    // TODO add other date button or change specific time to also time date
                    // TODO figure out how to do this without converting twice
//                    mostRecentInstant = Util.instantFromLocalDateTime(
//                                    Util.localDateTimeFromInstant(instantToCheck)
//                                            .withHour(23).withMinute(59)
//                            );
                }
//                mTitleString = getResources().getString(R.string.title_log_change);
            }
        }


        // get the date time of current log so we can set the default view with it
        mDateTime = Util.getDateTimeFromChange(mWhatToChange, mIngredientLog, mSymptomLog);


        //remove the all day button if it's a food log, since there isn't an end time option
        // for food
        // or start the listener for the all day symptom log button
        mButtonAllDay.setVisibility(visibleOrNot);

        // first time through this we're setting begin so leave second one null
        setButtonVisibilityByInstantAlreadySet(mostRecentInstant, middleInstant, longestAgoInstant);

    }


    private void setValidButtons(Integer validBeforeOrOnThisHour, Integer validWhenAfterOrOnThisHour){

        // TODO change this to cases for the int in the arraylist from util
        int i = 0;
        // we don't care about same day or not, this fragment is just about changing part of day
        // if it's a different day then changed will get set to began's day when we set the hour
        // for each hour we have a choice button for, look through them
        for (Button button : mButtonsArrayList) {
            // check that the hour the button corresponds to is
            Integer hourOfButton = mHourChoicesArrayList.get(i);
            // less than now (so if it's noon, hide the afternoon, evening and midnight buttons
            // which are larger numbers than our valid hour)
            // also later or same time than when it began
            if ( !Objects.isNull(validBeforeOrOnThisHour) ) {
                // if it is less, then set that corresponding button to invisible so the user isn't
                // distracted by illogical buttons
                if ( validWhenAfterOrOnThisHour > hourOfButton ){
                    button.setVisibility(View.INVISIBLE);
                }
            }
            if ( !Objects.isNull(validWhenAfterOrOnThisHour) ) {
                if ( validWhenAfterOrOnThisHour > hourOfButton ){
                    button.setVisibility(View.INVISIBLE);
                }
            }
            i++;
        }
    }
    public void setButtonVisibilityByInstantAlreadySet(Instant mostRecentInstant,
                                                       Instant middleInstant,
                                                       Instant longestAgoInstant){

        Log.d(TAG, " \nmBundle in setButtonVisibilityByInstantAlreadySet: \n" + getArguments().toString());
        Log.d(TAG, " \nmBmIsToday: \n" + mIsToday.toString());
        // TODO get this working for ingredient log as well, with acquired coming before cooked
        //  and cooked coming before consumed

        // if we are here to set when symptom began
        // it can't have began before now,
        // or else we're not recording our actual symptoms just predicting them which is wrong.
        Instant validWhenBeforeOrOnThisInstant = null;
        Instant validWhenAfterOrOnThisInstant = null;
        Integer validWhenAfterOrOnThisHour = null;
        Integer validBeforeOrOnThisHour = null;

        // only run this if it's today
        // buttons only vanish before current time if it's today
        // is begin and not today, then all times are valid because it hasn't set time yet, same
        // for acquired
        //TODO make invisibility work for checking valid date, symptom changed/ended can't be
        // before begin
        if ( mIsToday || Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange)
                || Util.isIngredientLogAcquired(mWhatToChange) ){

            // so set our valid instants
            // is if it's begin, the valid times are hours before now (same for changed)

            // there is no too early to be valid time for began
            validWhenAfterOrOnThisHour = 0;

            if (mostRecentInstant == null) {
                // we don't have a time for before
                validWhenBeforeOrOnThisInstant = Instant.now();
            } else {
                // not null so set to that
                validWhenBeforeOrOnThisInstant = mostRecentInstant;
            }
            validBeforeOrOnThisHour =
                    Util.localDateTimeFromInstant(validWhenBeforeOrOnThisInstant).getHour();

            if (longestAgoInstant == null) {
                // all hours 0 or greater will be valid, so this works for began
                validWhenAfterOrOnThisHour = 0;
            } else {
                validWhenAfterOrOnThisInstant = longestAgoInstant;
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
        }
        else if ( Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange) ){
            // TODO debug this
            // validBeforeOrOnThisHour =
            //        Util.localDateTimeFromInstant(mostRecentInstant).getHour();
            validWhenAfterOrOnThisHour = Util.localDateTimeFromInstant(longestAgoInstant).getHour();
        }

        // if is changed then valid time has to be after begin time and a button to specific date
        // in case it's the next day

        // is longestAgoInstant and not today, then valid

        // if is mostRecentInstant, then mostRecentInstant after longestAgoInstant, and make
        // specific date button visible

        // if it's middleInstant, after longestAgoInstant and before mostRecentInstant

        // if it's consumed then it has to be after cooked and acquired
        // if it's cooked then it has to be after acquired and before consumed
        //TODO if edit, ask user if they want to also edit the invalid times or cancel request

        //both values were set null before this, so if they aren't now they got the needed values
        // from the if statements

        // if there's a middle instant than set with it
        if ( middleInstant != null ){
            Integer mostRecentHour = Util.localDateTimeFromInstant(mostRecentInstant).getHour();
            Integer mostRecentDay = Util.localDateTimeFromInstant(mostRecentInstant).getDayOfYear();
            Integer mostRecentYear = Util.localDateTimeFromInstant(mostRecentInstant).getYear();

            Integer middleHour = Util.localDateTimeFromInstant(middleInstant).getHour();
            Integer middleDay = Util.localDateTimeFromInstant(middleInstant).getDayOfYear();
            Integer middleYear = Util.localDateTimeFromInstant(middleInstant).getYear();

            Integer longestAgoHour = Util.localDateTimeFromInstant(longestAgoInstant).getHour();
            Integer longestAgoDay = Util.localDateTimeFromInstant(longestAgoInstant).getDayOfYear();
            Integer longestAgoYear = Util.localDateTimeFromInstant(longestAgoInstant).getYear();

            // if longest ago day and year and middle day and year are same
            if ( longestAgoDay == middleDay && longestAgoYear == middleYear) {
                // then make invisible the buttons that are invalid
                setValidButtons(middleHour, longestAgoHour);
            }
            // it's possible we're in the middle of a day and both sides need to be invisible
            if ( mostRecentDay == middleDay && mostRecentYear == middleYear) {
                setValidButtons(mostRecentHour, middleHour);
            }
        }
        // if anything has been set and middle instant is null
        else if ( !Objects.isNull(validBeforeOrOnThisHour)
                || !Objects.isNull(validWhenAfterOrOnThisHour) )
        {
            setValidButtons(validBeforeOrOnThisHour, validWhenAfterOrOnThisHour);
        }

    }

    private void resetUIOrGoHome(ArrayList<Instant> instants){

        Instant mostRecentInstant = instants.get(0);
        Instant middleInstant = instants.get(1);
        Instant longestAgoInstant = instants.get(2);

        // if no more recent instant given, then right now is default
        if ( mostRecentInstant == null ){
            mostRecentInstant = Instant.now();
        }

        if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_log_changed_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED;
            // reset which buttons to be invisible
            // can't have changed later than now, so instant now is most recent Instant
            setButtonVisibilityByInstantAlreadySet(mostRecentInstant, null,
                    longestAgoInstant);
        }
        else if ( Util.isIngredientLogConsumed(mWhatToChange) ) {

            // if mostRecentInstant, mostRecentInstant after middleInstant and after longestAgoInstant

            // if middleInstant, middleInstant after longestAgoInstant and before mostRecentInstant

            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_ingredient_log_cooked_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED;
            // reset which buttons to be invisible
            // most recent possible time is right now, user could have eaten and cooked it right now
            setButtonVisibilityByInstantAlreadySet(mostRecentInstant, middleInstant, longestAgoInstant);
        }
        else if ( Util.isIngredientLogCooked(mWhatToChange) ) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_ingredient_log_acquired_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED;
            // reset which buttons to be invisible
            // if we're setting acquired then it has to come before cooked instant, so middle
            // instant is most recent instant to worry about
            setButtonVisibilityByInstantAlreadySet(middleInstant, null,
                    longestAgoInstant);
        } else if ( mIngredientLogViewModel != null ){
            // if we're any other flavor of ingredient log, go set next one

            // set the ingredient log id we're changing in to be accessible in next fragment
            Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                    mFragmentContainer,
                    mDefaultNextFragment, mBundleNext);
        } else {

            // symptom log so done so lets go home
            mBundleNext = Util.setDone(mBundleNext);

            //  we're done with the times for log, which is the
            // last thing to set for that, so now go back to either edit log or list logs
            // depending on which we came from
            Util.goToListOrEditActivity(null, thisActivity, mBundleNext);

        }
    }


    private void setLogInstantsThenGoToNext(Integer hourToSet, String whatToChange){


        // using the log that isn't null, figure out the instant from what to change and reset to
        // that instant changing only the value given, hour here
        // TODO consolidate this code, going back and forth from local date time to instant is
        //  inefficient
        Instant instantToSet = Util.setInstantFromLogAndIntegers(
                mSymptomLogViewModel, mSymptomLog, mIngredientLogViewModel, mIngredientLog,
                whatToChange, null, hourToSet, null, null, null);

        // get the date and time user picked and put them in a bundle and set log with them

        // only if we were told to move to next change should we reset what to change
        Boolean moveToNextWhatToChange = Boolean.TRUE;
        mBundleNext = Util.setLogInstants(whatToChange,
                mIngredientLogArray, mSymptomLogArray,
                mIngredientLogViewModel, mSymptomLogViewModel,
                mIngredientViewModel, mSymptomViewModel,
                Util.localDateTimeFromInstant(instantToSet), mBundleNext, moveToNextWhatToChange);


        // get the previous instant corresponding to our newly set log instant
        // if it's consumed, return the instant it was cooked
        // if it's changed, return begin
        ArrayList<Instant> instants = Util.setOrderOfLogInstants(whatToChange,
                mIngredientLogViewModel,
                mSymptomLogViewModel, mBundleNext );

        // reset our UI with setting the buttons invisible based on what the first instant is
        // or go home or to next fragment
        resetUIOrGoHome(instants);
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
                mWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS_ALL_DAY;
                setLogInstantsThenGoToNext(Util.earliest_hour, mWhatToChange);

                break;

            case R.id.button_log_part_of_day_early:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_early),
                        Toast.LENGTH_SHORT).show();
                // set the early date time to current time but with early hour and minute to 0
                setLogInstantsThenGoToNext(Util.early_hour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_midday:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midday),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(Util.midday_hour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_afternoon:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_afternoon),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(Util.afternoon_hour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_evening:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_evening),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(Util.evening_hour, mWhatToChange);
                break;

            case R.id.button_log_part_of_day_midnight:
                Toast.makeText(getContext(),
                        getResources().getString(R.string.toast_part_of_day_midnight),
                        Toast.LENGTH_SHORT).show();
                setLogInstantsThenGoToNext(Util.midnight_hour, mWhatToChange);
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

}//end fragment
