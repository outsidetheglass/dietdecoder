package com.dietdecoder.dietdecoder.activity.foodlog;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.util.UUID;

public class EditFoodLogFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    private String mFrom = Util.ARGUMENT_FROM_EDIT_FOOD_LOG;
    //Log.d(TAG, "onClick in if: mName = " + mName);

    Button mButtonSave, mButtonCancel;

    TextView mTextViewWhen;
    EditText mEditTextIngredientName, mEditTextIngredientBrand, mEditTextConsumed,
    mEditTextCooked, mEditTextAcquired;


    String mSaveString, mNothingChangedString, mName, mBrand, mFoodLogIdString,
            mFoodLogIngredientName,
            mFoodLogIngredientBrand, mCurrentIngredientName, mCurrentIngredientBrand, mGoToNext,
            mChangeDateTime, mWhatToChange;
    Boolean isNameEdited, isBrandEdited;

    Bundle mBundle, mBundleNext;
    Intent mIntent;

    FoodLogViewModel mFoodLogViewModel;
    UUID mFoodLogId;
    FoodLog mFoodLog;

    Fragment mNextFragment = null;

    public EditFoodLogFragment() {
        super(R.layout.fragment_edit_food_log);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_food_log, container, false);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);

        // get data from parent activity on which food log we're editing
        mBundle = getArguments();

        // get food log id as a string
        mFoodLogIdString = mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID);
        // turn it into its UUID
        mFoodLogId = UUID.fromString(mFoodLogIdString);
        // use that to get the food log itself
        mFoodLog = mFoodLogViewModel.viewModelGetFoodLogFromId(mFoodLogId);
        // then some values
        mFoodLogIngredientName = mFoodLog.getMIngredientName();
        mFoodLogIngredientBrand = mFoodLog.getMBrand();

        // then use the food log to set the edit text views
        mEditTextIngredientName =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_name);
        // set the edit text view to have good defaults
        mEditTextIngredientName =
                Util.setEditTextWordWrapNoEnter(mEditTextIngredientName);
        // set the default text to the food log data
        mEditTextIngredientName.setText(mFoodLogIngredientName);
        // also throw listeners on them
        mEditTextIngredientName.setOnClickListener(this);

        mEditTextIngredientBrand =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_brand);
        mEditTextIngredientBrand = Util.setEditTextWordWrapNoEnter(mEditTextIngredientBrand);
        mEditTextIngredientBrand.setText(mFoodLogIngredientBrand);
        mEditTextIngredientBrand.setOnClickListener(this);

        mEditTextConsumed =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_consumed);
        mEditTextConsumed = Util.setEditTextWordWrapNoEnter(mEditTextConsumed);
        mEditTextConsumed.setInputType(InputType.TYPE_NULL);
        mEditTextConsumed.setText(Util.stringFromInstant(mFoodLog.getMDateTimeConsumed()));
        mEditTextConsumed.setOnClickListener(this);

        mEditTextCooked =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_cooked);
        mEditTextCooked = Util.setEditTextWordWrapNoEnter(mEditTextCooked);
        mEditTextCooked.setInputType(InputType.TYPE_NULL);
        mEditTextCooked.setText(Util.stringFromInstant(mFoodLog.getMDateTimeCooked()));
        mEditTextCooked.setOnClickListener(this);

        mEditTextAcquired =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_acquired);
        mEditTextAcquired = Util.setEditTextWordWrapNoEnter(mEditTextAcquired);
        mEditTextAcquired.setInputType(InputType.TYPE_NULL);
        mEditTextAcquired.setText(Util.stringFromInstant(mFoodLog.getMDateTimeAcquired()));
        mEditTextAcquired.setOnClickListener(this);


        // whether the user presses save or cancel
        mButtonSave = view.findViewById(R.id.button_edit_food_log_save);
        mButtonSave.setOnClickListener(this);
        mButtonCancel = view.findViewById(R.id.button_edit_food_log_cancel);
        mButtonCancel.setOnClickListener(this);
    // Inflate the layout for this fragment
    return view;
}


    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mNothingChangedString = getResources().getString(R.string.nothing_changed_not_saved);
        mChangeDateTime = getResources().getString(R.string.change_date_time);

        // set our relevant data to use in new location
        mBundleNext = new Bundle();
        mBundleNext.putString(Util.ARGUMENT_FOOD_LOG_ID, mFoodLogIdString);

        // will only use that for date time changes
        LogSpecificDateTimeFragment logSpecificDateTimeFragment = new LogSpecificDateTimeFragment();

        // check for where to go next
        Boolean isNextFragmentSet = Boolean.FALSE;
        switch (view.getId()) {
            // save button was pressed
            case R.id.button_edit_food_log_save:
                // get the current values of the strings
                mCurrentIngredientName = mEditTextIngredientName.getText().toString();
                mCurrentIngredientBrand = mEditTextIngredientBrand.getText().toString();
                // if the old name is equal to current name, it was not edited
                isNameEdited = Util.isEdited(mFoodLogIngredientName, mCurrentIngredientName);
                isBrandEdited = Util.isEdited(mFoodLogIngredientBrand, mCurrentIngredientBrand);
                // if it was not edited
                if ( !isNameEdited && !isBrandEdited ) {
                    // set intent to tell user nothing changed
                    Toast.makeText(getContext(), mNothingChangedString, Toast.LENGTH_SHORT).show();
                    // we're done so go back to food log no changes
                } else {
                    // if view has a values, find which or both was changed
                    if ( isNameEdited ) {
                        // set strings
                        mName = mEditTextIngredientName.getText().toString();
                        mFoodLog.setIngredientName(mName);
                    }
                    if ( isBrandEdited ) {
                        // get strings
                        mBrand = mEditTextIngredientBrand.getText().toString();
                        mFoodLog.setMBrand(mBrand);
                    }
                    // then update the model with the new name and/or brand
                    mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edittext_edit_food_log_ingredient_consumed:
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();
                // go to specific date
                // set our relevant data to use in new location
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_CONSUMED);
                // go to a fragment
                isNextFragmentSet = Boolean.TRUE;

                break;
            case R.id.edittext_edit_food_log_ingredient_cooked:
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();
                // go to specific date
                // set our relevant data to use in new location
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_COOKED);
                isNextFragmentSet = Boolean.TRUE;

                break;
            case R.id.edittext_edit_food_log_ingredient_acquired:
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();
                // go to specific date
                // set our relevant data to use in new location
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_ACQUIRED);
                isNextFragmentSet = Boolean.TRUE;
                break;
            case R.id.button_edit_food_log_cancel:
                // toast to the user that nothing happened
                Toast.makeText(getContext(), mNothingChangedString, Toast.LENGTH_SHORT).show();
                // we're done so go back to food log no changes set
            default:
                break;
        }
        // if next fragment has been set use that
        if ( isNextFragmentSet ) {
            // put which we're changing into the bundle
            logSpecificDateTimeFragment.setArguments(mBundleNext);
            // actually go to the next place now
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(Util.fragmentContainerView, logSpecificDateTimeFragment);
            ft.commit();
        }
        // else it means go back to main activity
        else {
            // go back to food log activity
            // with the ID of foodlog set
            mIntent = new Intent(getActivity(), FoodLogActivity.class);
            mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, mFoodLogIdString);
            startActivity(mIntent);

        }

    }//end onClick

}
