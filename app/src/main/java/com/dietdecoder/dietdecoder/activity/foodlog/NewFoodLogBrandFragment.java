package com.dietdecoder.dietdecoder.activity.foodlog;

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
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.util.UUID;

public class NewFoodLogBrandFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mBrand = " + mBrand);

    Button mButtonSave;
    TextView mWhenAnsweredTime, mWhenAnsweredDate, mWhenAnsweredName;
    EditText mEditTextIngredientBrand;

    String mSaveString, mEmptyTryAgainString, mBrand, mName, mDateTimeString, mFoodLogIdString;
    Integer mHour, mMinute, mDay, mMonth, mYear;
    Boolean isBrandViewEmpty;

    Bundle mBundle;

    FoodLogViewModel mFoodLogViewModel;
    FoodLog mFoodLog;
    UUID mId;

    public NewFoodLogBrandFragment() {
        super(R.layout.fragment_new_food_log_brand);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_food_log_brand, container, false);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        mEditTextIngredientBrand =
                view.findViewById(R.id.edittext_new_food_log_brand_ingredient_brand);

        mWhenAnsweredDate = view.findViewById(R.id.textview_new_food_log_brand_chosen_date);
        mWhenAnsweredTime = view.findViewById(R.id.textview_new_food_log_brand_chosen_time);
        mWhenAnsweredName = view.findViewById(R.id.textview_new_food_log_brand_chosen_name);
        mButtonSave = view.findViewById(R.id.button_new_food_log_brand_save);
        mButtonSave.setOnClickListener(this);
    // Inflate the layout for this fragment
    return view;
}
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // set the data from the bundle to the activity variables
        mBundle = getArguments();
        Log.d(TAG, mBundle.toString());
        setDataFromBundle(mBundle);

        // set the textviews
        String whenConsumedName = getResources().getString(R.string.name_consumed);
        whenConsumedName = whenConsumedName.concat(" ")
                .concat(this.mName);
        mWhenAnsweredName.setText(whenConsumedName);

        String whenConsumedDate = null;
        whenConsumedDate = getResources().getString(R.string.when_consumed)
                .concat(" ")
                .concat(this.mDateTimeString);
        mWhenAnsweredDate.setText(whenConsumedDate);

//        String whenConsumedTime = getResources().getString(R.string.time_consumed);
//        whenConsumedTime.concat(" ")
//                .concat(this.mHour.toString()).concat(":")
//                .concat(Util.setMinutesString(this.mMinute));
//        mWhenAnsweredTime.setText(whenConsumedTime);

    }


    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);

        switch (view.getId()) {
            case R.id.button_new_food_log_brand_save:
                // if view is empty
                isBrandViewEmpty = TextUtils.isEmpty(mEditTextIngredientBrand.getText());
                if ( !isBrandViewEmpty ) {
                    // if view has a value
                    // get string
                    mBrand = mEditTextIngredientBrand.getText().toString();
                }
                Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();
                //update the food log
                mFoodLog.setMBrand(mBrand);
                mFoodLogViewModel.viewModelUpdateFoodLog(mFoodLog);

                //TODO not done, go to cooked and acquired fragments and make them
                startActivity(Util.intentWithFoodLogIdStringButton(getActivity(), mFoodLogIdString,
                        Util.ARGUMENT_DONE,
                        Util.ARGUMENT_FROM_INGREDIENT_BRAND));
                break;
            default:
                break;
        }
    }//end onClick


    private void setDataFromBundle (Bundle bundle) {

        // set the id
        this.mId = UUID.fromString(bundle.getString(Util.ARGUMENT_FOOD_LOG_ID));
        // now get the food log associated with that UUID
        this.mFoodLog = mFoodLogViewModel.viewModelGetFoodLogFromId(mId);
        //then set the values from the food log
        this.mName = mFoodLog.getMIngredientName();
        this.mDateTimeString = this.mFoodLog.getFoodLogDateTimeString();
        this.mFoodLogIdString = this.mFoodLog.getMFoodLogId().toString();

    }
}
