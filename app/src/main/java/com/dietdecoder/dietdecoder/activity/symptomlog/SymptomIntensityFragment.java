package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.LogDateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SymptomIntensityFragment extends Fragment implements View.OnClickListener,
        NumberPicker.OnValueChangeListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);
    private Activity thisActivity;
    private Context thisContext;

    Button mButtonSaveName;
    EditText mEditTextSymptomIntensity;
    TextView mTextViewSymptomName;
    ListView mListView;
    private KeyListener originalKeyListener;
    NumberPicker mNumberPicker;

    String mSaveString, mEmptyTryAgainString, mName, symptomIntensityLogIdString, mCurrentSymptomLogIdToAddString,
    mCurrentSymptomName, mSymptomLogIdsToAddStringArrayOriginal,
            mCurrentSymptomLogIdString, mInvalidTryAgainString, mSymptomLogIdsToAddString,
            mSymptomLogIdsToAddOriginalString, mAllTimesAreSame, mWhatToChangeNext;
    String[] mDisplayedStringList;
    int[] mIntensityColorList;
    Boolean isIntensityViewEmpty;
    Integer mIntensitySelectedIndex, mHowManyIdsToAdd, mIntensitySelected,
    mIntensityOfMostRecentSymptomLogWithSameSymptomName, mCurrentIdIndex;
    UUID mSymptomLogId, mCurrentSymptomId;
    Color mCurrentIntensityColor, mPreviousIntensityColor, mNextIntensityColor;

    SymptomLog mCurrentSymptomLog;
    SymptomLogViewModel mSymptomLogViewModel;
    SymptomLogListAdapter mSymptomLogListAdapter;
    Symptom mSymptom;
    SymptomViewModel mSymptomViewModel;
    ArrayList<String> mSymptomLogIdsToAddStringArray;

    Fragment mNextFragment, mRepeatThisFragment, mDefaultNextFragment;
    Bundle mBundle, mBundleNext;


    public SymptomIntensityFragment() {
        super(R.layout.fragment_new_symptom_intensity_log);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_symptom_intensity_log, container,
                false);
        ConstraintLayout constraintLayout =
                view.findViewById(R.id.constraintLayout_new_symptom_intensity_log);

        thisContext = getContext();
        thisActivity = this.getActivity();

        // get the info about which symptom we're logging
        // check if info passed in exists, if not then go home
        if ( getArguments() == null ) {
            // there's no information about which symptom to add, so
            // tell the user that they got here by mistake, it's a bug
            // must choose which symptom before this activity
            String mWrongPlaceLetsGoHome =
                    getResources().getString(R.string.wrong_place_lets_go_home);
            Toast.makeText(thisContext, mWrongPlaceLetsGoHome,
                    Toast.LENGTH_SHORT).show();
            Util.goToListSymptomLogActivity(null, thisActivity, mSymptomLogIdsToAddString);
        } else {
            // get the info
            mBundle = getArguments();

            //set UI variables
            mTextViewSymptomName = view.findViewById(R.id.subsubtitle_textview_symptom_name);
            mButtonSaveName = view.findViewById(R.id.button_new_symptom_intensity_log_save);
            //activate watching the save button for a click
            mButtonSaveName.setOnClickListener(this);
            mNumberPicker = view.findViewById(R.id.numberpicker_new_symptom_intensity);

            // initialize variables
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            // this is our holder string, remove from this arraylist as we go
            mSymptomLogIdsToAddStringArray = new ArrayList<String>();
            // after intensity is done being set the next default place is date times
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
            // default next place to go should be the next fragment
            mNextFragment = mDefaultNextFragment;
            // repeating this fragment
            mRepeatThisFragment = new SymptomIntensityFragment();
            // make our next bundle have the same info that came in
            mBundleNext = mBundle;
            mBundleNext.putString(Util.ARGUMENT_FROM,
                    Util.ARGUMENT_FROM_INTENSITY_SYMPTOM);
            mWhatToChangeNext = Util.ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN;

            // to pass on to the time and date fragments, save the original untouched array string
            mSymptomLogIdsToAddString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
            //how many ids to add
            //TODO fix this lazy code
            if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                    Util.ARGUMENT_ACTION_EDIT) ) {
                mHowManyIdsToAdd = 1;
                mCurrentIdIndex = 0;
            } else {
                mHowManyIdsToAdd =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY));
                mCurrentIdIndex =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
            }

            // parse the name from the array of ids and put that in the textview
            // clean the string of its brackets and spaces
            // turn into array list
            mSymptomLogIdsToAddStringArray = Util.cleanBundledStringIntoArrayList(
                    mSymptomLogIdsToAddString
            );


            // set the current symptom
            mCurrentSymptomLogIdString = mSymptomLogIdsToAddStringArray.get(mCurrentIdIndex);
            setCurrentSymptomTextViewNumberPicker(mCurrentSymptomLogIdString);

            mCurrentSymptomLog = mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(
                    UUID.fromString(mCurrentSymptomLogIdString) );
            mCurrentSymptomId = mCurrentSymptomLog.getSymptomLogSymptomId();
            mSymptom =
                    mSymptomViewModel.viewModelGetSymptomFromId(mCurrentSymptomId);

            // then the name value
            mCurrentSymptomName = mSymptom.getSymptomName();

            // numbers to put in the number picker for how intense the symptom is
            mDisplayedStringList =
                    getResources().getStringArray(R.array.strings_one_to_ten);
            // colors for the backgrounds to differentiate the numbers easily
            mIntensityColorList =
                    getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);

            // set our default intensity to first in list if no symptom has been logged yet, and
            // set to most recent log with same symptom name if it exists
            mIntensitySelected = setIntensityDefault(mCurrentSymptomId);
            mIntensitySelectedIndex = mIntensitySelected - 1;


            // set our numberpicker with our string of 1 to 10 and with default value of the same
            // as most recent symptom log with the same symptom
            mNumberPicker = Util.setNumberPicker(mNumberPicker, mDisplayedStringList,
                    mIntensitySelected, mCurrentIntensityColor);
            mNumberPicker.setOnValueChangedListener(this);

            //mNumberPicker.setBackgroundColor(mIntensityColorList[0]);
            GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                gradient = setGradient(gradient, mIntensitySelected, mIntensityColorList);
            }
            mNumberPicker.setBackground(gradient);

            // TODO have delete this symptom button

            // TODO have progress bar on bottom for how many symptoms are to be set and have been
            //  set

        }

    // Inflate the layout for this fragment
    return view;
    }


    // change the name on the text view to new current symptom and the new default intensity value
    private void setCurrentSymptomTextViewNumberPicker(String paramSymptomLogIdToAddString){

        // get first log in the list array
        mCurrentSymptomLog = mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(
                UUID.fromString(paramSymptomLogIdToAddString) );

        // get the name and Id of the symptom in this symptom log
        mCurrentSymptomId = mCurrentSymptomLog.getSymptomLogSymptomId();
        Symptom mCurrentSymptom =
                mSymptomViewModel.viewModelGetSymptomFromId(mCurrentSymptomId);
        mCurrentSymptomName = mCurrentSymptom.getSymptomName();

        // put in the UI what symptom we're changing now
        mTextViewSymptomName.setText(mCurrentSymptomName);

        // also reset the default value on the number picker
        mIntensitySelected = setIntensityDefault(mCurrentSymptomId);
        mNumberPicker.setValue(mIntensitySelected);
    }

    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
        mInvalidTryAgainString =
                getResources().getString(R.string.invalid_input_not_int_between_one_and_ten_try_again);
        mAllTimesAreSame = getResources().getString(R.string.heads_up_all_times_same);

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_new_symptom_intensity_log_save:

                    // tell user we're saving it
                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();

                    // then set the intensity
                    mCurrentSymptomLog.setSymptomLogSymptomIntensity(mIntensitySelected);
                    // update the log
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(mCurrentSymptomLog);

                // will set done or unfinished if action means we're from edit
                    mBundleNext = Util.setDoneIfFromEdit(mBundle);

                String howManyInArrayString =
                        mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY);
                Integer howManyInArrayInteger = Integer.parseInt(howManyInArrayString);
                String currentIndexInArrayString =
                        mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY);
                Integer currentIndexInArrayInteger = Integer.parseInt(currentIndexInArrayString);

                // if that was the last one to add
                    if ( currentIndexInArrayInteger == 0) {
                        //TODO make times show up and modifiable back in add new logs if I want,
                        // probably don't need all these fragments, or make it a preference when
                        // they want to be asked when the symptom happened

                        // if there was more than one symptom,
                        if ( howManyInArrayInteger > 1) {
                            // alert user that all symptoms will have the same time and date,
                            // they can be individually edited from the symptom log menu
                            Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT).show();
                            //if the current index is zero we've gone through them all
                            // and if the number in array was more than one
                            // then we're done
                            Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                                    Util.fragmentContainerViewAddSymptomLog,
                                    mDefaultNextFragment, mBundleNext);
                        }


                            // go to date fragment to set when the symptom(s) happened
                            Util.startNextFragmentBundleChange(thisActivity,
                                    getParentFragmentManager().beginTransaction(),
                                    Util.fragmentContainerViewAddSymptomLog,
                                    mDefaultNextFragment, mBundleNext, mWhatToChangeNext);

                    }  else {
                        // not the last in the array, repeat this fragment

                        // and lower our count for how many left to add
                        mCurrentIdIndex = mCurrentIdIndex - 1;
                        mBundleNext.putString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY, String.valueOf(mCurrentIdIndex));

                        Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                                Util.fragmentContainerViewAddSymptomLog,
                                mRepeatThisFragment, mBundleNext);
                    }


                break;
