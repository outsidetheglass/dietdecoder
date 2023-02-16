package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.ingredient.IngredientActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.FoodLogActivity;
import com.dietdecoder.dietdecoder.activity.recipe.RecipeActivity;
import com.dietdecoder.dietdecoder.activity.symptom.SymptomActivity;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

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
    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);


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
//
//        @Override
//        public boolean onCreateOptionsMenu(Menu menu){
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return super.onCreateOptionsMenu(menu);
//        }
//







  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }
} // end MainActivity
