package com.dietdecoder.dietdecoder.activity.recipe;

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
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeListAdapter;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = RecipeActivity.this;


  private RecipeViewModel mRecipeViewModel;
  private RecipeListAdapter mRecipeListAdapter;
  public static final int NEW_RECIPE_ACTIVITY_REQUEST_CODE = 1;
  public static final int EDIT_RECIPE_ACTIVITY_REQUEST_CODE = 2;
  public static final int DELETE_RECIPE_ACTIVITY_REQUEST_CODE = 3;


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

    Log.d(TAG, "onCreate: Before adapter");
    mRecipeListAdapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff());
    recyclerView.setAdapter(mRecipeListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    Log.d(TAG, "onCreate: After adapter, before model");

    mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

    Log.d(TAG, "onCreate: After model, before observer");
    mRecipeViewModel.viewModelGetAllRecipes().observe(this, recipes -> {

      Log.d(TAG, "onCreate: inside observe, before submitlist");
      // Update the cached copy of the words in the adapter.
      mRecipeListAdapter.submitList(recipes);
    });

    // FAB to add new recipe
    addButton = findViewById(R.id.add_button_recipe);
    addButton.setOnClickListener( view -> {
      addIntent = new Intent(thisActivity, NewRecipeActivity.class);
      startActivityForResult(addIntent, NEW_RECIPE_ACTIVITY_REQUEST_CODE);
    });


    Log.d(TAG, "onCreate: tags work");

  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult: made it here");

    // check which activity completed and work with the returned data
      if (requestCode == NEW_RECIPE_ACTIVITY_REQUEST_CODE) {
        newRecipeActivityResult(resultCode, data);
      }
      else if (requestCode == EDIT_RECIPE_ACTIVITY_REQUEST_CODE) {
        editRecipeActivityResult(resultCode, data);
      }
      else if (requestCode == DELETE_RECIPE_ACTIVITY_REQUEST_CODE) {
        deleteRecipeActivityResult(resultCode, data);
      } //end request codes to run method

    }//end request codes activity results


  private void newRecipeActivityResult(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {
      String recipeName = data.getStringExtra("recipe_name");
      String recipeIngredient = data.getStringExtra("ingredient");
      Log.d(TAG, "newRecipeActivityResult: " + recipeName + ": " + recipeIngredient);

      Recipe recipe = new Recipe(recipeName, recipeIngredient);
      mRecipeViewModel.viewModelRecipeInsert(recipe);
    }//end result_ok
    else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_ingredient_not_saved,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end new recipe activity result

  private void editRecipeActivityResult(int resultCode, Intent data) {
      // if editing recipe was successful
      if (resultCode == RESULT_OK) {

        // set which recipe we're updating
        String oldRecipeName = data.getStringExtra("old_name");
        String oldRecipeIngredient = data.getStringExtra("old_ingredient");
        //Recipe recipeToUpdate = mRecipeViewModel.viewModelGetRecipeFromNameIngredient(recipeName, recipeIngredient);
        Log.d(TAG, "editRecipeActivityResult: " + oldRecipeName + ": " + oldRecipeIngredient);

        Boolean isNewNameEmpty = data.getBooleanExtra("new_name", false);
        Boolean isNewIngredientEmpty = data.getBooleanExtra("new_ingredient", false);
        // if the new name and ingredient value was set from the edit activity, update it
        if (!isNewNameEmpty && !isNewIngredientEmpty) {
          String newRecipeName = data.getStringExtra("new_name");
          String newRecipeIngredient = data.getStringExtra("new_ingredient");
          mRecipeViewModel.viewModelRecipeUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
          mRecipeViewModel.viewModelRecipeUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
        }
        // only update name
        else if (!isNewNameEmpty) {
          String newRecipeName = data.getStringExtra("new_name");
          mRecipeViewModel.viewModelRecipeUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
        }
        // now only update ingredient
        else if (!isNewIngredientEmpty) {
          String newRecipeIngredient = data.getStringExtra("new_ingredient");
          mRecipeViewModel.viewModelRecipeUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
        } //end updating recipe
      }//end result_ok
      // edit was not successful, so tell user with toast
      else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_updated,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end edit recipe activity result

  private void deleteRecipeActivityResult(int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {

      // set which recipe we're updating
      String recipeName = data.getStringExtra("recipe_name");
      String recipeIngredient = data.getStringExtra("recipe_ingredient");

      Log.d(TAG, "onActivityResult: " + recipeName + ": " + recipeIngredient);

      // recipe must exist if we want to delete it, so let's check
      // check all the recipes listed right now to see if the recipe is one of them
      Boolean recipeExists = Boolean.FALSE;
      for (int i=0; i < mRecipeListAdapter.getCurrentList().size(); i++){
        Recipe currentRecipe = mRecipeListAdapter.getCurrentList().get(i);
        if (currentRecipe.getRecipeName() == recipeName && currentRecipe.getRecipeIngredient() == recipeIngredient) {
          recipeExists = Boolean.TRUE;
        }
      }

      // if the recipe exists, delete it
      if ( recipeExists ) {
        // TODO fix this, it won't delete because livedata is being weird
        mRecipeViewModel.viewModelRecipeDelete(recipeName, recipeIngredient);

      }
      // recipe does not exist, make toast to tell user can't delete
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

  }//end delete recipe activity result



}//end RecipeActivity



