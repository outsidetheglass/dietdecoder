package com.dietdecoder.dietdecoder.activity.foodlog;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.util.Objects;
import java.util.UUID;

public class NewFoodLogNameFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);

    Button mButtonSaveName;
    EditText mEditTextIngredientName;
    ListView mListView;

    String mSaveString, mEmptyTryAgainString, mName, foodLogIdString;
    Boolean isNameViewEmpty;

    private KeyListener originalKeyListener;

    Bundle mBundle;

    FoodLogViewModel mFoodLogViewModel;
    IngredientViewModel mIngredientViewModel;
    IngredientListAdapter mIngredientListAdapter;

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
        mEditTextIngredientName.setOnClickListener(this::onClick);

        mButtonSaveName = view.findViewById(R.id.button_new_food_log_name_save);
        mButtonSaveName.setOnClickListener(this);

        // TODO get search ingredients to autofill edittext value working
//        if (!Objects.isNull(getArguments())) {
//            Bundle mBundle = getArguments();
//            String ingredientIdString = mBundle.getString(Util.ARGUMENT_INGREDIENT_ID);
//            String ingredientName = mBundle.getString(Util.ARGUMENT_INGREDIENT_NAME);
//            mEditTextIngredientName.setText(ingredientName);
//        }
        //mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
   //     mListView = view.findViewById(R.id.list_view_new_food_log_name);
//        mEditTextIngredientName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    String searchData = mEditTextIngredientName.getText().toString();
//                    showResults(searchData); //passing string to search in your database to your method
//                    return true;
//                }
//                return false;
//            }
//        });
    // Inflate the layout for this fragment
    return view;
    }

    private void showResults(String query) {

        Ingredient mIngredient = mIngredientViewModel.viewModelGetIngredientFromSearchName(query);
        Toast.makeText(getActivity(), "ing: " + mIngredient.getIngredientName(), Toast.LENGTH_SHORT).show();
        mIngredientListAdapter =
                new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
        mListView.setAdapter((ListAdapter) mIngredientListAdapter);
        mListView.setOnClickListener(this::onClick);

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
            case R.id.edittext_new_food_log_name_ingredient_name:

                //TODO show list view when edit text is clicked here or use onEditorchangelistener
                break;
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
                    //TODO fix mName instead of ID
                    Ingredient useThisIngredient = null;
                    Ingredient ingredient =
                            mIngredientViewModel.viewModelGetIngredientFromName(mName);
                    if (Objects.isNull(ingredient)){
                        // if the ingredient wasn't found it doesn't exist, so make it
                        useThisIngredient = new Ingredient(mName);
                        mIngredientViewModel.viewModelInsert(useThisIngredient);
                    } else {
                        // if the ingredient did exist, the one we use should be it
                        useThisIngredient = ingredient;
                    }
                    FoodLog foodLog = new FoodLog(useThisIngredient.getIngredientId(), mName);
                    mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
                    mFoodLogViewModel.viewModelInsertFoodLog(foodLog);
                    // with the ID of foodlog set
                    foodLogIdString = foodLog.getFoodLogId().toString();

                    //TODO move this adding ingredient in to the ingredient view model called
                    // from the observer in the food log activity
//                    addIngredientIfNotExist(foodLog.getIngredientId());


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
//            // TODO get ingredient search as they type name working
//            case R.id.list_view_new_food_log_name:
//                //mEditTextIngredientName.setText(mIngredient);
//                break;
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
            Log.d(TAG,ingredient.getIngredientId().toString()+" "+ingredient.getIngredientName());
        } else {
            Log.d(TAG, "Ingredient exists: " + mIngredientViewModel.viewModelGetIngredientFromName(mIngredientName).getIngredientName());
        }//end checking if ingredient exists


    }

}
