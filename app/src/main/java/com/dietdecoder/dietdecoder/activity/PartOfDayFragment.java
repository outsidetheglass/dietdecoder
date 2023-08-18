package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class PartOfDayFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;
    String mFragmentFrom = Util.ARGUMENT_FROM_PART_OF_DAY_FRAGMENT;

    int mFragmentContainer, mAllDayButtonVisibleOrNot, mQuestionStringInt;

    ArrayList<Integer> mHourChoicesArrayList;


    Button mButtonEarly, mButtonMidday, mButtonAfternoon, mButtonEvening,
            mButtonMidnight, mButtonSpecificTime, mButtonAllDay;
    TextView questionTextView;

    Bundle mBundle, mButtonBundle, mBundleNext;
    Fragment mDefaultNextFragment, mNextFragment;

    Instant mInstantToChange, mInstantConsumed, mLongestAgoInstant = null, mMiddleInstant = null,
            mMostRecentInstant = null, mNowInstant = Instant.now(), mInstantToCheck =
            Instant.now(), mEarliestMidnightTodayInstant = Util.instantFromLocalDateTime(
                    Util.localDateTimeFromInstant(Instant.now())
                    .withHour(0).withMinute(0));
    Boolean mSettingIngredientLog = Boolean.FALSE, mSettingSymptomLog = Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE, mSetBoth = Boolean.FALSE, mIsToday = Boolean.TRUE, mMostRecentIsNow =
            Boolean.FALSE, mLongestAgoIsMidnight = Boolean.FALSE, mIsEdit = Boolean.FALSE;
    UUID mCurrentLogId;
    ArrayList<String> mSymptomLogIdsStringArray, mIngredientLogIdsStringArray, mLogIdStringArray;
    ArrayList<Button> mButtonsArrayList;
    LocalDateTime mIngredientLogDateTime, mDateTime;
    Integer mHourToSet, mCurrentLogIdIndex;
    String mCurrentLogIdString, ingredientLogIdString, mFragmentGoTo, mWhatToChange,
            mSymptomLogIdsString
            , mIngredientLogIdsString, mAction, mLogIdsString;

    IngredientLogViewModel mIngredientLogViewModel = null;
    SymptomLogViewModel mSymptomLogViewModel = null;
    IngredientViewModel mIngredientViewModel = null;
    SymptomViewModel mSymptomViewModel = null;

    IngredientLog mIngredientLog = null;
    SymptomLog mSymptomLog = null;
    ArrayList<IngredientLog> mIngredientLogArray = null;
    ArrayList<SymptomLog> mSymptomLogArray = null;

    Integer mNowHour = Util.hourFromInstant(Instant.now());
    Integer mEarliestPossibleHour = 0;
    Integer mLatestPossibleHour = 23;

    public PartOfDayFragment() {
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
        Log.d(TAG, "PartOfDayFragment onViewCreated : \n" + getArguments().toString());
        thisActivity = getActivity();

        // check the bundle exists and has what to change and an id array string
        Util.checkValidFragment(getArguments(), thisActivity);
        // set variables from bundle
        mBundle = getArguments();
        mBundleNext = Util.updateBundleGoToNext(mBundle);
        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
        mSymptomLogArray = new ArrayList<>();
        mIngredientLogArray = new ArrayList<>();
        mAction = mBundle.getString(Util.ARGUMENT_ACTION);
        mIsEdit = Util.isActionEditBundle(mBundle);


        questionTextView = view.findViewById(R.id.textview_question_log_part_of_day_specific_time_range);

        // set the listeners on the buttons
        // to run onClick method when they are clicked
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

        // find out if we have a ingredient log or symptom log to set the date time of
        // and set relevant values
        setAllValues();

    }//end onViewCreated

    // set the instants we work off of for which buttons should be visible
    private void setValidVisibilityInstants(Instant longestAgoInstant, Instant middleInstant,
                                            Instant mostRecentInstant){
        mLongestAgoInstant = longestAgoInstant;
        mMiddleInstant = middleInstant;
        mMostRecentInstant = mostRecentInstant;


        //TODO decide if setting based off of today logic is useful
//        if ( mIsToday ) {
//            mMostRecentInstant = Instant.now();
//            // if it's today and we're setting begin, earliest is midnight of today
//            mLongestAgoInstant = Util.instantFromLocalDateTime(
//                    Util.localDateTimeFromInstant(Instant.now())
//                            .withHour(0).withMinute(0) );
//        } else {
        // then changed is not set to today
        // so the most recent would be the latest time of the day in question
        // because it's a fair assumption they got to this menu after setting the
        // date itself first, or, the other date button should be an option
        // TODO add other date button or change specific time to also time date
        // TODO figure out how to do this without converting twice

//                    mMostRecentInstant = Util.instantFromLocalDateTime(
//                                    Util.localDateTimeFromInstant(mInstantToCheck)
//                                            .withHour(23).withMinute(59)
//                            );
//            mLongestAgoInstant = longestAgoInstant;
//            mMostRecentInstant = mostRecentInstant;
//        }
    }


    // set the index, string, array, and UUID for the current log
    private void setObjectArrayValues(String logIdsString){

        // if there is an index in the bundle, set to that, or to 0
        mCurrentLogIdIndex = Util.setIntegerCurrentIndexFromBundle(mBundle);
        // set our array of ID's, even if it's just one
        mLogIdsString = logIdsString;
        mLogIdStringArray = Util.cleanBundledStringIntoArrayList(mLogIdsString);
        // get our current log's id
        mCurrentLogIdString = mLogIdStringArray.get(mCurrentLogIdIndex);
        mCurrentLogId = UUID.fromString(mCurrentLogIdString);
    }

    // set accessing the view model for ingredient log
    private void setIngredientLogViewModel(){
        // if ingredient log view model was given then set those values
        mIngredientLogViewModel =
                (IngredientLogViewModel) new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        // now get the info associated with that UUID
        mIngredientLog =
                mIngredientLogViewModel.viewModelGetLogFromLogId(mCurrentLogId);

        // set our array of ID's, even if it's just one
        mIngredientLogArray = Util.setIngredientLogArrayFromStringArray(mLogIdStringArray,
                mIngredientLogViewModel);
    }

    // set accessing the view model for symptom log
    private void setSymptomLogViewModel(){

        // we were given symptom log to use so set those values
        mSymptomLogViewModel = (SymptomLogViewModel) new ViewModelProvider(this).get(SymptomLogViewModel.class);
        mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
        mSymptomLogArray = new ArrayList<>();

        // now get the info associated with that UUID
        mSymptomLog =
                mSymptomLogViewModel.viewModelGetLogFromLogId(UUID.fromString(mCurrentLogIdString));

        // for each string in array update that log's instant began
        // get the log associated with each UUID
        mSymptomLogArray = Util.setSymptomLogArrayFromStringArray(mLogIdStringArray,
                mSymptomLogViewModel);

    }

    // set which fragment container we're using and the question and visibility of optional button
    private void setDependentUI(int fragmentContainer, int questionStringInt, int
            allDayButtonVisibleOrNot){
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;

        //remove the all day button if it's a ingredient log,
        // since how long it took to eat something isn't something particularly helpful to record
        // or start the listener for the all day symptom log button
        mButtonAllDay.setVisibility(allDayButtonVisibleOrNot);

        // set the question asked to the user
        questionTextView.setText(questionStringInt);
    }

    // set the values that differ depending on what to change for ingredient log
    private void setIngredientLogDependentValues(){
        // set the needed values for ingredient log
        setObjectArrayValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setIngredientLogViewModel();

        //longestAgoInstant, middleInstant, mostRecentInstant
        setValidVisibilityInstants(mIngredientLog.getInstantAcquired(),
                mIngredientLog.getInstantCooked(),
                mIngredientLog.getInstantConsumed());

        // if consumed hasn't been set yet, then that's what we're changing
        if ( Util.isIngredientLogConsumed(mWhatToChange) ){
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                    R.string.question_textview_new_ingredient_log_consumed_time,
                    View.INVISIBLE);
            //nextFragment, instantToCheck
            setWhatToChangeValues(new DateTimeChoicesFragment(), mIngredientLog.getInstantConsumed());
        }
        else if ( Util.isIngredientLogCooked(mWhatToChange) ) {
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                    R.string.question_textview_new_ingredient_log_cooked_time,
                    View.INVISIBLE);
            //nextFragment, instantToCheck
            setWhatToChangeValues(new DateTimeChoicesFragment(),
                    mIngredientLog.getInstantCooked());
        }
        else if ( Util.isIngredientLogAcquired(mWhatToChange) ) {

            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                    R.string.question_textview_new_ingredient_log_acquired_time,
                    View.INVISIBLE);

