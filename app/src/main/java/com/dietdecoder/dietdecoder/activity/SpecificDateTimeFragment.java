package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class SpecificDateTimeFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private Activity thisActivity;
    //Log.d(TAG, "onClick: mDatePickerDateTime = " + mDatePickerDateTime.getHour());

    Button mDateButtonSave;
    DatePicker mDatePicker;
    EditText mEditTextTime, mEditTextDate;
    TextView titleTextView, mTextViewTimePicker;

    Bundle mBundle, mBundleNext, mBundleNextSave;
    IngredientLogViewModel mIngredientLogViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    IngredientLog mIngredientLog;
    SymptomLog mSymptomLog;

    IngredientViewModel mIngredientViewModel;
    SymptomViewModel mSymptomViewModel;
    Integer mHour, mMinute, mDay, mMonth, mYear, mCurrentLogIdIndex;
    UUID mCurrentLogId;
    String mIngredientLogIdString, mOnlySetDateOrTime, mWhatToChange, mWhereFrom, mLogIdString,
            mCurrentLogIdString, mTitleString;
    int mFragmentContainer, hour, minute;
    Instant mInstant, mInstantSet;
    LocalDateTime mDatePickerDateTime, mDateTime, mDateTimeSet;
    Boolean mGoBackToEdit, mSettingIngredientLog = Boolean.FALSE, mSettingSymptomLog =
            Boolean.FALSE,
            isSymptomLogBeginInstantSet = Boolean.FALSE, isSymptomLogChangedInstantSet =
            Boolean.FALSE;
    ArrayList<String> mLogIdStringArray;
    ArrayList<SymptomLog> mSymptomLogArray;
    ArrayList<IngredientLog> mIngredientLogArray;

    // IDs for which dialog to open
    static final int TIME_DIALOG_ID = 1111;
    static final int DATE_DIALOG_ID = 1112;
    Fragment mDefaultNextFragment;


    public SpecificDateTimeFragment() {
        super(R.layout.fragment_log_specific_date_time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_specific_date_time, container, false);

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        thisActivity = getActivity();
        // if there was data passed use that to get date time to change, else use current or go
        // back to list log activity
        mBundle = Util.checkValidFragment(getArguments(), thisActivity);
        mBundleNextSave = Util.updateBundleGoToNext(mBundle);

        //TODO finish setting this up for food vs symptom
        titleTextView = view.findViewById(R.id.textview_title_log_specific_date);

        // set the listener on the save button
        mDateButtonSave = view.findViewById(R.id.button_log_specific_date_save);
        mDateButtonSave.setOnClickListener(this);

        // set the time and date edit texts
        mEditTextTime = view.findViewById(R.id.edittext_log_specific_date_time);
        // set a listener on it to open the picker
        // to run onClick method when they are clicked
        mEditTextTime.setOnClickListener(this);
        // do the same for date
        mEditTextDate = view.findViewById(R.id.edittext_log_specific_date_date);
        mEditTextDate.setOnClickListener(this);


        // get info about what date time we're starting from
        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

        mWhatToChange = mBundle.getString(Util.ARGUMENT_CHANGE);
        // if we came from edit this will tell us
        mGoBackToEdit = Util.setGoToEditFromBundle(mBundle);

        // find out if we have a ingredient log or symptom log to set the date time of
        setAllValues();

        // Current date and time
        mHour = mDateTime.getHour();
        mMinute = mDateTime.getMinute();
        mDay = mDateTime.getDayOfMonth();
        mMonth = mDateTime.getMonthValue()-1;
        mYear = mDateTime.getYear();

        // set time and date
        mEditTextTime.setText(Util.setTimeString(mHour, mMinute));
        mEditTextDate.setText(Util.setDateString(mDay, mMonth, mYear));

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
                mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mCurrentLogId);

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
                mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(mCurrentLogIdString));

        // for each string in array update that log's instant began
        // get the log associated with each UUID
        mSymptomLogArray = Util.setSymptomLogArrayFromStringArray(mLogIdStringArray,
                mSymptomLogViewModel);

    }

    // set which fragment container we're using and the question and visibility of optional button
    private void setDependentUI(int fragmentContainer, int questionStringInt){
        // when we have to replace the fragment container, replace the right one
        mFragmentContainer = fragmentContainer;

        // set the question asked to the user
        titleTextView.setText(questionStringInt);
    }

    // set the values that differ depending on what to change for ingredient log
    private void setIngredientLogDependentValues(){
        // set the needed values for ingredient log
        setObjectArrayValues(mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setIngredientLogViewModel();


        // if consumed hasn't been set yet, then that's what we're changing
        if ( Util.isIngredientLogConsumed(mWhatToChange) ){
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                    R.string.title_log_consumed);
            //nextFragment, instantToCheck
            setWhatToChangeValues(new DateTimeChoicesFragment());
        }
        else if ( Util.isIngredientLogCooked(mWhatToChange) ) {
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                    R.string.title_log_cooked);
//          mDefaultNextFragment = new IngredientLogBrandFragment();
            //nextFragment, instantToCheck
            setWhatToChangeValues(new DateTimeChoicesFragment());
        }
        else if ( Util.isIngredientLogAcquired(mWhatToChange) ) {

            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddIngredientLog,
                    R.string.title_log_acquired);

