package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.LogDateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogBrandFragment;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NewSymptomIntensityFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);
    private Context thisContext;

    Button mButtonSaveName;
    EditText mEditTextSymptomIntensity;
    TextView mTextViewSymptomName;
    ListView mListView;

    String mSaveString, mEmptyTryAgainString, mName, symptomIntensityLogIdString,
    mCurrentSymptomName, mSymptomLogIdsToAddStringOriginal;
    Boolean isIntensityViewEmpty;

    private KeyListener originalKeyListener;

    Bundle mBundle;

    SymptomLog mCurrentSymptomLog;
    SymptomLogViewModel mSymptomLogViewModel;
    SymptomLogListAdapter mSymptomLogListAdapter;
    ArrayList<String> mSymptomLogIdsToAddString;

    public NewSymptomIntensityFragment() {
        super(R.layout.fragment_new_symptom_intensity_log);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_symptom_intensity_log, container,
                false);


        // get the info about which symptom we're logging
        // check if info passed in exists, if not then go home
        if ( getArguments() == null ) {
            // there's no information about which symptom to add, so
            // tell the user that they got here by mistake, it's a bug
            // must choose which symptom before this activity
            String mWrongPlaceLetsGoHome =
                    getResources().getString(R.string.wrong_place_lets_go_home);
            Toast.makeText(thisContext, mWrongPlaceLetsGoHome,
                    Toast.LENGTH_SHORT).show();
            Intent goHomeIntent = new Intent(thisContext,
                    MainActivity.class);
            thisContext.startActivity(goHomeIntent);
        } else {
            // get the info
            mBundle = getArguments();

            //set variables
            thisContext = getContext();
            mButtonSaveName = view.findViewById(R.id.button_new_symptom_intensity_log_save);
            //activate watching the save button for a click
            mButtonSaveName.setOnClickListener(this);
            // to pass on to the time and date fragments, save the original untouched array string
            mSymptomLogIdsToAddStringOriginal =
                    mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD_ORIGINAL);


            // TODO add symptom log for all symptom IDs chosen
            // TODO fix, right now it only will work for first symptom and maybe not even that
            // TODO make it work for intensity for all symptoms

            // parse the name from the array of ids and put that in the textview
            //TODO when intensity is chosen, remove that Id from list to check
            mTextViewSymptomName = view.findViewById(R.id.subsubtitle_textview_symptom_name);
            // this is our holder string, remove from this arraylist as we go
            mSymptomLogIdsToAddString = new ArrayList<String>();

            for (String symptomLogIdString :
                    Util.cleanArrayString(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD)).split(",")) {
                mSymptomLogIdsToAddString.add(symptomLogIdString);
            }
            mCurrentSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromId(UUID.fromString(mSymptomLogIdsToAddString.get(0)));

            mTextViewSymptomName.setText(mCurrentSymptomLog.getSymptomName());


        //TODO change this to integer instead of edittext
            mEditTextSymptomIntensity =
                    view.findViewById(R.id.edittext_new_symptom_intensity);
            mEditTextSymptomIntensity = Util.setEditTextWordWrapNoEnter(mEditTextSymptomIntensity);

            //TODO get the median choice out of recent logs and set the default
            // intensity/severity to that

            // Integer medianRecentSeverity = ;
            //mEditTextSymptomIntensity.setText(mCurrentSymptomLog.getSeverityScale(medianRecentSeverity));

            //mEditTextSymptomIntensity.setOnClickListener(this::onClick);

            //TODO list all symptoms and their default values
            // TODO have edit and delete buttons

        }






    // Inflate the layout for this fragment
    return view;
    }

    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
        String mAllTimesAreSame = getResources().getString(R.string.heads_up_all_times_same);

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_new_symptom_intensity_log_save:
                // get int

                Editable symptomIntensityViewEditable =
                        mEditTextSymptomIntensity.getText();
                isIntensityViewEmpty = TextUtils.isEmpty(symptomIntensityViewEditable.toString());
                // if view is empty
                if ( isIntensityViewEmpty ) {
                    // TODO set ask user to leave as default, being the most common used number
                    //  for this symptom
                    // set intent to tell user try again
                    Toast.makeText(getContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                } else {
                    // if view has a values
                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();

                    // set the intensity
                    Integer symptomIntensity =
                            Integer.parseInt(symptomIntensityViewEditable.toString());
                    mSymptomLogViewModel =
                            new ViewModelProvider(this).get(SymptomLogViewModel.class);
                    UUID mSymptomLogId =
                            UUID.fromString(mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID));
                    SymptomLog symptomIntensityLog =
                            mSymptomLogViewModel.viewModelGetSymptomLogFromId(mSymptomLogId);
                    symptomIntensityLog.setSeverityScale(symptomIntensity);
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(symptomIntensityLog);

                    // we've added it in, so remove this symptom from the ones to add
                    mSymptomLogIdsToAddString.remove(0);

                    // if this is the last intensity to check
                    if ( mSymptomLogIdsToAddString.isEmpty() ) {
                        // if there's more than one symptom,
                        // checked by if original string has a comma,
                        // alert user that all symptoms will have the same time and date,
                        // they can be individually edited from the symptom log menu
                        if (mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD).contains(",")) {
                            Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT).show();
                        }
                        //TODO make times show up and modifiable back in add new logs if I want,
                        // probably don't need all these fragments, or make it a preference

                        // go to date fragment to set when the symptom happened
                        symptomIntensityLogIdString =
                                symptomIntensityLog.getSymptomLogId().toString();
                        Bundle mBundleNext = new Bundle();
                        // set the IDs to add to be the ones from the previous bundle
                        mBundleNext.putString( Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD,
                                mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD) );
                        mBundleNext.putString(Util.ARGUMENT_FRAGMENT_FROM,
                                Util.ARGUMENT_FROM_INTENSITY_SYMPTOM);
                        mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN);
                        // and which fragment to go to next
                        Fragment mNextFragment = new LogDateTimeChoicesFragment();
                        // put which we're changing into the bundle
                        mNextFragment.setArguments(mBundleNext);
                        // actually go to the next place now
                        getParentFragmentManager().beginTransaction()
                                .replace(Util.fragmentContainerViewAddSymptomLog, mNextFragment)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        // not the last in the array, repeat this fragment
                        Fragment mNextFragment = new NewSymptomIntensityFragment();
                        Bundle mBundleNext = new Bundle();
                        mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD_ORIGINAL,
                                mSymptomLogIdsToAddStringOriginal);
                        mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD,
                                mSymptomLogIdsToAddString.toString());
                        // put which we're changing into the bundle
                        mNextFragment.setArguments(mBundleNext);
                        // actually go to the next place now
                        getParentFragmentManager().beginTransaction()
                                .replace(Util.fragmentContainerViewAddSymptomLog, mNextFragment)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                    // go back to new symptom if replacing fragment doesn't work
//                    Intent listSymptomIntensityIntent = new Intent(getContext(),
//                            ListSymptomLogActivity.class);
//                    startActivity(listSymptomIntensityIntent);

                }
                break;
//
            default:
                break;
        }
    }//end onClick



}
