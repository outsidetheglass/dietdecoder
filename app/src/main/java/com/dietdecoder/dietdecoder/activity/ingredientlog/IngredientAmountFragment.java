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
import com.dietdecoder.dietdecoder.activity.LogDateTimeChoicesFragment;
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
    mCurrentIngredientName, mIngredientLogIdsToAddStringArrayOriginal,
            mCurrentIngredientLogIdString, mInvalidTryAgainString, mIngredientLogIdsToAddString,
            mIngredientLogIdsToAddOriginalString, mAllTimesAreSame;
    String[] mDisplayedStringList;
    int[] mAmountColorList;
    Boolean isAmountViewEmpty;
    Integer mAmountSelectedIndex, mHowManyIdsToAdd, mCurrentLogIndex;
    Double mAmountSelected, mAmountOfMostRecentIngredientLogWithSameIngredientName;
    UUID mIngredientLogId, mCurrentIngredientId;
    Color mCurrentAmountColor, mPreviousAmountColor, mNextAmountColor;

    IngredientLog mCurrentIngredientLog;
    IngredientLogViewModel mIngredientLogViewModel;
    Ingredient mCurrentIngredient;
    IngredientViewModel mIngredientViewModel;
    IngredientLogListAdapter mIngredientLogListAdapter;
    ArrayList<String> mIngredientLogIdsToAddStringArray;

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
            Util.goToListIngredientLogActivity(null, thisActivity, null);
        } else {
            // get the info
            mBundle = getArguments();

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
            mIngredientLogIdsToAddStringArray = new ArrayList<String>();
            // after intensity is done being set the next default place is date times
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
            // default next place to go should be the next fragment
            mNextFragment = mDefaultNextFragment;
            // repeating this fragment
            mRepeatThisFragment = new IngredientAmountFragment();
            // make our next bundle have the same info that came in
            mBundleNext = mBundle;

            // to pass on to the time and date fragments, save the original untouched array string
            mIngredientLogIdsToAddString = mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY);
            //how many ids to add
            //TODO fix this lazy code
            if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                    Util.ARGUMENT_ACTION_EDIT) ) {
                mHowManyIdsToAdd = 1;
                mCurrentLogIndex = 0;
                mCurrentIngredientLogIdString = mIngredientLogIdsToAddString;
            } else {
                mHowManyIdsToAdd =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY));
                mCurrentLogIndex =
                        Integer.parseInt(mBundle.getString(Util.ARGUMENT_CURRENT_INDEX_IN_ARRAY));
                // parse the name from the array of ids and put that in the textview
                // clean the string of its brackets and spaces
                // turn into array list
                mIngredientLogIdsToAddStringArray = Util.cleanBundledStringIntoArrayList(
                        mIngredientLogIdsToAddString
                );
                // set the current ingredient to be the first
                mCurrentIngredientLogIdString =
                        mIngredientLogIdsToAddStringArray.get(mCurrentLogIndex);
            }

            // set that in the number picker and the textview for the user interface
            setCurrentIngredientUI(mCurrentIngredientLogIdString);

            // get current log info
            UUID currentLogId = UUID.fromString(mCurrentIngredientLogIdString);
            mCurrentIngredientLog = mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(currentLogId );
            // get the ingredient info from the log
            mCurrentIngredient =
                    mIngredientViewModel.viewModelGetIngredientFromId(currentLogId);
            mCurrentIngredientName = mCurrentIngredient.getIngredientName();
            mCurrentIngredientId = mCurrentIngredient.getIngredientId();

            // numbers to put in the number picker for how intense the ingredient is
            mDisplayedStringList =
                    getResources().getStringArray(R.array.strings_one_to_ten);
            // colors for the backgrounds to differentiate the numbers easily
            mAmountColorList =
                    getResources().getIntArray(R.array.colors_intensity_scale_one_to_ten);

            // set our default intensity to first in list if no ingredient has been logged yet, and
            // set to most recent log with same ingredient name if it exists
            mAmountSelected = Double.parseDouble(setAmountDefault(mCurrentIngredientName));
            mAmountSelectedIndex = Integer.parseInt(String.valueOf(mAmountSelected))-1;


            // set our numberpicker with our string of 1 to 10 and with default value of the same
            // as most recent ingredient log with the same ingredient
            mNumberPicker = Util.setNumberPicker(mNumberPicker, mDisplayedStringList,
                    Integer.parseInt(String.valueOf(mAmountSelected)), mCurrentAmountColor);
            mNumberPicker.setOnValueChangedListener(this);

            //mNumberPicker.setBackgroundColor(mAmountColorList[0]);
            GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                gradient = setGradient(gradient, Integer.parseInt(String.valueOf(
                        mAmountSelected)),
                        mAmountColorList);
            }
            mNumberPicker.setBackground(gradient);

            // TODO have delete this ingredient button

            // TODO have progress bar on bottom for how many ingredients are to be set and have been
            //  set

        }

    // Inflate the layout for this fragment
    return view;
    }


    // change the name on the text view to new current ingredient and the new default intensity
    // value
    private void setCurrentIngredientUI(String paramIngredientLogIdToAddString){

        // get first log in the list array
        UUID uuid = UUID.fromString(paramIngredientLogIdToAddString);
        mCurrentIngredientLog = mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(uuid);
        mCurrentIngredientName = mIngredientViewModel.viewModelGetIngredientFromId(
                mCurrentIngredientLog.getIngredientLogIngredientId()
        ).getIngredientName();

        // put in the UI what ingredient we're changing now
        mTextViewIngredientName.setText(mCurrentIngredientName);

        // also reset the default value on the number picker
        mAmountSelected = Double.valueOf(setAmountDefault(mCurrentIngredientName));
        mNumberPicker.setValue(Integer.parseInt(String.valueOf(mAmountSelected)));
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

                    // then set the intensity
                    mCurrentIngredientLog.setIngredientLogIngredientAmount(
                            Double.valueOf(mAmountSelected));
                    // update the log
                    mIngredientLogViewModel.viewModelUpdateIngredientLog(mCurrentIngredientLog);

                    // will set done or unfinished if action means we're from edit
                    mBundleNext = Util.setDoneIfFromEdit(mBundle);

                    // we've added it in, so remove this ingredient from the ones to add
                    mIngredientLogIdsToAddStringArray.remove(mCurrentIngredientLog.
                            getIngredientLogId().toString());
                    // and lower our count for how many left to add
                    mHowManyIdsToAdd = mHowManyIdsToAdd - 1;

                    // if this is the last intensity to check,
                    // checked by if the number of Ids left to add logs of is bigger than 0
                    if ( mHowManyIdsToAdd == 0 ) {
                        //TODO make times show up and modifiable back in add new logs if I want,
                        // probably don't need all these fragments, or make it a preference when
                        // they want to be asked when the ingredient happened

                        // go to date fragment to set when the ingredient(s) happened
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
                        mIngredientLogIdsToAddStringArray.toString());
                mBundleNext.putString(Util.ARGUMENT_HOW_MANY_ID_IN_ARRAY,
                        mHowManyIdsToAdd.toString());
                // we're still changing intensity
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_LOG_INTENSITY);
            } else {
                // there are no intensities left to set
                // add the original array as our array
                // the rest are all set together so it doesn't need to be a changing array, so we
                // don't need original and mutable array both, just need one
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY,
                    mIngredientLogIdsToAddStringArrayOriginal);
                // reset to setting begin time date for the next fragment
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN);

                // if there's more than one ingredient,
                // checked by if original string has a comma,
                // alert user that all ingredients will have the same time and date,
                // they can be individually edited from the ingredient log menu
                if ( mIngredientLogIdsToAddStringArrayOriginal.contains(",")) {
                    Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT).show();
                }
            }

            mBundleNext.putString(Util.ARGUMENT_FROM,
                    Util.ARGUMENT_FROM_INTENSITY_SYMPTOM);
            // put which we're changing into the bundle
            nextFragment.setArguments(mBundleNext);
        }

        // actually go to the next place now
        //TODO move this into Util
        if ( TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION), Util.ARGUMENT_ACTION_EDIT)){
            Util.goToEditActivityActionTypeId(null, thisActivity, Util.ARGUMENT_ACTION_EDIT,
                    Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, mCurrentIngredientLogIdString);
        } else {
//            Util.startNextFragment(getParentFragmentManager().beginTransaction(),
//                    Util.fragmentContainerViewAddIngredientLog, nextFragment);
        }
    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        // get the index of the spot in the array chosen
        int valuePicker = picker.getValue();
        // get the string value of the chosen intensity
        mAmountSelected =  Double.parseDouble(mDisplayedStringList[valuePicker]);

        // set background color to change with new value
        GradientDrawable gradient = (GradientDrawable) getResources().getDrawable(R.drawable.gradient);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            gradient = setGradient(gradient, Integer.valueOf(String.valueOf(mAmountSelected)), mAmountColorList);
        }
        picker.setBackground(gradient);
    }

    private String setAmountDefault(String mCurrentIngredientName){
        //TODO fix intensity color gradient, it breaks with default is 10 I think

        // if there is no ingredient log with same ingredient name
        if ( Objects.isNull(
                mIngredientLogViewModel.viewModelGetMostRecentIngredientLogWithIngredient(
                mCurrentIngredientId ) )) {
            // set our integer to first in the list
            mAmountSelected =
                    Double.parseDouble(mDisplayedStringList[0]);
        } else {
            // get the most recent intensity from most recent log and
            // set the default intensity to that
            mAmountOfMostRecentIngredientLogWithSameIngredientName =
                    mIngredientLogViewModel.viewModelGetMostRecentIngredientLogWithIngredient(
                                    mCurrentIngredientId )
                            .getIngredientLogIngredientAmountNumber();

            // set our default choice to save intensity of to be the most recent value
            mAmountSelected = mAmountOfMostRecentIngredientLogWithSameIngredientName;
        }
        return String.valueOf(Integer.parseInt(String.valueOf(mAmountSelected)));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private GradientDrawable setGradient(GradientDrawable gradient, Integer selectedInteger,
                                         int[] colorList){
        //TODO make this the default background color, white for now is fine
        int white = getResources().getColor(R.color.white, thisActivity.getTheme());
        int maxIndex = colorList.length-1;
        int selectedIndex = selectedInteger - 1;
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
