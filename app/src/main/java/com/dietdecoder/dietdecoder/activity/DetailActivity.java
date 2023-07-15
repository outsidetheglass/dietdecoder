package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogListAdapter;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeListAdapter;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogListAdapter;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.UUID;

//TODO make delete a fragment or a popup, change this after getting pass ID through the layers working

public class DetailActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = DetailActivity.this;
  private Context thisContext;

  public Button returnButton, actionButton;
  private Intent returnIntent;

  public static String mDetailString, mTypeString, mIdString;
  Boolean mIsHereToDelete;
  private TextView mDetailView;
  Bundle mBundle;


  SymptomLog mSymptomLog;
  SymptomLogViewModel mSymptomLogViewModel;
  SymptomLogListAdapter mSymptomLogListAdapter;

  Symptom mSymptom;
  SymptomViewModel mSymptomViewModel;
  SymptomListAdapter mSymptomListAdapter;

  FoodLog mFoodLog;
  FoodLogViewModel mFoodLogViewModel;
  FoodLogListAdapter mFoodLogListAdapter;

  Ingredient mIngredient;
  IngredientViewModel mIngredientViewModel;
  IngredientListAdapter mIngredientListAdapter;

  Recipe mRecipe;
  RecipeViewModel mRecipeViewModel;
  RecipeListAdapter mRecipeListAdapter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    thisContext = thisActivity.getApplicationContext();

    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_detail);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    mDetailView = findViewById(R.id.textview_detail_value);
    mDetailView.setMovementMethod(new ScrollingMovementMethod());

    // if weren't were given arguments for what to show details of
    if ( thisActivity.getIntent().getExtras() == null) {
      // go back if it was null
      Toast.makeText(this, "Can't view an invalid log", Toast.LENGTH_SHORT).show();
      Util.goToMainActivity(thisActivity);
    } else {
      // intent worked, we have something to list details of
      mBundle = thisActivity.getIntent().getExtras();

      mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
      mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
      mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
      mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
      mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);


      // get values from the intent for symptom we're displaying details for
      // TODO remove duplication, it's checking twice for what we're modifying
      mIdString = Util.setIdArrayFromBundle(mBundle).get(0);
      mDetailString = setDetailTypeString();

      mDetailView.setText(mDetailString);

      // set our action button to either
      // go to edit, if this is detail
      // or delete if user is here to confirm this is what they want to delete
      returnButton = findViewById(R.id.button_return_detail);
      actionButton = findViewById(R.id.button_action_detail);
      mIsHereToDelete = TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
              Util.ARGUMENT_ACTION_DELETE);
      if ( mIsHereToDelete ){
        TextView title = findViewById(R.id.title_textview_detail);
        title.setText(R.string.delete);
        actionButton.setText(R.string.delete);
      } else {
        actionButton.setText(R.string.edit);
      }

      // return to main activity
      returnButton.setOnClickListener(this::onClick);
      actionButton.setOnClickListener(this::onClick);

      Log.d(TAG, "onCreate: tags work");

    }
  }


  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

    } else if (item.getItemId() == R.id.action_go_home) {
      Util.goToMainActivity(thisActivity);
    }

    return false;
  }

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }

  private String setDetailTypeString() {
    String detailString = null;
    UUID id = UUID.fromString(mIdString);

    if ( mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) != null ) {
      detailString = mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(id).toString();
      mTypeString = Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY;
    } else if (mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID_ARRAY) != null){
      detailString = mFoodLogViewModel.viewModelGetFoodLogFromId(id).toString();
      mTypeString = Util.ARGUMENT_FOOD_LOG_ID_ARRAY;
    } else if (mBundle.getString(Util.ARGUMENT_INGREDIENT_ID_ARRAY) != null){
      detailString = mIngredientViewModel.viewModelGetIngredientFromId(id).toString();
      mTypeString = Util.ARGUMENT_INGREDIENT_ID_ARRAY;
    } else if (mBundle.getString(Util.ARGUMENT_RECIPE_ID_ARRAY) != null){
      detailString = mRecipeViewModel.viewModelGetRecipeFromId(id).toString();
      mTypeString = Util.ARGUMENT_RECIPE_ID_ARRAY;
    } else if (mBundle.getString(Util.ARGUMENT_SYMPTOM_ID_ARRAY) != null){
      detailString = mSymptomViewModel.viewModelGetSymptomFromId(id).toString();
      mTypeString = Util.ARGUMENT_SYMPTOM_ID_ARRAY;
    }


    return detailString;

  }


  private void deleteThis() {
    UUID id = UUID.fromString(mIdString);

    if ( mBundle.getString(
            Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY) != null ) {
      mSymptomLogViewModel.viewModelDeleteSymptomLog(mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(id));

    } else if (mBundle.getString(
            Util.ARGUMENT_FOOD_LOG_ID_ARRAY) != null){
      mFoodLogViewModel.viewModelDeleteFoodLog(mFoodLogViewModel.viewModelGetFoodLogFromId(id));

    } else if (mBundle.getString(
            Util.ARGUMENT_INGREDIENT_ID_ARRAY) != null){
      //TODO fix these
//      mFoodLogViewModel.viewModelDeleteFoodLog(mFoodLogViewModel.viewModelGetFoodLogFromId(id));

    } else if (mBundle.getString(
            Util.ARGUMENT_RECIPE_ID_ARRAY) != null){
//      mFoodLogViewModel.viewModelDeleteFoodLog(mFoodLogViewModel.viewModelGetFoodLogFromId(id));

    } else if (mBundle.getString(
            Util.ARGUMENT_SYMPTOM_ID_ARRAY) != null){
//      mFoodLogViewModel.viewModelDeleteFoodLog(mFoodLogViewModel.viewModelGetFoodLogFromId(id));

    }

  }


  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      // save button was pressed
      case R.id.button_return_detail:
        // done
        // TODO make it briefly highlight in list log to show which changed
        Util.goToListActivityTypeId(null, thisActivity, mTypeString, mIdString);
        break;
      case R.id.button_action_detail:
        if ( mIsHereToDelete ){
          deleteThis();
          Util.goToListActivityTypeId(null, thisActivity, mTypeString, mIdString);
        } else {
          Util.goToEditActivityActionTypeId(null, thisActivity, Util.ARGUMENT_ACTION_EDIT,
                  mTypeString,
                  mIdString);
        }
        break;
      default:
        break;

    }
  }
}