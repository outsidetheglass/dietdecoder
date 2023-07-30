package com.dietdecoder.dietdecoder.activity.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeListAdapter;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = RecipeActivity.this;


  private RecipeViewModel mRecipeViewModel;
  private RecipeListAdapter mRecipeListAdapter;


  public FloatingActionButton editButton;
  public FloatingActionButton deleteButton;
  public FloatingActionButton addButton;

  private Intent editIntent;
  private Intent deleteIntent;
  private Intent addIntent;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    RecyclerView recyclerView = findViewById(R.id.recyclerview_recipe);

    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_recipe);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    Log.d(TAG, "onCreate: Before adapter");
    // start the adapter, which we use to see the recipes listed
    mRecipeListAdapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff());
    recyclerView.setAdapter(mRecipeListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    Log.d(TAG, "onCreate: After adapter, before model");

    // view model to be the connection to database
    mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

    Log.d(TAG, "onCreate: After model, before observer");
    // get all the recipes in a livedata model view
    mRecipeViewModel.viewModelGetAllRecipes().observe(this, recipes -> {

      Log.d(TAG, "onCreate: inside observe, before submitlist");
      // Update the cached copy of the words in the adapter.
      mRecipeListAdapter.submitList(recipes);
    });

    // FAB to add new recipe
    addButton = findViewById(R.id.add_button_recipe);
    addButton.setOnClickListener( view -> {
      addIntent = new Intent(thisActivity, AddRecipeActivity.class);
      //TODO fix this
//      addIntent.putExtra();
      startActivity(addIntent);
    });


    Log.d(TAG, "onCreate: tags work");

  }



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

}//end RecipeActivity



