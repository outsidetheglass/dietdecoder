package com.dietdecoder.dietdecoder.activity.ingredient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListIngredientActivity extends AppCompatActivity implements View.OnClickListener,
        Toolbar.OnMenuItemClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  // Log.d(TAG, "onActivityResult: made it here");
  private final Activity thisActivity = ListIngredientActivity.this;
  private Context thisContext;

  Fragment mFragmentGoTo = null;

  EditText mEditTextSearch;

  private IngredientViewModel mIngredientViewModel;
  private IngredientListAdapter mIngredientListAdapter;
  public ArrayList<Ingredient> mIngredients;

  public String mGivenFilterString;

  public FloatingActionButton addButton;

  private Intent addIntent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_ingredient);


    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_list_ingredient);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);

    thisContext = getBaseContext();


    // if we have no view made
    if (savedInstanceState == null) {
      // set filter string if we have one
      if ( getIntent().getExtras() != null ) {

        if ( getIntent().getExtras().containsKey(Util.ARGUMENT_FILTER) ) {
          Log.d(TAG, "in bundle, getIntent().getExtras().getString(Util.ARGUMENT_FILTER): "
                  + getIntent().getExtras().getString(Util.ARGUMENT_FILTER));

          // we only want to set the filter string not keep this bundle
          mGivenFilterString = getIntent().getExtras().getString(Util.ARGUMENT_FILTER);
        }
      }

      // make the view for listing the items in the log
      RecyclerView recyclerViewIngredient = findViewById(R.id.recyclerview_list_ingredient);
      // add horizontal lines between each recyclerview item
      recyclerViewIngredient.addItemDecoration(new DividerItemDecoration(recyclerViewIngredient.getContext(),
              DividerItemDecoration.VERTICAL));


      mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
      mIngredients = mIngredientViewModel.viewModelGetAllArrayList();
      mIngredientListAdapter =
              new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
      recyclerViewIngredient.setAdapter(mIngredientListAdapter);
      recyclerViewIngredient.setLayoutManager(new LinearLayoutManager(this));

      mIngredientViewModel.viewModelGetAllLiveData(mGivenFilterString).observe(this,
              new Observer<List<Ingredient>>() {
                @Override
                public void onChanged(List<Ingredient> ingredients) {
                  // Update the cached copy of the words in the adapter.
                  mIngredientListAdapter.setFilterIngredientList(
                          ingredients, mGivenFilterString);

                }
              });

      // FAB to add new log
      addButton = findViewById(R.id.add_button_list_ingredient_log);
      addButton.setOnClickListener(this);
      mEditTextSearch = findViewById(R.id.edittext_choose_ingredient_search);
      mEditTextSearch.setOnClickListener(this::onClick);
    }

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.add_button_list_ingredient_log:
        // go to the list of ingredients the user experiences to allow user to select which ones
        // they're having now and then make those ingredient logs
        Util.goToChooseIngredientActivity(thisContext, thisActivity);

        break;
      case R.id.imagebutton_ingredient_search:
        if ( TextUtils.isEmpty(mEditTextSearch.getText()) ){

          Toast.makeText(thisActivity, "Please enter text in order to search...", Toast.LENGTH_SHORT).show();
        } else {
          // get the text in the search edit text and filter the adapter with it
          String filterString = mEditTextSearch.getText().toString().toLowerCase(Locale.ROOT);

          Util.searchThisActivity(thisContext, thisActivity, filterString);


        }
      default:
        break;
    }//end switch case
  }//end onClick



  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
      // TODO go to preferences when those have been made

    } else if (item.getItemId() == R.id.action_go_home) {
      Util.goToMainActivity(null, thisActivity);
    }  else if (item.getItemId() == R.id.action_more) {

      // Initializing the popup menu and giving the reference as current logContext
      PopupMenu popupMenu = new PopupMenu(thisContext, findViewById(R.id.action_more));
      // Inflating popup menu from popup_menu.xml file
      popupMenu.getMenuInflater().inflate(R.menu.item_more_menu, popupMenu.getMenu());
      popupMenu.setGravity(Gravity.END);
      // if an option in the menu is clicked
      popupMenu.setOnMenuItemClickListener(moreMenuItem -> {
        // which button was clicked
        switch (moreMenuItem.getItemId()) {

          // go to the right activity
          case R.id.more_all_symptoms:
            Util.goToListSymptomActivity(null, thisActivity, null);
            break;

          case R.id.more_all_ingredients:
            Util.goToListIngredientActivity(thisContext, thisActivity, null);
            break;

          default:
            break;
        }//end switch case for which menu item was chosen

        return true;
      });
      // Showing the popup menu
      popupMenu.show();

    }

    return false;
  }

}//end LogActivity



