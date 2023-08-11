package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = MainActivity.this;
  private Context thisContext;

  private Button saveButton, ingredientLogButton, symptomLogButton;

  private Intent foodLogIntent, symptomLogIntent;

  private SymptomViewModel symptomViewModel;
  private SymptomListAdapter symptomListAdapter;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //set view
      setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_main);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    // I set the context and activity this way because it's different between activities and
    // fragments and I want my code to be able to use the same thisContext and thisActivity sort
    // of variables
    thisContext = thisActivity.getApplicationContext();

    // I use mBundle to get the current values and set new values to bundle next, so I
    // know if I'm checking mBundle I'm never getting next fragment values, like what we
    // came in to this needing to change versus what to change next fragment
    //mBundle = getArguments();
    //mBundleNext = mBundle;

    //TODO
    // make symptoms to track at top of main activity,
    // then food log, symptom log next to each other at the bottom

    // Button to go to food log page
    ingredientLogButton = findViewById(R.id.button_ingredient_log);
    ingredientLogButton.setOnClickListener(this::onClick);

    // Button to got to recipe's page
    symptomLogButton = findViewById(R.id.button_symptom_log);
    symptomLogButton.setOnClickListener(this::onClick);

    //TODO put question by question way of adding symptoms here
    // Button to go to ingredient's list and edit and delete and add
//    saveButton = findViewById(R.id.button_main_save);
//    saveButton.setOnClickListener( view -> {
//      startActivity(new Intent(thisActivity, ListSymptomLogActivity.class));
//    });
    // make the view for listing the items in the log
//    RecyclerView recyclerViewLogASymptom = findViewById(R.id.recyclerview_log_a_symptom);
//    // add horizontal lines between each recyclerview item
//    recyclerViewLogASymptom.addItemDecoration(new DividerItemDecoration(recyclerViewLogASymptom.getContext(),
//            DividerItemDecoration.VERTICAL));
//    symptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff());
//    recyclerViewLogASymptom.setAdapter(symptomListAdapter);
//    recyclerViewLogASymptom.setLayoutManager(new LinearLayoutManager(this));
//    symptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
//    symptomViewModel.viewModelGetSymptomsToTrack().observe(this,
//            new Observer<List<Symptom>>() {
//              @Override
//              public void onChanged(List<Symptom> logs) {
//                // Update the cached copy of the words in the adapter.
//                symptomListAdapter.submitList(logs);
//              }
//            });


  } //end onCreate


    @Override
    public boolean onMenuItemClick(MenuItem item) {
      if (item.getItemId() == R.id.action_settings) {
                Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
                //TODO go to settings/preferences
        }
      else if (item.getItemId() == R.id.action_go_home) {
        Util.goToMainActivity(null, thisActivity);
      }
      else if (item.getItemId() == R.id.action_more) {
        //TODO make this have a menu to select from instead of the other activity
        Util.goToOtherActivity(null, thisActivity);
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
//
//        @Override
//        public boolean onCreateOptionsMenu(Menu menu){
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return super.onCreateOptionsMenu(menu);
//        }
//


  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      // save button was pressed
      case R.id.button_ingredient_log:
        Util.goToListIngredientLogActivity(null, thisActivity, null);
        break;

      case R.id.button_symptom_log:
        Util.goToListSymptomLogActivity(null, thisActivity, null);
        break;

      default:
        break;

    }
  }





} // end MainActivity
