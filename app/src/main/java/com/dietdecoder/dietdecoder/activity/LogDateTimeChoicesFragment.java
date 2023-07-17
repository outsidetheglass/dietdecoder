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
import com.dietdecoder.dietdecoder.activity.ingredientlog.NewIngredientAmountFragment;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class LogDateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;

    Button mButtonJustNow, mButtonEarlierToday, mButtonAnotherDate, mButtonYesterday;
    TextView questionTextView;

    // TODO set this to be a preference
    Integer hoursEarlierInt = 4;

    Bundle mBundle;
    int mFragmentContainer;

    IngredientLogViewModel mIngredientLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    IngredientLog mIngredientLog;
    SymptomLog mSymptomLog;

    String mCurrentLogIdToAddString, mWhatToChange;
    UUID mCurrentLogId;

    Fragment mDefaultNextFragment;

    Boolean isSymptomLogBeginInstantSet = Boolean.FALSE;
    Boolean isSymptomLogChangedInstantSet = Boolean.FALSE, mSettingSymptomLog, mSettingIngredientLog;
    Instant mInstantConsumed, mInstant;
    LocalDateTime mNowDateTime = LocalDateTime.now(), mEarlierTodayDateTime, mYesterdayDateTime,
            mDayBeforeYesterdayDateTime;
    ArrayList<String> mSymptomLogIdsToAddStringArray, mIngredientLogIdStringArray, mLogIdToAddStringArray;
    ArrayList<IngredientLog> mIngredientLogArray;
    ArrayList<SymptomLog> mSymptomLogArray;
    LocalDateTime mDateTime;


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
        thisActivity = this.getActivity();
        // set the food log id to add time to the bundle
        mBundle = getArguments();
        if ( mBundle.containsKey(Util.ARGUMENT_CHANGE) ){
            mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
        }
        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
        questionTextView = view.findViewById(R.id.textview_question_log_date_time_choices_time);
        mSymptomLogArray = new ArrayList<>();
        mIngredientLogArray = new ArrayList<>();


        // find out if we have a food log or symptom log to set the date time of
        // if food log ID was given then set that
        if ( mBundle.containsKey(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) ) {
            //TODO make food logs multiple set array same as symptoms
            // change which fragment we'll go to next based on what we're changing
            //TODO coming from logpartofday too, make it work with which food log it has
//            if (mWhatToChange == Util.ARGUMENT_CHANGE_CONSUMED) {
//                // when consumed is done being set,
//                // we'll circle back around and ask user for acquired and cooked
//                mDefaultNextFragment = new LogDateTimeChoicesFragment();
//            } else if (mWhatToChange == Util.ARGUMENT_CHANGE_ACQUIRED) {
//                mDefaultNextFragment = new LogDateTimeChoicesFragment();
//            } else if (mWhatToChange == Util.ARGUMENT_CHANGE_COOKED) {
//                // after cooked is set go to the next info needed
//                mDefaultNextFragment = new NewIngredientLogBrandFragment();
//            }

            setDependentValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY),
                    Boolean.FALSE,  Boolean.TRUE,Util.fragmentContainerViewAddIngredientLog);


        } else if ( mBundle.containsKey(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) ) {

            Log.d(TAG, mBundle.toString());
            setDependentValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY), Boolean.TRUE,
                    Boolean.FALSE, Util.fragmentContainerViewAddSymptomLog);

        }

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

    private void setDependentValues(String logIdToAddString, Boolean settingSymptomLog,
                                    Boolean settingIngredientLog, int fragmentContainer){
        // one of these will be false and the other true
        mSettingSymptomLog = settingSymptomLog;
        mSettingIngredientLog = settingIngredientLog;
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;
        // parse the ids from the string into an array
        mLogIdToAddStringArray =
                Util.cleanBundledStringIntoArrayList(logIdToAddString);
        Log.d(TAG, logIdToAddString);
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
                isSymptomLogBeginInstantSet = Boolean.FALSE;
                isSymptomLogChangedInstantSet = Boolean.FALSE;
//                mTitleString = getResources().getString(R.string.title_log_begin);
            } else if ( mWhatToChange == Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED) {
                isSymptomLogBeginInstantSet = Boolean.TRUE;
//                mTitleString = getResources().getString(R.string.title_log_change);
            }

        }
        // get the date time of current log so we can set the default view with it
        mDateTime = Util.getDateTimeFromChange(mWhatToChange, mIngredientLog, mSymptomLog);


    }

    @Override
    public void onClick(View view) {

        Bundle mBundleNext = mBundle;
        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_date_time_choices_just_now:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_just_now),
                        Toast.LENGTH_SHORT).show();

                // they chose just now
                // if we've made it here, then the log has already been made
                // which means it's been set to the default
                if (mSettingSymptomLog){
                    // still set the instants because we need to check the user is fine with the
                    // default for ended/changed time

                    mBundleNext = Util.setSymptomLogInstants(mWhatToChange, mSymptomLogArray,
                            mSymptomLogViewModel, Util.setBundleFromLocalDateTime(mNowDateTime),
                            mBundleNext);
                    Util.startNextFragmentBundleChange(thisActivity,
                            getParentFragmentManager().beginTransaction(),
                            mFragmentContainer, new LogDateTimeChoicesFragment(), mBundleNext,
                            Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
                }
                else if (mSettingIngredientLog) {
                    // just now is the default when making a ingredient log
                    // so we don't need to ask anything else about time or day

                    //then set the values from the food log
                    setIngredientLogConsumedInstant(mEarlierTodayDateTime, null);

                    Util.startNextFragmentBundleChange(thisActivity, getParentFragmentManager().beginTransaction(),
                            mFragmentContainer, new NewIngredientAmountFragment(), mBundle,
                            Util.ARGUMENT_CHANGE_INGREDIENT_CONSUMED);
                }
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_earlier_today),
                        Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mEarlierTodayDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);

                if (mSettingSymptomLog){

                    mBundleNext = Util.setSymptomLogInstants(mWhatToChange, mSymptomLogArray,
                            mSymptomLogViewModel, Util.setBundleFromLocalDateTime(mNowDateTime),
                            mBundleNext);
                    Util.startNextFragmentBundleChange(thisActivity, getParentFragmentManager().beginTransaction(),
                            mFragmentContainer, new LogPartOfDayFragment(), mBundleNext,
                            Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
                }
                else if (mSettingIngredientLog) {
                    //then set the values from the food log
                    setIngredientLogConsumedInstant(mEarlierTodayDateTime, null);
                }
                break;

            case R.id.button_log_date_time_choices_yesterday:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_yesterday), Toast.LENGTH_SHORT).show();
                mYesterdayDateTime = LocalDateTime.now().minusDays(1);

                // if we're setting symptom log
                if (mSettingSymptomLog){

                    // set the date time the symptom began
                    mBundleNext = Util.setSymptomLogInstants(mWhatToChange, mSymptomLogArray,
                            mSymptomLogViewModel, Util.setBundleFromLocalDateTime(mNowDateTime),
                            mBundleNext);
                    Log.d(TAG, "in yesterday");
                    // if they clicked yesterday
                    // then it should set defaults to yesterday at same time as now
                    // but now go to part of day for real time
                    Util.startNextFragmentBundleChange(thisActivity, getParentFragmentManager().beginTransaction(),
                            mFragmentContainer, new LogPartOfDayFragment(), mBundleNext,
                            Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
                } else if (mSettingIngredientLog){
                    //then set the values from the food log
                    setIngredientLogConsumedInstant(mYesterdayDateTime, new LogPartOfDayFragment());
                }
                // and which fragment to go to next
                break;

            case R.id.button_log_date_time_choices_another_date:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_another_date),
                        Toast.LENGTH_SHORT).show();

                // and which fragment to go to next
                Util.startNextFragmentBundleChange(thisActivity, getParentFragmentManager().beginTransaction(),
                        mFragmentContainer, new LogSpecificDateTimeFragment(), mBundle, mWhatToChange);
                break;

            default:
                break;
        }//end switch case


    }//end onClick


    //set the begin instant
    // and from recent symptoms of same type, calculate average duration
    // and set the instant changed to that
    //TODO take the duration and the other useful thing out of here and into Util, the button
    // visibility
