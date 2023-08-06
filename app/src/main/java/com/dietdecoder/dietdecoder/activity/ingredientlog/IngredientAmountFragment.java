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
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;

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
    EditText mEditTextIngredientAmount;
    TextView mTextViewIngredientName;
    ListView mListView;
    private KeyListener originalKeyListener;
    NumberPicker mNumberPicker;

    String mSaveString, mEmptyTryAgainString, mName, ingredientAmountLogIdString,
            mCurrentIngredientLogIdToAddString,
            mCurrentIngredientName, mIngredientLogIdsStringArrayOriginal,
            mCurrentIngredientLogIdString, mInvalidTryAgainString, mIngredientLogIdsString,
            mIngredientLogIdsOriginalString, mAllTimesAreSame, mWhatToChangeNext,
 mAmountSelected, mAmountOfMostRecentIngredientLogWithSameIngredientName;
    String[] mDisplayedStringList;
    int[] mAmountColorList;
    Boolean isAmountViewEmpty;
    Integer mAmountSelectedIndex, mHowManyIds
            , mCurrentIdIndex;
    UUID mIngredientLogId, mCurrentIngredientId;
    Color mCurrentAmountColor, mPreviousAmountColor, mNextAmountColor;

    IngredientLog mCurrentIngredientLog;
    IngredientLogViewModel mIngredientLogViewModel;
    IngredientLogListAdapter mIngredientLogListAdapter;
    Ingredient mIngredient;
    IngredientViewModel mIngredientViewModel;
    ArrayList<String> mIngredientLogIdsStringArray;

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

        // get the info about which ingredient we're logging
        // check if info passed in exists, if not then go home
        if ( getArguments() == null ) {
            // there's no information about which ingredient to add, so
            // tell the user that they got here by mistake, it's a bug
            // must choose which ingredient before this activity
            String mWrongPlaceLetsGoHome =
                    getResources().getString(R.string.wrong_place_lets_go_home);
            Toast.makeText(thisContext, mWrongPlaceLetsGoHome,
                    Toast.LENGTH_SHORT).show();
            Util.goToListIngredientLogActivity(null, thisActivity, mIngredientLogIdsString);
        } else {
            // get the info
            mBundle = getArguments();

            Log.d(TAG, mBundle.toString());
            //set UI variables
            mTextViewIngredientName = view.findViewById(R.id.subsubtitle_textview_ingredient_name);
            mButtonSaveName = view.findViewById(R.id.button_ingredient_amount_log_save);
            //activate watching the save button for a click
            mButtonSaveName.setOnClickListener(this);
            mNumberPicker = view.findViewById(R.id.numberpicker_ingredient_amount);

            // initialize variables
            mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
            mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
            // this is our holder string, remove from this arraylist as we go
            mIngredientLogIdsStringArray = new ArrayList<String>();
            // after amount is done being set the next default place is date times
            mDefaultNextFragment = new DateTimeChoicesFragment();
            // default next place to go should be the next fragment
            mNextFragment = mDefaultNextFragment;
            // repeating this fragment
            mRepeatThisFragment =
                    new IngredientAmountFragment();
            // make our next bundle have the same info that came in
            mBundleNext = mBundle;
            mBundle = Util.checkValidFragment(getArguments(), thisActivity);
            mBundleNext = Util.updateBundleGoToNext(mBundle);
            mBundleNext.putString(Util.ARGUMENT_FROM,
                    Util.ARGUMENT_FROM_INGREDIENT_AMOUNT_FRAGMENT);
            mWhatToChangeNext = Util.ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED;

            // to pass on to the time and date fragments, save the original untouched array string
            mIngredientLogIdsString = mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY);
            //how many ids to add
            //TODO fix this lazy code
            if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                    Util.ARGUMENT_ACTION_EDIT) ) {
                mHowManyIds = 1;
                mCurrentIdIndex = 0;
                mIngredientLogIdsString = Util.cleanArrayString(mIngredientLogIdsString);
                mIngredientLogIdsStringArray.add(mIngredientLogIdsString);
            } else {
                mHowManyIds =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY));
                mCurrentIdIndex =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
                // parse the name from the array of ids and put that in the textview
                // clean the string of its brackets and spaces
                // turn into array list
                mIngredientLogIdsStringArray = Util.cleanBundledStringIntoArrayList(
                        mIngredientLogIdsString
                );
            }

            Log.d(TAG, "mCurrentIdIndex " + mCurrentIdIndex);
            Log.d(TAG, "mIngredientLogIdsString " + mIngredientLogIdsString.toString());


            Log.d(TAG, "mIngredientLogIdsStringArray " + mIngredientLogIdsStringArray.toString());
            // set the current ingredient
            mCurrentIngredientLogIdString = mIngredientLogIdsStringArray.get(mCurrentIdIndex);
            setCurrentIngredientTextViewNumberPicker(mCurrentIngredientLogIdString);

            mCurrentIngredientLog = mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                    UUID.fromString(mCurrentIngredientLogIdString) );
            mCurrentIngredientId = mCurrentIngredientLog.getIngredientLogIngredientId();
            mIngredient =
                    mIngredientViewModel.viewModelGetIngredientFromId(mCurrentIngredientId);

            // then the name value
            mCurrentIngredientName = mIngredient.getIngredientName();

            // numbers to put in the number picker for how intense the ingredient is
            // colors for the backgrounds to differentiate the numbers easily
            mDisplayedStringList =
                    getResources().getStringArray(R.array.strings_amounts);
            mAmountColorList =
                    getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);

            // set our default amount to first in list if no ingredient has been logged yet, and
            // set to most recent log with same ingredient name if it exists
            //TODO fix default set to figure out what the index of the other log's value is
