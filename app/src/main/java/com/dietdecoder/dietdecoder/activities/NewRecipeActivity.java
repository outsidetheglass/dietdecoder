package com.dietdecoder.dietdecoder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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

    final Button button = findViewById(R.id.button_save);
    button.setOnClickListener(view -> {
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
        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
