package com.dietdecoder.dietdecoder.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.ingredient.IngredientActivity;
import com.dietdecoder.dietdecoder.activity.recipe.RecipeActivity;
import com.dietdecoder.dietdecoder.activity.symptom.SymptomActivity;

//TODO fix edit delete button
public class FirstStartWizardActivity extends AppCompatActivity {

  // make a TAG to use to export errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = FirstStartWizardActivity.this;

  // constant code for runtime permissions
  private static final int PERMISSION_REQUEST_CODE = 200;

  @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //set view
      setContentView(R.layout.activity_other);


    //TODO What symptoms do you want to track?
    //add those to symptoms
    //Do you know the name of your condition(s) that you're wanting to track?
    //if so, ask what condition they want to track
    //add condition to conditions database
    //get list of symptoms that are a part of that condition
    // list symptoms in order of how common they are
    //ask user to check which symptoms they want to track
    //and ask how concerning that symptom is to them on scale, autoselect lowest concern
    //ask user if they want to add any other symptoms to that condition
    //ask if they want to add another condition or symptom
    // for each symptom to track, open description box to type in description name

    // below code is used for
    // checking our permissions.
//        if (checkPermission()) {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//        } else {
//            requestPermission();
//        }


  } //end onCreate

  //TODO fix these permissions methods, they don't work yet
  private boolean checkPermission() {
    // checking of permissions.
    int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
    int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
    return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    // requesting permissions if not provided.
    ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0) {

        // after requesting permissions we are showing
        // users a toast message of permission granted.
        boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

        if (writeStorage && readStorage) {
          Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
          finish();
        }
      }
    }
  }

} // end MainActivity
