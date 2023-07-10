package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.foodlog.FoodLogActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.ListSymptomLogActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = MainActivity.this;
  private Context thisContext;

  private Button saveButton, foodLogButton, symptomLogButton;

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

    thisContext = thisActivity.getApplicationContext();

    //TODO
    // make symptoms to track at top of main activity,
    // then food log, symptom log next to each other at the bottom



    // Button to go to food log page
    foodLogButton = findViewById(R.id.button_food_log);
    foodLogButton.setOnClickListener( view -> {
      Util.goToFoodLogActivity(thisActivity);
    });
    // Button to got to recipe's page
    symptomLogButton = findViewById(R.id.button_symptom_log);
    symptomLogButton.setOnClickListener( view -> {
      Util.goToListSymptomLog(thisActivity);
    });

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
        Util.goToMainActivity(thisActivity);
      }
      else if (item.getItemId() == R.id.action_more) {
        //TODO make this have a menu to select from instead of the other activity
        Util.goToOtherActivity(thisActivity);
      }
      else {
        // do something
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







} // end MainActivity
