package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.MainActivity;

//TODO make delete a fragment or a popup, change this after getting pass ID through the layers working

public class DetailFoodLogActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = DetailFoodLogActivity.this;

  public Button returnButton;
  private Intent returnIntent;

  public static String mLogDetail;
  private TextView mLogDetailView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_food_log);

    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_food_log);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);
    mLogDetailView = findViewById(R.id.textview_log_detail_value);
    mLogDetailView.setMovementMethod(new ScrollingMovementMethod());

    Intent detailLogIntent = getIntent();


    // Null check the intent worked
    if (null == detailLogIntent) {
      // go back if it was null
      Toast.makeText(this, "Can't view an invalid log", Toast.LENGTH_SHORT).show();
      this.startActivity(new Intent(this, FoodLogActivity.class));
    } else {
    // intent worked
      // get values from the intent for symptom we're displaying details for
      mLogDetail = detailLogIntent.getStringExtra("food_log_detail");
      mLogDetailView.setText(mLogDetail);

      // return to all logs
      returnButton = findViewById(R.id.return_button_from_detail_to_all_log);
      returnButton.setOnClickListener(view -> {
        returnIntent = new Intent(thisActivity, FoodLogActivity.class);
        startActivity(returnIntent);
      });

      Log.d(TAG, "onCreate: tags work");

    }
  }


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
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }
}
