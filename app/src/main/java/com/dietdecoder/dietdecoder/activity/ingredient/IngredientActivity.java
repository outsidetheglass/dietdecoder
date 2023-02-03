package com.dietdecoder.dietdecoder.activity.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class IngredientActivity extends AppCompatActivity {

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

    RecyclerView recyclerView = findViewById(R.id.recyclerview_ingredient);

    mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
    recyclerView.setAdapter(mIngredientListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));


    mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);

    // if not null, then set list of ingredients
    mActivityAllIngredients = mIngredientViewModel.viewModelGetAllIngredients();

    // turn LiveData into list and set that in Adapter so we can get positions
    mIngredientListAdapter.setIngredientList(mActivityAllIngredients.getValue());

    //TODO move this if statement to setIngredientList somehow
    if ( mActivityAllIngredients != null ) {
      mActivityAllIngredients.observe(this, ingredients -> {
        // Update the cached copy of the words in the adapter.
        mIngredientListAdapter.submitList(ingredients);
      });

    }

    // FAB to add new ingredient
    addIngredientButton = findViewById(R.id.add_button_ingredient);
    addIngredientButton.setOnClickListener( view -> {
      addIngredientIntent = new Intent(thisActivity, NewIngredientActivity.class);
      startActivityForResult(addIngredientIntent, NEW_INGREDIENT_ACTIVITY_REQUEST_CODE);
    });


    Log.d(TAG, "onCreate: tags work");

    // TODO turn this into a fragment
    Intent fromActivityIntent = getIntent();
    if ( fromActivityIntent != null ){
      if (fromActivityIntent.getStringExtra("old_name") != null ){
        String editedIngredientName = fromActivityIntent.getStringExtra("old_name");
        String editedIngredientConcern = fromActivityIntent.getStringExtra("old_concern");
        String editedIngredientNewName = fromActivityIntent.getStringExtra("new_name");
        String editedIngredientNewConcern = fromActivityIntent.getStringExtra("new_concern");
      }
      ;
    }
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
        //deleteIngredientActivityResult(resultCode, data);
      } //end request codes to run method

    }//end request codes activity results

// new ingredient activity was run
  private void newIngredientActivityResult(int resultCode, Intent data) {

    // if new ingredient returned successfully
    if (resultCode == RESULT_OK) {
      String ingredientName = data.getStringExtra("ingredient_name");
      String ingredientConcern = data.getStringExtra("concern");
      //TODO fix this chemical amount and unit
      Double chemicalAmount = 1.1;
      String chemicalAmountUnit = "mL";
      Log.d(TAG, "newIngredientActivityResult: " + ingredientName + ": " + ingredientConcern);

      Ingredient ingredient = new Ingredient(ingredientName, ingredientConcern, chemicalAmount, chemicalAmountUnit);
      mIngredientViewModel.viewModelInsert(ingredient);
    }//end result_ok
    else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_saved,
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
        //TODO add other properties of ingredient type here
        //Boolean isNewConcernEmpty = data.getBooleanExtra("new_concern", false);
        // if the new name and concern value was set from the edit activity, update it
        if (!isNewNameEmpty /*&& !isNewConcernEmpty*/) {
          String newIngredientName = data.getStringExtra("new_name");
          String newIngredientConcern = data.getStringExtra("new_concern");
          //TODO add other properties of ingredient type here
//          mIngredientViewModel.viewModelUpdateConcern(oldIngredientName, oldIngredientConcern, newIngredientConcern);
          mIngredientViewModel.viewModelUpdateName(oldIngredientName, oldIngredientConcern, newIngredientName);
        }
        // only update name
        else if (!isNewNameEmpty) {
          String newIngredientName = data.getStringExtra("new_name");
          mIngredientViewModel.viewModelUpdateName(oldIngredientName, oldIngredientConcern, newIngredientName);
        }
        // now only update concern
        //TODO add other properties of ingredient type here
//        else if (!isNewConcernEmpty) {
//          String newIngredientConcern = data.getStringExtra("new_concern");
//          mIngredientViewModel.viewModelUpdateConcern(oldIngredientName, oldIngredientConcern, newIngredientConcern);
//        } //end updating ingredient
      }//end result_ok
      // edit was not successful, so tell user with toast
      else  {
      Toast.makeText(
        getApplicationContext(),
        R.string.empty_not_updated,
        Toast.LENGTH_LONG).show();
    } //end if result not okay

  }//end edit ingredient activity result
//
//  private void deleteIngredientActivityResult(int resultCode, Intent data) {
//
//    if (resultCode == RESULT_OK) {
//
//      // set which ingredient we're updating
//      String ingredientName = data.getStringExtra("ingredient_name");
//      String ingredientConcern = data.getStringExtra("ingredient_concern");
//
//      Log.d(TAG, "onActivityResult: " + ingredientName + ": " + ingredientConcern);
//
//      // ingredient must exist if we want to delete it, so let's check
//      // check all the ingredients listed right now to see if the ingredient is one of them
//      Boolean ingredientExists = Boolean.FALSE;
//      for (int i=0; i < mIngredientListAdapter.getCurrentList().size(); i++){
//        Ingredient currentIngredient = mIngredientListAdapter.getCurrentList().get(i);
//        if (currentIngredient.getIngredientName() == ingredientName && currentIngredient.getIngredientConcern() == ingredientConcern) {
//          ingredientExists = Boolean.TRUE;
//        }
//      }
//
//      // if the ingredient exists, delete it
//      if ( ingredientExists ) {
//        // TODO fix this, it won't delete because livedata is being weird
//        mIngredientViewModel.viewModelDelete(ingredient);
//
//      }
//      // ingredient does not exist, make toast to tell user can't delete
//      else {
//        Toast.makeText(
//          getApplicationContext(),
//          R.string.value_not_found_not_deleted,
//          Toast.LENGTH_LONG).show();
//
//      }
//
//    }//end result_ok
//    else  {
//      Toast.makeText(
//        getApplicationContext(),
//        R.string.empty_not_deleted,
//        Toast.LENGTH_LONG).show();
//    } //end if result not okay
//
//  }//end delete ingredient activity result



}//end IngredientActivity



