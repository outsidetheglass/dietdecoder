package com.dietdecoder.dietdecoder.activity.log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;

//TODO make delete a fragment or a popup, change this after getting pass ID through the layers working

public class DetailLogActivity extends AppCompatActivity {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = DetailLogActivity.this;

  public Button returnButton;
  private Intent returnIntent;

  public static String mLogDetail;
  private TextView mLogDetailView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_log);

    mLogDetailView = findViewById(R.id.textview_log_detail_value);
    mLogDetailView.setMovementMethod(new ScrollingMovementMethod());

    Intent detailLogIntent = getIntent();


    // Null check the intent worked
    if (null == detailLogIntent) {
      // go back if it was null
      Toast.makeText(this, "Can't view an invalid log", Toast.LENGTH_SHORT).show();
      this.startActivity(new Intent(this, LogActivity.class));
    } else {
    // intent worked
      // get values from the intent for symptom we're displaying details for
      mLogDetail = detailLogIntent.getStringExtra("log_detail");
      mLogDetailView.setText(mLogDetail);

      // return to all logs
      returnButton = findViewById(R.id.return_button_from_detail_to_all_log);
      returnButton.setOnClickListener(view -> {
        returnIntent = new Intent(thisActivity, LogActivity.class);
        startActivity(returnIntent);
      });

      Log.d(TAG, "onCreate: tags work");

    }
  }

}
