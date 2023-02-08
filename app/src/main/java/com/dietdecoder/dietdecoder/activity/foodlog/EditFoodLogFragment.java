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

    Bundle mBundle;
    Intent mIntent;

    FoodLogViewModel mFoodLogViewModel;
    UUID mFoodLogId;
    FoodLog mFoodLog;

    Fragment mNextFragment;

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
        mEditTextConsumed.setText(mFoodLog.getMDateTimeConsumed().toString());
        mEditTextConsumed.setOnClickListener(this);

        mEditTextCooked =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_cooked);
        mEditTextCooked = Util.setEditTextWordWrapNoEnter(mEditTextCooked);
        mEditTextCooked.setInputType(InputType.TYPE_NULL);
        mEditTextCooked.setText(mFoodLog.getMDateTimeCooked().toString());
        mEditTextCooked.setOnClickListener(this);

        mEditTextAcquired =
                view.findViewById(R.id.edittext_edit_food_log_ingredient_acquired);
        mEditTextAcquired = Util.setEditTextWordWrapNoEnter(mEditTextAcquired);
        mEditTextAcquired.setInputType(InputType.TYPE_NULL);
        mEditTextAcquired.setText(mFoodLog.getMDateTimeAcquired().toString());
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
        mIntent = Util.intentWithFoodLogIdStringButton(getActivity(),
                mFoodLogIdString,
                mGoToNext,
                mFrom);

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
                    mGoToNext = Util.ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY;
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
                mGoToNext = Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT;
                mWhatToChange = Util.ARGUMENT_CHANGE_CONSUMED;
                mIntent = Util.intentChangeWithFoodLogIdStringButton(getActivity(),
                    mFoodLogIdString,
                    mGoToNext,
                    mFrom,
                    mWhatToChange);

                break;
            case R.id.edittext_edit_food_log_ingredient_cooked:
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();
                // go to specific date
                mGoToNext = Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT;
                mWhatToChange = Util.ARGUMENT_CHANGE_COOKED;
                mIntent = Util.intentChangeWithFoodLogIdStringButton(getActivity(),
                    mFoodLogIdString,
                    mGoToNext,
                    mFrom,
                    mWhatToChange);

                break;
            case R.id.edittext_edit_food_log_ingredient_acquired:
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();
                // go to specific date
                mGoToNext = Util.ARGUMENT_GO_TO_SPECIFIC_DATE_FRAGMENT;
                mWhatToChange = Util.ARGUMENT_CHANGE_ACQUIRED;
                mIntent = Util.intentChangeWithFoodLogIdStringButton(getActivity(),
                        mFoodLogIdString,
                        mGoToNext,
                        mFrom,
                        mWhatToChange);

                break;
            case R.id.button_edit_food_log_cancel:

                Toast.makeText(getContext(), mNothingChangedString, Toast.LENGTH_SHORT).show();
                // we're done so go back to food log no changes
                mGoToNext = Util.ARGUMENT_GO_TO_FOOD_LOG_ACTIVITY;
            default:
                break;
        }

        // go back to edit food log activity
        // with the ID of foodlog set
        // and the fact we came from this fragment
        // and which fragment to go to next
        startActivity(mIntent);

    }//end onClick

}