//                mDefaultNextFragment = new IngredientLogBrandFragment();
            //nextFragment, instantToCheck
            setWhatToChangeValues(null, mIngredientLog.getInstantAcquired());
        }
    }

    // set the values that differ depending on what to change for symptom log
    private void setSymptomLogDependentValues(){
        // set the needed values for ingredient log
        setObjectArrayValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setSymptomLogViewModel();

        //longestAgoInstant, middleInstant, mostRecentInstant
        setValidVisibilityInstants(mSymptomLog.getInstantBegan(), null,
                mSymptomLog.getInstantChanged());

        // if consumed hasn't been set yet, then that's what we're changing
        if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ){
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddSymptomLog,
                    R.string.question_textview_new_symptom_log_begin_time,
                    View.VISIBLE);
            //nextFragment, instantToCheck
            setWhatToChangeValues(new DateTimeChoicesFragment(), mSymptomLog.getInstantBegan());
            // get if the instant to change is today, we'll change button visibility from this
            mIsToday = Util.isTodayFromInstant(mInstantToCheck);
            //TODO change the logic to work off of booleans for what to set
//            if ( mIsToday ) {
//                // these are based off of what to change and whether it's today or same day or not
//                mMostRecentIsNow = Boolean.TRUE;
//                // if it's today and we're setting begin, earliest is midnight of today
//                mLongestAgoIsMidnight = Boolean.TRUE;
//            }
        }
        else if ( Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange) ) {
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddSymptomLog,
                    R.string.question_textview_new_symptom_log_changed_time,
                    View.VISIBLE);
            //nextFragment, instantToCheck
            setWhatToChangeValues(null, mSymptomLog.getInstantChanged());
        }

        // set a listener on our symptom log only button, saying the symptom lasted all day
        mButtonAllDay.setOnClickListener(this);
    }

    // set the values that differ depending on what to change
    private void setWhatToChangeValues(Fragment nextFragment, Instant instantToCheck){
        mDefaultNextFragment = nextFragment;
        mInstantToCheck = instantToCheck;
        // get if the instant to change is today, we'll change button visibility from this
        mIsToday = Util.isTodayFromInstant(mInstantToCheck);
    }

    // the function to check if ingredient log or symptom log and running the set value functions
    private void setAllValues(){

        // one of these will be false and the other true
        mSettingIngredientLog = Util.isIngredientLogBundle(mBundle);
        mSettingSymptomLog = Util.isSymptomLogBundle(mBundle);

        if ( mSettingIngredientLog ){
            setIngredientLogDependentValues();
        }
        else if ( mSettingSymptomLog ){
            setSymptomLogDependentValues();
        }
        else {
            Log.d(TAG, "Neither symptom log nor ingredient log were given for the object to set a" +
                    " date time to.");
        }

        // get the date time of current log so we can set the default view with it
        mDateTime = Util.getDateTimeFromChange(mWhatToChange, mIngredientLog, mSymptomLog);

        // set buttons invisible based on logic for
        // which one had to happen before or after another
        setButtonVisibilityByInstantAlreadySet(mMostRecentInstant, mMiddleInstant,
                mLongestAgoInstant);

    }


    // set the buttons to be invisible or not
    // based on valid hours before or after each button option
    private void setValidButtons(Integer validBeforeOrOnThisHour, Integer validAfterOrOnThisHour){

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
                if ( validBeforeOrOnThisHour <= hourOfButton ){
                    button.setVisibility(View.INVISIBLE);
                }
            }
            if ( !Objects.isNull(validAfterOrOnThisHour) ) {
                if ( validAfterOrOnThisHour >= hourOfButton ){
                    button.setVisibility(View.INVISIBLE);
                }
            }
            i++;
        }
    }


    // check if the times of each instant set in the log are valid and go back to list if so
    private void checkIfInvalid(Instant mLongestAgoInstant, Instant mMiddleInstant
            , Instant mMostRecentInstant){

        //TODO if edit, ask user if they want to also edit the invalid times or cancel request

        // we want these to be false, true on these mean something's invalid
        Boolean cookedDayHappenedAfterConsumedDay = Util.isEarlierDayYear(mMiddleInstant,
                mMostRecentInstant);
        Boolean acquiredHappenedAfterConsumed = Util.isEarlierDayYear(mLongestAgoInstant,
                mMostRecentInstant);
        Boolean changedHappenedBeforeBegin = Util.isEarlierDayYear(mLongestAgoInstant,
                mMostRecentInstant);
        // changed needs to happen today or in the past, can't be in the future
        // so most recent needs to be today or previous day
        // so the most recent day needs to be earlier than or equal to now
        Boolean changedHappensInFuture = null;
        // TODO fix this not sure it'll work for both begin and changed set
        if ( mIsToday || Util.isEarlierDayYear(mMostRecentInstant,
                Instant.now()) ){
            // is valid
            changedHappensInFuture = Boolean.FALSE;
        } else {
            // changed happens in future
            changedHappensInFuture = Boolean.TRUE;
        }

        // if it's any of the invalid boolean checks
        // begin set after changed only matters if it's editing
        if ( cookedDayHappenedAfterConsumedDay
                || acquiredHappenedAfterConsumed
                || ( changedHappenedBeforeBegin && mIsEdit ||
                changedHappensInFuture
                )
        ){
            String firstTypeString = "";
            String secondTypeString = "";
            // give the function "before"
            String beforeOrAfterString = Util.ARGUMENT_BEFORE;

           if ( cookedDayHappenedAfterConsumedDay ) {
                firstTypeString = "cooked";
                secondTypeString = "consumed";
            }
            // check if consumed is valid time
            // if consumed incorrectly set to the past
            else if ( acquiredHappenedAfterConsumed ){
                firstTypeString ="acquired" ;
                secondTypeString ="consumed" ;
            } else if ( changedHappenedBeforeBegin ){
                // invalid if we're in edit and checking the other is wrong
               // so if from edit we set to modify begin, invalid if changed is before begin
               firstTypeString ="changed/ended" ;
               secondTypeString ="began" ;
           }

            // tell the user it's invalid
            Util.toastInvalidBeforeAfter(firstTypeString, secondTypeString, beforeOrAfterString,
                    thisActivity);

            Util.goToLogSpecificDateTimeFragment(thisActivity,
                    getParentFragmentManager().beginTransaction(),mFragmentContainer,
                    mBundleNext);
        }
        // if cooked day happened before consumed day and after acquired AKA it's valid

    }

    // return a bundle with before hour and after hour for deciding which buttons should be visible
    private Bundle validBeforeAfter(Bundle validHoursBundle, Integer beforeHour, Integer afterHour){
        validHoursBundle.putInt(Util.ARGUMENT_AFTER, afterHour);
        validHoursBundle.putInt(Util.ARGUMENT_BEFORE, beforeHour);
        return validHoursBundle;
    }
    private Bundle validAfter(Bundle validHoursBundle, Integer afterHour){
        validHoursBundle.putInt(Util.ARGUMENT_AFTER, afterHour);
        return validHoursBundle;
    }
    private Bundle validBefore(Bundle validHoursBundle, Integer beforeHour){
        validHoursBundle.putInt(Util.ARGUMENT_BEFORE, beforeHour);
        return validHoursBundle;
    }
    private Bundle getValidHoursIngredientLogCooked(Instant mLongestAgoInstant,
                                                    Instant mMiddleInstant,
                                                    Instant mMostRecentInstant,
                                                    Integer longestAgoHour,
                                                    Integer mostRecentHour) {
        Bundle validHoursBundle = new Bundle();

        Boolean cookedAcquiredSameDay = Util.isSameDayYear(mLongestAgoInstant, mMiddleInstant);
        Boolean cookedConsumedSameDay = Util.isSameDayYear(mMiddleInstant, mMostRecentInstant);

        // if cooked day happened before consumed day and after acquired AKA it's valid

        if (cookedAcquiredSameDay){
            // earliest is acquired hour
            validHoursBundle = validAfter(validHoursBundle, longestAgoHour);
        } else {
            // then valid anytime after midnight and before now
            validHoursBundle = validAfter(validHoursBundle, mEarliestPossibleHour);
        }

        //is today && cooked acquired same day && cooked consumed same day
        if (mIsToday && cookedConsumedSameDay) {
            // cooked happens before consumed
            // so consumed is latest possible
            // so most recent is valid before that hour
            // is elsewise now hour
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour,
                    mLatestPossibleHour);
            if ( mostRecentHour < mNowHour){
                validHoursBundle = validBefore(validHoursBundle, mostRecentHour);
            }
        }
        //is today && not cooked consumed same day
        else if (mIsToday && !cookedConsumedSameDay ) {
            validHoursBundle = validBefore(validHoursBundle, mNowHour);
        }
        //is not today && is cooked consumed same day
        else if ( !mIsToday && cookedConsumedSameDay){
            validHoursBundle = validBefore(validHoursBundle, mostRecentHour);
        }
        //is not today && is not cooked consumed same day
        else if ( !mIsToday && !cookedConsumedSameDay){
            // both happened yesterday or will happen tomorrow or in the future
                // so if both happened yesterday and latest is midnight
            validHoursBundle = validBefore(validHoursBundle, mLatestPossibleHour);
        }

        return validHoursBundle;
    }
    private Bundle getValidHoursIngredientLogConsumed(Instant mLongestAgoInstant,
                                                    Instant mMiddleInstant,
                                                    Instant mMostRecentInstant,
                                                    Integer longestAgoHour, Integer middleHour) {
        Bundle validHoursBundle = new Bundle();

        Boolean consumedAcquiredSameDay = Util.isSameDayYear(mLongestAgoInstant,
                mMostRecentInstant);
        Boolean consumedCookedSameDay = Util.isSameDayYear(mMiddleInstant, mMostRecentInstant);

        // if cooked day happened before consumed day and after acquired AKA it's valid

        //is today && cooked acquired same day && cooked consumed same day
        if (mIsToday && consumedCookedSameDay && consumedAcquiredSameDay) {
            // is today and was cooked and acquired today
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour, middleHour);
            // find which happened closer to now, so the bigger number, and valid after that
            // and before now
            if ( longestAgoHour > middleHour ){
                // bigger number is longest ago so that is the closer number
                validHoursBundle = validAfter(validHoursBundle, longestAgoHour);
            }
        }
        //is today && not cooked consumed same day
        else if ( mIsToday  && consumedCookedSameDay && !consumedAcquiredSameDay){
            // is today and was cooked today and acquired yesterday
            // valid after middle hour and before now
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour, middleHour);
        }
        //is not today && is not cooked consumed same day
        else if ( mIsToday && !consumedCookedSameDay && !consumedAcquiredSameDay){
            // is today and cooked yesterday and acquired yesterday
            // so earliest hour after that valid, and before now
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour, mEarliestPossibleHour);
        }
        //is not today && is cooked consumed same day
        else if ( mIsToday  && !consumedCookedSameDay && consumedAcquiredSameDay){
            // is today and cooked yesterday and acquired today
            // so valid after longest ago and before now
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour, longestAgoHour);
        }
        //is not today && is not cooked consumed same day
        else if ( !mIsToday && consumedCookedSameDay && !consumedAcquiredSameDay){
            // consumed yesterday and cooked yesterday but acquired the day before that
            // middle hour is valid after that, and valid before latest
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour, middleHour);
        }
        //is not today && is cooked consumed same day
        else if ( !mIsToday  && consumedCookedSameDay && consumedAcquiredSameDay){
            // yesterday was acquired and cooked
            // whichever is closest in time to consumed, that's valid after that time
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                    longestAgoHour);
            if ( longestAgoHour < middleHour){
                validHoursBundle = validAfter(validHoursBundle, middleHour);
            }
        }
        //is not today && is not cooked consumed same day
        else if ( !mIsToday && !consumedCookedSameDay && consumedAcquiredSameDay){
            // yesterday and was cooked the day before that and was acquired also yesderday
            // can't acquire after consume
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                    longestAgoHour);
        }
        //is not today && is not cooked consumed same day
        else if ( !mIsToday && !consumedCookedSameDay &&  !consumedAcquiredSameDay){
            // yesterday consumed
            // two days ago acquired and cooked
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                    mEarliestPossibleHour);
        }

        return validHoursBundle;
    }


    private Bundle getValidHoursIngredientLogAcquired(Instant mLongestAgoInstant,
                                                      Instant mMostRecentInstant,
                                                      Integer mostRecentHour){
        Bundle validHoursBundle = new Bundle();

        // if consumed and acquired were same day and cooked wasn't, it was cooked
        // before they got it and ate it, so acquired and cooked times have nothing to do
        // with each other
//            Boolean acquiredCookedSameDay = Util.isSameDayYear(mLongestAgoInstant,
//            mMiddleInstant);
        Boolean acquiredConsumedSameDay = Util.isSameDayYear(mLongestAgoInstant,
                mMostRecentInstant);

        // if not today, valid after begin time until midnight
        if ( !mIsToday && !acquiredConsumedSameDay ){
            // valid anytime before the next day, the maximum hour
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                    mEarliestPossibleHour);
        }
        else if ( mIsToday && acquiredConsumedSameDay) {
            // valid anytime after midnight, since acquired is the earliest to set
            // must be set wrong to be in the future or it's now
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour,
                    mEarliestPossibleHour);
            // if it was consumed before now, say it was eaten at noon but it's afternoon
            // now, still has to be acquired before consumed at noon, so noon is our cutoff,
            // valid before noon, valid before whichever is earlier, valid before whichever
            // is smaller number
            if ( mostRecentHour < mNowHour){
                validHoursBundle = validBefore(validHoursBundle, mostRecentHour);
            }
        }
        // acquired today and consumed was earlier or later
        else if ( mIsToday && !acquiredConsumedSameDay ){
            // acquired is valid, it happened today and consumed is set to after acquired
            // so is today, then valid anytime after midnight before now
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour,
                    mEarliestPossibleHour);
        }
        // both happened yesterday or will happen tomorrow or in the future
        else if ( !mIsToday && acquiredConsumedSameDay ) {
            // so if both happened yesterday
            // earliest is midnight and latest is consumed hour
            validHoursBundle = validBeforeAfter(validHoursBundle, mostRecentHour,
                    mEarliestPossibleHour);
        }


        return validHoursBundle;
    }

    private Bundle getValidHoursSymptomLogBegin(Instant mLongestAgoInstant,
                                                      Instant mMostRecentInstant,
                                                Integer longestAgoHour,
                                                      Integer mostRecentHour){
        Bundle validHoursBundle = new Bundle();


        Boolean beginChangedSameDay = Util.isSameDayYear(mLongestAgoInstant,
                mMostRecentInstant);

        Boolean isSymptomBegin = Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange);
        Boolean isSymptomChanged = Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange);

        // if we're editing then we do care when it changed
        if ( mIsEdit && beginChangedSameDay && !mIsToday ) {
            // changed is later than begin, so begin has to be before most recent
            // if not today, it's yesterday, so begin can be earliest.
            validHoursBundle = validBeforeAfter(validHoursBundle, mostRecentHour,
                    mEarliestPossibleHour);

        } else if ( mIsEdit && beginChangedSameDay && mIsToday ) {
            // if today, we're setting for earlier today, so still earliest
            validHoursBundle = validBeforeAfter(validHoursBundle, mostRecentHour,
                    mEarliestPossibleHour);

        } else if ( mIsEdit && !beginChangedSameDay && !mIsToday ) {
            //if changed is not same day and not today, latest hour is correct
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                    mEarliestPossibleHour);
            //TODO remove or finish, trying the boolean option out
