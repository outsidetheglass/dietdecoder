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
import java.util.UUID;

public class LogDateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;

    Button mButtonJustNow, mButtonEarlierToday, mButtonAnotherDate, mButtonYesterday;
    TextView questionTextView;

    // TODO set this to be a preference
    Integer hoursEarlierInt = 4;

    Bundle mBundle, mBundleNext, mBundleNextJustNow;
    int mFragmentContainer;

    IngredientLogViewModel mIngredientLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    IngredientViewModel mIngredientViewModel;
    SymptomViewModel mSymptomViewModel;
    IngredientLog mIngredientLog;
    SymptomLog mSymptomLog;

    String mCurrentLogIdString, mWhatToChange, mWhatToChangeJustNow, mEarlierTodayWhatToChange;
    UUID mCurrentLogId;

    Fragment mDefaultNextFragment, mDefaultNextFragmentJustNow;
    Class mDefaultNextActivityJustNow, mDefaultNextActivity;

    Boolean mSettingSymptomLog, mSettingIngredientLog;
    Instant mInstantConsumed, mInstant;
    Integer mCurrentLogIndex;
    LocalDateTime mNowDateTime = LocalDateTime.now(), mEarlierTodayDateTime, mYesterdayDateTime,
            mDayBeforeYesterdayDateTime;
    ArrayList<String> mSymptomLogIdsToAddStringArray, mIngredientLogIdStringArray, mLogIdStringArray;
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
        if ( getArguments()==null ){
            Util.goToMainActivity(null, thisActivity);
        } else {
            mBundle = getArguments();
            mBundleNext = mBundle;
            mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);

            questionTextView = view.findViewById(R.id.textview_question_log_date_time_choices_time);

            // one of these will be false and the other true
            mSettingIngredientLog = Util.isIngredientLogBundle(mBundle);
            mSettingSymptomLog = Util.isSymptomLogBundle(mBundle);

            // find out if we have a food log or symptom log to set the date time of
            // if food log ID was given then set that
            if (mSettingIngredientLog) {
                setDependentValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY),
                         Util.fragmentContainerViewAddIngredientLog);

            } else if (mSettingSymptomLog) {
                setDependentValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY),
                        Util.fragmentContainerViewAddSymptomLog);
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

        }
    }//end onViewCreated

    private void setDependentValues(String logIdToAddString, int fragmentContainer){

        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;

        // parse the ids from the string into an array
        mLogIdStringArray =
                Util.cleanBundledStringIntoArrayList(logIdToAddString);
        mCurrentLogIndex =
                Integer.parseInt(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
        mCurrentLogIdString = mLogIdStringArray.get(mCurrentLogIndex);
        mCurrentLogId = UUID.fromString(mCurrentLogIdString);
        if ( mSettingIngredientLog ){
            mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
            mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
            mIngredientLogArray = new ArrayList<>();

            mSymptomLogViewModel = null;
            mSymptomLogArray = null;
            mSymptomLog = null;

            // now get the info associated with that UUID
            mIngredientLog =
                    mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mCurrentLogId);

            // set the fragment to each of the instant options
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
            mIngredientLogArray = Util.setIngredientLogArrayFromStringArray(mLogIdStringArray,
                    mIngredientLogViewModel);

            // if just now is picked then we're still only modifying one at a time
            mWhatToChangeJustNow = mWhatToChange;
            // default what to change won't change for ingredient if earlier today is chosen
            mEarlierTodayWhatToChange = mWhatToChange;


            questionTextView.setText(Util.setIngredientLogStringFromChangeInstant(mWhatToChange,
                    String.valueOf(questionTextView.getText()),
                    String.valueOf(R.string.question_textview_new_ingredient_log_cooked_time),
                    String.valueOf(R.string.question_textview_new_ingredient_log_acquired_time)
            ) );

            //TODO get brand working
//            mDefaultNextFragmentJustNow = Util.setFragmentFromChangeInstant(mWhatToChange,
//                    null, null, new IngredientLogBrandFragment()
//            );

        } else if ( mSettingSymptomLog ){
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            mSymptomLogArray = new ArrayList<>();
            mIngredientLogViewModel = null;
            mIngredientLog = null;
            mIngredientLogArray = null;

            // now get the info associated with that UUID
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(mCurrentLogIdString));


            mDefaultNextFragment = new LogDateTimeChoicesFragment();


            mSymptomLogArray = Util.setSymptomLogArrayFromStringArray(mLogIdStringArray,
                    mSymptomLogViewModel);

            // if just now is clicked,
            // for symptom log we're done so then return to list or edit
            mDefaultNextFragmentJustNow = null;
            mBundleNextJustNow = Util.setDone(mBundle);

            // if earlier today for symptom begin
            // then we need part of day is fragment for changed
            // and update what to change is symptom changed/ended now
            // it doesn't hurt to set symptom changed to itself again
            mEarlierTodayWhatToChange = Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED;



            // if just now is picked then symptom begin and changed/ended can all be set to now
            // but if it was changed and clicked on just now, that doesn't mean set begin, so
            // it's only if it was originally begin to make it all instants
            String[] symptomLogStringsFromChangeInstant = Util.setSymptomLogStringFromChangeInstant(mWhatToChange,
                    String.valueOf(R.string.question_textview_new_symptom_log_begin_time),
                    String.valueOf(R.string.question_textview_new_symptom_log_changed_time),
                    Util.ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS,
                    Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED
            );
            String questionText = symptomLogStringsFromChangeInstant[0];
            // set the fragment to each of the instant options
            mWhatToChangeJustNow = symptomLogStringsFromChangeInstant[1];

            questionTextView.setText(questionText);

        }

        // get the date time of current log so we can set the default view with it
        mDateTime = Util.getDateTimeFromChange(mWhatToChange, mIngredientLog, mSymptomLog);


    }

    @Override
    public void onClick(View view) {

        //TODO add logic for if the time selected is valid based on from edit and other relevant
        // values

        Bundle mBundleNext = mBundle;
        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_date_time_choices_just_now:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_just_now),
                        Toast.LENGTH_SHORT).show();

                // just now means now date and time and the what to change that varies for which
                // object
                mWhatToChange = mWhatToChangeJustNow;
                mDateTime = mNowDateTime;
                mDefaultNextActivity = mDefaultNextActivityJustNow;
                mBundleNext = mBundleNextJustNow;
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_earlier_today),
                        Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mEarlierTodayDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);
                mDateTime = mEarlierTodayDateTime;
                mWhatToChange = mEarlierTodayWhatToChange;

                break;

            case R.id.button_log_date_time_choices_yesterday:
                // if they clicked yesterday
                Toast.makeText(getContext(), getResources().getString(R.string.toast_yesterday), Toast.LENGTH_SHORT).show();
                // then it should set defaults to yesterday at same time as now
                mYesterdayDateTime = LocalDateTime.now().minusDays(1);
                mDateTime = mYesterdayDateTime;

                break;

            case R.id.button_log_date_time_choices_another_date:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_another_date),
                        Toast.LENGTH_SHORT).show();
                mDefaultNextFragment = new LogSpecificDateTimeFragment();
                break;

            default:
                break;
        }//end switch case

        // we're setting log
        mBundleNext = Util.setLogInstants(mWhatToChange,
                mIngredientLogArray, mSymptomLogArray,
                mIngredientLogViewModel, mSymptomLogViewModel,
                mIngredientViewModel, mSymptomViewModel, mDateTime, mBundleNext);

        // and which fragment to go to next
        Util.startNextFragmentBundle(thisActivity,
                getParentFragmentManager().beginTransaction(),
                mFragmentContainer, mDefaultNextFragment, mBundleNext);

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

}//end fragment
