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
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class NewSymptomIntensityFragment extends Fragment implements View.OnClickListener,
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
            mSymptomLogIdsToAddOriginalString, mAllTimesAreSame;
    String[] mDisplayedStringList;
    int[] mIntensityColorList;
    Boolean isIntensityViewEmpty;
    Integer mIntensitySelectedIndex, mHowManyIdsToAdd, mIntensitySelected, mIntensityOfMostRecentSymptomLogWithSameSymptomName;
    UUID mSymptomLogId;
    Color mCurrentIntensityColor, mPreviousIntensityColor, mNextIntensityColor;

    SymptomLog mCurrentSymptomLog;
    SymptomLogViewModel mSymptomLogViewModel;
    SymptomLogListAdapter mSymptomLogListAdapter;
    ArrayList<String> mSymptomLogIdsToAddStringArray;

    Fragment mNextFragment, mRepeatThisFragment, mDefaultNextFragment;
    Bundle mBundle, mBundleNext;


    public NewSymptomIntensityFragment() {
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
            Util.goToListSymptomLog(thisActivity);
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
            // this is our holder string, remove from this arraylist as we go
            mSymptomLogIdsToAddStringArray = new ArrayList<String>();
            // after intensity is done being set the next default place is date times
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
            // default next place to go should be the next fragment
            mNextFragment = mDefaultNextFragment;
            // repeating this fragment
            mRepeatThisFragment = new NewSymptomIntensityFragment();
            // make our next bundle have the same info that came in
            mBundleNext = mBundle;

            // to pass on to the time and date fragments, save the original untouched array string
            mSymptomLogIdsToAddString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
            //how many ids to add
            //TODO fix this lazy code
            if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                    Util.ARGUMENT_ACTION_EDIT) ) {
                mHowManyIdsToAdd = 1;
                mSymptomLogIdsToAddStringArrayOriginal = mSymptomLogIdsToAddString;
            } else {
                mSymptomLogIdsToAddStringArrayOriginal =
                        mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_ORIGINAL);
                mHowManyIdsToAdd =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_SYMPTOM_LOG_ID_IN_ARRAY));
            }

            // parse the name from the array of ids and put that in the textview
            // clean the string of its brackets and spaces
            // turn into array list
            mSymptomLogIdsToAddStringArray = Util.cleanBundledStringIntoArrayList(
                    mSymptomLogIdsToAddString
            );

            // set the current symptom to be the first
            mCurrentSymptomLogIdString = mSymptomLogIdsToAddStringArray.get(0);
            setCurrentSymptomTextViewNumberPicker(mCurrentSymptomLogIdString);

            mCurrentSymptomLog = mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(
                    UUID.fromString(mCurrentSymptomLogIdString) );
            mCurrentSymptomName = mCurrentSymptomLog.getSymptomLogSymptomName();

            // numbers to put in the number picker for how intense the symptom is
            mDisplayedStringList =
                    getResources().getStringArray(R.array.strings_one_to_ten);
            // colors for the backgrounds to differentiate the numbers easily
            mIntensityColorList =
                    getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);

            // set our default intensity to first in list if no symptom has been logged yet, and
            // set to most recent log with same symptom name if it exists
            mIntensitySelected = setIntensityDefault(mCurrentSymptomName);
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
        UUID uuid = UUID.fromString(paramSymptomLogIdToAddString);
        mCurrentSymptomLog = mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(uuid);
        mCurrentSymptomName = mCurrentSymptomLog.getSymptomLogSymptomName();

        // put in the UI what symptom we're changing now
        mTextViewSymptomName.setText(mCurrentSymptomName);

        // also reset the default value on the number picker
        mIntensitySelected = setIntensityDefault(mCurrentSymptomName);
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

                    // we've added it in, so remove this symptom from the ones to add
                    mSymptomLogIdsToAddStringArray.remove(mCurrentSymptomLog.getSymptomLogId().toString());
                    // and lower our count for how many left to add
                    mHowManyIdsToAdd = mHowManyIdsToAdd - 1;

                    // if this is the last intensity to check,
                    // checked by if the number of Ids left to add logs of is bigger than 0
                    if ( mHowManyIdsToAdd == 0 ) {
                        //TODO make times show up and modifiable back in add new logs if I want,
                        // probably don't need all these fragments, or make it a preference when
                        // they want to be asked when the symptom happened

                        // go to date fragment to set when the symptom(s) happened
                        mNextFragment = mDefaultNextFragment;
                    } else {
                        // not the last in the array, repeat this fragment

                        mNextFragment = mRepeatThisFragment;
                        // actually go to the next place now
                    }
                    goToNextFragment(mNextFragment);


                break;
//
            default:
                break;
        }
    }//end onClick


    private void goToNextFragment(Fragment nextFragment){
        // and now which fragment to go to next

        // if nothing given use the default fragment
        if ( Objects.isNull(nextFragment)) {
            // so just go home
            nextFragment = mDefaultNextFragment;
        } else {
            // we were given a fragment to use
            // let's set all the info the next fragment will need

            // if this is not the last intensity to check
            if ( mHowManyIdsToAdd > 0 ) {
                // if there's still logs left to change intensity of
                // add the current log IDs left to add into the bundle
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY,
                        mSymptomLogIdsToAddStringArray.toString());
                // and also add the original full array
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_ORIGINAL,
                        mSymptomLogIdsToAddStringArrayOriginal);
                mBundleNext.putString(Util.ARGUMENT_HOW_MANY_SYMPTOM_LOG_ID_IN_ARRAY,
                        mHowManyIdsToAdd.toString());
                // we're still changing intensity
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_INTENSITY);
            } else {
                // there are no intensities left to set
                // add the original array as our array
                // the rest are all set together so it doesn't need to be a changing array, so we
                // don't need original and mutable array both, just need one
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY,
                    mSymptomLogIdsToAddStringArrayOriginal);
                // reset to setting begin time date for the next fragment
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN);

                // if there's more than one symptom,
                // checked by if original string has a comma,
                // alert user that all symptoms will have the same time and date,
                // they can be individually edited from the symptom log menu
                if ( mSymptomLogIdsToAddStringArrayOriginal.contains(",")) {
                    Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT).show();
                }
            }

            mBundleNext.putString(Util.ARGUMENT_FRAGMENT_FROM,
                    Util.ARGUMENT_FROM_INTENSITY_SYMPTOM);
            // put which we're changing into the bundle
            nextFragment.setArguments(mBundleNext);
        }

        // actually go to the next place now
        //TODO move this into Util
        if ( TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION), Util.ARGUMENT_ACTION_EDIT)){
            Util.goToEditActivityActionTypeId(null, thisActivity, Util.ARGUMENT_ACTION_EDIT,
                    Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, mCurrentSymptomLogIdString);
        } else {
            Util.startNextFragment(getParentFragmentManager().beginTransaction(),
                    Util.fragmentContainerViewAddSymptomLog, nextFragment);
        }
    }


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

    private Integer setIntensityDefault(String mCurrentSymptomName){
        // if there is no symptom log with same symptom name
        if ( Objects.isNull(mSymptomLogViewModel.viewModelGetMostRecentSymptomLogWithSymptom(
                mCurrentSymptomName ) )) {
            // set our integer to first in the list
            mIntensitySelected =
                    Integer.parseInt(mDisplayedStringList[0]);
        } else {
            // get the most recent intensity from most recent log and
            // set the default intensity to that
            mIntensityOfMostRecentSymptomLogWithSameSymptomName =
                    mSymptomLogViewModel.viewModelGetMostRecentSymptomLogWithSymptom(
                                    mCurrentSymptomName )
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
