package com.dietdecoder.dietdecoder.activity.foodlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeListAdapter;
import com.dietdecoder.dietdecoder.ui.recipe.RecipeViewModel;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChooseRecipeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseRecipeActivity.this;
    private Context thisContext;

    //TODO fix names
    Bundle mBundle;

    String mTooManyTryAgainString, mSaveString, mEmptyTryAgainString;
    Button mButtonSaveName;

    ArrayList<String> mRecipesSelectedIdsArrayListStrings = null;

    private Fragment nextFragment = null;

    RecipeListAdapter mRecipeListAdapter;
    RecipeViewModel mRecipeViewModel;
    FoodLogViewModel mFoodLogViewModel;
    RecyclerView recyclerViewRecipeNameChoices;

    ColorStateList selectedColor;
    ColorStateList unSelectedColor;

    public ChooseRecipeActivity() {
        super(R.layout.activity_choose_recipe);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_choose_recipe);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        thisContext = thisActivity.getApplicationContext();


        if (savedInstanceState == null) {
//
//            // basic variable setup
//            mRecipesSelectedIdsArrayListStrings = new ArrayList<>();
//            if ( getIntent().getExtras().isEmpty() ) {
//                mBundle = new Bundle();
//            } else {
//                mBundle = getIntent().getExtras();
//            }
//
//            //save button
//            mButtonSaveName = findViewById(R.id.button_choose_recipe_save);
//            mButtonSaveName.setOnClickListener(this);
//
//
//            // make the view for listing the items in the log
//            recyclerViewRecipeNameChoices =
//                    findViewById(R.id.recyclerview_recipe_name_choices);
//            // add horizontal lines between each recyclerview item
//            recyclerViewRecipeNameChoices.addItemDecoration(new DividerItemDecoration(recyclerViewRecipeNameChoices.getContext(),
//                    DividerItemDecoration.VERTICAL));
//
//
//            mRecipeListAdapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff());
//            recyclerViewRecipeNameChoices.setAdapter(mRecipeListAdapter);
//            recyclerViewRecipeNameChoices.setLayoutManager(new LinearLayoutManager(this));
//            mRecipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
//            mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);
//
//            mRecipeViewModel.viewModelGetRecipesToTrack().observe(this,
//                    new Observer<List<Recipe>>() {
//                        @Override
//                        public void onChanged(List<Recipe> recipes) {
//                            // Update the cached copy of the words in the adapter.
//                            mRecipeListAdapter.submitList(recipes);
//
//                        }
//                    });
//
//
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
            // do something

        } else if (item.getItemId() == R.id.action_go_home) {
            Util.goToMainActivity(thisActivity);
        }
        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View view) {
//
//        // basic variables setup
//        // get saving string from resources so everything can translate languages easy
//        mSaveString = getResources().getString(R.string.saving);
//        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
//        mTooManyTryAgainString = getResources().getString(R.string.too_many_selected_not_saved);
//
//
//        switch (view.getId()) {
//            //do something when edittext is clicked
////            case R.id.edittext_new_recipe_intensity:
////
////                break;
//            case R.id.button_choose_recipe_save:
//                // get int
////
//                for (int childCount = recyclerViewRecipeNameChoices.getChildCount(), i = 0; i < childCount; ++i) {
//
//                    final RecyclerView.ViewHolder holder =
//                            recyclerViewRecipeNameChoices.getChildViewHolder(recyclerViewRecipeNameChoices.getChildAt(i));
//                    TextView viewHolderTextView =
//                            holder.itemView.findViewById(R.id.textview_recipe_item);
//                    int textViewColorInt =
//                            viewHolderTextView.getTextColors().getDefaultColor();
//
//                    selectedColor =
//                            holder.itemView.getResources().getColorStateList(R.color.selected_text_color,
//                            getTheme());
//                    int selectInt = selectedColor.getDefaultColor();
//
//                    // if the text color of the current item in the list is the color set when
//                    // selected by the user
//                    if (Objects.equals(selectInt, textViewColorInt) ){
//                        // get the ID of the recipe that has the selected color text
//                        int position = holder.getBindingAdapterPosition();
//                        String currentRecipeSelectedIdString =
//                                mRecipeViewModel.viewModelGetRecipesToTrack().getValue().get(position)
//                                        .getRecipeId().toString();
//                        // add that string ID to our array of selected recipes
//                        mRecipesSelectedIdsArrayListStrings.add(currentRecipeSelectedIdString);
//
//                    }
//                }
//
//                // after done with the for loop,
//                // all the recipes to add have been put into the array
//                // check if we're here to edit a single log and therefore need only one selected
//                Boolean needsOnlyOneLog = Boolean.FALSE;
//                if ( mBundle.containsKey(Util.ARGUMENT_ACTION) ){
//                    // if the action to be taken is edit,
//                    // then we need to ensure we're only changing one
//                    if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
//                            Util.ARGUMENT_ACTION_EDIT )){
//                        needsOnlyOneLog = Boolean.TRUE;
//                    }
//                }
//                Integer howManySelected = mRecipesSelectedIdsArrayListStrings.size();
//
//                // so check how many have been selected and put in the array
//                // check if recipes to add array is empty
//                if ( mRecipesSelectedIdsArrayListStrings.isEmpty() ) {
//                    // if empty, alert user none were selected and don't do anything else
//                    Toast.makeText(thisContext, mEmptyTryAgainString,
//                            Toast.LENGTH_SHORT).show();
//                } else if (  howManySelected > 1 && needsOnlyOneLog ) {
//                    // the user selected more than one recipe, but is here to only change one log
//                    // tell them to try again and select only one
//                    Toast.makeText(thisContext, mTooManyTryAgainString,
//                            Toast.LENGTH_SHORT).show();
//                } else if (  howManySelected == 1 && needsOnlyOneLog ) {
//                    // the user selected only one recipe, and is here to only change one log
//                    Toast.makeText(thisContext, mSaveString,
//                            Toast.LENGTH_SHORT).show();
//                    // which means success so we can go back to edit after saving it
//                    String recipeLogIdString =
//                            mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
//                    String recipeIdString = mRecipesSelectedIdsArrayListStrings.get(0);
//                    FoodLog recipeLog =
//                            mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(recipeLogIdString));
//                    // get the recipe matching the recipe ID we got from the UI choice
//                    Recipe recipe =
//                            mRecipeViewModel.viewModelGetRecipeFromId(UUID.fromString(recipeIdString));
//                    // save our updated recipe log with the new recipe ID and name
//                    recipeLog.setRecipeName(recipe.getRecipeName());
//                    recipeLog.setRecipeId(recipe.getRecipeId());
//                    mFoodLogViewModel.viewModelUpdateFoodLog(recipeLog);
//
//                    // done with setting the recipe go back to editing this recipe log
//                    Util.goToEditActivityActionTypeId(thisContext, Util.ARGUMENT_ACTION_EDIT,
//                            Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, recipeLogIdString);
//
//                } else {
//                    // if not empty, put the array into the intent to go add recipes
//                    mBundle =
//                            Util.setBundleNewFoodLogIntensity(mRecipesSelectedIdsArrayListStrings);
//                    // go to set the intensity of the recipe
//                    Log.d(TAG, "recipes array " + mRecipesSelectedIdsArrayListStrings);
//                    Util.goToAddFoodLogWithBundle(thisActivity, mBundle);
//
//                }
//
//                break;
////
//            default:
//                break;
//        }
    }//end onClick
}

