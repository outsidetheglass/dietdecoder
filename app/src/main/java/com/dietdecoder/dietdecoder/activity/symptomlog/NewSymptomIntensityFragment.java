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

import java.time.Duration;
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
    private KeyListener originalKeyListener;

    String mSaveString, mEmptyTryAgainString, mName, symptomIntensityLogIdString, mCurrentSymptomLogIdToAddString,
    mCurrentSymptomName, mSymptomLogIdsToAddStringArrayOriginal,
            symptomIntensityViewEditableString, mInvalidTryAgainString, mSymptomLogIdsToAddString,
            mSymptomLogIdsToAddOriginalString, mAllTimesAreSame;
    Boolean isIntensityViewEmpty;
    Integer symptomIntensity, mHowManyIdsToAdd;
    UUID mSymptomLogId;

    SymptomLog mCurrentSymptomLog;
    SymptomLogViewModel mSymptomLogViewModel;
    SymptomLogListAdapter mSymptomLogListAdapter;
    ArrayList<String> mSymptomLogIdsToAddStringArray;

    Fragment mNextFragment, mRepeatThisFragment, mDefaultNextFragment;
    Bundle mBundle, mBundleNext;


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
            goToListSymptomLog();
        } else {
            // get the info
            mBundle = getArguments();

            //set variables
            thisContext = getContext();
            mButtonSaveName = view.findViewById(R.id.button_new_symptom_intensity_log_save);
            //activate watching the save button for a click
            mButtonSaveName.setOnClickListener(this);
            // to pass on to the time and date fragments, save the original untouched array string
            mSymptomLogIdsToAddStringArrayOriginal =
                    mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD_ORIGINAL);
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
            // default next place to go should be the next fragment
            mNextFragment = new LogDateTimeChoicesFragment();
            mBundleNext = new Bundle();
            mTextViewSymptomName = view.findViewById(R.id.subsubtitle_textview_symptom_name);
            // this is our holder string, remove from this arraylist as we go
            mSymptomLogIdsToAddStringArray = new ArrayList<String>();
            // after intensity is done being set the next default place is date times
            mDefaultNextFragment = new LogDateTimeChoicesFragment();
            // repeating this fragment
            mRepeatThisFragment = new NewSymptomIntensityFragment();
            mSymptomLogIdsToAddString = mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD);
            mSymptomLogIdsToAddOriginalString =
                    mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD_ORIGINAL);

            //how many ids to add
            mHowManyIdsToAdd =
                    Integer.parseInt(mBundle.getString(Util.ARGUMENT_HOW_MANY_SYMPTOM_LOG_ID_IN_ARRAY));

            // parse the name from the array of ids and put that in the textview
            // clean the string of its brackets and spaces
            // turn into array list
            mSymptomLogIdsToAddStringArray = Util.cleanBundledStringIntoArrayList(
                    mSymptomLogIdsToAddString
            );
            // get first log in the list array
            mCurrentSymptomLog =
                    mSymptomLogViewModel.viewModelGetSymptomLogFromId(UUID.fromString(
                                    mSymptomLogIdsToAddStringArray.get(0)
                            ));

            // put in the UI what symptom we're changing now
            mTextViewSymptomName.setText(mCurrentSymptomLog.getSymptomName());


        //TODO change this to integer instead of edittext
            mEditTextSymptomIntensity =
                    view.findViewById(R.id.edittext_new_symptom_intensity);
            mEditTextSymptomIntensity = Util.setEditTextWordWrapNoEnter(mEditTextSymptomIntensity);

            //TODO get the median choice out of recent logs and set the default
            // intensity/severity to that
