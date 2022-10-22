package com.dietdecoder.dietdecoder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import com.dietdecoder.dietdecoder.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//TODO fix edit delete button
public class MainActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  private IngredientViewModel mIngredientViewModel;
  private IngredientListAdapter mIngredientListAdapter;
  public static final int NEW_INGREDIENT_ACTIVITY_REQUEST_CODE = 1;
  public static final int EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE = 2;
  public static final int DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE = 3;


  public Button editButton;
  public Button deleteButton;
  public Button addButton;

  private Intent editIntent;
  private Intent deleteIntent;
  private Intent addIntent;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //set view with RecyclerView
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
        recyclerView.setAdapter(mIngredientListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

        mIngredientViewModel.viewModelGetAllIngredients().observe(this, ingredients -> {
          // Update the cached copy of the words in the adapter.
          mIngredientListAdapter.submitList(ingredients);
        });

    // Button to edit ingredient
    editButton = findViewById(R.id.edit_button);
    editButton.setOnClickListener( view -> {
      editIntent = new Intent(MainActivity.this, EditIngredientActivity.class);
      startActivityForResult(editIntent, EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE);
    });
    // Button to delete ingredient
    deleteButton = findViewById(R.id.delete_button);
    deleteButton.setOnClickListener( view -> {
      deleteIntent = new Intent(MainActivity.this, DeleteIngredientActivity.class);
      startActivityForResult(deleteIntent, DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE);
    });

        // FAB to add new ingredient
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener( view -> {
          addIntent = new Intent(MainActivity.this, NewIngredientActivity.class);
          startActivityForResult(addIntent, NEW_INGREDIENT_ACTIVITY_REQUEST_CODE);
        });


        Log.d(TAG, "onCreate: tags work");
  } //end onCreate



  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult: made it here");
    //TODO turn these into functions instead of inefficient if statements
    if (resultCode == RESULT_OK) {
      if (requestCode == NEW_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        String ingredientName = data.getStringExtra("ingredient_name");
        String ingredientConcern = data.getStringExtra("concern");
        Log.d(TAG, "onActivityResult: " + ingredientName + ": " + ingredientConcern);

        Ingredient ingredient = new Ingredient(ingredientName, ingredientConcern);
        mIngredientViewModel.viewModelInsert(ingredient);
      }//end new ingredient activity result
      else if (requestCode == EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        // set which ingredient we're updating
        String oldIngredientName = data.getStringExtra("old_name");
        String oldIngredientConcern = data.getStringExtra("old_concern");
        //Ingredient ingredientToUpdate = mIngredientViewModel.viewModelGetIngredientFromNameConcern(ingredientName, ingredientConcern);

        Log.d(TAG, "onActivityResult: " + oldIngredientName + ": " + oldIngredientConcern);

        Boolean isNewNameEmpty = data.getBooleanExtra("new_name", false);
        Boolean isNewConcernEmpty = data.getBooleanExtra("new_concern", false);
        // if the new name and concern value was set from the edit activity, update it
        if (!isNewNameEmpty && !isNewConcernEmpty) {
          String newIngredientName = data.getStringExtra("new_name");
          String newIngredientConcern = data.getStringExtra("new_concern");
          mIngredientViewModel.viewModelUpdateConcern(oldIngredientName, oldIngredientConcern, newIngredientConcern);
          mIngredientViewModel.viewModelUpdateName(oldIngredientName, oldIngredientConcern, newIngredientName);
        }
        // only update name
        else if (!isNewNameEmpty) {
          String newIngredientName = data.getStringExtra("new_name");
          mIngredientViewModel.viewModelUpdateName(oldIngredientName, oldIngredientConcern, newIngredientName);
        }
        // now only update concern
        else if (!isNewConcernEmpty) {
          String newIngredientConcern = data.getStringExtra("new_concern");
          mIngredientViewModel.viewModelUpdateConcern(oldIngredientName, oldIngredientConcern, newIngredientConcern);
        }

      }
      else if (requestCode == DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        // set which ingredient we're updating
        String ingredientName = data.getStringExtra("ingredient_name");
        String ingredientConcern = data.getStringExtra("ingredient_concern");

        Log.d(TAG, "onActivityResult: " + ingredientName + ": " + ingredientConcern);

        Boolean ingredientExists = Boolean.FALSE;
        for (int i=0; i < mIngredientListAdapter.getCurrentList().size(); i++){
          Ingredient currentIngredient = mIngredientListAdapter.getCurrentList().get(i);
          if (currentIngredient.getIngredientName() == ingredientName && currentIngredient.getIngredientConcern() == ingredientConcern) {
            ingredientExists = Boolean.TRUE;
          }
        }
        if ( ingredientExists ) {
          // TODO fix this, it won't delete because livedata is being weird
          mIngredientViewModel.viewModelDelete(ingredientName, ingredientConcern);

        } else
        {
          Toast.makeText(
            getApplicationContext(),
            R.string.value_not_found_not_deleted,
            Toast.LENGTH_LONG).show();

        }
      }
    }
    // result code wasn't RESULT_OK, so let's make the invalid toast
    else {
      if (requestCode == NEW_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        Toast.makeText(
          getApplicationContext(),
          R.string.empty_not_saved,
          Toast.LENGTH_LONG).show();
      } else if (requestCode == EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        Toast.makeText(
          getApplicationContext(),
          R.string.empty_not_updated,
          Toast.LENGTH_LONG).show();
      } else if (requestCode == DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        Toast.makeText(
          getApplicationContext(),
          R.string.empty_not_deleted,
          Toast.LENGTH_LONG).show();
      }
    }//end request codes activity results

  }//end onActivityResult


} // end MainActivity
