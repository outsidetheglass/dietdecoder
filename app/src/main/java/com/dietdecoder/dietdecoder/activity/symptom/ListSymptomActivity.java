package com.dietdecoder.dietdecoder.activity.symptom;

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
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListSymptomActivity extends AppCompatActivity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = ListSymptomActivity.this;
  private Context thisContext;

  Fragment mFragmentGoTo = null;

  private SymptomViewModel mSymptomViewModel;
  private SymptomListAdapter mSymptomListAdapter;
  public ArrayList<Symptom> mSymptoms;

  public FloatingActionButton addButton;

  private Intent addIntent;
  private ArrayList<SymptomLog> mSymptomLogs;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_symptom);


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_list_symptom);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    thisContext = getBaseContext();

    // if we had no view made, go straight to asking user for intensity
    if (savedInstanceState == null) {
      // make the view for listing the items in the log
      RecyclerView recyclerViewSymptom = findViewById(R.id.recyclerview_list_symptom);
      // add horizontal lines between each recyclerview item
      recyclerViewSymptom.addItemDecoration(new DividerItemDecoration(recyclerViewSymptom.getContext(),
              DividerItemDecoration.VERTICAL));


      mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
      mSymptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff() );
      recyclerViewSymptom.setAdapter(mSymptomListAdapter);
      recyclerViewSymptom.setLayoutManager(new LinearLayoutManager(this));

      mSymptomViewModel.viewModelGetAllLiveData().observe(this,
              new Observer<List<Symptom>>() {
                @Override
                public void onChanged(List<Symptom> symptoms) {
                  // Update the cached copy of the words in the adapter.
                  mSymptomListAdapter.submitSymptomList(symptoms);
                }
              });

      // FAB to add new log
      addButton = findViewById(R.id.add_button_list_symptom);
      addButton.setOnClickListener(this);
    }

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.add_button_list_symptom:
        // go to the list of symptoms the user experiences to allow user to select which ones
        // they're having now and then make those symptom logs
        Util.goToAddEditSymptomActivity(thisContext, thisActivity, null);
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



