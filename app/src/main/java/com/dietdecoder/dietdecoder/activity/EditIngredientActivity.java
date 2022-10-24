package com.dietdecoder.dietdecoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.Ingredient;

public class EditIngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mOldIngredientName;
  public static String mOldIngredientConcern;
  public static String mNewIngredientName;
  public static String mNewIngredientConcern;

  private EditText mEditIngredientNewNameView;
  private EditText mEditIngredientNewConcernView;

  private TextView mIngredientOldNameView;
  private TextView mIngredientOldConcernView;
  private String mIngredientOldName;
  private String mIngredientOldConcern;

  private Boolean isNewNameViewEmpty;
  private Boolean isNewConcernViewEmpty;

  private Button editIngredientSaveButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_ingredient);

    mEditIngredientNewNameView = findViewById(R.id.edittext_new_ingredient_name);
    mEditIngredientNewConcernView = findViewById(R.id.edittext_new_ingredient_concern);

    mIngredientOldNameView = findViewById(R.id.textview_old_ingredient_name);
    mIngredientOldConcernView = findViewById(R.id.textview_old_ingredient_concern);

    Intent editIngredientIntent = getIntent();

    // Null check the intent worked
    if (null == editIngredientIntent) {
      // go back if it was null
      Toast.makeText(this, "Can't edit an invalid ingredient", Toast.LENGTH_SHORT).show();
      this.startActivity( new Intent(this, EditIngredientActivity.class));
    }

    mIngredientOldName = editIngredientIntent.getStringExtra("ingredient_name");
    mIngredientOldConcern = editIngredientIntent.getStringExtra("ingredient_concern");
    mIngredientOldNameView.setText(mIngredientOldName);
    mIngredientOldConcernView.setText(mIngredientOldConcern);

    //TODO fix this when I just can save it through here instead of going back to save
    // make an intent to hold our edited ingredient to go back to where we can save it
    Intent replyIntent = new Intent(this, IngredientActivity.class);

    replyIntent.putExtra("old_name", mIngredientOldName);
    replyIntent.putExtra("old_concern", mIngredientOldConcern);

    editIngredientSaveButton = findViewById(R.id.button_edit_ingredient_save);
    editIngredientSaveButton.setOnClickListener(view -> {

      // check for if views are empty
      isNewNameViewEmpty = TextUtils.isEmpty(mEditIngredientNewNameView.getText());
      isNewConcernViewEmpty = TextUtils.isEmpty(mEditIngredientNewConcernView.getText());

      // One of the new values needs to have been updated
      if (isNewNameViewEmpty && isNewConcernViewEmpty) {
        // tell user nope
        Toast.makeText(this, "Update one of the fields to save", Toast.LENGTH_SHORT).show();
      }
      // at least one of the new values is filled
      else {
        // check both values, if they're not empty add them to intent
        if (!isNewNameViewEmpty) {
          // Name is not empty, so add that
          mNewIngredientName = mEditIngredientNewNameView.getText().toString();
          replyIntent.putExtra("new_concern", mNewIngredientConcern);
        }
        if (!isNewConcernViewEmpty) {
          // Concern is not empty, so add it
          mNewIngredientConcern = mEditIngredientNewConcernView.getText().toString();
          replyIntent.putExtra("new_concern", mNewIngredientConcern);
        }

      }

      this.startActivity( replyIntent );

    finish();
    });
  }

}