//                mDefaultNextFragment = new IngredientLogBrandFragment();
            //nextFragment, instantToCheck
            setWhatToChangeValues(null);
        }
    }

    // set the values that differ depending on what to change for symptom log
    private void setSymptomLogDependentValues(){
        // set the needed values for ingredient log
        setObjectArrayValues(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setSymptomLogViewModel();

        // if consumed hasn't been set yet, then that's what we're changing
        if ( Util.isSymptomLogWhatToChangeSetToBegin(mWhatToChange) ){
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddSymptomLog,
                    R.string.title_log_begin);
            //nextFragment, instantToCheck
            setWhatToChangeValues(new DateTimeChoicesFragment());
        }
        else if ( Util.isSymptomLogWhatToChangeSetToChanged(mWhatToChange) ) {
            // fragmentContainer, questionStringInt, allDayButtonVisibleOrNot
            setDependentUI(Util.fragmentContainerViewAddSymptomLog,
                    R.string.title_log_change);
            //nextFragment, instantToCheck
            setWhatToChangeValues(null);
            // done so lets go home
            mBundleNextSave = Util.setDoneIfFromEdit(mBundleNext);
        }

    }

    // set the values that differ depending on what to change
    private void setWhatToChangeValues(Fragment nextFragment){
        mDefaultNextFragment = nextFragment;
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

    }


    private void setViewModelObjectArray(Class logViewModelClass){
        // if ingredient log view model was given then set those values
        if ( logViewModelClass == IngredientLogViewModel.class ) {
            mIngredientLogViewModel = (IngredientLogViewModel) new ViewModelProvider(this).get(logViewModelClass);
            mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
            mIngredientLogArray = new ArrayList<>();

            // now get the info associated with that UUID
            mIngredientLog =
                    mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mCurrentLogId);


            mIngredientLogArray = Util.setIngredientLogArrayFromStringArray(mLogIdStringArray,
                    mIngredientLogViewModel);
        }
        // we were given symptom log to use so set those values
        else if ( logViewModelClass == SymptomLogViewModel.class){
            mSymptomLogViewModel = (SymptomLogViewModel) new ViewModelProvider(this).get(logViewModelClass);
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            mSymptomLogArray = new ArrayList<>();

            // now get the info associated with that UUID
            mSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(mCurrentLogIdString));

            mSymptomLogArray = Util.setSymptomLogArrayFromStringArray(mLogIdStringArray,
                    mSymptomLogViewModel);
        }
        else {
            Log.d(TAG, "Neither symptom log nor ingredient log were given for the object to set a" +
                    " date time to.");
        }
    }


    // create time picker dialog
    public Dialog createDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set to true for getting 24 hour time or not
                // if switched, also switch the pretty string print for set Time in Util
                Boolean setTo24HourTime = Boolean.TRUE;

                // set time picker as given time
                return new TimePickerDialog(getActivity(), timePickerListener, mHour, mMinute,
                    setTo24HourTime);

            case DATE_DIALOG_ID:

                // set date picker as given date
                return new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth,
                        mDay);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            mHour = hour;
            mMinute = minute;
            mEditTextTime.setText(Util.setTimeString(hour, minute));
        }

    };

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            mYear = year;
            mMonth = month;
            mDay = day;
            mEditTextDate.setText(Util.setDateString(day, month, year));
        }

    };


    //
    @Override
    public void onClick(View view) {
//
        // set our relevant data to use in new location
        mBundleNext = Util.setBundleLogToNextInstant(mBundle);

//        //int id = view.getId();
        switch (view.getId()) {
            // which button was clicked

            case R.id.edittext_log_specific_date_time:
                Toast.makeText(getContext(), getResources().getString(R.string.pick_time),
                        Toast.LENGTH_SHORT).show();
                createDialog(TIME_DIALOG_ID).show();

                break;
            case R.id.edittext_log_specific_date_date:
                createDialog(DATE_DIALOG_ID).show();

                break;
            case R.id.button_log_specific_date_save:
                Toast.makeText(getContext(), getResources().getString(R.string.saving),
                        Toast.LENGTH_SHORT).show();

                // set our bundle to what should happen when it's saved
                mBundleNext = mBundleNextSave;
                // if we're saving and it's a valid value, we're done so lets set to move to next
                Boolean moveToNextWhatToChange = Boolean.TRUE;
                // get the date and time user picked and put them in a bundle and set log with them
                mBundleNext = Util.setLogInstants(mWhatToChange,
                        mIngredientLogArray, mSymptomLogArray,
                        mIngredientLogViewModel, mSymptomLogViewModel,
                        mIngredientViewModel, mSymptomViewModel,
                        Util.localDateTimeFromInstant(
                                Util.instantFromValues(mMinute, mHour, mDay, mMonth, mYear) ),
                        mBundleNext, moveToNextWhatToChange);

                // this does check if from edit and done, then will go to list or e
                Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                        mFragmentContainer,
                        mDefaultNextFragment, mBundleNext);

                // if next fragment has been set use that
//                if ( !TextUtils.isEmpty(mOnlySetDateOrTime) ) {
//                    // put which we're changing into the bundle
//                    LogPartOfDayFragment logPartOfDayFragment = new LogPartOfDayFragment();
//                    logPartOfDayFragment.setArguments(mBundleNext);
//                    // actually go to the next place now
//                    getParentFragmentManager().beginTransaction()
//                            .replace(Util.fragmentContainerViewEditIngredientLog,
//                            logPartOfDayFragment)
//                            .setReorderingAllowed(true)
//                            .addToBackStack(null)
//                            .commit();
//                }
                // else it means go back to main activity or edit
                //else {
                //}


                break;

            default:
                break;
        }//end switch case

    }//end onClick


}//end fragment
