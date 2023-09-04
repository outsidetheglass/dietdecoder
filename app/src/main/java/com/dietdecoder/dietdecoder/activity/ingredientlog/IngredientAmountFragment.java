package com.dietdecoder.dietdecoder.activity.ingredientlog;

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
import com.dietdecoder.dietdecoder.activity.DateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.activity.SpecificDateTimeFragment;
import com.dietdecoder.dietdecoder.activity.symptomlog.SymptomIntensityFragment;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class IngredientAmountFragment extends Fragment implements View.OnClickListener,
        NumberPicker.OnValueChangeListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);
    private Activity thisActivity;
    private Context thisContext;

    Button mButtonSaveName;
    TextView mTextViewIngredientName;
    NumberPicker mNumberPicker;

    String mSaveString, mEmptyTryAgainString,
            mCurrentIngredientName, mInvalidTryAgainString,
            mCurrentAmount, mAllTimesAreSame, mWhatToChangeNext;
    String[] mDisplayedStringList;
    int[] mAmountColorList;
    int mAmountSelectedIndex;
    int mHowManyIds;
    String mAmountSelected;
    int mCurrentIndex;

    UUID mCurrentLogId, mCurrentIngredientId;
    Color mCurrentAmountColor, mPreviousAmountColor, mNextAmountColor;

    IngredientLog mCurrentIngredientLog;
    IngredientLogViewModel mIngredientLogViewModel;
    IngredientLogListAdapter mIngredientLogListAdapter;
    Ingredient mCurrentIngredient;
    IngredientViewModel mIngredientViewModel;
    ArrayList<String> mLogIdsStringArray;

    Fragment mNextFragment, mRepeatThisFragment, mDefaultNextFragment;
    Bundle mBundle, mBundleNext;


    public IngredientAmountFragment() {
        super(R.layout.fragment_ingredient_amount_log);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_amount_log, container,
                false);
        ConstraintLayout constraintLayout =
                view.findViewById(R.id.constraintLayout_ingredient_amount_log);

        thisContext = getContext();
        thisActivity = this.getActivity();

        // get only va/id info about which ingredient we're logging
        // check if info passed in exists, if not then go home
        Util.checkValidFragment(getArguments(), thisActivity);

        // get the info
        mBundle = getArguments();
        mBundleNext = Util.updateBundleGoToNext(mBundle);
        mBundleNext = Util.setBundleWhatToChangeNext(mBundleNext,
                null, Util.ARGUMENT_CHANGE_INGREDIENT_LOG_ALL_INSTANTS);

        Log.d(TAG, mBundle.toString());



        //set UI variables
        mTextViewIngredientName = view.findViewById(R.id.subsubtitle_textview_ingredient_name);
        mButtonSaveName = view.findViewById(R.id.button_ingredient_amount_log_save);
        //activate watching the save button for a click
        mButtonSaveName.setOnClickListener(this);
        mNumberPicker = view.findViewById(R.id.numberpicker_ingredient_amount);


        setDependentValues();
