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
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
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
    ImageButton mConsumedValueButton, mCookedValueButton, mAcquiredValueButton,
            mNameValueButton, mBrandValueButton,
            mAmountValueButton;

    TextView mNameValueTextView, mConsumedValueTextView, mCookedValueTextView,
            mAcquiredValueTextView, mBrandValueTextView,
            mAmountValueTextView;


    String mSaveString, mNothingChangedString, mLogIdString,
            mLogIngredientName, mDateTime, mChangeIngredient, mChangeAmountToastString,
            mIngredientBrand, mIngredientSubjectiveAmount, mDuplicateLogIdString;
    Boolean isNameEdited;

    Bundle mBundle, mBundleNext;
    Intent mIntent;

    IngredientLogViewModel mIngredientLogViewModel;
    IngredientViewModel mIngredientViewModel;
    UUID mLogId;
    IngredientLog mLog, mDuplicateLog;
    Ingredient mIngredient;

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
                view.findViewById(R.id.value_textview_edit_ingredient_log_name);
        mBrandValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_brand);
        mAmountValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_amount);
        mConsumedValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_consumed);
        mCookedValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_cooked);
        mAcquiredValueTextView =
                view.findViewById(R.id.value_textview_edit_ingredient_log_acquired);

        mNameValueButton = view.findViewById(R.id.imagebutton_edit_ingredient_log_name);
        mBrandValueButton =
                view.findViewById(R.id.imagebutton_edit_ingredient_log_brand);
        mAmountValueButton =
                view.findViewById(R.id.imagebutton_edit_ingredient_log_amount);
        mConsumedValueButton =
                view.findViewById(R.id.imagebutton_edit_ingredient_log_consumed);
        mCookedValueButton =
                view.findViewById(R.id.imagebutton_edit_ingredient_log_cooked);
        mAcquiredValueButton =
                view.findViewById(R.id.imagebutton_edit_ingredient_log_acquired);

        mButtonDone = view.findViewById(R.id.button_edit_ingredient_log_done);

        // out database accesses
        mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);


        // if we have arguments for an id to edit
        if ( getArguments().getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) != null) {
            // get data from parent activity on which food log we're editing
            mBundle = getArguments();

            // get id as a string
            // this is edit so it's only one id for sure
            mLogIdString = Util.cleanArrayString(
                    mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY) );

            // turn it into its UUID
            mLogId = UUID.fromString(mLogIdString);
            // use that to get the log itself
            mLog = mIngredientLogViewModel.viewModelGetLogFromLogId(mLogId);

            // if it's duplicate, don't need to set UI, just do it
            if (Util.isActionDuplicateBundle(mBundle) ) {
                //TODO fix, duplicate breaks when multiple are chosen
                mDuplicateLog = mIngredientLogViewModel.viewModelDuplicate(mLog);
                mDuplicateLogIdString = mDuplicateLog.getLogId().toString();

                // now that the log has been duplicated, go set the time consumed because that's
                // the only required difference from the duplicated log (besides UUID and time
                // logged, which have been reset to instant now)

                // set our relevant data to use in new location
                mBundleNext =
                        Util.setEditIngredientLogBundle(mDuplicateLogIdString,
                                Util.ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED);

                // begin is a time, so go to the date time fragment to set it
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

            } else {

                // then the ingredient values
                mIngredient =
                        mIngredientViewModel.viewModelGetFromId(mLog.getLogIngredientId());
                mLogIngredientName = mIngredient.getName();
                mIngredientBrand = mIngredient.getBrand();

                mIngredientSubjectiveAmount = mLog.getLogIngredientSubjectiveAmount();

                // then use the log to set the text views
                // set the default text to the data
                mNameValueTextView.setText(mLogIngredientName);
                mAmountValueTextView.setText(mIngredientSubjectiveAmount);
                mBrandValueTextView.setText(mIngredientBrand);

                mConsumedValueTextView.setText(Util.stringFromInstant(mLog.getInstantConsumed()));
                mCookedValueTextView.setText(Util.stringFromInstant(mLog.getInstantCooked()));
                mAcquiredValueTextView.setText(Util.stringFromInstant(mLog.getInstantAcquired()));


                // also throw listeners on them
                mNameValueButton.setOnClickListener(this);
                mAmountValueButton.setOnClickListener(this);
                mBrandValueButton.setOnClickListener(this);

                mConsumedValueButton.setOnClickListener(this);
                mCookedValueButton.setOnClickListener(this);
                mAcquiredValueButton.setOnClickListener(this);

                mButtonDone.setOnClickListener(this);
            } // end checking duplicate or edit
        }
    // Inflate the layout for this fragment
    return view;
}


    @Override
    public void onClick(View view) {


        //TODO fix why whatToChange is null in Util set log instants, used badly it
        // could reset good dates (like if
        // we think we're editing consumed but it's null and the time meant to be just consumed
        // is also set on acquired and cooked. It's probably in edit here somewhere

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mNothingChangedString = getResources().getString(R.string.nothing_changed_not_saved);
        mDateTime = getResources().getString(R.string.change_date_time);
        mChangeIngredient = getResources().getString(R.string.change_ingredient);
        mChangeAmountToastString = getResources().getString(R.string.change_amount);

        switch (view.getId()) {
            // save button was pressed
            case R.id.button_edit_ingredient_log_done:
                // done with editing
                Util.goToListIngredientLogActivity(null, thisActivity, mLogIdString);
                break;
            case R.id.imagebutton_edit_ingredient_log_name:

                Toast.makeText(getContext(), mChangeIngredient, Toast.LENGTH_SHORT).show();
                try {

                    Util.startNextActivityActionChangeIdArray(thisActivity,
                            mNextActivityClass,
                            Util.ARGUMENT_ACTION_EDIT,
                            Util.ARGUMENT_CHANGE_INGREDIENT_LOG_NAME,
                            Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mLogIdString);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.imagebutton_edit_ingredient_log_brand:

                Toast.makeText(getContext(), mChangeIngredient, Toast.LENGTH_SHORT).show();
                try {

                    Util.startNextActivityActionChangeIdArray(thisActivity,
                            mNextActivityClass,
                            Util.ARGUMENT_ACTION_EDIT,
                            Util.ARGUMENT_CHANGE_INGREDIENT_LOG_BRAND,
                            Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mLogIdString);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.imagebutton_edit_ingredient_log_amount:

                Toast.makeText(getContext(), mChangeAmountToastString, Toast.LENGTH_SHORT).show();
                mBundleNext =
                        Util.setEditIngredientLogBundle(mLogIdString,
                                Util.ARGUMENT_CHANGE_INGREDIENT_LOG_AMOUNT);
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new IngredientAmountFragment(), mBundleNext);

                break;
            case R.id.imagebutton_edit_ingredient_log_consumed:
                // when symptom began option was clicked, so tell the user we'll go change that
                // date time
                Toast.makeText(getContext(), mDateTime, Toast.LENGTH_SHORT).show();

                // set our relevant data to use in new location
                mBundleNext =
                        Util.setEditIngredientLogBundle(mLogIdString,
                        Util.ARGUMENT_CHANGE_INGREDIENT_LOG_CONSUMED);

                // begin is a time, so go to the date time fragment to set it
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_edit_ingredient_log_cooked:
                // when symptom began option was clicked, so tell the user we'll go change that
                // date time
                Toast.makeText(getContext(), mDateTime, Toast.LENGTH_SHORT).show();

                // set our relevant data to use in new location
                mBundleNext =
                        Util.setEditIngredientLogBundle(mLogIdString,
                                Util.ARGUMENT_CHANGE_INGREDIENT_LOG_COOKED);

                // begin is a time, so go to the date time fragment to set it
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_edit_ingredient_log_acquired:
                // changed date time was clicked, so tell the user we'll go change that date time
                Toast.makeText(getContext(), mDateTime, Toast.LENGTH_SHORT).show();

                mBundleNext =
                        Util.setEditIngredientLogBundle(mLogIdString,
                                Util.ARGUMENT_CHANGE_INGREDIENT_LOG_ACQUIRED);
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            default:
                break;
        }

    }//end onClick

}
