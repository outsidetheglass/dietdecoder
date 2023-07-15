package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogListAdapter;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ListFoodLogActivity extends AppCompatActivity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = ListFoodLogActivity.this;
  private Context thisContext;

  Fragment mFragmentGoTo = null;

  private FoodLogViewModel mFoodLogViewModel;
  private FoodLogListAdapter mFoodLogListAdapter;

  public FloatingActionButton addButton;

  private Intent addIntent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_symptom_log);


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_list_symptom_log);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    thisContext = getBaseContext();

    // if we had no view made, go straight to asking user for intensity
    if (savedInstanceState == null) {
      // make the view for listing the items in the log
      RecyclerView recyclerViewSymptom = findViewById(R.id.recyclerview_list_symptom_log);
      // add horizontal lines between each recyclerview item
      recyclerViewSymptom.addItemDecoration(new DividerItemDecoration(recyclerViewSymptom.getContext(),
              DividerItemDecoration.VERTICAL));


      mFoodLogListAdapter = new FoodLogListAdapter(new FoodLogListAdapter.LogDiff());
      recyclerViewSymptom.setAdapter(mFoodLogListAdapter);
      recyclerViewSymptom.setLayoutManager(new LinearLayoutManager(this));
      mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);

//      Log.d(TAG,
//              "food logs " + mFoodLogViewModel.viewModelGetAllFoodLogAfterDateTime(Instant.now().minus(10, ChronoUnit.DAYS)).toString());

      mFoodLogViewModel.viewModelGetAllFoodLogs().observe(this,
              new Observer<List<FoodLog>>() {
                @Override
                public void onChanged(List<FoodLog> logs) {
                  // Update the cached copy of the words in the adapter.
                  mFoodLogListAdapter.submitList(logs);
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
        Util.goToChooseActivityTypeId(thisActivity, Util.ARGUMENT_FOOD_LOG_ID_ARRAY);
        break;
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
      Util.goToMainActivity(thisActivity);
    }

    return false;
  }

}//end LogActivity



