package com.dietdecoder.dietdecoder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.Ingredient;
import com.dietdecoder.dietdecoder.ui.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.IngredientViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = IngredientActivity.this;


  private IngredientViewModel mIngredientViewModel;
  private IngredientListAdapter mIngredientListAdapter;
  public static final int NEW_INGREDIENT_ACTIVITY_REQUEST_CODE = 1;
  public static final int EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE = 2;
  public static final int DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE = 3;


  public FloatingActionButton editButton;
  public FloatingActionButton deleteButton;
  public FloatingActionButton addButton;

  private Intent editIntent;
  private Intent deleteIntent;
  private Intent addIntent;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ingredient);

    RecyclerView recyclerView = findViewById(R.id.recyclerview_ingredient);

    mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
    recyclerView.setAdapter(mIngredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

    mIngredientViewModel.viewModelGetAllIngredients().observe(this, ingredients -> {
      // Update the cached copy of the words in the adapter.
      mIngredientListAdapter.submitList(ingredients);
    });

    // Button to edit ingredient
    editButton = findViewById(R.id.edit_button_ingredient);
    editButton.setOnClickListener( view -> {
      editIntent = new Intent(thisActivity, EditIngredientActivity.class);
      //TODO fix depreciated forResult
      startActivityForResult(editIntent, EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE);
    });
    // Button to delete ingredient
    deleteButton = findViewById(R.id.delete_button_ingredient);
    deleteButton.setOnClickListener( view -> {
      deleteIntent = new Intent(thisActivity, DeleteIngredientActivity.class);
      startActivityForResult(deleteIntent, DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE);
    });

    // FAB to add new ingredient
    addButton = findViewById(R.id.add_button_ingredient);
    addButton.setOnClickListener( view -> {
      addIntent = new Intent(thisActivity, NewIngredientActivity.class);
      startActivityForResult(addIntent, NEW_INGREDIENT_ACTIVITY_REQUEST_CODE);
    });


    Log.d(TAG, "onCreate: tags work");

  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult: made it here");

    // check which activity completed and work with the returned data
      if (requestCode == NEW_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        newIngredientActivityResult(resultCode, data);
      }
      else if (requestCode == EDIT_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        editIngredientActivityResult(resultCode, data);
      }
      else if (requestCode == DELETE_INGREDIENT_ACTIVITY_REQUEST_CODE) {
        deleteIngredientActivityResult(resultCode, data);
      } //end request codes to run method

    }//end request codes activity results


  private void newIngredientActivityResult(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {
      String ingredientName = data.getStringExtra("ingredient_name");
      String ingredientConcern = data.getStringExtra("concern");
      Log.d(TAG, "newIngredientActivityResult: " + ingredientName + ": " + ingredientConcern);

      Ingredient ingredient = new Ingredient(ingredientName, ingredientConcern);
      mIngredientViewModel.viewModelInsert(ingredient);
    }//end result_ok
    else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_ingredient_not_saved,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end new ingredient activity result

  private void editIngredientActivityResult(int resultCode, Intent data) {
      // if editing ingredient was successful
      if (resultCode == RESULT_OK) {

        // set which ingredient we're updating
        String oldIngredientName = data.getStringExtra("old_name");
        String oldIngredientConcern = data.getStringExtra("old_concern");
        //Ingredient ingredientToUpdate = mIngredientViewModel.viewModelGetIngredientFromNameConcern(ingredientName, ingredientConcern);
        Log.d(TAG, "editIngredientActivityResult: " + oldIngredientName + ": " + oldIngredientConcern);

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
        } //end updating ingredient
      }//end result_ok
      // edit was not successful, so tell user with toast
      else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_updated,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end edit ingredient activity result

  private void deleteIngredientActivityResult(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {

      // set which ingredient we're updating
      String ingredientName = data.getStringExtra("ingredient_name");
      String ingredientConcern = data.getStringExtra("ingredient_concern");

      Log.d(TAG, "onActivityResult: " + ingredientName + ": " + ingredientConcern);

      // ingredient must exist if we want to delete it, so let's check
      // check all the ingredients listed right now to see if the ingredient is one of them
      Boolean ingredientExists = Boolean.FALSE;
      for (int i=0; i < mIngredientListAdapter.getCurrentList().size(); i++){
        Ingredient currentIngredient = mIngredientListAdapter.getCurrentList().get(i);
        if (currentIngredient.getIngredientName() == ingredientName && currentIngredient.getIngredientConcern() == ingredientConcern) {
          ingredientExists = Boolean.TRUE;
        }
      }

      // if the ingredient exists, delete it
      if ( ingredientExists ) {
        // TODO fix this, it won't delete because livedata is being weird
        mIngredientViewModel.viewModelDelete(ingredientName, ingredientConcern);

      }
      // ingredient does not exist, make toast to tell user can't delete
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

  }//end delete ingredient activity result



}//end IngredientActivity



