package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.ExportActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.IngredientActivity;
import com.dietdecoder.dietdecoder.activity.recipe.RecipeActivity;
import com.dietdecoder.dietdecoder.activity.symptom.SymptomActivity;

//TODO fix edit delete button
public class OtherActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

  // make a TAG to use to export errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = OtherActivity.this;

  public Button ingredientButton, recipeButton, exportButton, symptomButton;

  private Intent ingredientIntent, recipeIntent, exportIntent, symptomIntent;

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

      // do something
    } else if (item.getItemId() == R.id.action_go_home) {
      // do something
      startActivity(new Intent(thisActivity, MainActivity.class));
    } else {
      // do something
    }

    return false;
  }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //set view
      setContentView(R.layout.activity_other);

    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_other);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);
      // Button to go to ingredient's list and edit and delete and add
      ingredientButton = findViewById(R.id.button_ingredient);
      ingredientButton.setOnClickListener( view -> {
        ingredientIntent = new Intent(thisActivity, IngredientActivity.class);
            startActivity(ingredientIntent);
      });

      // Button to got to recipe's page
      recipeButton = findViewById(R.id.button_recipe);
      recipeButton.setOnClickListener( view -> {
        recipeIntent = new Intent(thisActivity, RecipeActivity.class);
        startActivity(recipeIntent);
      });

    // Button to got to recipe's page
    exportButton = findViewById(R.id.button_export);
    exportButton.setOnClickListener( view -> {
      exportIntent = new Intent(thisActivity, ExportActivity.class);
      startActivity(exportIntent);
    });

    // Button to got to recipe's page
    symptomButton = findViewById(R.id.button_symptom);
    symptomButton.setOnClickListener( view -> {
      symptomIntent = new Intent(thisActivity, SymptomActivity.class);
      startActivity(symptomIntent);
    });

  } //end onCreate

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }
} // end MainActivity