//            mLatestHourIsLatestPossible = Boolean.TRUE;
//            mEarliestHourIsEarliestPossible = Boolean.TRUE;
        } else if ( mIsEdit && !beginChangedSameDay && mIsToday ) {
            //if changed is not same day and is today, now is latest hour
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour,
                    mEarliestPossibleHour);
            //TODO remove or finish, trying the boolean option out
//            mLatestHourIsLatestPossible = Boolean.TRUE;
//            mEarliestHourIsEarliestPossible = Boolean.TRUE;
        }
        // is not edit
        // we must be here from add, and this is begin, so we don't care about same day
        else if ( !mIsEdit && !mIsToday) {
                // we're adding a new symptom log, on yesterday, so earliest and latest
                validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                        mEarliestPossibleHour);

        }
        // is today and adding new log so must be anytime before now
        else if ( !mIsEdit && mIsToday) {
                validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour,
                        mEarliestPossibleHour);
        }


        return validHoursBundle;
    }
    private Bundle getValidHoursSymptomLogChanged(Instant mLongestAgoInstant,
                                                Instant mMostRecentInstant,
                                                Integer longestAgoHour,
                                                Integer mostRecentHour){
        Bundle validHoursBundle = new Bundle();

        // check if begin is same day as changed
        // if we're setting changed then that's the most recent
        // and began is longest ago
        Boolean changedAndBeginAreSameDay = Util.isSameDayYear(mLongestAgoInstant,
                mMostRecentInstant);
        //begin could have started yesterday, so default to midnight
        validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                mEarliestPossibleHour);

        // if begin is same day and not today, valid after begin time until midnight
        if ( changedAndBeginAreSameDay && !mIsToday ){
            // if begin is same day as changed,
            // then changed is only valid after begin instant
            // valid anytime before the next day, the maximum hour
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour,
                    longestAgoHour);
        }
        // if begin is same day and today, valid after begin time until mNowHour
        else if ( changedAndBeginAreSameDay && mIsToday ){
            // if begin is same day as changed,
            // then changed is only valid after begin instant
            // valid anytime before now because it's today
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour, longestAgoHour);
        }
        // not the same day as begin,
        // if begin was yesterday and changed is today, valid before mNowHour and after
        // midnight
        else if (!changedAndBeginAreSameDay && mIsToday){
            // changed is on today but if we're here then it's not same day as began, so
            // began was yesterday or some earlier day
            // but it can't change after current moment
            validHoursBundle = validBeforeAfter(validHoursBundle, mNowHour, mEarliestPossibleHour);
        }
        // if begin was two days ago and changed is yesterday, midnight is both earliest and
        // latest, all day is valid
        else if (!changedAndBeginAreSameDay && !mIsToday){
            validHoursBundle = validBeforeAfter(validHoursBundle, mLatestPossibleHour, mEarliestPossibleHour);
        }

        return validHoursBundle;
    }
    private Bundle getValidHours(Instant mLongestAgoInstant, Instant mMiddleInstant,
                                                Instant mMostRecentInstant){
        Bundle validHoursBundle = new Bundle();

        // first check if consumed is valid time
        // if it's invalid tell user and go back to list activity
        checkIfInvalid(mLongestAgoInstant, mMiddleInstant, mMostRecentInstant);

        Integer longestAgoHour = Util.hourFromInstant(mLongestAgoInstant);
        Integer middleHour = Util.hourFromInstant(mMiddleInstant);
        Integer mostRecentHour = Util.hourFromInstant(mMostRecentInstant);

        //TODO change the logic to work off of booleans for what to set,
        // such as from begin set this, so then we'd set from mMostRecentIsNow etc:
//        if ( mIsToday ) {
//            // these are based off of what to change and whether it's today or same day or not
//            mMostRecentIsNow = Boolean.TRUE;
//            // if it's today and we're setting begin, earliest is midnight of today
//            mLongestAgoIsMidnight = Boolean.TRUE;
//        }

        // buttons only vanish before and after times based on the other relevant instants
        if ( Util.isIngredientLogAcquired(mWhatToChange) ){
            validHoursBundle = getValidHoursIngredientLogAcquired(mLongestAgoInstant,
                    mMostRecentInstant,
                    mostRecentHour);
        }
        else if ( Util.isIngredientLogCooked(mWhatToChange) ){
            validHoursBundle = getValidHoursIngredientLogCooked(
                    mLongestAgoInstant, mMiddleInstant, mMostRecentInstant,
                    longestAgoHour, mostRecentHour);
        }
        else if (Util.isIngredientLogConsumed(mWhatToChange) ){
            validHoursBundle = getValidHoursIngredientLogConsumed(mLongestAgoInstant,
                    mMiddleInstant,
                    mMostRecentInstant, longestAgoHour, middleHour);
        }
        else if ( Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange) ){
            validHoursBundle = getValidHoursSymptomLogChanged(mLongestAgoInstant,
                    mMostRecentInstant, longestAgoHour, mostRecentHour);
        }
        else if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ){
            validHoursBundle = getValidHoursSymptomLogBegin(mLongestAgoInstant,
                    mMostRecentInstant, longestAgoHour, mostRecentHour);
        }
        return validHoursBundle;
    }
    public void setButtonVisibilityByInstantAlreadySet(Instant mMostRecentInstant,
                                                       Instant mMiddleInstant,
                                                       Instant mLongestAgoInstant){

        Log.d(TAG, " \nmBundle in setButtonVisibilityByInstantAlreadySet: \n" + getArguments().toString());
        Log.d(TAG, " \nmBmIsToday: \n" + mIsToday.toString());
        // TODO get this working for ingredient log as well, with acquired coming before cooked
        //  and cooked coming before consumed

        //TODO make invisibility work for checking valid date, symptom changed/ended can't be
        // before begin

        Integer validAfterOrOnThisHour = null;
        Integer validBeforeOrOnThisHour = null;
        Bundle validHoursBundle = getValidHours(mLongestAgoInstant, mMiddleInstant,
                mMostRecentInstant);

        //both values were set null before this, so if they aren't now they got the needed values
        // from the if statements

        validBeforeOrOnThisHour = validHoursBundle.getInt(Util.ARGUMENT_BEFORE);
        validAfterOrOnThisHour = validHoursBundle.getInt(Util.ARGUMENT_AFTER);

        // if anything has been set
        if ( !Objects.isNull(validBeforeOrOnThisHour)
                || !Objects.isNull(validAfterOrOnThisHour) )
        {
            setValidButtons(validBeforeOrOnThisHour, validAfterOrOnThisHour);
        }

    }

    private void resetUIOrGoHome(ArrayList<Instant> instants){

        Instant mMostRecentInstant = instants.get(0);
        Instant mMiddleInstant = instants.get(1);
        Instant mLongestAgoInstant = instants.get(2);

        // if no more recent instant given, then right now is default
        if ( mMostRecentInstant == null ){
            mMostRecentInstant = Instant.now();
        }

        if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_log_changed_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED;
            // reset which buttons to be invisible
            // can't have changed later than now, so instant now is most recent Instant
            setButtonVisibilityByInstantAlreadySet(mMostRecentInstant, null,
                    mLongestAgoInstant);
        }
        else if ( Util.isIngredientLogConsumed(mWhatToChange) ) {

            // if mMostRecentInstant, mMostRecentInstant after mMiddleInstant and after
            // mLongestAgoInstant

            // if mMiddleInstant, mMiddleInstant after mLongestAgoInstant and before
            // mMostRecentInstant

            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_ingredient_log_cooked_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED;
            // reset which buttons to be invisible
            // most recent possible time is right now, user could have eaten and cooked it right now
            setButtonVisibilityByInstantAlreadySet(mMostRecentInstant, mMiddleInstant,
                    mLongestAgoInstant);
        }
        else if ( Util.isIngredientLogCooked(mWhatToChange) ) {
            // we're done
            questionTextView.setText(getResources().getString(R.string.question_textview_new_ingredient_log_acquired_time));
            // we have changed begin so now set changed/ended instead
            mWhatToChange = Util.ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED;
            // reset which buttons to be invisible
            // if we're setting acquired then it has to come before cooked instant, so middle
            // instant is most recent instant to worry about
            setButtonVisibilityByInstantAlreadySet(mMiddleInstant, null,
                    mLongestAgoInstant);
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
                Util.goToLogSpecificDateTimeFragment(
                        thisActivity, getParentFragmentManager().beginTransaction(),
                        mFragmentContainer, mBundleNext);
                break;

            default:
                break;
        }//end switch case




    }//end onClick

}//end fragment
