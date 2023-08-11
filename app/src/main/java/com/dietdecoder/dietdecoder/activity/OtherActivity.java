package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.ExportActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.IngredientActivity;
import com.dietdecoder.dietdecoder.activity.recipe.RecipeActivity;
import com.dietdecoder.dietdecoder.activity.symptom.SymptomActivity;

//TODO fix edit delete button
public class OtherActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

  // make a TAG to use to export errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = OtherActivity.this;

  private Context thisContext;

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
    }   else if (item.getItemId() == R.id.action_more) {

      // Initializing the popup menu and giving the reference as current logContext
      PopupMenu popupMenu = new PopupMenu(thisContext, findViewById(R.id.action_more));
      // Inflating popup menu from popup_menu.xml file
      popupMenu.getMenuInflater().inflate(R.menu.item_more_menu, popupMenu.getMenu());
      popupMenu.setGravity(Gravity.END);
      // if an option in the menu is clicked
      popupMenu.setOnMenuItemClickListener(moreMenuItem -> {
        // which button was clicked
        switch (moreMenuItem.getItemId()) {

          // go to the right activity
          case R.id.more_all_symptoms:
            Util.goToListSymptomActivity(null, thisActivity, null);
            break;

          case R.id.more_all_ingredients:
            Util.goToListIngredientActivity(thisContext, thisActivity, null);
            break;

          case R.id.more_export_activity:
            Util.goToExportActivity(thisContext, thisActivity);
            break;

          default:
            break;
        }//end switch case for which menu item was chosen

        return true;
      });
      // Showing the popup menu
      popupMenu.show();

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
    thisContext = this.getBaseContext();
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
