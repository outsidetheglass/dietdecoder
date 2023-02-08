package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
//import com.dietdecoder.dietdecoder.database.log.Log;
//import com.dietdecoder.dietdecoder.ui.LogListAdapter;
//import com.dietdecoder.dietdecoder.ui.LogViewModel;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogListAdapter;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FoodLogActivity extends AppCompatActivity implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = FoodLogActivity.this;

  Fragment mFragmentGoTo = null;

  private FoodLogViewModel mFoodLogViewModel;
  private FoodLogListAdapter mFoodLogListAdapter;

  public FloatingActionButton addButton;

  private Intent addIntent;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food_log);

    //TODO make scrollable left and right (maybe tabs) for automatic filters too, by category
    // like snacks

    // mate the view for listing the items in the log
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
                }
            });

    // FAB to add new log
    addButton = findViewById(R.id.add_button_log);
    addButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.add_button_log:
        Toast.makeText(thisActivity, getResources().getString(R.string.adding),
                Toast.LENGTH_SHORT).show();
        addIntent = new Intent(thisActivity, NewFoodLogActivity.class);
        startActivity(addIntent);
        break;
      default:
        break;
    }//end switch case
  }//end onClick

}//end LogActivity



