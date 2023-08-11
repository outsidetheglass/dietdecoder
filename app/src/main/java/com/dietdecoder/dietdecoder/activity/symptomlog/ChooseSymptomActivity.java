package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
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
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChooseSymptomActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseSymptomActivity.this;
    private Context thisContext;

    //TODO fix names
    int mFragmentContainerView = Util.fragmentContainerViewChooseSymptomLog;
    Bundle mBundle, mBundleNext;

    String mTooManyTryAgainString, mSaveString, mEmptyTryAgainString;
    UUID mSymptomLogId, mSymptomId;

    SymptomLog mSymptomLog;
    Button mButtonSaveName;

    public FloatingActionButton addButton;
    ArrayList<String> mSymptomsSelectedIdsArrayListStrings = null;
    ArrayList<Symptom> mSymptomSelectedArrayList = null;

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

        // if they haven't started this activity yet
        if (savedInstanceState == null) {
            // check valid
            Util.checkValidFragment(getIntent().getExtras(), thisActivity);

            // basic variable setup
            mBundle = getIntent().getExtras();
            mBundleNext = mBundle;
            setDependentVariables();

            //save button
            mButtonSaveName = findViewById(R.id.button_choose_symptom_save);
            mButtonSaveName.setOnClickListener(this);


            // make the view for listing the items in the log
            recyclerViewSymptomNameChoices =
                    findViewById(R.id.recyclerview_symptom_name_choices);
            // add horizontal lines between each recyclerview item
            recyclerViewSymptomNameChoices.addItemDecoration(
                    new DividerItemDecoration(recyclerViewSymptomNameChoices.getContext(),
                    DividerItemDecoration.VERTICAL));


            mSymptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff());
            recyclerViewSymptomNameChoices.setAdapter(mSymptomListAdapter);
            recyclerViewSymptomNameChoices.setLayoutManager(new LinearLayoutManager(this));
            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

            mSymptomViewModel.viewModelGetAllLiveDataToTrack().observe(this,
                    new Observer<List<Symptom>>() {
                        @Override
                        public void onChanged(List<Symptom> symptoms) {
                            // Update the cached copy of the words in the adapter.
                            // true to only to list symptoms user wants to track are listed
                            mSymptomListAdapter.submitSymptomList(symptoms);
                            //checkRecylcerviewForChosenObject();

                        }
                    });
        } // end if they haven't started the activity yet

        // FAB to add new log
        addButton = findViewById(R.id.add_button_choose_symptom);
        addButton.setOnClickListener(this);
    } // end onCreate

    private void setSymptomVariables(){

        // get the objects selected in the UI from the adapter who gets it from view holder
        mSymptomsSelectedIdsArrayListStrings = new ArrayList<>();

    }

    private void setDependentVariables(){
        setSymptomVariables();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
            // do something

        } else if (item.getItemId() == R.id.action_go_home) {
            Util.goToMainActivity(null, thisActivity);
        }  else if (item.getItemId() == R.id.action_more) {

            // Initializing the popup menu and giving the reference as current logContext
            PopupMenu popupMenu = new PopupMenu(thisContext, findViewById(R.id.action_more));
            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.item_more_menu, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.END);
            // if an option in the menu is clicked
            popupMenu.setOnMenuItemClickListener(moreMenuItem -> {
                // which button was clicked
                switch (moreMenuItem.getItemId()) {

                    // go to the right activity
                    case R.id.more_all_symptoms:
                        Util.goToListSymptomActivity(null, thisActivity, null);
                        break;

                    case R.id.more_all_ingredients:
                        Util.goToListIngredientActivity(thisContext, thisActivity, null);
                        break;

                    case R.id.more_export_activity:
                        Util.goToExportActivity(thisContext, thisActivity);
                        break;

                    default:
                        break;
                }//end switch case for which menu item was chosen

                return true;
            });
            // Showing the popup menu
            popupMenu.show();

        }
        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_choose_symptom_save:

                //TODO debug if below actually saves, if it doesn't then I can use the code in
                // ChooseIngredientActivity to make it all work again

                // check what to do based on if it's empty, editing, too many are selected,
                // or if we need to save
                // then go to correct place or just toast the error
                Util.ifInvalidRepeatOrValidAdd(
                        thisActivity, mSymptomListAdapter, mSymptomLogViewModel, null, null);


                break;
//
            case R.id.add_button_choose_symptom:
                // go to the list of symptoms the user experiences to allow user to select which ones
                // they're having now and then make those symptom logs
                Util.goToAddEditSymptomActivity(thisContext, thisActivity, null);
            default:
                break;
        } // end cases switch
    }//end onClick

}// end activity

