package com.dietdecoder.dietdecoder.activity.ingredient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

public class AddIngredientActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mIngredientName, mIngredientChemical;

  private EditText mEditIngredientNameView, mEditIngredientChemicalView;

  private Boolean isNameViewEmpty, isChemicalViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_ingredient);
    mEditIngredientNameView = findViewById(R.id.edittext_ingredient_name);
//    mEditIngredientChemicalView = findViewById(R.id.edittext_ingredient_chemical);

    final Button button = findViewById(R.id.button_save_new_ingredient);
    button.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditIngredientNameView.getText());

      // if the view is empty
      if ( isNameViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
        // get strings for name and Chemical
        mIngredientName = mEditIngredientNameView.getText().toString();
        //make it
        IngredientViewModel mIngredientViewModel =
                new ViewModelProvider(this).get(IngredientViewModel.class);
        Ingredient mIngredient = new Ingredient(mIngredientName);
        mIngredientViewModel.viewModelInsert(mIngredient);

        // now add Chemical if it was there
//        isChemicalViewEmpty = TextUtils.isEmpty(mEditIngredientChemicalView.getText());
//
//        if ( !isChemicalViewEmpty ) {
////          mIngredientChemical = mEditIngredientChemicalView.getText().toString();
//          //TODO get this working with Ingredient's new values, needs edittexts
//          // String ingredientChemicalName, Double ingredientChemicalAmountNumber,
//          // String ingredientChemicalAmountUnit
//          //mIngredientViewModel.viewModelUpdate;
//        }

        //TODO make this into a resource string, and all toasts
        Toast.makeText(this, "Ingredient successfully made!", Toast.LENGTH_SHORT).show();

      }
      finish();
    });
  }

}
