package com.dietdecoder.dietdecoder.activity.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;

public class NewRecipeActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mRecipeName;
  public static String mRecipeIngredient;

  private EditText mEditRecipeNameView;
  private EditText mEditRecipeIngredientView;

  private Boolean isNameViewEmpty;
  private Boolean isIngredientViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_recipe);
    mEditRecipeNameView = findViewById(R.id.edittext_recipe_name);
    mEditRecipeIngredientView = findViewById(R.id.edittext_recipe_ingredient);

    // Buttons to save
    final Button saveAndDonebutton = findViewById(R.id.button_save_and_done);
    final Button saveAndAddAnotherbutton = findViewById(R.id.button_save_and_add_another);

    saveAndDonebutton.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditRecipeNameView.getText());
      isIngredientViewEmpty = TextUtils.isEmpty(mEditRecipeIngredientView.getText());

      // if either of the views are empty
      if ( isNameViewEmpty || isIngredientViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
        // get strings for name and ingredient
        mRecipeName = mEditRecipeNameView.getText().toString();
        mRecipeIngredient = mEditRecipeIngredientView.getText().toString();

        Log.d(TAG, "onCreate: "+mRecipeName+": "+mRecipeIngredient);
        // send back values for new recipe and tell user succeeded
        replyIntent.putExtra("recipe_name", mRecipeName);
        replyIntent.putExtra("ingredient", mRecipeIngredient);
        replyIntent.putExtra("add_another_ingredient_or_done", "done");

        Toast.makeText(this, "Saved! Recipe complete, hoorah!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
    saveAndAddAnotherbutton.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditRecipeNameView.getText());
      isIngredientViewEmpty = TextUtils.isEmpty(mEditRecipeIngredientView.getText());

      // if either of the views are empty
      if ( isNameViewEmpty || isIngredientViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
        // get strings for name and ingredient
        mRecipeName = mEditRecipeNameView.getText().toString();
        mRecipeIngredient = mEditRecipeIngredientView.getText().toString();

        Log.d(TAG, "onCreate: "+mRecipeName+": "+mRecipeIngredient);
        // send back values for new recipe and tell user succeeded
        replyIntent.putExtra("recipe_name", mRecipeName);
        replyIntent.putExtra("ingredient", mRecipeIngredient);
        replyIntent.putExtra("add_another_ingredient_or_done", "add_another_ingredient");
        Toast.makeText(this, "Saved! Let's do another, yay!", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
