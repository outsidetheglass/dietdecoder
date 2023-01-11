package com.dietdecoder.dietdecoder.activity.foodlog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class NewFoodLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  private EditText mEditTextDateTimeHour;
  private EditText mEditTextDateTimeDay;
  private EditText mEditTextDateTimeMonth;
  private EditText mEditTextDateTimeYear;
  private EditText mEditTextIngredientName;
  private EditText mEditTextIngredientBrand;

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
  private Integer logIngredientDateTimeDay;
  private Integer logIngredientDateTimeYear;
  private Integer logIngredientDateTimeHour;
  private Integer logIngredientDateTimeMonth;

  private Instant mFoodLogDateTimeCooked;
  private Instant mFoodLogDateTimeAcquired;

  private LocalDateTime dateTimeNow;
  private Integer dateTimeNowDay;
  private Integer dateTimeNowYear;
  private Integer dateTimeNowMonth;
  private Integer dateTimeNowHour;


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

    mEditTextDateTimeDay = findViewById(R.id.edittext_new_log_datetime_day);
    mEditTextDateTimeMonth = findViewById(R.id.edittext_new_log_datetime_month);
    mEditTextDateTimeYear = findViewById(R.id.edittext_new_log_datetime_year);

    // this is checking for if this activity was made from a duplicate action or a new log click
    Bundle extras = getIntent().getExtras();
    // if from a duplicate action
    if (extras != null) {
      mNewLogName = extras.getString("ingredientName");
      mNewLogBrand = extras.getString("ingredientBrand");
      logIngredientDateTimeDay = extras.getInt("ingredientDateTimeDay");
      logIngredientDateTimeYear = extras.getInt("ingredientDateTimeYear");
      logIngredientDateTimeHour = extras.getInt("ingredientDateTimeHour");
      logIngredientDateTimeMonth = extras.getInt("ingredientDateTimeMonth");
      mFoodLogDateTimeCooked = (Instant) extras.get("ingredientDateTimeCooked");
      mFoodLogDateTimeAcquired = (Instant) extras.get("ingredientDateTimeAcquired");


      //set the edittexts to say the above values given
      mEditTextDateTimeHour.setText(logIngredientDateTimeHour.toString());
      mEditTextIngredientName.setText(mNewLogName);
      mEditTextIngredientBrand.setText(mNewLogBrand);
      mEditTextDateTimeDay.setText(logIngredientDateTimeDay.toString());
      mEditTextDateTimeMonth.setText(logIngredientDateTimeMonth.toString());
      mEditTextDateTimeYear.setText(logIngredientDateTimeYear.toString());
    }else {
      // we weren't given a duplication, so set the time to be now
      // the user can change the time if it wasn't now
      dateTimeNow = LocalDateTime.now();
      dateTimeNowDay = dateTimeNow.getDayOfMonth();
      dateTimeNowYear = dateTimeNow.getYear();
      dateTimeNowMonth = dateTimeNow.getMonthValue();
      // digestive symptoms that need a diet log won't show up sooner than an hour can capture
      // I can get a migraine within 30min but that's categorically the same
      // as an hour as far as how extreme my triggers are
      // so we don't need minutes here
      dateTimeNowHour = dateTimeNow.getHour();

      // no duplication extras given, so it didn't have a cooked and acquired time
      // so assume it was cooked just now and acquired just before that
      mFoodLogDateTimeCooked = Instant.now();
      mFoodLogDateTimeAcquired = Instant.now();

      // set the edit texts to say now
      // the user can change it if it was earlier or about to eat
      mEditTextDateTimeHour.setText(dateTimeNowHour.toString());
      mEditTextDateTimeDay.setText(dateTimeNowDay.toString());
      mEditTextDateTimeMonth.setText(dateTimeNowMonth.toString());
      mEditTextDateTimeYear.setText(dateTimeNowYear.toString());

      Log.d(TAG, "newFoodLogActivity OnCreate: did not have extras." );
    }//end if not null extras


    // TODO Next step
    //  make edittext for putting in recipe to search for
    // then put that recipe name into the View Model
    // list the Adapter with all the recipe's ingredients
    // then an add ingredient button

    // save info into log database and go back to log page
    saveButton = findViewById(R.id.button_save_new_log);
    saveButton.setOnClickListener(view -> {
      Intent replyIntent = new Intent();

      // check for if views are empty
      isNameViewEmpty = TextUtils.isEmpty(mEditTextIngredientName.getText());
      // if views are empty
      if ( isNameViewEmpty ) {
        // set intent to tell user result is cancelled
        setResult(RESULT_CANCELED, replyIntent);
      }
      // views have values
      else {
        // get string
        mNewLogName = mEditTextIngredientName.getText().toString();
        mNewLogBrand = mEditTextIngredientBrand.getText().toString();

        // TODO forget all this about intents, just save it directly from here
        Log.d(TAG, "onCreate: "+mNewLogName);
        replyIntent.putExtra("ingredientDateTimeAcquired", mFoodLogDateTimeAcquired);
        replyIntent.putExtra("ingredientDateTimeCooked", mFoodLogDateTimeCooked.toString());

        Instant logInstant;
          Calendar logCalendar = Calendar.getInstance();
//          logCalendar.set(Calendar.MONTH, Integer.getInteger(mEditTextDateTimeMonth.getText().toString()));
//          logCalendar.set(Calendar.YEAR, Integer.getInteger(mEditTextDateTimeYear.getText().toString()));
//          logCalendar.set(Calendar.HOUR_OF_DAY, Integer.getInteger(mEditTextDateTimeHour.getText().toString()));
//          logCalendar.set(Calendar.DAY_OF_MONTH,
//            Integer.getInteger(mEditTextDateTimeDay.getText().toString())
//          );

          logInstant = logCalendar.toInstant();
          FoodLog foodLog = new FoodLog(mNewLogName, mNewLogBrand, logInstant,
            logInstant);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
          mFoodLogViewModel.viewModelInsertFoodLog(foodLog);




        setResult(RESULT_OK, replyIntent);
      }
      finish();
    });

  }//end OnCreate




}//end NewLogActivity
