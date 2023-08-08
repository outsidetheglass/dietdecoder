package com.dietdecoder.dietdecoder.activity.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class IngredientActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = IngredientActivity.this;


  private IngredientViewModel mIngredientViewModel;
  private IngredientListAdapter mIngredientListAdapter;

  private LiveData<List<Ingredient>> mActivityAllIngredients;

  public static final int NEW_INGREDIENT_ACTIVITY_REQUEST_CODE = 1;
  public static final int EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE = 2;
  public static final int DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE = 3;


  public FloatingActionButton editIngredientButton;
  public FloatingActionButton deleteIngredientButton;
  public FloatingActionButton addIngredientButton;

  private Intent editIngredientIntent;
  private Intent deleteIngredientIntent;
  private Intent addIngredientIntent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ingredient);

    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_ingredient);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    RecyclerView recyclerView = findViewById(R.id.recyclerview_ingredient);

    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));

    mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
    recyclerView.setAdapter(mIngredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


    mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

    // if not null, then set list of ingredients
    mActivityAllIngredients = mIngredientViewModel.viewModelGetAllLiveData(null);

    // turn LiveData into list and set that in Adapter so we can get positions
    mIngredientListAdapter.setIngredientList(mActivityAllIngredients.getValue());

    mActivityAllIngredients.observe(this, ingredients -> {
      // Update the cached copy of the words in the adapter.
      mIngredientListAdapter.submitList(ingredients);
    });


    // FAB to add new ingredient
    addIngredientButton = findViewById(R.id.add_button_ingredient);
    addIngredientButton.setOnClickListener( view -> {
      addIngredientIntent = new Intent(thisActivity, AddIngredientActivity.class);
      startActivity(addIngredientIntent);
    });

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




}//end IngredientActivity



