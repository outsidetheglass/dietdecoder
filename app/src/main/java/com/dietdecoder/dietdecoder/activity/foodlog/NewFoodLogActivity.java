package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogListAdapter;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeListAdapter;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

public class NewFoodLogActivity extends AppCompatActivity implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = NewFoodLogActivity.this;

  private EditText mEditTextDateTimeHour;
  private EditText mEditTextDateTimeMinute;
  private EditText mEditTextDateTimeDay;
  private EditText mEditTextDateTimeMonth;
  private EditText mEditTextDateTimeYear;
  private EditText mEditTextIngredientName;
  private EditText mEditTextIngredientBrand;


  private Boolean isNameViewEmpty;

  private FoodLogViewModel mNewFoodLogViewModel;
  private FoodLogListAdapter mNewFoodLogListAdapter;
  private RecipeViewModel mRecipeViewModel;
  private RecipeListAdapter mRecipeListAdapter;

  private Button saveButton;
  private Button mTimeButton;

  private List<Recipe> mActivityAllRecipe;

  private Intent addIntent;

  private String mNewLogName;
  private String mNewLogBrand;
  private Integer logIngredientDateTimeHour;
  private Integer logIngredientDateTimeMinute;
  private Integer logIngredientDateTimeDay;
  private Integer logIngredientDateTimeYear;
  private Integer logIngredientDateTimeMonth;

  private Instant mFoodLogDateTimeCooked;
  private Instant mFoodLogDateTimeAcquired;

  private LocalDateTime dateTimeNow;
  private Integer dateTimeNowDay;
  private Integer dateTimeNowYear;
  private Integer dateTimeNowMonth;
  private Integer dateTimeNowHour;
  private Integer dateTimeNowMinute;

  private Integer mHour;
  private Integer mMinute;
  private Integer mDay;
  private Integer mMonth;
  private Integer mYear;

  private FoodLogViewModel mFoodLogViewModel;
  private TextView mTextViewSelectedDate;
  private TimePicker mTimepicker;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    //TODO make recipe dropdown, listed in order of frequency made
    // the dropdown should be typeable, which selects for that recipe
    // if chosen, auto put the ingredients of that recipe into the view
    // beside each ingredient, edittext dropdown for common replacements, can search select for by typing
    // Add new ingredient at bottom, adds another row of edittext
    // Add option for unknown ingredients were in this
    // add option next to each to select if it was "this" or maybe "that", like on chips where it says Canola Oil or Sunflower Oil
    // if add new ingredient was clicked, when save is pressed add that in as a variant of the recipe
    // add view for now or earlier, which then allows user to say today, yesterday, earlier,
    // earlier selected then option shows days of the month,
    // then clicks broad times of day like morning, midday, evening, overnight, then specific times as an optional selection


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_food_log);

    mEditTextIngredientName = findViewById(R.id.edittext_new_log_ingredient_name);
    mEditTextIngredientBrand = findViewById(R.id.edittext_new_log_ingredient_brand);

    mEditTextDateTimeHour = findViewById(R.id.edittext_new_log_datetime_hour);
    mEditTextDateTimeMinute = findViewById(R.id.edittext_new_log_datetime_minute);

    mEditTextDateTimeDay = findViewById(R.id.edittext_new_log_datetime_day);
    mEditTextDateTimeMonth = findViewById(R.id.edittext_new_log_datetime_month);
    mEditTextDateTimeYear = findViewById(R.id.edittext_new_log_datetime_year);
    mTimeButton = findViewById(R.id.time_button);

    saveButton = findViewById(R.id.button_save_new_log);

    // this is checking for if this activity was made from a duplicate action or a new log click
    Bundle extras = getIntent().getExtras();
    // if from a duplicate action
    if (extras != null) {
      mNewLogName = extras.getString(Util.ARGUMENT_INGREDIENT_NAME);
      mNewLogBrand = extras.getString(Util.ARGUMENT_INGREDIENT_BRAND);
      //TODO change arguments to hh_mm_dd_mm_yy as saved string for time cooked
      logIngredientDateTimeDay = extras.getInt("ingredientDateTimeDay");
      logIngredientDateTimeYear = extras.getInt("ingredientDateTimeYear");
      logIngredientDateTimeHour = extras.getInt("ingredientDateTimeHour");
      logIngredientDateTimeMinute = extras.getInt("ingredientDateTimeMinute");
      logIngredientDateTimeMonth = extras.getInt("ingredientDateTimeMonth");
      mFoodLogDateTimeCooked = (Instant) extras.get("ingredientDateTimeCooked");
      mFoodLogDateTimeAcquired = (Instant) extras.get("ingredientDateTimeAcquired");



    } else {
      // we weren't given a duplication, so set the time to be now
      // the user can change the time if it wasn't now
      dateTimeNow = LocalDateTime.now();
      logIngredientDateTimeDay = dateTimeNow.getDayOfMonth();
      logIngredientDateTimeYear = dateTimeNow.getYear();
      // this returns not zero indexed number, so let's zero index it for consistency
      logIngredientDateTimeMonth = dateTimeNow.getMonth().getValue()-1;
      logIngredientDateTimeHour = dateTimeNow.getHour();
      logIngredientDateTimeMinute = dateTimeNow.getMinute();

      // no duplication extras given, so it didn't have a cooked and acquired time
      // so assume it was cooked just now and acquired just now
      mFoodLogDateTimeCooked = Instant.now();
      mFoodLogDateTimeAcquired = Instant.now();

      Log.d(TAG, "newFoodLogActivity OnCreate: did not have extras." );
    }//end if not null extras

    //set the edittexts to say the above values given
    mEditTextDateTimeHour.setText(logIngredientDateTimeHour.toString());
    mEditTextIngredientName.setText(mNewLogName);
    mEditTextIngredientBrand.setText(mNewLogBrand);
    // if minutes is less than 10, put a 0 before it for displaying
    mEditTextDateTimeMinute.setText(Util.setMinutesString(logIngredientDateTimeMinute) );
    mEditTextDateTimeMonth.setText( Util.setMonthString(logIngredientDateTimeMonth) );
    mEditTextDateTimeYear.setText(logIngredientDateTimeYear.toString());
    mEditTextDateTimeDay.setText(logIngredientDateTimeDay.toString());

    // TODO make edittext for putting in recipe to search for
    // then put that recipe name into the View Model
    // list the Adapter with all the recipe's ingredients
    // then an add ingredient button

    // on below line we are adding click listener
    // for our pick date button
    // and for our time picker
    mEditTextDateTimeYear.setOnClickListener(this);
    mEditTextDateTimeDay.setOnClickListener(this);
    mEditTextDateTimeMonth.setOnClickListener(this);

    mTimeButton.setOnClickListener(this);



    // save info into log database and go back to log page
    saveButton.setOnClickListener(this);


  }//end OnCreate


  private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
      return source;
    }
  }//end numeric keypad function


  @Override
  public void onClick(View v) {
    Log.d(TAG, "onCreate: something was clicked");
    // case switch for which button was pressed
    switch (v.getId()) {
      // if day or month or year is pressed
      // start date picker
      case R.id.edittext_new_log_datetime_day:
      case R.id.edittext_new_log_datetime_month:
      case R.id.edittext_new_log_datetime_year:

        // default method for handling onClick Events..
        // create a variable for date picker dialog.
        // passing context.
        // setting date to our text view.
        DatePickerDialog datePickerDialog = new DatePickerDialog( thisActivity,
          new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
              mTextViewSelectedDate.setText( (Util.setMonthString(logIngredientDateTimeMonth))+
                      "-" + dayOfMonth  + "-" + year);
            }
          },
          // on below line we are passing year,
          // month and day for selected date in our date picker.
          logIngredientDateTimeYear,
          logIngredientDateTimeMonth,
          logIngredientDateTimeDay
        );
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
        break;
      // if it's minute or hour pressed
      // start TimePicker
      case R.id.time_button:
        //in the Utility

        break;
      case R.id.button_save_new_log:

        Intent replyIntent = new Intent();

        // TODO add validation for correct values, i.e. if less than 1900 make it 1900 etc
        //  year = (year<1900)?1900:(year>2100)?2100:year;
        // check for if views are empty
        isNameViewEmpty = TextUtils.isEmpty(mEditTextIngredientName.getText());
        // if views are empty
        if ( isNameViewEmpty ) {
          // set intent to tell user result is cancelled
          setResult(RESULT_CANCELED, replyIntent);
        }
        // if views have values
        else {
          // get strings
          mNewLogName = mEditTextIngredientName.getText().toString();
          mNewLogBrand = mEditTextIngredientBrand.getText().toString();
          Log.d(TAG, "onCreate: mNewLogName = " + mNewLogName);

          // get the values from the edit texts
          mYear = Integer.parseInt(mEditTextDateTimeYear.getText().toString());
          mMonth = Util.getMonthInteger(mEditTextDateTimeMonth.getText().toString());
          mHour = Integer.parseInt(mEditTextDateTimeHour.getText().toString());
          mMinute = Integer.parseInt(mEditTextDateTimeMinute.getText().toString());
          mDay = Integer.parseInt(mEditTextDateTimeDay.getText().toString());

          // make an instant from our values
          Instant logInstant = Util.instantFromValues(mHour, mMinute, mDay, mMonth, mYear);
          // make our food log
          FoodLog foodLog = new FoodLog(mNewLogName, mNewLogBrand, logInstant, logInstant, logInstant);

          mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
          mFoodLogViewModel.viewModelInsertFoodLog(foodLog);




          setResult(RESULT_OK, replyIntent);
        }
        finish();
        break;
      default:
        break;
    }

  }



}//end NewLogActivity
