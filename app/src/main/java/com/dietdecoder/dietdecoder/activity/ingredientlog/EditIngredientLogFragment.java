package com.dietdecoder.dietdecoder.activity.ingredientlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.SpecificDateTimeFragment;
import com.dietdecoder.dietdecoder.activity.symptomlog.ChooseSymptomActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.SymptomIntensityFragment;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;

import java.util.UUID;

public class EditIngredientLogFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);
    private Activity thisActivity;
    private Context thisContext;
    Class mNextActivityClass = new ChooseIngredientActivity().getClass();

    Button mButtonDone;
    ImageButton mConsumedValueOptionButton, mCookedValueOptionButton, mAcquiredValueOptionButton,
            mNameValueOptionButton, mBrandValueOptionButton,
            mAmountValueOptionButton;

    TextView mNameValueTextView, mConsumedValueTextView, mCookedValueTextView,
            mAcquiredValueTextView, mBrandValueTextView,
            mAmountValueTextView;


    String mSaveString, mNothingChangedString, mLogIdString,
            mLogIngredientName, mDateTime, mChangeIngredient, mChangeIntensityToastString;
    Boolean isNameEdited;

    Bundle mBundle, mBundleNext;
    Intent mIntent;

    IngredientLogViewModel mIngredientLogViewModel;
    IngredientViewModel mIngredientViewModel;
    UUID mLogId;
    SymptomLog mLog;
    Symptom mSymptom;

    Fragment mNextFragment = null;

    public EditIngredientLogFragment() {
        super(R.layout.fragment_edit_ingredient_log);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_ingredient_log, container, false);

        thisContext = getContext();
        thisActivity = this.getActivity();

        // set our UI views
        mNameValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_ingredient_name);
        mConsumedValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_consumed);
        mCookedValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_cooked);
        mAcquiredValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_acquired);
        mBrandValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_ingredient_brand);
        mAmountValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_ingredient_amount);

        mNameValueOptionButton = view.findViewById(R.id.imagebutton_ingredient_log_name_option);
        mConsumedValueOptionButton =
                view.findViewById(R.id.imagebutton_ingredient_log_consumed_option);
        mCookedValueOptionButton =
                view.findViewById(R.id.imagebutton_ingredient_log_cooked_option);
        mAcquiredValueOptionButton =
                view.findViewById(R.id.imagebutton_ingredient_log_acquired_option);
        mAmountValueOptionButton =
                view.findViewById(R.id.imagebutton_ingredient_log_amount_option);
        mBrandValueOptionButton =
                view.findViewById(R.id.imagebutton_ingredient_log_brand_option);

        mButtonDone = view.findViewById(R.id.button_edit_ingredient_log_done);

        // out database accesses
        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);


        // if we have arguments for an id to edit
        if ( getArguments().getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) != null) {
            // get data from parent activity on which food log we're editing
            mBundle = getArguments();

            // get id as a string
            mLogIdString = mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY);
            if ( mLogIdString.contains(",") ) {
                Toast.makeText(thisActivity, "An array of log ID's were passed in, try again with only one to edit.", Toast.LENGTH_SHORT).show();
            }
            // turn it into its UUID
            mLogId = UUID.fromString(mLogIdString);
            // use that to get the log itself
            mLog = mIngredientLogViewModel.viewModelGetLogFromLogId(mLogId);
            mSymptom =
                    mIngredientViewModel.viewModelGetFromId(mLog.getId());

            // then the name value
            mLogIngredientName = mSymptom.getSymptomName();

            // then use the log to set the text views
            // set the default text to the data
            mNameValueTextView.setText(mLogIngredientName);
            mConsumedValueTextView.setText(Util.stringFromInstant(mLog.getInstantConsumed()));
            mCookedValueTextView.setText(Util.stringFromInstant(mLog.getInstantCooked()));
            mAcquiredValueTextView.setText(Util.stringFromInstant(mLog.getInstantAcquired()));
            mAmountValueTextView.setText(String.valueOf(mLog.getLogIngredientAmount()));
            mBrandValueTextView.setText(String.valueOf(mLog.getLogIngredientBrand()));

            // also throw listeners on them
            mNameValueOptionButton.setOnClickListener(this);
            mConsumedValueOptionButton.setOnClickListener(this);
            mCookedValueOptionButton.setOnClickListener(this);
            mAcquiredValueOptionButton.setOnClickListener(this);
            mAmountValueOptionButton.setOnClickListener(this);
            mBrandValueOptionButton.setOnClickListener(this);
            mButtonDone.setOnClickListener(this);
        }
    // Inflate the layout for this fragment
    return view;
}


    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mNothingChangedString = getResources().getString(R.string.nothing_changed_not_saved);
        mDateTime = getResources().getString(R.string.change_date_time);
        mChangeIngredient = getResources().getString(R.string.change_symptom);
        mChangeIntensityToastString = getResources().getString(R.string.change_intensity);

        switch (view.getId()) {
            // save button was pressed
            case R.id.button_edit_ingredient_log_done:
                // done with editing
                Util.goToListSymptomLogActivity(null, thisActivity, mLogIdString);
                break;
            case R.id.imagebutton_ingredient_log_consumed_option:
                // when symptom began option was clicked, so tell the user we'll go change that
                // date time
                Toast.makeText(getContext(), mDateTime, Toast.LENGTH_SHORT).show();

                // set our relevant data to use in new location
                mBundleNext =
                        Util.setEditSymptomLogBundle(mLogIdString,
                        Util.ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED);

                // begin is a time, so go to the date time fragment to set it
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_ingredient_log_cooked_option:
                // when symptom began option was clicked, so tell the user we'll go change that
                // date time
                Toast.makeText(getContext(), mDateTime, Toast.LENGTH_SHORT).show();

                // set our relevant data to use in new location
                mBundleNext =
                        Util.setEditSymptomLogBundle(mLogIdString,
                                Util.ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED);

                // begin is a time, so go to the date time fragment to set it
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_ingredient_log_acquired_option:
                // changed date time was clicked, so tell the user we'll go change that date time
                Toast.makeText(getContext(), mDateTime, Toast.LENGTH_SHORT).show();

                mBundleNext =
                        Util.setEditSymptomLogBundle(mLogIdString,
                                Util.ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED);
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_ingredient_log_name_option:

                Toast.makeText(getContext(), mChangeIngredient, Toast.LENGTH_SHORT).show();
                try {

                    Util.startNextActivityActionChangeIdArray(thisActivity,
                            mNextActivityClass,
                            Util.ARGUMENT_ACTION_EDIT,
                            Util.ARGUMENT_CHANGE_INGREDIENT_LOG_AMOUNT,
                            Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mLogIdString);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.imagebutton_ingredient_log_amount_option:

                Toast.makeText(getContext(), mChangeIntensityToastString, Toast.LENGTH_SHORT).show();
                mBundleNext =
                        Util.setEditSymptomLogBundle(mLogIdString,
                                Util.ARGUMENT_CHANGE_SYMPTOM_LOG_INTENSITY);
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SymptomIntensityFragment(), mBundleNext);

                break;
            default:
                break;
        }

    }//end onClick

}
