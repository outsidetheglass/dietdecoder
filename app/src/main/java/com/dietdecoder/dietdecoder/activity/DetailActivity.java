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
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
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
  UUID mId;
  Boolean mIsHereToDelete;
  private TextView mDetailView;
  Bundle mBundle;


  SymptomLog mSymptomLog;
  SymptomLogViewModel mSymptomLogViewModel;
  SymptomLogListAdapter mSymptomLogListAdapter;

  Symptom mSymptom;
  SymptomViewModel mSymptomViewModel;
  SymptomListAdapter mSymptomListAdapter;


  Ingredient mIngredient;
  IngredientViewModel mIngredientViewModel;
  IngredientListAdapter mIngredientListAdapter;

  IngredientLog mIngredientLog;
  IngredientLogViewModel mIngredientLogViewModel;
  IngredientLogListAdapter mIngredientLogListAdapter;


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
      Util.goToMainActivity(null, thisActivity);
    } else {
      // intent worked, we have something to list details of
      mBundle = thisActivity.getIntent().getExtras();

      Log.d(TAG, mBundle.toString());
      mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
      mSymptomLogViewModel = new ViewModelProvider(this).get(SymptomLogViewModel.class);
      mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);
      mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
      mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);


      // get values from the intent for symptom we're displaying details for
      // TODO remove duplication, it's checking twice for what we're modifying
      mIdString = Util.setLogIdArrayFromBundle(mBundle).get(0);
      mId = UUID.fromString(mIdString);
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


    }
  }


  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

    } else if (item.getItemId() == R.id.action_go_home) {
      Util.goToMainActivity(null, thisActivity);
    }

    return false;
  }

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }

  private String setDetailTypeString() {
    String detailString = null;

    // using the view model for each object, symptom, ingredient log, etc
    // get the detailed string if the bundle contains that object type id
    String[] detailTypeString = Util.setStringAndTypeByIdArrayFromBundleElseIfStrings(
            mBundle,
            mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(mId).toString(),
            mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mId).toString(),
            mIngredientViewModel.viewModelGetIngredientFromId(mId).toString(),
            mSymptomViewModel.viewModelGetSymptomFromId(mId).toString(),
            mRecipeViewModel.viewModelGetRecipeFromId(mId).toString()
    );
    detailString = detailTypeString[0];
    mTypeString = detailTypeString[1];

    return detailString;

  }


  private void deleteThis() {

    if ( Util.isSymptomLogBundle(mBundle) ) {
      mSymptomLogViewModel.viewModelDeleteSymptomLog(
              mSymptomLogViewModel.viewModelGetSymptomLogFromLogId(mId));

    } else if (Util.isIngredientLogBundle(mBundle)){
      mIngredientLogViewModel.viewModelDeleteIngredientLog(
              mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(mId));

    } else if (Util.isIngredientBundle(mBundle)){
      //TODO fix these
      mIngredientViewModel.viewModelDelete(mIngredientViewModel.viewModelGetIngredientFromId(mId));

    }
    else if (Util.isSymptomBundle(mBundle)){
      mSymptomViewModel.viewModelDelete(mSymptomViewModel.viewModelGetSymptomFromId(mId));

    }
//    else if (Util.isSymptomLogBundle(mBundle)){
//      mIngredientLogViewModel.viewModelDeleteIngredientLog(mIngredientLogViewModel
//      .viewModelGetIngredientLogFromId
//      (mId));
//    }

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
