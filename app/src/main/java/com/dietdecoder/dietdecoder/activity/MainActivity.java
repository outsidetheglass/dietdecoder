package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.ingredient.IngredientActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.FoodLogActivity;
import com.dietdecoder.dietdecoder.activity.recipe.RecipeActivity;
import com.dietdecoder.dietdecoder.activity.symptom.SymptomActivity;

//TODO fix edit delete button
public class MainActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = MainActivity.this;

  public Button otherButton, logButton;

  private Intent ingredientIntent, logIntent;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //set view
      setContentView(R.layout.activity_main);


    // Button to got to recipe's page
    logButton = findViewById(R.id.button_log);
    logButton.setOnClickListener( view -> {
      logIntent = new Intent(thisActivity, FoodLogActivity.class);
      startActivity(logIntent);
    });

    // Button to go to ingredient's list and edit and delete and add
    otherButton = findViewById(R.id.button_other);
    otherButton.setOnClickListener( view -> {
      startActivity(new Intent(thisActivity, OtherActivity.class));
    });

  } //end onCreate

} // end MainActivity
