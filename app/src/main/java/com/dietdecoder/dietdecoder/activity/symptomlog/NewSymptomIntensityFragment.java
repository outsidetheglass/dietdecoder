package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.foodlog.LogDateTimeChoicesFragment;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogBrandFragment;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.Objects;
import java.util.UUID;

public class NewSymptomIntensityFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, "onClick in if: mName = " + mName);

    Button mButtonSaveName;
    EditText mEditTextSymptomIntensity;
    ListView mListView;

    String mSaveString, mEmptyTryAgainString, mName, symptomIntensityLogIdString;
    Boolean isIntensityViewEmpty;

    private KeyListener originalKeyListener;

    Bundle mBundle;

    SymptomLogViewModel mSymptomLogViewModel;
    SymptomViewModel mSymptomViewModel;
    SymptomListAdapter mSymptomListAdapter;

    public NewSymptomIntensityFragment() {
        super(R.layout.fragment_new_symptom_intensity_log);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_symptom_intensity_log, container,
                false);

        mBundle = getArguments();

        //TODO change this to integer instead of edittext
        mEditTextSymptomIntensity =
                view.findViewById(R.id.edittext_new_symptom_intensity);
        mEditTextSymptomIntensity = Util.setEditTextWordWrapNoEnter(mEditTextSymptomIntensity);
        //mEditTextSymptomIntensity.setOnClickListener(this::onClick);

        mButtonSaveName = view.findViewById(R.id.button_new_symptom_intensity_log_save);
        mButtonSaveName.setOnClickListener(this);

    // Inflate the layout for this fragment
    return view;
    }

    @Override
    public void onClick(View view) {

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_new_symptom_intensity_log_save:
                isIntensityViewEmpty = TextUtils.isEmpty(mEditTextSymptomIntensity.getText());
                // if view is empty
                if ( isIntensityViewEmpty ) {
                    // set intent to tell user try again
                    Toast.makeText(getContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                } else {
                    // if view has a values
                    Toast.makeText(getContext(), mSaveString, Toast.LENGTH_SHORT).show();
                    // get int
                    Integer symptomIntensity =
                            Integer.getInteger(mEditTextSymptomIntensity.getText().toString());



                    mSymptomLogViewModel =
                            new ViewModelProvider(this).get(SymptomLogViewModel.class);
                    UUID mSymptomLogId =
                            UUID.fromString(mBundle.getString(Util.ARGUMENT_SYMPTOM_ID));
                    SymptomLog symptomIntensityLog =
                            mSymptomLogViewModel.viewModelGetSymptomLogFromId(mSymptomLogId);
                    // set the intensity
                    symptomIntensityLog.setSeverityScale(symptomIntensity);
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(symptomIntensityLog);

                    //TODO get go to date working
                    // with the ID of symptom intensity log go set date
                    symptomIntensityLogIdString =
                            symptomIntensityLog.getSymptomLogId().toString();
                    Bundle mBundleNext = new Bundle();
                    mBundleNext.putString(Util.ARGUMENT_SYMPTOM_LOG_ID,
                            symptomIntensityLogIdString);
                    mBundleNext.putString(Util.ARGUMENT_FRAGMENT_FROM,
                            Util.ARGUMENT_FROM_INTENSITY_SYMPTOM);
                    // and which fragment to go to next
                    Fragment mNextFragment = new LogDateTimeChoicesFragment();
                    // put which we're changing into the bundle
                    mNextFragment.setArguments(mBundleNext);
                    // actually go to the next place now
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(Util.fragmentContainerViewAddSymptomLog, mNextFragment);
                    ft.commit();

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