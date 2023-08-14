package com.dietdecoder.dietdecoder.activity.symptomlog;

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
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.UUID;

public class EditSymptomLogFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);
    private Activity thisActivity;
    private Context thisContext;
    Class mNextActivityClass = new ChooseSymptomActivity().getClass();

    Button mButtonDone;
    ImageButton mBeganValueOptionButton, mChangedValueOptionButton, mNameValueOptionButton,
            mIntensityValueOptionButton;

    TextView mNameValueTextView, mBeganValueTextView, mChangedValueTextView, mIntensityValueTextView;


    String mSaveString, mNothingChangedString, mSymptomLogIdString,
            mSymptomLogSymptomName, mChangeDateTime, mChangeSymptom, mChangeIntensityToastString;
    Boolean isNameEdited;

    Bundle mBundle, mBundleNext;
    Intent mIntent;

    SymptomLogViewModel mSymptomLogViewModel;
    SymptomViewModel mSymptomViewModel;
    UUID mSymptomLogId;
    SymptomLog mSymptomLog;
    Symptom mSymptom;

    Fragment mNextFragment = null;

    public EditSymptomLogFragment() {
        super(R.layout.fragment_edit_symptom_log);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_symptom_log, container, false);

        thisContext = getContext();
        thisActivity = this.getActivity();

        // set our UI views
        mNameValueTextView =
                view.findViewById(R.id.value_textview_edit_symptom_log_symptom_name);
        mBeganValueTextView =
                view.findViewById(R.id.value_textview_edit_symptom_log_symptom_began);
        mChangedValueTextView =
                view.findViewById(R.id.value_textview_edit_symptom_log_symptom_changed);
        mIntensityValueTextView =
                view.findViewById(R.id.value_textview_edit_symptom_log_symptom_intensity);

        mNameValueOptionButton = view.findViewById(R.id.imagebutton_symptom_log_name_option);
        mBeganValueOptionButton = view.findViewById(R.id.imagebutton_symptom_log_began_option);
        mChangedValueOptionButton =
                view.findViewById(R.id.imagebutton_symptom_log_changed_option);
        mIntensityValueOptionButton =
                view.findViewById(R.id.imagebutton_symptom_log_intensity_option);

        mButtonDone = view.findViewById(R.id.button_edit_symptom_log_done);

        // out database accesses
        mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
        mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);


        // if we have arguments for an id to edit
        if ( getArguments().getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) != null) {
            // get data from parent activity on which food log we're editing
            mBundle = getArguments();

            // get id as a string
            mSymptomLogIdString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
            if ( mSymptomLogIdString.contains(",") ) {
                Toast.makeText(thisActivity, "An array of log ID's were passed in, try again with only one to edit.", Toast.LENGTH_SHORT).show();
            }
            // turn it into its UUID
            mSymptomLogId = UUID.fromString(mSymptomLogIdString);
            // use that to get the log itself
            mSymptomLog = mSymptomLogViewModel.viewModelGetLogFromLogId(mSymptomLogId);
            mSymptom =
                    mSymptomViewModel.viewModelGetSymptomFromId(mSymptomLog.getLogSymptomId());

            // then the name value
            mSymptomLogSymptomName = mSymptom.getName();

            // then use the log to set the text views
            // set the default text to the data
            mNameValueTextView.setText(mSymptomLogSymptomName);
            mBeganValueTextView.setText(Util.stringFromInstant(mSymptomLog.getInstantBegan()));
            mChangedValueTextView.setText(Util.stringFromInstant(mSymptomLog.getInstantChanged()));
            mIntensityValueTextView.setText(String.valueOf(mSymptomLog.getLogSymptomIntensity()));

            // also throw listeners on them
            mNameValueOptionButton.setOnClickListener(this);
            mBeganValueOptionButton.setOnClickListener(this);
            mChangedValueOptionButton.setOnClickListener(this);
            mIntensityValueOptionButton.setOnClickListener(this);
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
        mChangeDateTime = getResources().getString(R.string.change_date_time);
        mChangeSymptom = getResources().getString(R.string.change_symptom);
        mChangeIntensityToastString = getResources().getString(R.string.change_intensity);

        switch (view.getId()) {
            // save button was pressed
            case R.id.button_edit_symptom_log_done:
                // done with editing
                Util.goToListSymptomLogActivity(null, thisActivity, mSymptomLogIdString);
                break;
            case R.id.imagebutton_symptom_log_began_option:
                // when symptom began option was clicked, so tell the user we'll go change that
                // date time
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();

                // set our relevant data to use in new location
                mBundleNext =
                        Util.setEditSymptomLogBundle(mSymptomLogIdString,
                        Util.ARGUMENT_CHANGE_SYMPTOM_LOG_BEGIN);

                // begin is a time, so go to the date time fragment to set it
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_symptom_log_changed_option:
                // changed date time was clicked, so tell the user we'll go change that date time
                Toast.makeText(getContext(), mChangeDateTime, Toast.LENGTH_SHORT).show();

                mBundleNext =
                        Util.setEditSymptomLogBundle(mSymptomLogIdString,
                                Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED);
                Util.startNextFragmentBundle(thisActivity,
                        getParentFragmentManager().beginTransaction(),
                        Util.fragmentContainerViewEdit, new SpecificDateTimeFragment(), mBundleNext);

                break;
            case R.id.imagebutton_symptom_log_name_option:

                Toast.makeText(getContext(), mChangeSymptom, Toast.LENGTH_SHORT).show();
                try {

                    Util.startNextActivityActionChangeIdArray(thisActivity,
                            mNextActivityClass,
                            Util.ARGUMENT_ACTION_EDIT,
                            Util.ARGUMENT_CHANGE_SYMPTOM_LOG_CHANGED,
                            Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, mSymptomLogIdString);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.imagebutton_symptom_log_intensity_option:

                Toast.makeText(getContext(), mChangeIntensityToastString, Toast.LENGTH_SHORT).show();
                mBundleNext =
                        Util.setEditSymptomLogBundle(mSymptomLogIdString,
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
