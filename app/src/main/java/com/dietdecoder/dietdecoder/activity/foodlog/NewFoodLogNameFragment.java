package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

public class NewFoodLogNameFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);

    Button mButtonSaveName;
    TextView mTextViewWhen;
    EditText mEditTextIngredientName;

    String mSaveString, mEmptyTryAgainString, mName, foodLogIdString;
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
        mEditTextIngredientName = Util.setEditTextWordWrapNoEnter(mEditTextIngredientName);
        mButtonSaveName = view.findViewById(R.id.button_new_food_log_name_save);
        mButtonSaveName.setOnClickListener(this);
    // Inflate the layout for this fragment
    return view;
}
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // set the data from the bundle to the activity variables
        mBundle = new Bundle();
    }


    @Override
    public void onClick(View view) {

        mBundle = getArguments();
        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);

        switch (view.getId()) {
            case R.id.button_new_food_log_name_save:
                isNameViewEmpty = TextUtils.isEmpty(mEditTextIngredientName.getText());
                // if view is empty
                if ( isNameViewEmpty ) {
                    // set intent to tell user try again
                    Toast.makeText(getContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                } else {
                    // if view has a values
                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();
                    // get strings
                    mName = mEditTextIngredientName.getText().toString();
                    // name is enough to save the food log
                    // it will default to having been consumed now
                    FoodLog foodLog = new FoodLog(mName);
                    mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
                    mFoodLogViewModel.viewModelInsertFoodLog(foodLog);
                    foodLogIdString = foodLog.getMFoodLogId().toString();
                    Log.d(TAG, foodLogIdString);

                    // go back to new food log activity
                    // with the ID of foodlog set
                    // and the fact we came from this fragment
                    // and which fragment to go to next
                    startActivity(Util.intentWithFoodLogIdStringButton(getActivity(),
                            foodLogIdString,
                            Util.ARGUMENT_GO_TO_DATE_TIME_CHOICES,
                            Util.ARGUMENT_FROM_INGREDIENT_NAME));
                }
                break;
            default:
                break;
        }
    }//end onClick

}
