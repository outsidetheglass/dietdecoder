package com.dietdecoder.dietdecoder.activity.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;

public class DeleteLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String logName;
//  public static String logConcern;

  private EditText logNameView;
//  private EditText logConcernView;

  private Boolean isNameViewEmpty;
//  private Boolean isConcernViewEmpty;

  private Button deleteButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //TODO make delete a fragment or a popup, change this after getting pass ID through the layers working
//    setContentView(R.layout.activity_delete_log);
//    logNameView = findViewById(R.id.edittext_delete_log_name);
//    logConcernView = findViewById(R.id.edittext_delete_log_concern);

    deleteButton = findViewById(R.id.button_delete);
    deleteButton.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(logNameView.getText());
//      isConcernViewEmpty = TextUtils.isEmpty(logConcernView.getText());

      // if either of the old views are empty
      if ( isNameViewEmpty /*|| isConcernViewEmpty */) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
          // get strings for name and concern
          logName = logNameView.getText().toString();
//          logConcern = logConcernView.getText().toString();

          replyIntent.putExtra("log_name", logName);
//          replyIntent.putExtra("log_concern", logConcern);

          // check both values
          Log.d(TAG, "onCreate: " + logName /*+ ": " + logConcern*/);
          // send back values for new log and tell user succeeded
          setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
