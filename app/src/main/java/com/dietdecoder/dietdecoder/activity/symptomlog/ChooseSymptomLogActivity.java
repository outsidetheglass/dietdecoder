package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewHolder;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class ChooseSymptomLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseSymptomLogActivity.this;

    //TODO fix names
    int mFragmentContainerView = Util.fragmentContainerViewChooseSymptomLog;
    Bundle mBundle;

    String mWhichFragmentGoTo, mSaveString, mEmptyTryAgainString;
    Button mButtonSaveName;

    private Fragment nextFragment = null;

    SymptomListAdapter mSymptomListAdapter;
    SymptomViewModel mSymptomViewModel;
    RecyclerView recyclerViewSymptomNameChoices;

    ColorStateList selectedColor;
    ColorStateList unSelectedColor;

    ArrayList<String> symptomsSelectedIdsArrayListStrings = null;


    public ChooseSymptomLogActivity() {
        super(R.layout.activity_choose_symptom_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_choose_symptom_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        if (savedInstanceState == null) {

            //save button
            mButtonSaveName = findViewById(R.id.button_choose_symptom_log_save);
            mButtonSaveName.setOnClickListener(this);


            // make the view for listing the items in the log
            recyclerViewSymptomNameChoices =
                    findViewById(R.id.recyclerview_new_symptom_log_name_choices);
            // add horizontal lines between each recyclerview item
            recyclerViewSymptomNameChoices.addItemDecoration(new DividerItemDecoration(recyclerViewSymptomNameChoices.getContext(),
                    DividerItemDecoration.VERTICAL));


            mSymptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff());
            recyclerViewSymptomNameChoices.setAdapter(mSymptomListAdapter);
            recyclerViewSymptomNameChoices.setLayoutManager(new LinearLayoutManager(this));
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);

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
            // do something
            startActivity(new Intent(thisActivity, MainActivity.class));
        } else {
            // do something
        }

        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View view) {

        // setup for adding which symptoms to add to the intent
        Context symptomViewHolderContext = getApplicationContext();
        Intent addSymptomIntensityIntent = new Intent(symptomViewHolderContext,
                NewSymptomLogActivity.class);

        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_choose_symptom_log_save:
                // get int
//
                // TODO check if any were chosen
                // TODO if none chosen, show error on save click
                // TODO if one or more was chosen, send those on to what time and intensity for each
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
                        int position = holder.getBindingAdapterPosition();
                        Symptom currentSymptomSelected =
                                mSymptomListAdapter.getCurrentList().get(position);

                        // add symptom ID to list that will be passed into fragment to add
                        // these symptoms in symptom logs
                        String currentSymptomIdString = String.valueOf(currentSymptomSelected.getSymptomId());
                        symptomsSelectedIdsArrayListStrings.add(currentSymptomIdString);
                    }
                }

                // after done with the for loop, all the symptoms to add have been put in to the
                // array
                // check if symptoms to add array is empty
                if ( symptomsSelectedIdsArrayListStrings.isEmpty() ) {
                    // if empty, alert user none were selected and don't do anything else
                    Toast.makeText(getApplicationContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                } else {
                    // if not empty, put the array into the intent to go add symptoms
                    mBundle.putString(Util.ARGUMENT_SYMPTOMS_TO_ADD,
                            String.valueOf(symptomsSelectedIdsArrayListStrings));
                    addSymptomIntensityIntent.putExtras(mBundle);
                    // go to set the intensity of the symptom
                    symptomViewHolderContext.startActivity(addSymptomIntensityIntent);

                }

                break;
//
            default:
                break;
        }
    }//end onClick
}