//            //TODO convert this average duration to the intensity log add
//            Duration averageDuration =
//                    mSymptomLogViewModel.viewModelGetAverageSymptomDuration(mCurrentSymptomLog.getSymptomName());
//            // and set default for changed time to be from that average
//            mCurrentSymptomLog.setInstantChanged(Util.instantFromDurationAndStartInstant(mInstantBegan,
//                    averageDuration));

            // Integer medianRecentIntensity = ;
            //mEditTextSymptomIntensity.setText(mCurrentSymptomLog.getIntensityScale
            // (medianRecentIntensity));
            //mEditTextSymptomIntensity.setOnClickListener(this::onClick);

            // TODO have delete button

            // TODO have progress bar on bottom for how many symptoms are to be set and have been
            //  set

        }

    // Inflate the layout for this fragment
    return view;
    }


    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
        mInvalidTryAgainString =
                getResources().getString(R.string.invalid_input_not_int_between_one_and_ten_try_again);
        mAllTimesAreSame = getResources().getString(R.string.heads_up_all_times_same);

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_new_symptom_intensity_log_save:
                // get what the user input for intensity
                symptomIntensityViewEditableString = mEditTextSymptomIntensity.getText().toString();
                isIntensityViewEmpty = TextUtils.isEmpty(symptomIntensityViewEditableString);

                // if view is empty
                if ( isIntensityViewEmpty ) {
                    // TODO set ask user to leave as default, being the most common used number
                    //  for this symptom
                    // set intent to tell user try again
                    Toast.makeText(getContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                }
                // if the number is not between 1 and 10 solid integer
                else if ( !Util.isIntegerBetween1to10( Integer.parseInt(symptomIntensityViewEditableString) ) ) {
                    // not a valid intensity number, tell user to try again
                    Toast.makeText(getContext(), mInvalidTryAgainString, Toast.LENGTH_SHORT).show();
                }
                else {
                    // if view has a valid value
                    // tell user we're saving it
                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();

                    // get the intensity from the user input
                    symptomIntensity =
                            Integer.parseInt(symptomIntensityViewEditableString);
                    // then set the intensity
                    mCurrentSymptomLog.setIntensityScale(symptomIntensity);
                    // update the log
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(mCurrentSymptomLog);

                    Log.d(TAG, "before removing mSymptomLogIdsToAddStringArray " +
                            mSymptomLogIdsToAddStringArray.toString());
                    Log.d(TAG, "mSymptomLogIdsToAddStringArray.size() " +
                            mSymptomLogIdsToAddStringArray.size());
                    // we've added it in, so remove this symptom from the ones to add
                    mSymptomLogIdsToAddStringArray.remove(mCurrentSymptomLog.getSymptomId().toString());
                    // and lower our count for how many left to add
                    mHowManyIdsToAdd = mHowManyIdsToAdd - 1;

                    Log.d(TAG, "after removing mSymptomLogIdsToAddStringArray " +
                            mSymptomLogIdsToAddStringArray.toString());
                    Log.d(TAG, "mSymptomLogIdsToAddStringArray.size() " +
                            mSymptomLogIdsToAddStringArray.size());
                    // if this is the last intensity to check,
                    // checked by if the number of Ids left to add logs of is bigger than 0
                    if ( mHowManyIdsToAdd == 0 ) {
                        //TODO make times show up and modifiable back in add new logs if I want,
                        // probably don't need all these fragments, or make it a preference when
                        // they want to be asked when the symptom happened

                        // go to date fragment to set when the symptom(s) happened
                        mNextFragment = mDefaultNextFragment;
                    } else {
                        // not the last in the array, repeat this fragment
                        mNextFragment = mRepeatThisFragment;
                        // actually go to the next place now
                    }
                    goToNextFragment(mNextFragment);

                }
                break;
//
            default:
                break;
        }
    }//end onClick


    private void goToListSymptomLog(){
        thisContext.startActivity(new Intent(thisContext,
                ListSymptomLogActivity.class));
    }

    private void goToNextFragment(Fragment nextFragment){
        // and now which fragment to go to next

        // if nothing given use the default fragment
        if ( Objects.isNull(nextFragment)) {
            // so just go home
            nextFragment = mDefaultNextFragment;
        } else {
            // we were given a fragment to use
            // let's set all the info the next fragment will need

            // if this is not the last intensity to check
            if ( mHowManyIdsToAdd > 0 ) {
                // if there's still logs left to change intensity of
                // add the current log IDs left to add into the bundle
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD,
                        mSymptomLogIdsToAddStringArray.toString());
                // and also add the original full array
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD_ORIGINAL,
                        mSymptomLogIdsToAddStringArrayOriginal);
                mBundleNext.putString(Util.ARGUMENT_HOW_MANY_SYMPTOM_LOG_ID_IN_ARRAY,
                        mHowManyIdsToAdd.toString());
                // we're still changing intensity
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_INTENSITY);
            } else {
                // there are no intensities left to set
                // add the original array as our array
                // the rest are all set together so it doesn't need to be a changing array, so we
                // don't need original and mutable array both, just need one
                mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD,
                    mSymptomLogIdsToAddStringArrayOriginal);
                // reset to setting begin time date for the next fragment
                mBundleNext.putString(Util.ARGUMENT_CHANGE, Util.ARGUMENT_CHANGE_SYMPTOM_BEGIN);

                Log.d(TAG,
                        "mSymptomLogIdsToAddStringArrayOriginal " + mSymptomLogIdsToAddStringArrayOriginal);
                // if there's more than one symptom,
                // checked by if original string has a comma,
                // alert user that all symptoms will have the same time and date,
                // they can be individually edited from the symptom log menu
                if (mSymptomLogIdsToAddStringArrayOriginal.contains(",")) {
                    Toast.makeText(getContext(), mAllTimesAreSame, Toast.LENGTH_SHORT).show();
                }
            }

            mBundleNext.putString(Util.ARGUMENT_FRAGMENT_FROM,
                    Util.ARGUMENT_FROM_INTENSITY_SYMPTOM);
            // put which we're changing into the bundle
            nextFragment.setArguments(mBundleNext);
        }

        // actually go to the next place now
        getParentFragmentManager().beginTransaction()
                .replace(Util.fragmentContainerViewAddSymptomLog, nextFragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }


}
