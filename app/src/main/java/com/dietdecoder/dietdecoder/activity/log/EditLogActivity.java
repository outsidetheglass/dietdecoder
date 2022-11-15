package com.dietdecoder.dietdecoder.activity.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.log.LogActivity;

//TODO make delete a fragment or a popup, change this after getting pass ID through the layers working

public class EditLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mOldLogName;
//  public static String mOldLogConcern;
  public static String mNewLogName;
//  public static String mNewLogConcern;

  private EditText mEditLogNewNameView;
//  private EditText mEditLogNewConcernView;

  private TextView mLogOldNameView;
//  private TextView mLogOldConcernView;
  private String mLogOldName;
//  private String mLogOldConcern;

  private Boolean isNewNameViewEmpty;
//  private Boolean isNewConcernViewEmpty;

  private Button editLogSaveButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //TODO make delete a fragment or a popup, change this after getting pass ID through the layers working

//    setContentView(R.layout.activity_edit_log);
//
//    mEditLogNewNameView = findViewById(R.id.edittext_new_log_name);
////    mEditLogNewConcernView = findViewById(R.id.edittext_new_log_concern);
//
//    mLogOldNameView = findViewById(R.id.textview_old_log_name);
//    mLogOldConcernView = findViewById(R.id.textview_old_log_concern);

    Intent editLogIntent = getIntent();

    // Null check the intent worked
    if (null == editLogIntent) {
      // go back if it was null
      Toast.makeText(this, "Can't edit an invalid log", Toast.LENGTH_SHORT).show();
      this.startActivity( new Intent(this, EditLogActivity.class));
    }

    mLogOldName = editLogIntent.getStringExtra("log_name");
//    mLogOldConcern = editLogIntent.getStringExtra("log_concern");
    mLogOldNameView.setText(mLogOldName);
//    mLogOldConcernView.setText(mLogOldConcern);

    //TODO fix this when I just can save it through here instead of going back to save
    // make an intent to hold our edited log to go back to where we can save it
    Intent replyIntent = new Intent(this, LogActivity.class);

    replyIntent.putExtra("old_name", mLogOldName);
//    replyIntent.putExtra("old_concern", mLogOldConcern);

//    editLogSaveButton = findViewById(R.id.button_edit_log_save);
    editLogSaveButton.setOnClickListener(view -> {

      // check for if views are empty
      isNewNameViewEmpty = TextUtils.isEmpty(mEditLogNewNameView.getText());
//      isNewConcernViewEmpty = TextUtils.isEmpty(mEditLogNewConcernView.getText());

      // One of the new values needs to have been updated
      if (isNewNameViewEmpty/* && isNewConcernViewEmpty*/) {
        // tell user nope
        Toast.makeText(this, "Update one of the fields to save", Toast.LENGTH_SHORT).show();
      }
      // at least one of the new values is filled
      else {
        // check both values, if they're not empty add them to intent
        if (!isNewNameViewEmpty) {
          // Name is not empty, so add that
          mNewLogName = mEditLogNewNameView.getText().toString();
          replyIntent.putExtra("new_name", mNewLogName);
        }
//        if (!isNewConcernViewEmpty) {
//          // Concern is not empty, so add it
//          mNewLogConcern = mEditLogNewConcernView.getText().toString();
//          replyIntent.putExtra("new_concern", mNewLogConcern);
//        }

      }

      this.startActivity( replyIntent );

    finish();
    });
  }

}