//
//        // initialize variables
//        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
//        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
//        // this is our holder string, remove from this arraylist as we go
//        mIngredientLogIdsStringArray = new ArrayList<String>();
//        // after amount is done being set the next default place is date times
//        mDefaultNextFragment = new DateTimeChoicesFragment();
//        // default next place to go should be the next fragment
//        mNextFragment = mDefaultNextFragment;
//        // repeating this fragment
//        mRepeatThisFragment =
//                new IngredientAmountFragment();
//        // make our next bundle have the same info that came in
//        mBundleNext = mBundle;
//        mBundle = Util.checkValidFragment(getArguments(), thisActivity);
//        mBundleNext = Util.updateBundleGoToNext(mBundle);
//        mBundleNext.putString(Util.ARGUMENT_FROM,
//                Util.ARGUMENT_FROM_INGREDIENT_AMOUNT_FRAGMENT);
//        mWhatToChangeNext = Util.ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED;
//
//        // to pass on to the time and date fragments, save the original untouched array string
//        mIngredientLogIdsString = mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY);
//        //how many ids to add
//        //TODO fix this lazy code
//        if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
//                Util.ARGUMENT_ACTION_EDIT) ) {
//            mHowManyIds = 1;
//            mCurrentIdIndex = 0;
//            mIngredientLogIdsString = Util.cleanArrayString(mIngredientLogIdsString);
//            mIngredientLogIdsStringArray.add(mIngredientLogIdsString);
//        } else {
//            mHowManyIds =
//                    Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY));
//            mCurrentIdIndex =
//                    Integer.parseInt(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
//            // parse the name from the array of ids and put that in the textview
//            // clean the string of its brackets and spaces
//            // turn into array list
//            mIngredientLogIdsStringArray = Util.cleanBundledStringIntoArrayList(
//                    mIngredientLogIdsString
//            );
//        }
//
//        Log.d(TAG, "mCurrentIdIndex " + mCurrentIdIndex);
//        Log.d(TAG, "mIngredientLogIdsString " + mIngredientLogIdsString.toString());
//
//
//        Log.d(TAG, "mIngredientLogIdsStringArray " + mIngredientLogIdsStringArray.toString());
//        // set the current ingredient
//        mCurrentIngredientLogIdString = mIngredientLogIdsStringArray.get(mCurrentIdIndex);
//        setCurrentIngredientTextViewNumberPicker(mCurrentIngredientLogIdString);
//
//        mCurrentIngredientLog = mIngredientLogViewModel.viewModelGetLogFromLogId(
//                UUID.fromString(mCurrentIngredientLogIdString) );
//        mCurrentIngredientId = mCurrentIngredientLog.getLogIngredientId();
//        mIngredient =
//                mIngredientViewModel.viewModelGetFromId(mCurrentIngredientId);
//
//        // then the name value
//        mCurrentIngredientName = mIngredient.getName();
//
//        // numbers to put in the number picker for how intense the ingredient is
//        // colors for the backgrounds to differentiate the numbers easily
//        mDisplayedStringList =
//                getResources().getStringArray(R.array.strings_amounts);
//        mAmountColorList =
//                getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);
//
//        // set our default amount to first in list if no ingredient has been logged yet, and
//        // set to most recent log with same ingredient name if it exists
//        //TODO fix default set to figure out what the index of the other log's value is
////            mAmountSelected = setAmountDefaultIndex(mCurrentIngredientId);
//        mAmountSelectedIndex = 0;
//
//
//        // set our numberpicker with our string of 1 to 10 and with default value of the same
//        // as most recent ingredient log with the same ingredient
//        mNumberPicker = Util.setNumberPickerStrings(mNumberPicker, mDisplayedStringList,
//                mAmountSelectedIndex, mCurrentAmountColor);
//        mNumberPicker.setOnValueChangedListener(this);
//
//        //mNumberPicker.setBackgroundColor(mAmountColorList[0]);
//        GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            gradient = setGradient(gradient, mAmountSelectedIndex, mAmountColorList);
//        }
//        mNumberPicker.setBackground(gradient);
//
//        // TODO have delete this ingredient button
//
//        // TODO have progress bar on bottom for how many ingredients are to be set and have been


        // Inflate the layout for this fragment
        return view;
    }

    // set accessing the view model for symptom log
    private void setIngredientLogViewModel(){
        //TODO fix this mess, I moved the initialized variables from datetimechoices to a
        // separate method in here and then the ones from here's create view into this method and
        // now it's confused

        // we were given symptom log to use so set those values
        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        // get how many times to repeat this fragment
        mHowManyIds =
                Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY));
        // parse array of ids
        mLogIdsStringArray = Util.cleanBundledStringIntoArrayList(
                mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY));
        // if there is an index in the bundle, set to that, or to 0
        mCurrentIndex = Util.setIntegerCurrentIndexFromBundle(mBundle);
        // get our current log's id
        mCurrentLogId = UUID.fromString(Util.cleanArrayString(
                mLogIdsStringArray.get(mCurrentIndex) ));
        // use the index of which one we're currently working with on the given array
        mCurrentIngredientLog = mIngredientLogViewModel.viewModelGetLogFromLogId(
                mCurrentLogId);

        mCurrentIngredientId = mCurrentIngredientLog.getLogIngredientId();
        mCurrentIngredient =
                mIngredientViewModel.viewModelGetFromId(mCurrentIngredientId);
        mCurrentIngredientName = mCurrentIngredient.getName();
        mCurrentAmount = mCurrentIngredientLog.getLogIngredientSubjectiveAmount();


        setCurrentIngredientTextViewNumberPicker();
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
        mRepeatThisFragment = new IngredientAmountFragment();


        // this has to come after setObjectArrayValues,
        // it uses a value set in there, mCurrentLogId
        setIngredientLogViewModel();

        setNumberPickerUI();
        setCurrentIngredientTextViewNumberPicker();

    }


    // change the name on the text view to new current ingredient and the new default amount
    // value
    private void setCurrentIngredientTextViewNumberPicker(){

        // put in the UI what ingredient we're changing now
        mTextViewIngredientName.setText(mCurrentIngredientName);

        // also reset the default value on the number picker
        mAmountSelectedIndex = setAmountDefaultIndex(mCurrentIngredientId);
        mNumberPicker.setValue(mAmountSelectedIndex);
    }

    private void setNumberPickerUI(){


        // numbers to put in the number picker for how intense the symptom is
        // colors for the backgrounds to differentiate the numbers easily
        mAmountColorList =
                getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);
        mDisplayedStringList =
                getResources().getStringArray(R.array.strings_amounts);

        // set our default intensity to first in list if no symptom has been logged yet, and
        // set to most recent log with same symptom name if it exists
        mAmountSelectedIndex = setAmountDefault(mCurrentIngredientId);


        // set our numberpicker with our string of 1 to 10 and with default value of the same
        // as most recent symptom log with the same symptom
        mNumberPicker = Util.setNumberPickerStrings(mNumberPicker, mDisplayedStringList,
                mAmountSelectedIndex, mCurrentAmountColor);
        mNumberPicker.setOnValueChangedListener(this);

        //mNumberPicker.setBackgroundColor(mIntensityColorList[0]);
        GradientDrawable gradient =
                (GradientDrawable) getResources().getDrawable(R.drawable.gradient, thisActivity.getTheme());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gradient = setGradient(gradient, mAmountSelectedIndex, mAmountColorList);
        }
        //Log.d(TAG, " gradient " + gradient.toString());
        mNumberPicker.setBackground(gradient);

    }

    private Integer setAmountDefault(UUID ingredientId){
        //TODO fix intensity color gradient, it breaks with default is 10 I think

        // if there is no symptom log with same symptom
        if ( Objects.isNull(mIngredientLogViewModel.viewModelGetMostRecentLogWithIngredient(
                ingredientId ) )) {
            // set our integer to first in the list
            mAmountSelectedIndex = 0;
            // Log.d(TAG, mSymptomLogViewModel.toString());
        } else {
            // get the most recent intensity from most recent log
            // and set our default choice to to be that most recent value
            mAmountSelected = mIngredientLogViewModel.viewModelGetMostRecentLogWithIngredient(
                            ingredientId )
                    .getLogIngredientSubjectiveAmount();

            // find the index matching the subjective amount so the color is right
            int findIndex = 0;
            for (String string:mDisplayedStringList
                 ) {
                if (Objects.equals(string, mAmountSelected)){
                    mAmountSelectedIndex = findIndex;
                } else {
                    findIndex++;
                }

            }
            // Log.d(TAG, String.valueOf(mAmountSelectedIndex));
        }

        return mAmountSelectedIndex;
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
//            case R.id.edittext_ingredient_amount:
//
//                break;
            case R.id.button_ingredient_amount_log_save:

                //TODO debug, when editing this isn't saving, and the default isn't working
                // either, the color is off and auto selecting tons and making it red, while
                // showing the text almost nothing

                // tell user we're saving it
                Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();

                // then set the amount
//                mAmountSelected = mDisplayedStringList[mAmountSelectedIndex+1];
                mCurrentIngredientLog.setLogIngredientSubjectiveAmount(mAmountSelected);
                // update the log
                mIngredientLogViewModel.viewModelUpdate(mCurrentIngredientLog);

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
                    // they want to be asked when the ingredient happened

                    // if there was more than one ingredient,
                    if ( howManyInArrayInteger > 1) {
                        // alert user that all ingredients will have the same time and date,
                        // they can be individually edited from the ingredient log menu
                        Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT).show();
                        //if the current index is zero we've gone through them all
                        // and if the number in array was more than one
                        // then we're done
                        Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                                Util.fragmentContainerViewAddIngredientLog,
                                mDefaultNextFragment, mBundleNext);
                    }


                    // go to date fragment to set when the ingredient(s) happened
                    Util.startNextFragmentBundleChange(thisActivity,
                            getParentFragmentManager().beginTransaction(),
                            Util.fragmentContainerViewAddIngredientLog,
                            mDefaultNextFragment, mBundleNext, mWhatToChangeNext);

                }  else {
                    // not the last in the array, repeat this fragment

                    // and lower our count for how many left to add
                    mCurrentIndex = mCurrentIndex - 1;
                    mBundleNext.putString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY, String.valueOf(mCurrentIndex));

                    Util.startNextFragmentBundle(thisActivity, getParentFragmentManager().beginTransaction(),
                            Util.fragmentContainerViewAddIngredientLog,
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
        // get the string value of the chosen amount
//        mAmountSelected =  Integer.parseInt(mDisplayedStringList[valuePicker]);
        mAmountSelected =  mDisplayedStringList[valuePicker];

        // set background color to change with new value
        GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gradient = setGradient(gradient, valuePicker,
                    mAmountColorList);
        }
        picker.setBackground(gradient);
    }

    private Integer setAmountDefaultIndex(UUID ingredientId){
        //TODO fix amount color gradient, it breaks with default is 10 I think

        mAmountSelectedIndex = 0;
        // if there is no ingredient log with same ingredient name
        if ( Objects.isNull(mIngredientLogViewModel.viewModelGetMostRecentLogWithIngredient(
                ingredientId ) )) {
            // set our integer to first in the list
//            mAmountSelected = mDisplayedStringList[0];
        } else {
            // get the most recent amount from most recent log and
            // set the default amount to that
//            mAmountOfMostRecentIngredientLogWithSameIngredientName =
//                    mIngredientLogViewModel.viewModelGetMostRecentIngredientLogWithIngredient(
//                                    ingredientId )
//                            .getIngredientLogIngredientSubjectiveAmount();
//
//            // set our default choice to save amount of to be the most recent value
//            mAmountSelectedIndex = mAmountOfMostRecentIngredientLogWithSameIngredientName;
        }
        return mAmountSelectedIndex;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private GradientDrawable setGradient(GradientDrawable gradient, Integer selectedInteger,
                                         int[] colorList){
        //TODO make this the default background color, white for now is fine
        int white = getResources().getColor(R.color.white, thisActivity.getTheme());
        int maxIndex = colorList.length-1;
        int selectedIndex = selectedInteger;
        int previousAmountColorInt = white, nextAmountColorInt = white,
                currentAmountColorInt = colorList[selectedIndex];
        Log.d(TAG, "maxIndex " + maxIndex);
        Log.d(TAG, "colorList[0] " + colorList[0]);
        Log.d(TAG, "colorList[1] " + colorList[1]);

        // set which color we're using
        if ( selectedIndex == 0 ) {
            // we're zero so let's leave it white to show top of numberpicker
            nextAmountColorInt = colorList[selectedIndex + 1];
        } else if ( selectedIndex == maxIndex ) {
            // at bottom of picker so leave the next one white
            previousAmountColorInt = colorList[selectedIndex - 1];
        } else {
            // somewhere in the middle so set both
            previousAmountColorInt = colorList[selectedIndex - 1];
            nextAmountColorInt = colorList[selectedIndex + 1];
        }
//        colorsToSet[0] = previousAmountColorInt;
//        colorsToSet[1] = nextAmountColorInt;
        int[] colorsToSet = new int[]{previousAmountColorInt,currentAmountColorInt,
                nextAmountColorInt};

        gradient.setColors(colorsToSet);

        return gradient;
    }
}

