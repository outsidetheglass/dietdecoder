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
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class DateTimeChoicesFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;

    Button mButtonJustNow, mButtonEarlierToday, mButtonAnotherDate, mButtonYesterday;
    TextView questionTextView;

    // TODO set this to be a preference
    Integer hoursEarlierInt = 4;

    Bundle mBundle, mBundleNext, mBundleNextJustNow, mBundleNextJustNowSymptomLog;
    int mFragmentContainer;

    IngredientLogViewModel mIngredientLogViewModel = null;
    SymptomLogViewModel mSymptomLogViewModel = null;
    IngredientViewModel mIngredientViewModel = null;
    SymptomViewModel mSymptomViewModel = null;
    IngredientLog mIngredientLog = null;
    SymptomLog mSymptomLog = null;
    Symptom mSymptom = null;

    String mQuestionTextViewString = null;
    String mCurrentLogIdString, mWhatToChange, mWhatToChangeJustNow, mWhatToChangeEarlierToday;
    UUID mCurrentLogId;

    Fragment mNextFragmentJustNow, mNextFragmentEarlierToday, mNextFragment = null,
            mRepeatThisFragment, mNextFragmentAnotherDate, mNextFragmentYesterday;
    Class mDefaultNextActivityJustNow, mDefaultNextActivity;

    Boolean mSettingSymptomLog, mSettingIngredientLog, mMoveToNextWhatToChange = Boolean.FALSE,
            mMoveToNextWhatToChangeAnotherDate = Boolean.FALSE, mMoveToNextWhatToChangeEarlierToday =
            Boolean.FALSE, mMoveToNextWhatToChangeJustNow = Boolean.FALSE,
            mMoveToNextWhatToChangeYesterday = Boolean.FALSE;
    Instant mInstantConsumed, mInstant;
    Integer mCurrentLogIdIndex;
    LocalDateTime mDateTimeNow, mDateTimeEarlierToday, mDateTimeYesterday;
    ArrayList<String> mSymptomLogIdsToAddStringArray, mIngredientLogIdStringArray, mLogIdStringArray;
    ArrayList<IngredientLog> mIngredientLogArray;
    ArrayList<SymptomLog> mSymptomLogArray;
    LocalDateTime mDateTime;


    public DateTimeChoicesFragment() {
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
        // check this fragment has all that it needs and go back to list or edit if we don't
        // if we got past the valid fragment check then we can grab our values
        mBundle = Util.checkValidFragment(getArguments(), thisActivity);
        // update our from to be this fragment
        mBundleNext = Util.updateBundleGoToNext(mBundle);
        // we do have a specific time date to change so let's set what we do next
        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
        mWhatToChangeJustNow = mWhatToChange;

        questionTextView = view.findViewById(R.id.textview_question_log_date_time_choices_time);

        // could set this to next fragment but no matter what the default next one is, repeat
        // should be this fragment only
        mRepeatThisFragment = new DateTimeChoicesFragment();
        // set the default next fragment to be repeating this fragment
        // since next is usually a start time to an end time or other time values
        mNextFragment = mRepeatThisFragment;

        // set just now's what to change and bundle to same as default
        // set datetime to now
        // next fragment is null because symptom log if either is clicked we're going home
        // if it's ingredient log then it's go to word input/brand but that isn't made yet
        mBundleNextJustNow = mBundle;

        // set all the values for ingredient log and symptom log
        setAllValues();

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


    // set the index, string, array, and UUID for the current log
    private void setObjectArrayValues(String logIdsString){

        // if there is an index in the bundle, set to that, or to 0
        mCurrentLogIdIndex = Util.setIntegerCurrentIndexFromBundle(mBundle);
        // set our array of ID's, even if it's just one
        mLogIdStringArray = Util.cleanBundledStringIntoArrayList(logIdsString);
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

        mSymptom = mSymptomViewModel.viewModelGetSymptomFromId(mSymptomLog.getLogSymptomId());
        // for each string in array update that log's instant began
        // get the log associated with each UUID
        mSymptomLogArray = Util.setSymptomLogArrayFromStringArray(mLogIdStringArray,
                mSymptomLogViewModel);

    }

    // set which fragment container we're using and the question and visibility of optional button
    private void setDependentUI(int fragmentContainer, String questionString){
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;

        // set the question asked to the user
        questionTextView.setText(questionString);
    }

    // set the values that differ depending on what to change for ingredient log
    private void setIngredientLogDependentValues(){
        // set the needed values for ingredient log
        setObjectArrayValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setIngredientLogViewModel();

        // change the question that's set to ask the user based on which instant we're changing
        String questionStringIfConsumed =
                getResources().getString(R.string.question_textview_new_log);
        String questionStringIfCooked =
                getResources().getString(R.string.question_textview_new_ingredient_log_cooked_time);
        String questionStringIfAcquired =
                getResources().getString(R.string.question_textview_new_ingredient_log_acquired_time);
        // no bundle needed since it's just one string answer
        mQuestionTextViewString = Util.setIngredientLogStringFromChangeInstant(mWhatToChange,
                questionStringIfConsumed, questionStringIfCooked, questionStringIfAcquired
        );

        //TODO get brand working
//            mNextFragmentJustNow = Util.setFragmentFromChangeInstant(mWhatToChange,
//                    null, null, new IngredientLogBrandFragment()
//            );


        // fragmentContainer, questionStringInt
        setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                mQuestionTextViewString);
        //nextFragment
        setWhatToChangeValues(new PartOfDayFragment());

    }

    // set the values that differ depending on what to change for symptom log
    private void setSymptomLogDependentValues(){
        // set the needed values for ingredient log
        setObjectArrayValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setSymptomLogViewModel();

        // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
        setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                mQuestionTextViewString);

        // if just now is picked then symptom begin and changed/ended can all be set to now
        // but if it was changed and clicked on just now, that doesn't mean set begin, so
        // it's only if it was originally begin to make it all instants
        String questionStringIfBegin = getResources().getString(R.string.question_textview_new_symptom_log_begin_time);
        String questionStringIfChanged = getResources().getString(R.string.question_textview_new_symptom_log_changed_time);

        // along with our question string defaults
        // for just now, if begin was clicked then set both it and changed/ended: ALL_INSTANTS
        // if changed/ended was clicked then set only it and leave begin alone
        Bundle questionJustNowBundle =
                Util.setQuestionStringWhatToChangeJustNow( mWhatToChange,
                        questionStringIfBegin, questionStringIfChanged,
                        Util.ARGUMENT_CHANGE_SYMPTOM_LOG_ALL_INSTANTS,
                        Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED, mSymptom.getName() );
        // set the question and what to change based on which of the instant options
        mQuestionTextViewString = questionJustNowBundle.getString(Util.ARGUMENT_QUESTION);

        // set that we'll be done if just now is clicked, since for both begin and changed
        // they'd set their time to be now and be done
        mWhatToChangeJustNow = questionJustNowBundle.getString(Util.ARGUMENT_CHANGE_JUST_NOW);
        // we need what to change just now because if it was clicked, then we can't ask for a
        // future time when it changed/ended, so we'll just set that to now too and be done


