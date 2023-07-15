package com.dietdecoder.dietdecoder.activity.ingredientlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListIngredientLogActivity extends AppCompatActivity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = ListIngredientLogActivity.this;
  private Context thisContext;

  Fragment mFragmentGoTo = null;

  private IngredientLogViewModel mIngredientLogViewModel;
  private IngredientViewModel mIngredientViewModel;
  private IngredientLogListAdapter mIngredientLogListAdapter;
  public ArrayList<Ingredient> mIngredients;

  public FloatingActionButton addButton;

  private Intent addIntent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_ingredient_log);


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_list_ingredient_log);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    thisContext = getBaseContext();


    // if we have no view made
    if (savedInstanceState == null) {
      // make the view for listing the items in the log
      RecyclerView recyclerViewIngredient = findViewById(R.id.recyclerview_list_ingredient_log);
      // add horizontal lines between each recyclerview item
      recyclerViewIngredient.addItemDecoration(new DividerItemDecoration(recyclerViewIngredient.getContext(),
              DividerItemDecoration.VERTICAL));


      mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
      mIngredients = mIngredientViewModel.viewModelGetAllIngredientArrayList();
      mIngredientLogListAdapter =
              new IngredientLogListAdapter(new IngredientLogListAdapter.LogDiff()
                      //, mIngredientViewModel
              );
      recyclerViewIngredient.setAdapter(mIngredientLogListAdapter);
      recyclerViewIngredient.setLayoutManager(new LinearLayoutManager(this));
      mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);

      mIngredientLogViewModel.viewModelGetAllIngredientLogs().observe(this,
              new Observer<List<IngredientLog>>() {
                @Override
                public void onChanged(List<IngredientLog> logs) {
                  // Update the cached copy of the words in the adapter.
                  mIngredientLogListAdapter.setIngredientLogListSubmitList(logs, mIngredients);
                  //TODO this is where we should be checking ingredient and recipe adapters
                  // and adding the ingredient or recipe if it doesn't exist

                }
              });

      // FAB to add new log
      addButton = findViewById(R.id.add_button_list_ingredient_log);
      addButton.setOnClickListener(this);
    }

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.add_button_list_ingredient_log:
        // go to the list of ingredients the user experiences to allow user to select which ones
        // they're having now and then make those ingredient logs
        //Util.goToChooseIngredientLogActivity(thisActivity);
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
      Util.goToMainActivity(thisActivity);
    }

    return false;
  }

}//end LogActivity


