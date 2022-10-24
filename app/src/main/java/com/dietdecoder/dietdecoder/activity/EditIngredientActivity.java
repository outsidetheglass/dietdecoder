package com.dietdecoder.dietdecoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;

public class EditIngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mOldIngredientName;
  public static String mOldIngredientConcern;
  public static String mNewIngredientName;
  public static String mNewIngredientConcern;

  private EditText mEditIngredientNewNameView;
  private EditText mEditIngredientNewConcernView;
  private EditText mEditIngredientOldNameView;
  private EditText mEditIngredientOldConcernView;

  private Boolean isOldNameViewEmpty;
  private Boolean isOldConcernViewEmpty;
  private Boolean isNewNameViewEmpty;
  private Boolean isNewConcernViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_ingredient);
    mEditIngredientNewNameView = findViewById(R.id.edittext_new_ingredient_name);
    mEditIngredientNewConcernView = findViewById(R.id.edittext_new_ingredient_concern);
    mEditIngredientOldNameView = findViewById(R.id.edittext_old_ingredient_name);
    mEditIngredientOldConcernView = findViewById(R.id.edittext_old_ingredient_concern);

    final Button button = findViewById(R.id.button_save);
    button.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isOldNameViewEmpty = TextUtils.isEmpty(mEditIngredientOldNameView.getText());
      isOldConcernViewEmpty = TextUtils.isEmpty(mEditIngredientOldConcernView.getText());
      isNewNameViewEmpty = TextUtils.isEmpty(mEditIngredientNewNameView.getText());
      isNewConcernViewEmpty = TextUtils.isEmpty(mEditIngredientNewConcernView.getText());

      // if either of the old views are empty
      if ( isOldNameViewEmpty || isOldConcernViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both old views have values
      else {
        // One of the new values needs to have been updated
        if (isNewNameViewEmpty && isNewConcernViewEmpty) {
          // set intent to tell user result is cancelled
          setResult(RESULT_CANCELED, replyIntent);
        }
        // at least one of the new values is filled
        else {

          // get strings for name and concern
          mOldIngredientName = mEditIngredientOldNameView.getText().toString();
          mOldIngredientConcern = mEditIngredientOldConcernView.getText().toString();

          replyIntent.putExtra("old_name", mOldIngredientName);
          replyIntent.putExtra("old_concern", mOldIngredientConcern);

          // check both values, if they're not empty add them to intent
          if (!isNewNameViewEmpty) {
            // Name is not empty, so add that
            mNewIngredientName = mEditIngredientNewNameView.getText().toString();
            replyIntent.putExtra("new_name", mNewIngredientName);
          }
          if (!isNewConcernViewEmpty) {
            // Concern is not empty, so add it
            mNewIngredientConcern = mEditIngredientNewConcernView.getText().toString();
            replyIntent.putExtra("new_concern", mNewIngredientConcern);
          }

          Log.d(TAG, "onCreate: " + mNewIngredientName + ": " + mNewIngredientConcern);
          // send back values for new ingredient and tell user succeeded
          setResult(RESULT_OK, replyIntent);

        }
      }
      finish();
    });
  }

}