//        if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ){
//        }
//        else if ( Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange) ) {
//        }

    }

    // set the values that differ depending on what to change
    private void setWhatToChangeValues(Fragment nextFragment){
        // TODO update this to work for just now vs yesterday etc
        mNextFragment = nextFragment;
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

        questionTextView.setText( mQuestionTextViewString);
    }


    @Override
    public void onClick(View view) {

        //TODO add logic for if the time selected is valid based on from edit and other relevant
        // values

        switch (view.getId()) {
            // which button was clicked
            case R.id.button_log_date_time_choices_just_now:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_just_now),
                        Toast.LENGTH_SHORT).show();

                // just now means now date and time and the what to change that varies for which
                // object
                mDateTime = LocalDateTime.now();
//                mBundleNext = mBundleNextJustNow;
//                mBundleNext.putString(Util.ARGUMENT_DONE_OR_UNFINISHED, Util.ARGUMENT_DONE);
                mBundleNext = Util.setDone(mBundleNext);
                mNextFragment = null;
                mWhatToChange = mWhatToChangeJustNow;
                mMoveToNextWhatToChange =  Boolean.TRUE;
                break;

            case R.id.button_log_date_time_choices_earlier_today:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_earlier_today),
                        Toast.LENGTH_SHORT).show();
                // set times to be earlier today, time set automatically
                mDateTime = LocalDateTime.now().minusHours(hoursEarlierInt);
                mNextFragment = new PartOfDayFragment();
                mMoveToNextWhatToChange = Boolean.FALSE;
                break;

            case R.id.button_log_date_time_choices_yesterday:
                // if they clicked yesterday
                Toast.makeText(getContext(), getResources().getString(R.string.toast_yesterday), Toast.LENGTH_SHORT).show();
                // then it should set defaults to yesterday at same time as now
                mDateTime = LocalDateTime.now().minusDays(1);
                // part of day yesterday
                mNextFragment = new PartOfDayFragment();
                mMoveToNextWhatToChange = Boolean.FALSE;

                break;

            case R.id.button_log_date_time_choices_another_date:
                Toast.makeText(getContext(), getResources().getString(R.string.toast_another_date),
                        Toast.LENGTH_SHORT).show();
                // given no specific info yet we can't set anything, so just go there
                mNextFragment = new SpecificDateTimeFragment();
                mMoveToNextWhatToChange = Boolean.FALSE;
                break;

            default:
                break;
        }//end switch case

//        Log.d(TAG, " \n\nmBundleNext before set:\n " + mBundleNext.toString());
        // we're setting log
        // mWhatToChange is what's being set right now, so if it's consumed, then consumed time
        // date is what gets set
        // mBundleNext is what gets changed to the next one, so if it's begin symptom time, it
        // changes to changed/ended symptom time
        mBundleNext = Util.setLogInstants(mWhatToChange,
                mIngredientLogArray, mSymptomLogArray,
                mIngredientLogViewModel, mSymptomLogViewModel,
                mIngredientViewModel, mSymptomViewModel, mDateTime, mBundleNext,
                mMoveToNextWhatToChange);

//        Log.d(TAG, " \nmBundleNext after set: \n" + mBundleNext.toString());

        // set our bundle to say it's going to the fragment we're about to go to
//        mBundleNext = Util.setGoToFromNextFragment(mBundleNext, mNextFragment);
//
//        Log.d(TAG, " \nafter set next fragment \n" + mBundleNext.toString());
        // set the data to pass along info
        // given from the previous fragment
        // or the duplicated ID if we did that
//        mNextFragment.setArguments(mBundleNext);
//        getParentFragmentManager().beginTransaction()
//                .replace(Util.fragmentContainerViewAddSymptomLog,
//                        mNextFragment)
//                .setReorderingAllowed(true)
//                .addToBackStack(null)
//                .commit();
        // and which fragment to go to next
        Util.startNextFragmentBundle(thisActivity,
                getParentFragmentManager().beginTransaction(),
                mFragmentContainer, mNextFragment, mBundleNext);

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
