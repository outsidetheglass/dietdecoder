package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChooseSymptomActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseSymptomActivity.this;
    private Context thisContext;

    //TODO fix names
    int mFragmentContainerView = Util.fragmentContainerViewChooseSymptomLog;
    Bundle mBundle;

    String mTooManyTryAgainString, mSaveString, mEmptyTryAgainString;
    Button mButtonSaveName;

    ArrayList<String> mSymptomsSelectedIdsArrayListStrings = null;

    private Fragment nextFragment = null;

    SymptomListAdapter mSymptomListAdapter;
    SymptomViewModel mSymptomViewModel;
    SymptomLogViewModel mSymptomLogViewModel;
    RecyclerView recyclerViewSymptomNameChoices;

    ColorStateList selectedColor;
    ColorStateList unSelectedColor;

    public ChooseSymptomActivity() {
        super(R.layout.activity_choose_symptom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_choose_symptom);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        thisContext = thisActivity.getApplicationContext();


        if (savedInstanceState == null) {

            // basic variable setup
            mSymptomsSelectedIdsArrayListStrings = new ArrayList<>();
            if ( getIntent().getExtras().isEmpty() ) {
                mBundle = new Bundle();
            } else {
                mBundle = getIntent().getExtras();
            }

            //save button
            mButtonSaveName = findViewById(R.id.button_choose_symptom_save);
            mButtonSaveName.setOnClickListener(this);


            // make the view for listing the items in the log
            recyclerViewSymptomNameChoices =
                    findViewById(R.id.recyclerview_symptom_name_choices);
            // add horizontal lines between each recyclerview item
            recyclerViewSymptomNameChoices.addItemDecoration(new DividerItemDecoration(recyclerViewSymptomNameChoices.getContext(),
                    DividerItemDecoration.VERTICAL));


            mSymptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff());
            recyclerViewSymptomNameChoices.setAdapter(mSymptomListAdapter);
            recyclerViewSymptomNameChoices.setLayoutManager(new LinearLayoutManager(this));
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

            mSymptomViewModel.viewModelGetSymptomsToTrack().observe(this,
                    new Observer<List<Symptom>>() {
                        @Override
                        public void onChanged(List<Symptom> symptoms) {
                            // Update the cached copy of the words in the adapter.
                            mSymptomListAdapter.submitList(symptoms);

                        }
                    });


        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
            // do something

        } else if (item.getItemId() == R.id.action_go_home) {
            Util.goToMainActivity(thisActivity);
        }
        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View view) {

        // basic variables setup
        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
        mTooManyTryAgainString = getResources().getString(R.string.too_many_selected_not_saved);


        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_choose_symptom_save:
                // get int
//
                mSymptomsSelectedIdsArrayListStrings = new ArrayList<>();
                for (int childCount = recyclerViewSymptomNameChoices.getChildCount(), i = 0; i < childCount; ++i) {

                    final RecyclerView.ViewHolder holder =
                            recyclerViewSymptomNameChoices.getChildViewHolder(recyclerViewSymptomNameChoices.getChildAt(i));
                    TextView viewHolderTextView =
                            holder.itemView.findViewById(R.id.textview_symptom_item);
                    int textViewColorInt =
                            viewHolderTextView.getTextColors().getDefaultColor();

                    selectedColor =
                            holder.itemView.getResources().getColorStateList(R.color.selected_text_color,
                            getTheme());
                    int selectInt = selectedColor.getDefaultColor();

                    // if the text color of the current item in the list is the color set when
                    // selected by the user
                    if (Objects.equals(selectInt, textViewColorInt) ){
                        // get the ID of the symptom that has the selected color text
                        int position = holder.getBindingAdapterPosition();
                        String currentSymptomSelectedIdString =
                                mSymptomViewModel.viewModelGetSymptomsToTrack().getValue().get(position)
                                        .getSymptomId().toString();
                        // add that string ID to our array of selected symptoms
                        mSymptomsSelectedIdsArrayListStrings.add(currentSymptomSelectedIdString);

                    }
                }

                // after done with the for loop,
                // all the symptoms to add have been put into the array
                // check if we're here to edit a single log and therefore need only one selected
                Boolean needsOnlyOneLog = Boolean.FALSE;
                if ( mBundle.containsKey(Util.ARGUMENT_ACTION) ){
                    // if the action to be taken is edit,
                    // then we need to ensure we're only changing one
                    if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                            Util.ARGUMENT_ACTION_EDIT )){
                        needsOnlyOneLog = Boolean.TRUE;
                    }
                }
                Integer howManySelected = mSymptomsSelectedIdsArrayListStrings.size();

                // so check how many have been selected and put in the array
                // check if symptoms to add array is empty
                if ( mSymptomsSelectedIdsArrayListStrings.isEmpty() ) {
                    // if empty, alert user none were selected and don't do anything else
                    Toast.makeText(thisContext, mEmptyTryAgainString,
                            Toast.LENGTH_SHORT).show();
                } else if (  howManySelected > 1 && needsOnlyOneLog ) {
                    // the user selected more than one symptom, but is here to only change one log
                    // tell them to try again and select only one
                    Toast.makeText(thisContext, mTooManyTryAgainString,
                            Toast.LENGTH_SHORT).show();
                } else if (  howManySelected == 1 && needsOnlyOneLog ) {
                    // the user selected only one symptom, and is here to only change one log
                    Toast.makeText(thisContext, mSaveString,
                            Toast.LENGTH_SHORT).show();
                    // which means success so we can go back to edit after saving it
                    String symptomLogIdString =
                            mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
                    String symptomIdString = mSymptomsSelectedIdsArrayListStrings.get(0);
                    SymptomLog symptomLog =
                            mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(UUID.fromString(symptomLogIdString));
                    // get the symptom matching the symptom ID we got from the UI choice
                    Symptom symptom =
                            mSymptomViewModel.viewModelGetSymptomFromId(UUID.fromString(symptomIdString));
                    // save our updated symptom log with the new symptom ID and name
                    //TODO debug this it isn't saving
                    symptomLog.setSymptomLogSymptomName(symptom.getSymptomName());
                    symptomLog.setSymptomLogSymptomId(symptom.getSymptomId());
                    mSymptomLogViewModel.viewModelUpdateSymptomLog(symptomLog);

                    // done with setting the symptom go back to editing this symptom log
                    Util.goToEditActivityActionTypeId(null, thisActivity,
                            Util.ARGUMENT_ACTION_EDIT,
                            Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, symptomLogIdString);

                } else {
                    // if not empty, put the array into the intent to go add symptoms
                    mBundle =
                            Util.setBundleNewSymptomLogIntensity(mSymptomsSelectedIdsArrayListStrings);
                    // go to set the intensity of the symptom
                    Log.d(TAG, "symptoms array " + mSymptomsSelectedIdsArrayListStrings);
                    Util.goToAddSymptomLogWithBundle(thisActivity, mBundle);

                }

                break;
//
            default:
                break;
        }
    }//end onClick
}

