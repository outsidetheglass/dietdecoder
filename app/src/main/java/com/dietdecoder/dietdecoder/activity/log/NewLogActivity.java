package com.dietdecoder.dietdecoder.activity.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.ui.log.LogListAdapter;
import com.dietdecoder.dietdecoder.ui.log.LogViewModel;
import com.dietdecoder.dietdecoder.ui.log.LogListAdapter;
import com.dietdecoder.dietdecoder.ui.log.LogViewModel;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeListAdapter;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;

import java.util.List;

public class NewLogActivity extends AppCompatActivity {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public static String mNewLogName;
  //public static String mNewLogConcern;

  private EditText mEditLogNameView;
  //private EditText mEditLogConcernView;

  private Boolean isNameViewEmpty;
  //private Boolean isConcernViewEmpty;

  private LogViewModel mNewLogViewModel;
  private LogListAdapter mNewLogListAdapter;
  private RecipeViewModel mRecipeViewModel;
  private RecipeListAdapter mRecipeListAdapter;

  private List<Recipe> mActivityAllRecipe;

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
    setContentView(R.layout.activity_new_log);

    // TODO Next step
    //  make edittext for putting in recipe to search for
    // then put that recipe name into the View Model
    // list the Adapter with all the recipe's ingredients
    // then an add ingredient button
    // then make ingredients edit texts


    RecyclerView recyclerView = findViewById(R.id.recyclerview_new_log);

    // Setup view to get info from database
    mRecipeListAdapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff());
    recyclerView.setAdapter(mNewLogListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    // Setup access to database
    mNewLogViewModel = new ViewModelProvider(this).get(LogViewModel.class);
    // if not null, then set list of logs
//    mActivityAllRecipe = mRecipeViewModel.viewModelGetAllRecipeFromName(recipeName);
//    // turn LiveData into list and set that in Adapter so we can get positions
//    mNewLogListAdapter.setLogList(mActivityAllLogs.getValue());
//
//    //TODO move this if statement to setLogList somehow
//    if ( mActivityAllLogs != null ) {
//      mActivityAllLogs.observe(this, logs -> {
//        // Update the cached copy of the words in the adapter.
//        mNewLogListAdapter.submitList(logs);
//      });
//






//    mEditLogNameView = findViewById(R.id.edittext_log);
    //mEditLogConcernView = findViewById(R.id.edittext_log_concern);

//    final Button button = findViewById(R.id.button_save);
//    button.setOnClickListener(view -> {
//      Intent replyIntent = new Intent();
//
//      // check for if views are empty
//      isNameViewEmpty = TextUtils.isEmpty(mEditLogNameView.getText());
////      isConcernViewEmpty = TextUtils.isEmpty(mEditLogConcernView.getText());
//
//      // if either of the views are empty
//      if ( isNameViewEmpty /*|| isConcernViewEmpty*/ ) {
//        // set intent to tell user result is cancelled
//        setResult(RESULT_CANCELED, replyIntent);
//      }
//      // both views have values
//      else {
//        // get strings for name and concern
//        mNewLogName = mEditLogNameView.getText().toString();
////        mNewLogConcern = mEditLogConcernView.getText().toString();
//
//        Log.d(TAG, "onCreate: "+mNewLogName/*+": "+mNewLogConcern*/);
//        // send back values for new log and tell user succeeded
//        replyIntent.putExtra("log_name", mNewLogName);
////        replyIntent.putExtra("concern", mNewLogConcern);
//        setResult(RESULT_OK, replyIntent);
//      }
//      finish();
//    });
  }

}
