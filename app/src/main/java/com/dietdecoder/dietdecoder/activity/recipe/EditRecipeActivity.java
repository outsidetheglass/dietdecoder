package com.dietdecoder.dietdecoder.activity.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;

public class EditRecipeActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  private RecipeViewModel mRecipeViewModel;
  public static String mOldRecipeName;
  public static String mOldRecipeIngredient;
  public static String mNewRecipeName;
  public static String mNewRecipeIngredient;

  private EditText mEditRecipeNewNameView;
  private EditText mEditRecipeNewIngredientView;
  private EditText mEditRecipeOldNameView;
  private EditText mEditRecipeOldIngredientView;

  private Boolean isOldNameViewEmpty;
  private Boolean isOldIngredientViewEmpty;
  private Boolean isNewNameViewEmpty;
  private Boolean isNewIngredientViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_recipe);

    mEditRecipeNewNameView = findViewById(R.id.edittext_new_recipe_name);
    mEditRecipeNewIngredientView = findViewById(R.id.edittext_new_recipe_ingredient);
    mEditRecipeOldNameView = findViewById(R.id.edittext_old_recipe_name);
    mEditRecipeOldIngredientView = findViewById(R.id.edittext_old_recipe_ingredient);

    final Button button = findViewById(R.id.button_save);

    button.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isOldNameViewEmpty = TextUtils.isEmpty(mEditRecipeOldNameView.getText());
      isOldIngredientViewEmpty = TextUtils.isEmpty(mEditRecipeOldIngredientView.getText());
      isNewNameViewEmpty = TextUtils.isEmpty(mEditRecipeNewNameView.getText());
      isNewIngredientViewEmpty = TextUtils.isEmpty(mEditRecipeNewIngredientView.getText());

      // if either of the old views are empty
      if ( isOldNameViewEmpty || isOldIngredientViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both old views have values
      else {
        // One of the new values needs to have been updated
        if (isNewNameViewEmpty && isNewIngredientViewEmpty) {
          // set intent to tell user result is cancelled
          setResult(RESULT_CANCELED, replyIntent);
        }
        // at least one of the new values is filled
        else {

          // get strings for name and ingredient
          mOldRecipeName = mEditRecipeOldNameView.getText().toString();
          mOldRecipeIngredient = mEditRecipeOldIngredientView.getText().toString();

          replyIntent.putExtra("old_name", mOldRecipeName);
          replyIntent.putExtra("old_ingredient", mOldRecipeIngredient);

          // check both values, if they're not empty add them to intent
          if (!isNewNameViewEmpty) {
            // Name is not empty, so add that
            mNewRecipeName = mEditRecipeNewNameView.getText().toString();
            replyIntent.putExtra("new_name", mNewRecipeName);
          }
          if (!isNewIngredientViewEmpty) {
            // Ingredient is not empty, so add it
            mNewRecipeIngredient = mEditRecipeNewIngredientView.getText().toString();
            replyIntent.putExtra("new_ingredient", mNewRecipeIngredient);
          }

          Log.d(TAG, "onCreate: " + mNewRecipeName + ": " + mNewRecipeIngredient);
          // send back values for new recipe and tell user succeeded
          setResult(RESULT_OK, replyIntent);

        }
      }
      finish();
    });
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    String mRecipeName = data.getStringExtra("recipe_name");

    // if we're here from starting a new recipe, or else know the name
    // set the name
    mEditRecipeOldNameView.setText(mRecipeName);
    // set the other name view to invisible
    mEditRecipeNewNameView.setVisibility(View.INVISIBLE);

    // now use the repository, might also need the adapter
    mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
    // this won't work with multiple ingredients
    Recipe recipe = mRecipeViewModel.viewModelGetRecipeFromName(mRecipeName);

    mEditRecipeOldIngredientView.setText(recipe.getmRecipeIngredientNames().get(0));

  }
}
