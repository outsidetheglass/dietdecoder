package com.dietdecoder.dietdecoder.activity.symptomlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListSymptomLogActivity extends AppCompatActivity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = ListSymptomLogActivity.this;
  private Context thisContext;

  Fragment mFragmentGoTo = null;

  private SymptomLogViewModel mSymptomLogViewModel;
  private SymptomViewModel mSymptomViewModel;
  private SymptomLogListAdapter mSymptomLogListAdapter;
  public ArrayList<Symptom> mSymptoms;

  public FloatingActionButton addButton;

  private Intent addIntent;
  private ArrayList<SymptomLog> mSymptomLogs;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_symptom_log);


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_list_symptom_log);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    thisContext = getBaseContext();

  //TODO Are you experiencing any symptoms right now?
    //Any past symptoms to log?
    //if yes, list symptoms to track from preferences

    //checkbox for each symptom
    // for each one clicked, open severity scale, autofill medium severity, option to click worse
    // more than it's ever been before, adds new severity to scale
    // when click save, for each severity scale save symptom severity
    //any new types of symptoms or conditions?
    //if yes, add
    // list previous symptoms and ask when it stopped/changed, if they did

    // if we had no view made, go straight to asking user for intensity
    if (savedInstanceState == null) {
      // make the view for listing the items in the log
      RecyclerView recyclerViewSymptom = findViewById(R.id.recyclerview_list_symptom_log);
      // add horizontal lines between each recyclerview item
      recyclerViewSymptom.addItemDecoration(new DividerItemDecoration(recyclerViewSymptom.getContext(),
              DividerItemDecoration.VERTICAL));


      mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
      mSymptoms = mSymptomViewModel.viewModelGetAllSymptomArrayList();
      mSymptomLogListAdapter = new SymptomLogListAdapter(new SymptomLogListAdapter.LogDiff() );
      recyclerViewSymptom.setAdapter(mSymptomLogListAdapter);
      recyclerViewSymptom.setLayoutManager(new LinearLayoutManager(this));
      mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);

      mSymptomLogViewModel.viewModelGetAllLiveData().observe(this,
              new Observer<List<SymptomLog>>() {
                @Override
                public void onChanged(List<SymptomLog> logs) {
                  // Update the cached copy of the words in the adapter.
                  mSymptomLogListAdapter.setSymptomLogListSubmitList(logs, mSymptoms);
                  //mSymptomLogs = logs;
                  //TODO this is where we should be checking ingredient and recipe adapters
                  // and adding the ingredient or recipe if it doesn't exist
                }
              });

      // FAB to add new log
      addButton = findViewById(R.id.add_button_list_symptom_log);
      addButton.setOnClickListener(this);
    }

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.add_button_list_symptom_log:
        // go to the list of symptoms the user experiences to allow user to select which ones
        // they're having now and then make those symptom logs
        Util.goToChooseSymptomActivity(thisContext, thisActivity);
        break;
        // TODO add edit and delete buttons in here
      default:
        break;
    }//end switch case
  }//end onClick



  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
      // TODO go to preferences when those have been made

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

}//end LogActivity



