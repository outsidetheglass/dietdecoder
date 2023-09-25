package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.DateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.activity.SpecificDateTimeFragment;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
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
    TextView mTextViewSymptomName;
    NumberPicker mNumberPicker;

    String mSaveString, mEmptyTryAgainString,
    mCurrentSymptomName,
            mCurrentLogIdString, mInvalidTryAgainString, mLogIdsString,
            mAllTimesAreSame, mWhatToChangeNext;
    String[] mDisplayedStringList;
    int[] mIntensityColorList;
    int mIntensitySelectedIndex, mHowManyIds, mIntensitySelected, mCurrentIndex;
    UUID mCurrentLogId, mCurrentSymptomId;
    Color mCurrentIntensityColor;

    SymptomLog mCurrentSymptomLog;
    SymptomLogViewModel mSymptomLogViewModel;
    Symptom mCurrentSymptom;
    SymptomViewModel mSymptomViewModel;
    ArrayList<String> mLogIdsStringArray;

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
        Util.checkValidFragment(getArguments(), thisActivity);

        // get the info
        mBundle = getArguments();
        // make our next bundle have the same info that came in
        mBundleNext = Util.updateBundleGoToNext(mBundle);
        mBundleNext = Util.setBundleWhatToChangeNext(mBundleNext,
                null, Util.ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN);

        //set UI variables
        mTextViewSymptomName = view.findViewById(R.id.subsubtitle_textview_symptom_name);
        mButtonSaveName = view.findViewById(R.id.button_new_symptom_intensity_log_save);
        //activate watching the save button for a click
        mButtonSaveName.setOnClickListener(this);
        mNumberPicker = view.findViewById(R.id.numberpicker_new_symptom_intensity);

        // default is one before the symptom log logic finds differently
        mIntensitySelected = 1;

        // set the current symptom
        setDependentValues();

        // TODO have delete this symptom button

        // TODO have progress bar on bottom for how many symptoms are to be set and have been
        //  set



    // Inflate the layout for this fragment
    return view;
    }

    // set accessing the view model for symptom log
    private void setSymptomLogViewModel(){
        //TODO fix this mess, I moved the initialized variables from datetimechoices to a
        // separate method in here and then the ones from here's create view into this method and
        // now it's confused

        // we were given symptom log to use so set those values
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
        mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);

        // get how many times to repeat this fragment
        mHowManyIds =
                Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY));
        // parse array of ids
        mLogIdsStringArray = Util.cleanBundledStringIntoArrayList(
                mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY));
        // if there is an index in the bundle, set to that, or to 0
        mCurrentIndex = Util.setIntegerCurrentIndexFromBundle(mBundle);
        // get our current log's id
        mCurrentLogId = UUID.fromString(mLogIdsStringArray.get(mCurrentIndex));
        // use the index of which one we're currently working with on the given array
        mCurrentSymptomLog = mSymptomLogViewModel.viewModelGetLogFromLogId(
                mCurrentLogId);

        mCurrentSymptomId = mCurrentSymptomLog.getLogSymptomId();
        mCurrentSymptom =
                mSymptomViewModel.viewModelGetSymptomFromId(mCurrentSymptomId);
        mCurrentSymptomName = mCurrentSymptom.getName();


        setCurrentSymptomTextViewNumberPicker();
    }

    // set the values that differ depending on what to change for symptom log
    private void setDependentValues(){

        // initialize variables
        // after intensity is done being set the next default place is date times
        // TODO change back to date time choices and debug why earlier today isn't working
        //mDefaultNextFragment = new DateTimeChoicesFragment();
        mDefaultNextFragment = new SpecificDateTimeFragment();
        // default next place to go should be the next fragment
        mNextFragment = mDefaultNextFragment;
        // repeating this fragment
        mRepeatThisFragment = new SymptomIntensityFragment();


        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setSymptomLogViewModel();

        setNumberPickerUI();
        setCurrentSymptomTextViewNumberPicker();

    }

    // change the name on the text view to new current symptom and the new default intensity value
    private void setCurrentSymptomTextViewNumberPicker(){


        // put in the UI what symptom we're changing now
        mTextViewSymptomName.setText(mCurrentSymptomName);

        // also reset the default value on the number picker
        mIntensitySelected = setIntensityDefault(mCurrentSymptomId);
        mNumberPicker.setValue(mIntensitySelected);
    }


    private void setNumberPickerUI(){


        // numbers to put in the number picker for how intense the symptom is
        mDisplayedStringList =
                getResources().getStringArray(R.array.strings_one_to_ten);
        // colors for the backgrounds to differentiate the numbers easily
        mIntensityColorList =
                getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);

        // set our default intensity to first in list if no symptom has been logged yet, and
        // set to most recent log with same symptom name if it exists
        mIntensitySelected = setIntensityDefault(mCurrentSymptomId);


        // set our numberpicker with our string of 1 to 10 and with default value of the same
        // as most recent symptom log with the same symptom
        mNumberPicker = Util.setNumberPickerIntegers(mNumberPicker, mDisplayedStringList,
                mIntensitySelected, mCurrentIntensityColor);
        mNumberPicker.setOnValueChangedListener(this);

        //mNumberPicker.setBackgroundColor(mIntensityColorList[0]);
        GradientDrawable gradient =
                (GradientDrawable) getResources().getDrawable(R.drawable.gradient, thisActivity.getTheme());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gradient = setGradient(gradient, mIntensitySelected, mIntensityColorList);
        }
        //Log.d(TAG, " gradient " + gradient.toString());
        mNumberPicker.setBackground(gradient);

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
                mCurrentSymptomLog.setLogSymptomIntensity(mIntensitySelected);
                // update the log
                mSymptomLogViewModel.viewModelUpdate(mCurrentSymptomLog);

                // will set done or unfinished if action means we're from edit
                mBundleNext = Util.setDoneIfFromEdit(mBundle);
                //Log.d(TAG, mBundle.toString());
                if ( Util.isFromEdit(mBundle) ){
                    Log.d(TAG, mBundle.toString());
                    Util.goToEditSymptomLogActivity(thisContext, thisActivity, mCurrentLogIdString);
                }


                // if that was the last one to add
                    if ( mCurrentIndex == 0) {
                        //TODO make times show up and modifiable back in add new logs if I want,
                        // probably don't need all these fragments, or make it a preference when
                        // they want to be asked when the symptom happened

                        // if there was more than one symptom,
                        if ( mHowManyIds > 1) {
                            // alert user that all symptoms will have the same time and date,
                            // they can be individually edited from the symptom log menu

                            //Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT)

                            // .show();
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
                        mCurrentIndex = mCurrentIndex - 1;
                        mBundleNext.putString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY,
                                String.valueOf(mCurrentIndex));

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
        mIntensitySelected = valuePicker+1;

        // set background color to change with new value
        GradientDrawable gradient = (GradientDrawable) getResources()
                .getDrawable(R.drawable.gradient, thisContext.getTheme());
        //Log.d(TAG, String.valueOf(mIntensitySelected));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gradient = setGradient(gradient, mIntensitySelected, mIntensityColorList);
        }
        picker.setBackground(gradient);
    }

    private Integer setIntensityDefault(UUID symptomId){
        //TODO fix intensity color gradient, it breaks with default is 10 I think

        SymptomLog mostRecentSymptomLogWithSameSymptom =
                mSymptomLogViewModel.viewModelGetMostRecentLogWithSymptom(
                symptomId );

        // if there is no symptom log with same symptom
        if ( Objects.isNull(mostRecentSymptomLogWithSameSymptom) ) {
            // set our integer to first in the list
            mIntensitySelected = 1;
           // Log.d(TAG, mSymptomLogViewModel.toString());
        } else {
            // get the most recent intensity from most recent log
            // and set our default choice to to be that most recent value
            mIntensitySelected = mostRecentSymptomLogWithSameSymptom.getLogSymptomIntensity();
           // Log.d(TAG, String.valueOf(mIntensitySelected));
        }

        //mIntensitySelected = mIntensitySelected - 1;
        return mIntensitySelected;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private GradientDrawable setGradient(GradientDrawable gradient, Integer selectedInteger,
                                         int[] colorList){
        //TODO make this the default background color, white for now is fine
        int white = getResources().getColor(R.color.white, thisActivity.getTheme());
        int maxIndex = colorList.length -1;
        int selectedIndex = selectedInteger;
        //int selectedIndex = selectedInteger-1;
        int previousIntensityColorInt = white;
        int nextIntensityColorInt = white;
        int currentIntensityColorInt = 0;
       // Log.d(TAG, "setGradient: " + String.valueOf(selectedIndex));
//        Log.d(TAG, "maxIndex should be 9: " + maxIndex);
//        Log.d(TAG, "selectedInteger should start at 1: " + selectedInteger);
//        for (int str : colorList){
//            Log.d(TAG, "colorList " + str);
//        }

        // set which color we're using
        if ( selectedIndex <= 1 ) {
            // we're zero so let's leave it white to show top of numberpicker
            nextIntensityColorInt = colorList[selectedIndex + 1];
            currentIntensityColorInt = colorList[selectedIndex];
        } else if ( selectedIndex >= maxIndex ) {
            // at bottom of picker so leave the next one white
            previousIntensityColorInt = colorList[selectedIndex - 1];
            currentIntensityColorInt = colorList[selectedIndex-1];
        } else {
            // somewhere in the middle so set both
//            previousIntensityColorInt = colorList[selectedIndex - 1];
//            nextIntensityColorInt = colorList[selectedIndex + 1];
//            currentIntensityColorInt = colorList[selectedIndex];

            previousIntensityColorInt = colorList[selectedIndex - 2];
            nextIntensityColorInt = colorList[selectedIndex];
            currentIntensityColorInt = colorList[selectedIndex-1];
        }
//        Log.d(TAG, "previousIntensityColorInt " + previousIntensityColorInt);
//        Log.d(TAG, "currentIntensityColorInt " + currentIntensityColorInt);
//        Log.d(TAG, "nextIntensityColorInt " + nextIntensityColorInt);

//        colorsToSet[0] = previousIntensityColorInt;
//        colorsToSet[1] = nextIntensityColorInt;
        int[] colorsToSet = new int[]{previousIntensityColorInt,currentIntensityColorInt,
                nextIntensityColorInt};

        gradient.setColors(colorsToSet);

        return gradient;
    }
}
