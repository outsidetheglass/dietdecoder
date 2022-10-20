package com.dietdecoder.dietdecoder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class NewIngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  public static String mIngredientName;
  public static String mIngredientChemical;

  private EditText mEditIngredientNameView;
  private EditText mEditIngredientChemicalView;

  private Boolean isNameViewEmpty;
  private Boolean isChemicalViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_ingredient);
    mEditIngredientNameView = findViewById(R.id.edittext_ingredient_name);
    mEditIngredientChemicalView = findViewById(R.id.edittext_ingredient_chemical);

    final Button button = findViewById(R.id.button_save);
    button.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditIngredientNameView.getText());
      isChemicalViewEmpty = TextUtils.isEmpty(mEditIngredientChemicalView.getText());

      // if either of the views are empty
      if ( isNameViewEmpty || isChemicalViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
        // get strings for name and chemical
        mIngredientName = mEditIngredientNameView.getText().toString();
        mIngredientChemical = mEditIngredientChemicalView.getText().toString();

        Log.d(TAG, "onCreate: "+mIngredientName+": "+mIngredientChemical);
        // send back values for new ingredient and tell user succeeded
        replyIntent.putExtra("ingredient_name", mIngredientName);
        replyIntent.putExtra("chemical", mIngredientChemical);
        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
