package com.dietdecoder.dietdecoder.activity.ingredientlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
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
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ChooseIngredientActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener,
        View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseIngredientActivity.this;
    private Context thisContext;

    //TODO fix names
//    int mFragmentContainerView = Util.fragmentContainerViewChooseIngredientLog;
    Bundle mBundle, mBundleNext;

    String mTooManyTryAgainString, mSaveString, mEmptyTryAgainString, mGivenFilterString;
    Button mButtonSaveName;
    FloatingActionButton mButtonAdd;
    EditText mEditTextSearch;
    SearchView searchView;
    ImageButton mButtonSubmitSearch;

    ArrayList<String> mSelectedArrayList = null;

    private Fragment nextFragment = null;

    IngredientListAdapter mIngredientListAdapter;
    IngredientViewModel mIngredientViewModel;
    IngredientLogViewModel mIngredientLogViewModel;
    RecyclerView recyclerViewIngredientNameChoices;

    ColorStateList selectedColor;
    ColorStateList unSelectedColor;

    public ChooseIngredientActivity() {
        super(R.layout.activity_choose_ingredient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_choose_ingredient);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        thisContext = thisActivity.getApplicationContext();


        if (savedInstanceState == null) {

            // basic variable setup
            mSelectedArrayList = new ArrayList<>();
            if ( getIntent().getExtras() == null ) {
                mBundle = new Bundle();
            } else {
                Log.d(TAG,
                        "in bundle, getIntent().getExtras(): " + getIntent().getExtras());

                if ( getIntent().getExtras().containsKey(Util.ARGUMENT_FILTER) ) {
                    Log.d(TAG, "in bundle, getIntent().getExtras().getString(Util.ARGUMENT_FILTER): "
                            + getIntent().getExtras().getString(Util.ARGUMENT_FILTER));

                    // we only want to set the filter string not keep this bundle
                    mGivenFilterString = getIntent().getExtras().getString(Util.ARGUMENT_FILTER);
                    mBundle = new Bundle();
                } else {
                    mBundle = getIntent().getExtras();
                }
            }
            mBundleNext = mBundle;

            //save button
            mButtonSaveName = findViewById(R.id.button_choose_ingredient_save);
            mButtonSaveName.setOnClickListener(this);
            mButtonAdd = findViewById(R.id.button_choose_ingredient_add);
            mButtonAdd.setOnClickListener(this);

            //search bar and button
            mButtonSubmitSearch = findViewById(R.id.imagebutton_ingredient_search);
            mButtonSubmitSearch.setOnClickListener(this::onClick);
            mEditTextSearch = findViewById(R.id.edittext_choose_ingredient_search);
            mEditTextSearch.setOnClickListener(this::onClick);
            // TODO get active search bar working
            //TODO Ingredient log will need to search ingredients for the ingredient being added,
            // and if it doesn’t already exist add it in,
            // if it does then use existing ingredient ID.
            //TODO add search bar in top with add button next to it OR make an add button at bottom
            //TODO make it case insensitive

            // make the view for listing the items in the log
            recyclerViewIngredientNameChoices =
                    findViewById(R.id.recyclerview_ingredient_name_choices);
            // add horizontal lines between each recyclerview item
            recyclerViewIngredientNameChoices.addItemDecoration(new DividerItemDecoration(recyclerViewIngredientNameChoices.getContext(),
                    DividerItemDecoration.VERTICAL));


            mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
            recyclerViewIngredientNameChoices.setAdapter(mIngredientListAdapter);
            recyclerViewIngredientNameChoices.setLayoutManager(new LinearLayoutManager(this));
            mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
            mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);

            // if we have no filter show normal database
//            if ( mGivenFilterString == null ){
            mIngredientViewModel.viewModelGetAllLiveData(mGivenFilterString).observe(this,
                    new Observer<List<Ingredient>>() {
                        @Override
                        public void onChanged(List<Ingredient> ingredients) {
                            // Update the cached copy of the words in the adapter.
                            mIngredientListAdapter.setFilterIngredientList(
                                    ingredients, mGivenFilterString);

                        }
                    });
            }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
            // do something

        } else if (item.getItemId() == R.id.action_go_home) {
            Util.goToMainActivity(null, thisActivity);
        }
        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View view) {

        //TODO make submit button invisible if anything besides it is clicked and the edittext is
        // empty

        // basic variables setup
        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
        mTooManyTryAgainString = getResources().getString(R.string.too_many_selected_not_saved);


        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_ingredient_intensity:
//
//                break;
            case R.id.button_choose_ingredient_save:
                // get int
                Log.d(TAG, "in save bundle: " + mBundle.toString());


                // check what to do based on if it's empty, editing, too many are selected,
                // or if we need to save
                // then go to correct place or just toast the error
                Util.ifInvalidRepeatOrValidAdd(
                        thisActivity, null, null, mIngredientListAdapter, mIngredientLogViewModel);


                // TODO uncomment all this below here if the above saves correctly
                // all the ingredients to add have been put into the array
                // check if we're here to edit a single log and therefore need only one selected
//                Boolean needsOnlyOneLog = Boolean.FALSE;
//                if ( mBundle.containsKey(Util.ARGUMENT_ACTION) ){
//                    // if the action to be taken is edit,
//                    // then we need to ensure we're only changing one
//                    if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
//                            Util.ARGUMENT_ACTION_EDIT )){
//                        needsOnlyOneLog = Boolean.TRUE;
//                    }
//                }
//                Integer howManySelected = mSelectedArrayList.size();
//
//                // so check how many have been selected and put in the array
//                // check if ingredients to add array is empty
//                if ( mSelectedArrayList.isEmpty() ) {
//                    // if empty, alert user none were selected and don't do anything else
//                    Toast.makeText(thisContext, mEmptyTryAgainString,
//                            Toast.LENGTH_SHORT).show();
//                } else if (  howManySelected > 1 && needsOnlyOneLog ) {
//                    // the user selected more than one ingredient, but is here to only change one
//                    // log
//                    // tell them to try again and select only one
//                    Toast.makeText(thisContext, mTooManyTryAgainString,
//                            Toast.LENGTH_SHORT).show();
//                } else if (  howManySelected == 1 && needsOnlyOneLog ) {
//                    // the user selected only one ingredient, and is here to only change one log
//                    Toast.makeText(thisContext, mSaveString,
//                            Toast.LENGTH_SHORT).show();
//                    // which means success so we can go back to edit after saving it
//                    String ingredientLogIdString =
//                            mBundle.getString(Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY);
//                    String ingredientIdString = mSelectedArrayList.get(0);
//                    IngredientLog ingredientLog =
//                            mIngredientLogViewModel.viewModelGetLogFromLogId(UUID.fromString(ingredientLogIdString));
//                    // get the ingredient matching the ingredient ID we got from the UI choice
//                    Ingredient ingredient =
//                            mIngredientViewModel.viewModelGetFromId(UUID.fromString(ingredientIdString));
//                    // save our updated ingredient log with the new ingredient ID and name
//                    //TODO debug this it isn't saving
//                    ingredientLog.setLogIngredientId(ingredient.getIngredientId());
//                    mIngredientLogViewModel.viewModelUpdate(ingredientLog);
//
//                    Log.d(TAG, "in howManySelected edit bundle: " + mBundleNext.toString());
//                    // done with setting the ingredient go back to editing this ingredient log
//                    Util.goToEditActivityActionTypeId(null, thisActivity,
//                            Util.ARGUMENT_ACTION_EDIT,
//                            Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, ingredientLogIdString);
//
//                } else {
//                    // if not empty, put the array into the intent to go add ingredients
//                    mBundleNext =
//                            Util.setNewIngredientLogBundleFromLogIdStringArray(mSelectedArrayList);
//                    // go to set the intensity of the ingredient
//                    Log.d(TAG, "in howManySelected bundle: " + mBundleNext.toString());
//                    Util.goToAddIngredientLogActivity(thisActivity,
//                            String.valueOf(mSelectedArrayList), mBundleNext);
//
//                }

                break;
            case R.id.button_choose_ingredient_add:
                // add new ingredient button
                Util.goToAddIngredientActivityMakeAddBundle(null, thisActivity);
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
        }
    }//end onClick

    //TODO get active search working
//    @Override
//    public boolean onCreateOptionsMenu( Menu menu) {
//        getMenuInflater().inflate( R.menu.menu_main, menu);
//
//        MenuItem myActionMenuItem = menu.findItem( R.id.search_menu_item);
//        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (TextUtils.isEmpty(newText)) {
////                    mSearchAdapter.filter("");
////                    listView.clearTextFilter();
//                } else {
//                    Toast.makeText(thisActivity, "separate activity worked, newtext: " + newText,
//                            Toast.LENGTH_SHORT).show();
////                    mSearchAdapter.filter(newText);
//                }
//                return true;
//            }
//        });
//
//        return true;
//    }//end options menu
}

