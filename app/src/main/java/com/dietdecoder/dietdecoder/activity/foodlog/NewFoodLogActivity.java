package com.dietdecoder.dietdecoder.activity.foodlog;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
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
import java.util.Objects;

public class NewFoodLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  private EditText mEditTextDateTimeHour;
  private EditText mEditTextDateTimeMinute;
  private EditText mEditTextDateTimeDay;
  private EditText mEditTextDateTimeMonth;
  private EditText mEditTextDateTimeYear;
  private EditText mEditTextIngredientName;
  private EditText mEditTextIngredientBrand;


  String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
    "Sept", "Oct", "Nov", "Dec" };

  private Boolean isNameViewEmpty;
  //private Boolean isConcernViewEmpty;

  private FoodLogViewModel mNewFoodLogViewModel;
  private FoodLogListAdapter mNewFoodLogListAdapter;
  private RecipeViewModel mRecipeViewModel;
  private RecipeListAdapter mRecipeListAdapter;

  private Button saveButton;

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


  private FoodLogViewModel mFoodLogViewModel;

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

    //TODO make month into dropdown or all the date into the calendar select

    mEditTextDateTimeHour.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    mEditTextDateTimeMinute.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    mEditTextDateTimeDay.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    mEditTextDateTimeYear.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    mEditTextDateTimeHour.setTransformationMethod(new NumericKeyBoardTransformationMethod());
    mEditTextDateTimeMinute.setTransformationMethod(new NumericKeyBoardTransformationMethod());
    mEditTextDateTimeDay.setTransformationMethod(new NumericKeyBoardTransformationMethod());
    mEditTextDateTimeYear.setTransformationMethod(new NumericKeyBoardTransformationMethod());

    // this is checking for if this activity was made from a duplicate action or a new log click
    Bundle extras = getIntent().getExtras();
    // if from a duplicate action
    if (extras != null) {
      mNewLogName = extras.getString("ingredientName");
      mNewLogBrand = extras.getString("ingredientBrand");
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
    mEditTextDateTimeDay.setText(logIngredientDateTimeDay.toString());
    mEditTextDateTimeMonth.setText(setMonthString(logIngredientDateTimeMonth));
    mEditTextDateTimeYear.setText(logIngredientDateTimeYear.toString());
    // if minutes is less than 10, put a 0 before it for displaying
    mEditTextDateTimeMinute.setText( setMinutesString(logIngredientDateTimeMinute) );

    // TODO make edittext for putting in recipe to search for
    // then put that recipe name into the View Model
    // list the Adapter with all the recipe's ingredients
    // then an add ingredient button

    // save info into log database and go back to log page
    saveButton = findViewById(R.id.button_save_new_log);
    saveButton.setOnClickListener(view -> {
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

        Log.d(TAG, "onCreate: "+mNewLogName);

        // save it directly from here
        Instant logInstant;
          Calendar logCalendar = Calendar.getInstance();
          logCalendar.set(Calendar.MONTH,
            getMonthInteger(mEditTextDateTimeMonth.getText().toString()));
          logCalendar.set(Calendar.YEAR, Integer.parseInt(mEditTextDateTimeYear.getText().toString()));
        logCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mEditTextDateTimeHour.getText().toString()));
        logCalendar.set(Calendar.MINUTE,Integer.parseInt(mEditTextDateTimeMinute.getText().toString()));
          logCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mEditTextDateTimeDay.getText().toString()));

          logInstant = logCalendar.toInstant();
          FoodLog foodLog = new FoodLog(mNewLogName, mNewLogBrand, logInstant, logInstant, logInstant);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
        mFoodLogViewModel.viewModelInsertFoodLog(foodLog);




        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });

  }//end OnCreate


  private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
      return source;
    }
  }//end numeric keypad function

  // given an integer of minute of datetime
  // return a string to set for displaying
  // with a 0 in front if the minute is less than 10
  private String setMinutesString(Integer minute){

    String mMinutesString;

    if ( minute < 10 ) {
      mMinutesString = "0" + minute.toString();
    } else {
      mMinutesString = minute.toString();
    }

    return mMinutesString;
  }//end setMinutesString function

  // set month from zero indexed value to a string of the month name
  private String setMonthString(Integer monthIndex){

    // if they're invalid values less than Jan or more than Dec, set to those
    if (monthIndex < 0) {
      monthIndex = 0;
    }
    else if (monthIndex > 11) {
      monthIndex = 11;
    }
    String month = months[monthIndex];

    return month;
  }

  // get the integer zero indexed from a three char string of the month
  // if it's anything but the given three char strings, return 0 for January
  private Integer getMonthInteger(String monthString){
    Integer monthInt;

    if (monthString == "Jan") {
      monthInt = 0;
    } else if (Objects.equals(monthString, "Feb")) {
      monthInt = 1;
    } else if (Objects.equals(monthString, "Mar")) {
      monthInt = 2;
    } else if (Objects.equals(monthString, "Apr")) {
      monthInt = 3;
    } else if (Objects.equals(monthString, "May")) {
      monthInt = 4;
    } else if (Objects.equals(monthString, "Jun")) {
      monthInt = 5;
    } else if (Objects.equals(monthString, "Jul")) {
      monthInt = 6;
    } else if (Objects.equals(monthString, "Aug")) {
      monthInt = 7;
    } else if (Objects.equals(monthString, "Sep")) {
      monthInt = 8;
    } else if (Objects.equals(monthString, "Oct")) {
      monthInt = 9;
    } else if (Objects.equals(monthString, "Nov")) {
      monthInt = 10;
    } else if (Objects.equals(monthString, "Dec")) {
      monthInt = 11;
    } else {
      monthInt = 0;
    }

    Log.d(TAG, "newFoodLogActivity monthString=" + monthString );
    Log.d(TAG, "newFoodLogActivity monthInt=" + monthInt.toString() );
    return monthInt;
  }

}//end NewLogActivity
