package com.dietdecoder.dietdecoder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import com.dietdecoder.dietdecoder.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//TODO fix edit delete button
public class MainActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  private IngredientViewModel mIngredientViewModel;
  private IngredientListAdapter mIngredientListAdapter;
  public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

  // binding for Data Binding for edit and delete by item position
  private ActivityMainBinding binding;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        // adapter is passing context so the cardClicked can make a toast pop up
        mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff(), this, mIngredientViewModel);

        // put an observer on the model to watch if data changes
        mIngredientViewModel.viewModelGetAllIngredients().observe(this, ingredients -> {
          // Update the cached copy of the words in the adapter.
          mIngredientListAdapter.submitList(ingredients);
        });

        // bind the UI's recyclerview to the adapter's knowledge of the database
        binding.recyclerview.setAdapter(mIngredientListAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // FAB to add new ingredient
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
          Intent intent = new Intent(MainActivity.this, NewIngredientActivity.class);
          startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        });


        Log.d(TAG, "onCreate: tags work");
  } //end onCreate



  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult: made it here");
    if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
      String ingredientName = data.getStringExtra("ingredient_name");
      String ingredientConcern = data.getStringExtra("concern");
      Log.d(TAG, "onActivityResult: "+ingredientName+": "+ingredientConcern);

      Ingredient ingredient = new Ingredient(ingredientName, ingredientConcern);
      mIngredientViewModel.viewModelInsert(ingredient);
    } else {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_saved,
        Toast.LENGTH_LONG).show();
    }
  }//end onActivityResult


} // end MainActivity
