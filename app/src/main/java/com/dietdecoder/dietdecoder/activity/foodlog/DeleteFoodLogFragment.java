package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.time.Instant;
import java.util.UUID;

public class DeleteFoodLogFragment extends Fragment implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = getActivity();

  private UUID mFoodLogId;
  public static String mStringFoodLogDetails, mFoodLogIdString;

  TextView mTextViewFoodLogDetails;

  private Button mButtonSave;
  private Button mButtonCancel;

  Bundle mBundle;
  FoodLogViewModel mFoodLogViewModel;
  FoodLog mFoodLog;

  Intent mIntent;

  public DeleteFoodLogFragment() {
    super(R.layout.fragment_delete_food_log);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_delete_food_log, container, false);

  }//end onCreateView

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    mButtonSave = view.findViewById(R.id.button_delete_food_log_save);
    mButtonCancel = view.findViewById(R.id.button_delete_food_log_cancel);
    mTextViewFoodLogDetails = view.findViewById(R.id.textview_delete_food_log_details);

    // set the listeners on the buttons
    // to run onClick method when they are clicked
    mButtonSave.setOnClickListener(this);
    mButtonCancel.setOnClickListener(this);

    // set which food log we're asking if they want to delete
    mBundle = getArguments();
    mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
    mFoodLogId = UUID.fromString( mBundle.getString(Util.ARGUMENT_FOOD_LOG_ID) );
    mFoodLog = mFoodLogViewModel.viewModelGetFoodLogFromId(mFoodLogId);
    mStringFoodLogDetails = mFoodLog.toString();
    mTextViewFoodLogDetails.setText(mStringFoodLogDetails);

  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      // which button was clicked
      case R.id.button_delete_food_log_save:
        Toast.makeText(getContext(), getResources().getString(R.string.deleting),
                Toast.LENGTH_SHORT).show();
        // and now delete the one chosen
        mFoodLogViewModel.viewModelDeleteFoodLog(mFoodLog);
        break;

      case R.id.button_delete_food_log_cancel:
        // just toast them that the transaction is cancelled
        Toast.makeText(getContext(), getResources().getString(R.string.cancelling),
                Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }//end switch case

    // go back to activity to go to the next fragment
    mIntent = new Intent(thisActivity, FoodLogActivity.class);
    startActivity(mIntent);

  }//end onClick
}