//            mAmountSelected = setAmountDefaultIndex(mCurrentIngredientId);
            mAmountSelectedIndex = 0;


            // set our numberpicker with our string of 1 to 10 and with default value of the same
            // as most recent ingredient log with the same ingredient
            mNumberPicker = Util.setNumberPickerStrings(mNumberPicker, mDisplayedStringList,
                    mAmountSelectedIndex, mCurrentAmountColor);
            mNumberPicker.setOnValueChangedListener(this);

            //mNumberPicker.setBackgroundColor(mAmountColorList[0]);
            GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                gradient = setGradient(gradient, mAmountSelectedIndex, mAmountColorList);
            }
            mNumberPicker.setBackground(gradient);

            // TODO have delete this ingredient button

            // TODO have progress bar on bottom for how many ingredients are to be set and have been
            //  set

        }

        // Inflate the layout for this fragment
        return view;
    }


    // change the name on the text view to new current ingredient and the new default amount
    // value
    private void setCurrentIngredientTextViewNumberPicker(String paramIngredientLogIdToAddString){

        // get first log in the list array
        mCurrentIngredientLog = mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(
                UUID.fromString(paramIngredientLogIdToAddString) );

        // get the name and Id of the ingredient in this ingredient log
        mCurrentIngredientId = mCurrentIngredientLog.getIngredientLogIngredientId();
        Ingredient mCurrentIngredient =
                mIngredientViewModel.viewModelGetIngredientFromId(mCurrentIngredientId);
        mCurrentIngredientName = mCurrentIngredient.getIngredientName();

        // put in the UI what ingredient we're changing now
        mTextViewIngredientName.setText(mCurrentIngredientName);

        // also reset the default value on the number picker
        //TODO fix default
//        mAmountSelected = setAmountDefaultIndex(mCurrentIngredientId);
        mAmountSelectedIndex = setAmountDefaultIndex(mCurrentIngredientId);
        mNumberPicker.setValue(mAmountSelectedIndex);
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

                // tell user we're saving it
                Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();

                // then set the amount
//                mAmountSelected = mDisplayedStringList[mAmountSelectedIndex+1];
                mCurrentIngredientLog.setIngredientLogIngredientSubjectiveAmount(mAmountSelected);
                // update the log
                mIngredientLogViewModel.viewModelUpdateIngredientLog(mCurrentIngredientLog);

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
                    mCurrentIdIndex = mCurrentIdIndex - 1;
                    mBundleNext.putString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY, String.valueOf(mCurrentIdIndex));

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
        if ( Objects.isNull(mIngredientLogViewModel.viewModelGetMostRecentIngredientLogWithIngredient(
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

