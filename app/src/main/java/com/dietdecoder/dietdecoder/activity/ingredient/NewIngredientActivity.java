package com.dietdecoder.dietdecoder.activity.ingredient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.dietdecoder.dietdecoder.R;

public class NewIngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mIngredientName;
  public static String mIngredientConcern;

  private EditText mEditIngredientNameView;
  private EditText mEditIngredientConcernView;

  private Boolean isNameViewEmpty;
  private Boolean isConcernViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_ingredient);
    mEditIngredientNameView = findViewById(R.id.edittext_ingredient_name);
    mEditIngredientConcernView = findViewById(R.id.edittext_ingredient_concern);

    final Button button = findViewById(R.id.button_save);
    button.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditIngredientNameView.getText());
      isConcernViewEmpty = TextUtils.isEmpty(mEditIngredientConcernView.getText());

      // if either of the views are empty
      if ( isNameViewEmpty || isConcernViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
        // get strings for name and concern
        mIngredientName = mEditIngredientNameView.getText().toString();
        mIngredientConcern = mEditIngredientConcernView.getText().toString();

        Log.d(TAG, "onCreate: "+mIngredientName+": "+mIngredientConcern);
        // send back values for new ingredient and tell user succeeded
        replyIntent.putExtra("ingredient_name", mIngredientName);
        replyIntent.putExtra("concern", mIngredientConcern);
        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
