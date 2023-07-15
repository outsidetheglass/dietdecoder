package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
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
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogListAdapter;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class FoodLogActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

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


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_food_log);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);
    //TODO make scrollable left and right (maybe tabs) for automatic filters too, by category
    // like snacks

    if (savedInstanceState == null) {
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

                  Log.d(TAG, " logs " + logs.toString());
                  // Update the cached copy of the words in the adapter.
                  mFoodLogListAdapter.submitList(logs);
                  //TODO this is where we should be checking ingredient and recipe adapters
                  // and adding the ingredient or recipe if it doesn't exist

                }
              });

      Log.d(TAG, " food number" + recyclerViewFood.getChildCount());
      Log.d(TAG,
              " mFoodLogViewModel getIngredientName " + mFoodLogViewModel.viewModelGetAllFoodLogs().toString());
      // FAB to add new log
      addButton = findViewById(R.id.add_button_food_log);
      addButton.setOnClickListener(this);
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.add_button_food_log:
        addIntent = new Intent(thisActivity, NewFoodLogActivity.class);
        startActivity(addIntent);
        break;
      default:
        break;
    }//end switch case
  }//end onClick



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

}//end LogActivity



