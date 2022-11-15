package com.dietdecoder.dietdecoder.activity.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;

public class NewLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mLogName;
  //public static String mLogConcern;

  private EditText mEditLogNameView;
  //private EditText mEditLogConcernView;

  private Boolean isNameViewEmpty;
  //private Boolean isConcernViewEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_log);
    mEditLogNameView = findViewById(R.id.edittext_log_name);
    //mEditLogConcernView = findViewById(R.id.edittext_log_concern);

    final Button button = findViewById(R.id.button_save);
    button.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditLogNameView.getText());
//      isConcernViewEmpty = TextUtils.isEmpty(mEditLogConcernView.getText());

      // if either of the views are empty
      if ( isNameViewEmpty /*|| isConcernViewEmpty*/ ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // both views have values
      else {
        // get strings for name and concern
        mLogName = mEditLogNameView.getText().toString();
//        mLogConcern = mEditLogConcernView.getText().toString();

        Log.d(TAG, "onCreate: "+mLogName/*+": "+mLogConcern*/);
        // send back values for new log and tell user succeeded
        replyIntent.putExtra("log_name", mLogName);
//        replyIntent.putExtra("concern", mLogConcern);
        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });
  }

}