//    private void setSymptomInstants(LocalDateTime localDateTime, Fragment whereTo){
//        mInstant = Util.instantFromLocalDateTime(localDateTime);
//
//        // for each symptom log ID
//        for (String symptomLogIdString : mSymptomLogIdsToAddStringArray) {
//
//            // get symptom log from the list array
//            mCurrentSymptomLog =
//                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(
//                            symptomLogIdString
//                    ));
//            // check if this is the first run through of the fragment, i.e. should we set begin
//            // or when the symptom changed/ended
//            // if begin has already been set then we set the changed time
//            if (isSymptomLogBeginInstantSet) {
//                // we don't set begin because it already was
//                // so set the end instant
//                mCurrentSymptomLog.setInstantChanged(mInstant);
//            } else {
//                // begin has not been set yet, so set that first
//                mCurrentSymptomLog.setInstantBegan(mInstant);
//
//                // and then that means the end should be set to the default for now
//                // so find the average duration of the recent symptom logs of the same symptom
//                Duration averageDuration =
//                        mSymptomLogViewModel.viewModelGetAverageSymptomDuration(
//                                mCurrentSymptomLog.getSymptomLogSymptomId()
//                        );
//
//                // and set default for changed time to be from that average
//                mCurrentSymptomLog.setInstantChanged(
//                        Util.instantFromDurationAndStartInstant(mInstant,
//                        averageDuration)
//                );
//            }
//            // set the database with the updated symptom log time
//            mSymptomLogViewModel.viewModelUpdateSymptomLog(mCurrentSymptomLog);
//        }
//
//        // check again if this is first runthrough of fragment to see where to go now
//        if (isSymptomLogBeginInstantSet) {
//            // if we're in this fragment and begin has been set, we just set symptom changed
//            isSymptomLogChangedInstantSet = Boolean.TRUE;
//            // so we're done so go back to list symptoms log
//            //TODO when back in list symptom log from here
//            // make the newly added symptoms highlight new color so user can see it worked
//            // so they easily can click which ones to modify if they need to
//
//            Util.goToListSymptomLogActivity(null, thisActivity, mSymptomLogIdsToAddStringArray.toString());
//        } else {
//            // we're done setting begin, so we'll go to set symptom changed now
//            isSymptomLogBeginInstantSet = Boolean.TRUE;
//            // if they clicked yesterday
//            // then it should set defaults to yesterday at same time as now
//            // but now go to part of day for real time
//            if (!(mBundle.)) {
//                mSymptomLogIdsToAddStringArray
//                mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
//                goToNextFragment(whereTo);
//            } else {
//                mBundle.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_CHANGED);
//                // else they did not click yesterday and time has been set, so we need to set
//                // question and get another user input for when time changed
//                // reset question to ask about end then
//                questionTextView.setText(getResources().getString(R.string.question_textview_new_symptom_intensity_log_changed_time));
//                // if they did not choose yesterday or another date for begin
//                // future time for symptom ended doesn't make sense
//                // so make those buttons vanish
//                mButtonAnotherDate.setVisibility(View.INVISIBLE);
//                mButtonYesterday.setVisibility(View.INVISIBLE);
//            }
//        }
//
//
//    }

    //TODO replace this with the one in Util
    private void setIngredientLogConsumedInstant(LocalDateTime localDateTime, Fragment whereTo){
        mInstantConsumed = Util.instantFromLocalDateTime(localDateTime);
        //TODO make this work for repeating the datetime fragments for each consumed or cooked or
        // acquired
        mIngredientLog.setInstantConsumed(mInstantConsumed);
        mIngredientLog.setInstantCooked(mInstantConsumed);
        mIngredientLog.setInstantAcquired(mInstantConsumed);
        mIngredientLogViewModel.viewModelUpdateIngredientLog(mIngredientLog);

    }

}//end fragment
