package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.util.Objects;

public class NewFoodLogNameFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);

    Button mButtonSaveName;
    EditText mEditTextIngredientName;

    String mSaveString, mEmptyTryAgainString, mName, foodLogIdString;
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
                    // with the ID of foodlog set
                    foodLogIdString = foodLog.getMFoodLogId().toString();

                    //TODO move this adding ingredient in to the ingredient view model called
                    // from the observer in the food log activity
                    addIngredientIfNotExist(foodLog.getMIngredientName());


                    // go to ask what date
                    Bundle mBundleNext = new Bundle();
                    mBundleNext.putString(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);
                    // and which fragment to go to next
                    Fragment mNextFragment = new LogDateTimeChoicesFragment();
                    // put which we're changing into the bundle
                    mNextFragment.setArguments(mBundleNext);
                    // actually go to the next place now
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(Util.fragmentContainerViewAddFoodLog, mNextFragment);
                    ft.commit();

                }
                break;
            default:
                break;
        }
    }//end onClick

    // TODO move this when I understand how to call another view model on a different view
    //  model's list from an observer
    private void addIngredientIfNotExist(String mIngredientName) {
        Log.d(TAG, "original ingredient: " + mIngredientName);
        IngredientViewModel mIngredientViewModel =
                new ViewModelProvider(this).get(IngredientViewModel.class);
        // check if the ingredient has been added to ingredient list yet
        // search ingredients database for a matching name
        // if it doesn't exist, add it
        if ( Objects.isNull(mIngredientViewModel.viewModelGetIngredientFromName(mIngredientName)) ) {

            Log.d(TAG, "Ingredient does not exist, making it now..." );
            Ingredient ingredient = new Ingredient(mIngredientName);
            mIngredientViewModel.viewModelInsert(ingredient);
            Log.d(TAG,ingredient.getMIngredientId().toString()+" "+ingredient.getIngredientName());
        } else {
            Log.d(TAG, "Ingredient exists: " + mIngredientViewModel.viewModelGetIngredientFromName(mIngredientName).getIngredientName());
        }//end checking if ingredient exists


    }

}
