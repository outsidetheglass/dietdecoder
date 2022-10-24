package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;

import com.dietdecoder.dietdecoder.R;

//TODO fix edit delete button
public class MainActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = MainActivity.this;

  public Button ingredientButton;
  public Button recipeButton;
  public Button logButton;

  private Intent ingredientIntent;
  private Intent recipeIntent;
  private Intent logIntent;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //set view
      setContentView(R.layout.activity_main);

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
      logButton = findViewById(R.id.button_log);
      logButton.setOnClickListener( view -> {
        logIntent = new Intent(thisActivity, LogActivity.class);
        startActivity(logIntent);
      });

      Log.d(TAG, "onCreate: tags work");
  } //end onCreate

} // end MainActivity