//
            default:
                break;
        }
    }//end onClick


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // get the index of the spot in the array chosen
        int valuePicker = picker.getValue();
        // get the string value of the chosen intensity
        mIntensitySelected =  Integer.parseInt(mDisplayedStringList[valuePicker]);

        // set background color to change with new value
        GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gradient = setGradient(gradient, mIntensitySelected, mIntensityColorList);
        }
        picker.setBackground(gradient);
    }

    private Integer setIntensityDefault(UUID symptomId){
        //TODO fix intensity color gradient, it breaks with default is 10 I think

        // if there is no symptom log with same symptom name
        if ( Objects.isNull(mSymptomLogViewModel.viewModelGetMostRecentSymptomLogWithSymptom(
                symptomId ) )) {
            // set our integer to first in the list
            mIntensitySelected =
                    Integer.parseInt(mDisplayedStringList[0]);
        } else {
            // get the most recent intensity from most recent log and
            // set the default intensity to that
            mIntensityOfMostRecentSymptomLogWithSameSymptomName =
                    mSymptomLogViewModel.viewModelGetMostRecentSymptomLogWithSymptom(
                                    symptomId )
                            .getSymptomLogSymptomIntensity();

            // set our default choice to save intensity of to be the most recent value
            mIntensitySelected = mIntensityOfMostRecentSymptomLogWithSameSymptomName;
        }
        return mIntensitySelected;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private GradientDrawable setGradient(GradientDrawable gradient, Integer selectedInteger,
                                         int[] colorList){
        //TODO make this the default background color, white for now is fine
        int white = getResources().getColor(R.color.white, thisActivity.getTheme());
        int maxIndex = colorList.length-1;
        int selectedIndex = selectedInteger - 1;
        int previousIntensityColorInt = white, nextIntensityColorInt = white,
                currentIntensityColorInt = colorList[selectedIndex];
        Log.d(TAG, "maxIndex " + maxIndex);
        Log.d(TAG, "colorList[0] " + colorList[0]);
        Log.d(TAG, "colorList[1] " + colorList[1]);

        // set which color we're using
        if ( selectedIndex == 0 ) {
            // we're zero so let's leave it white to show top of numberpicker
            nextIntensityColorInt = colorList[selectedIndex + 1];
        } else if ( selectedIndex == maxIndex ) {
            // at bottom of picker so leave the next one white
            previousIntensityColorInt = colorList[selectedIndex - 1];
        } else {
            // somewhere in the middle so set both
            previousIntensityColorInt = colorList[selectedIndex - 1];
            nextIntensityColorInt = colorList[selectedIndex + 1];
        }
//        colorsToSet[0] = previousIntensityColorInt;
//        colorsToSet[1] = nextIntensityColorInt;
        int[] colorsToSet = new int[]{previousIntensityColorInt,currentIntensityColorInt,
                nextIntensityColorInt};

        gradient.setColors(colorsToSet);

        return gradient;
    }
}
