package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.time.Instant;
import java.time.LocalDateTime;

public class NewFoodLogNameFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();

    Button mButtonSave;
    String mSaveString;
    String mEmptyTryAgainString;
    String mName;

    TextView mWhenAnsweredTime;
    TextView mWhenAnsweredDate;

    EditText mEditTextIngredientName;

    Integer mHour, mMinute, mDay, mMonth, mYear;

    Boolean isNameViewEmpty;

    Bundle mBundle;

    FoodLogViewModel mFoodLogViewModel;

    public NewFoodLogNameFragment() {
        super(R.layout.fragment_new_food_log_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_food_log_name, container, false);

        mEditTextIngredientName =
                view.findViewById(R.id.edittext_new_food_log_name_ingredient_name);

        mWhenAnsweredDate = view.findViewById(R.id.textview_new_food_log_name_chosen_date);
        mWhenAnsweredTime = view.findViewById(R.id.textview_new_food_log_name_chosen_time);
        mButtonSave = view.findViewById(R.id.button_new_food_log_name_save);
        mButtonSave.setOnClickListener(this);
    // Inflate the layout for this fragment
    return view;
}
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // set the data from the bundle to the activity variables
        mBundle = getArguments();
        setDataFromBundle(mBundle);
        Log.d(TAG, " onViewCreated, mBundle hour: " + mHour);

        // set the textviews
        String whenConsumedDate = getResources().getString(R.string.date_consumed);
        String whenConsumedTime = getResources().getString(R.string.time_consumed);
        mWhenAnsweredTime.setText(
                whenConsumedTime + " " +
                this.mHour + ":" +
                Util.setMinutesString(this.mMinute));
        mWhenAnsweredDate.setText(
                whenConsumedDate + " " +
                        Util.setMonthString(this.mMonth) + " " +
                        this.mDay + ", " +
                        this.mYear
        );
    }


    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_ingredient_not_saved);

        Log.d(TAG, "onClick: mName = " + mName);
        switch (view.getId()) {
            case R.id.button_new_food_log_name_save:

                isNameViewEmpty = TextUtils.isEmpty(mEditTextIngredientName.getText());
                // if view is empty
                if ( isNameViewEmpty ) {
                    Log.d(TAG, "onClick in if: mName = " + mName);
                    // set intent to tell user try again
                    Toast.makeText(getContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                } else {
                    // if views have values
                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();
                    // get strings
                    mName = mEditTextIngredientName.getText().toString();
                    Log.d(TAG, "onClick in else: mName = " + mName);
                    // name and time and date is enough to
                    // save the food log
                    // make an instant from our values
                    Instant logInstant = Util.instantFromValues(mHour, mMinute, mDay, mMonth, mYear);
                    // make our food log
                    FoodLog foodLog = new FoodLog(mName, "", logInstant, logInstant, logInstant);

                    mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
                    mFoodLogViewModel.viewModelInsertFoodLog(foodLog);

                    // TODO now go to next fragment
                    Toast.makeText(getContext(), " todo make next fragment", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }//end onClick


    private void setDataFromBundle (Bundle bundle) {

        // set the times
        this.mHour = bundle.getInt(Util.ARGUMENT_HOUR);
        this.mMinute = bundle.getInt(Util.ARGUMENT_MINUTE);
        this.mDay = bundle.getInt(Util.ARGUMENT_DAY);
        this.mMonth = bundle.getInt(Util.ARGUMENT_MONTH);
        this.mYear = bundle.getInt(Util.ARGUMENT_YEAR);

    }
}
