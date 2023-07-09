package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
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
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseSymptomLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseSymptomLogActivity.this;
    private Context thisContext;

    //TODO fix names
    int mFragmentContainerView = Util.fragmentContainerViewChooseSymptomLog;
    Bundle mBundle;

    String mWhichFragmentGoTo, mSaveString, mEmptyTryAgainString;
    Button mButtonSaveName;

    ArrayList<String> mSymptomsSelectedIdsArrayListStrings = null;

    private Fragment nextFragment = null;

    SymptomListAdapter mSymptomListAdapter;
    SymptomViewModel mSymptomViewModel;
    RecyclerView recyclerViewSymptomNameChoices;

    ColorStateList selectedColor;
    ColorStateList unSelectedColor;

    public ChooseSymptomLogActivity() {
        super(R.layout.activity_choose_symptom_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_choose_symptom_log);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        thisContext = thisActivity.getApplicationContext();


        if (savedInstanceState == null) {

            // basic variable setup
            mSymptomsSelectedIdsArrayListStrings = new ArrayList<>();
            mBundle = new Bundle();

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

        // basic variables setup
        // the next activity to go to from here
        Intent addSymptomIntensityIntent = new Intent(thisActivity,
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
                for (int childCount = recyclerViewSymptomNameChoices.getChildCount(), i = 0; i < childCount; ++i) {

                    final RecyclerView.ViewHolder holder =
                            recyclerViewSymptomNameChoices.getChildViewHolder(recyclerViewSymptomNameChoices.getChildAt(i));
                    TextView viewHolderTextView =
                            holder.itemView.findViewById(R.id.textview_symptom_item);
                    //Log.d(TAG, "viewHolderTextView "+viewHolderTextView.getText());

                    int textViewColorInt =
                            viewHolderTextView.getTextColors().getDefaultColor();

                    selectedColor =
                            holder.itemView.getResources().getColorStateList(R.color.selected_text_color,
                            getTheme());
                    int selectInt = selectedColor.getDefaultColor();
//                    Log.d(TAG, "selectInt "+selectInt);
//                    Log.d(TAG, "textViewColorInt"+textViewColorInt);

                    // if the text color of the current item in the list is the color set when
                    // selected by the user
                    if (Objects.equals(selectInt, textViewColorInt) ){
                        // get the ID of the symptom that has the selected color text
                        int position = holder.getBindingAdapterPosition();
                        String currentSymptomSelectedViewModel =
                                mSymptomViewModel.viewModelGetSymptomsToTrack().getValue().get(position)
                                        .getSymptomId().toString();
                        // add that string ID to our array of selected symptoms
                        mSymptomsSelectedIdsArrayListStrings.add(currentSymptomSelectedViewModel);

                    }
                }

                // after done with the for loop,
                // all the symptoms to add have been put into the array
                // so first we check if symptoms to add array is empty
                if ( mSymptomsSelectedIdsArrayListStrings.isEmpty() ) {
                    // if empty, alert user none were selected and don't do anything else
                    Toast.makeText(getApplicationContext(), mEmptyTryAgainString, Toast.LENGTH_SHORT).show();
                } else {
                    // if not empty, put the array into the intent to go add symptoms
                    mBundle.putString(Util.ARGUMENT_SYMPTOM_IDS_ARRAY_TO_ADD,
                            String.valueOf(mSymptomsSelectedIdsArrayListStrings));
                    mBundle.putString(Util.ARGUMENT_HOW_MANY_SYMPTOM_LOG_ID_IN_ARRAY,
                            String.valueOf(mSymptomsSelectedIdsArrayListStrings.size()));
                    mBundle.putString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY_TO_ADD_ORIGINAL, String.valueOf(mSymptomsSelectedIdsArrayListStrings));
                    addSymptomIntensityIntent.putExtras(mBundle);
//                    Log.d(TAG, "mSymptomListIdString "+ mSymptomsSelectedIdsArrayListStrings.toString() );

                    // go to set the intensity of the symptom
                    startActivity(addSymptomIntensityIntent);

                }

                break;
//
            default:
                break;
        }
    }//end onClick
}

