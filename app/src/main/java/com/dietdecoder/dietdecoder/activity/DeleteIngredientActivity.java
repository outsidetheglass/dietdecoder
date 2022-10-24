package com.dietdecoder.dietdecoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;

public class DeleteIngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String ingredientName;
  public static String ingredientConcern;

  private EditText ingredientNameView;
  private EditText ingredientConcernView;

  private Boolean isNameViewEmpty;
  private Boolean isConcernViewEmpty;

  private Button deleteButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_delete_ingredient);
    ingredientNameView = findViewById(R.id.edittext_delete_ingredient_name);
    ingredientConcernView = findViewById(R.id.edittext_delete_ingredient_concern);

    deleteButton = findViewById(R.id.button_delete);
    deleteButton.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(ingredientNameView.getText());
      isConcernViewEmpty = TextUtils.isEmpty(ingredientConcernView.getText());

      // if either of the old views are empty
      if ( isNameViewEmpty || isConcernViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
          // get strings for name and concern
          ingredientName = ingredientNameView.getText().toString();
          ingredientConcern = ingredientConcernView.getText().toString();

          replyIntent.putExtra("ingredient_name", ingredientName);
          replyIntent.putExtra("ingredient_concern", ingredientConcern);

          // check both values
          Log.d(TAG, "onCreate: " + ingredientName + ": " + ingredientConcern);
          // send back values for new ingredient and tell user succeeded
          setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
