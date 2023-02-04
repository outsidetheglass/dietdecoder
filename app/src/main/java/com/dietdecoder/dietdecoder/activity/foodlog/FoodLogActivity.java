package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
//import com.dietdecoder.dietdecoder.database.log.Log;
//import com.dietdecoder.dietdecoder.ui.LogListAdapter;
//import com.dietdecoder.dietdecoder.ui.LogViewModel;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogListAdapter;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FoodLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = FoodLogActivity.this;


  private FoodLogViewModel mFoodLogViewModel;
  private FoodLogListAdapter mFoodLogListAdapter;
  public static final int NEW_LOG_ACTIVITY_REQUEST_CODE = 1;
  public static final int EDIT_LOG_ACTIVITY_REQUEST_CODE = 2;
  public static final int DELETE_LOG_ACTIVITY_REQUEST_CODE = 3;


  public FloatingActionButton editButton;
  public FloatingActionButton deleteButton;
  public FloatingActionButton addButton;

  private Intent editIntent;
  private Intent deleteIntent;
  private Intent addIntent;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_log);

    //TODO make scrollable left and right (maybe tabs) for snacks too, by category
    //Making everything food first, add Drink in after Food works
    //RecyclerView recyclerViewDrink = findViewById(R.id.recyclerview_log_drink);
    RecyclerView recyclerViewFood = findViewById(R.id.recyclerview_log_food);
    // add horizontal lines between each recyclerview item
    recyclerViewFood.addItemDecoration(new DividerItemDecoration(recyclerViewFood.getContext(),
            DividerItemDecoration.VERTICAL));


    mFoodLogListAdapter = new FoodLogListAdapter(new FoodLogListAdapter.LogDiff());
    recyclerViewFood.setAdapter(mFoodLogListAdapter);
    recyclerViewFood.setLayoutManager(new LinearLayoutManager(this));
    mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);

    mFoodLogViewModel.viewModelGetAllFoodLogs().observe(this,
            new Observer<List<FoodLog>>() {
              @Override
              public void onChanged(List<FoodLog> logs) {
                // Update the cached copy of the words in the adapter.
                mFoodLogListAdapter.submitList(logs);
                Log.d(TAG, "FoodLogActivity in Observer on Change, newName=" + logs.get(0).getMIngredientName());
              }
            });

    // Button to edit log
//    editButton = findViewById(R.id.edit_button_log);
//    editButton.setOnClickListener( view -> {
//
//      //TODO uncomment edit when made activity
//      editIntent = new Intent(thisActivity, EditLogActivity.class);
//      //TODO fix depreciated forResult
//      startActivityForResult(editIntent, EDIT_LOG_ACTIVITY_REQUEST_CODE);
//    });
//    // Button to delete log
//    //TODO get delete working and uncomment this
//    deleteButton = findViewById(R.id.delete_button_log);
//    deleteButton.setOnClickListener( view -> {
//      deleteIntent = new Intent(thisActivity, DeleteLogActivity.class);
//      startActivityForResult(deleteIntent, DELETE_LOG_ACTIVITY_REQUEST_CODE);
//    });

    // FAB to add new log
    addButton = findViewById(R.id.add_button_log);
    addButton.setOnClickListener( view -> {
      addIntent = new Intent(thisActivity, NewFoodLogChoicesActivity.class);
      startActivityForResult(addIntent, NEW_LOG_ACTIVITY_REQUEST_CODE);
    });

  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // check which activity completed and work with the returned data
      if (requestCode == NEW_LOG_ACTIVITY_REQUEST_CODE) {
        newLogActivityResult(resultCode, data);
      }
//    //TODO get delete and edit working and uncomment this
//      else if (requestCode == EDIT_LOG_ACTIVITY_REQUEST_CODE) {
//        editLogActivityResult(resultCode, data);
//      }
//      else if (requestCode == DELETE_LOG_ACTIVITY_REQUEST_CODE) {
//        deleteLogActivityResult(resultCode, data);
//      } //end request codes to run method

    }//end request codes activity results


  private void newLogActivityResult(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {

    }//end result_ok
    else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_saved,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end new log activity result

  private void editLogActivityResult(int resultCode, Intent data) {
      // if editing log was successful
      if (resultCode == RESULT_OK) {

        // set which log we're updating
        String oldLogName = data.getStringExtra(Util.ARGUMENT_OLD_NAME);
        String oldLogIngredient = data.getStringExtra(Util.ARGUMENT_OLD_INGREDIENT);
        //Log logToUpdate = mLogViewModel.viewModelGetLogFromNameIngredient(logName, logIngredient);

        //TODO finish bringing the new_name arguments into Util
        Boolean isNewNameEmpty = data.getBooleanExtra("new_name", false);
        Boolean isNewIngredientEmpty = data.getBooleanExtra("new_ingredient", false);
        // if the new name and ingredient value was set from the edit activity, update it
        if (!isNewNameEmpty && !isNewIngredientEmpty) {
          String newLogName = data.getStringExtra("new_name");
          String newLogIngredient = data.getStringExtra("new_ingredient");
          //TODO uncomment when activity is working
//          mLogViewModel.viewModelUpdateIngredient(oldLogName, oldLogIngredient, newLogIngredient);
//          mLogViewModel.viewModelUpdateName(oldLogName, oldLogIngredient, newLogName);
        }
        // only update name
        else if (!isNewNameEmpty) {
          String newLogName = data.getStringExtra("new_name");
          //TODO uncomment when activity is working
//          mLogViewModel.viewModelUpdateName(oldLogName, oldLogIngredient, newLogName);
        }
        // now only update ingredient
        else if (!isNewIngredientEmpty) {
          String newLogIngredient = data.getStringExtra("new_ingredient");
          //TODO uncomment when activity is working
//          mLogViewModel.viewModelUpdateIngredient(oldLogName, oldLogIngredient, newLogIngredient);
        } //end updating log
      }//end result_ok
      // edit was not successful, so tell user with toast
      else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_updated,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end edit log activity result

  private void deleteLogActivityResult(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {

      // set which log we're updating
      String logName = data.getStringExtra("log_name");
      String logIngredient = data.getStringExtra("log_ingredient");

      // log must exist if we want to delete it, so let's check
      // check all the logs listed right now to see if the log is one of them
      Boolean logExists = Boolean.FALSE;
      //TODO uncomment when activity is working
//      for (int i=0; i < mLogListAdapter.getCurrentList().size(); i++){
//        Log currentLog = mLogListAdapter.getCurrentList().get(i);
//        if (currentLog.getLogName() == logName && currentLog.getLogIngredient() == logIngredient) {
//          logExists = Boolean.TRUE;
//        }
//      }

      // if the log exists, delete it
      if ( logExists ) {
        // TODO fix this, it won't delete because livedata is being weird
        //mLogViewModel.viewModelDelete(logName, logIngredient);

      }
      // log does not exist, make toast to tell user can't delete
      else {
        Toast.makeText(
          getApplicationContext(),
          R.string.value_not_found_not_deleted,
          Toast.LENGTH_LONG).show();

      }

    }//end result_ok
    else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_deleted,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end delete log activity result



}//end LogActivity



