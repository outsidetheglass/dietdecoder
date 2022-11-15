package com.dietdecoder.dietdecoder.ui.recipe;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.recipe.Recipe;

//TODO fix this, its accessing ingredients right now
public class RecipeListAdapter extends ListAdapter<Recipe, RecipeViewHolder> {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();


  public RecipeListAdapter(@NonNull DiffUtil.ItemCallback<Recipe> diffCallbackRecipe) {
    super(diffCallbackRecipe);
  }//end RecipeListAdapter


  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parentRecipe, int viewTypeRecipe) {
    return RecipeViewHolder.create(parentRecipe);
  }// end RecipeViewHolder


  @Override
  public void onBindViewHolder(RecipeViewHolder holderRecipe, int positionRecipe) {
    Recipe currentRecipe = getItem(positionRecipe);
    Log.d(TAG, "onBindViewHolder: " + currentRecipe.getmRecipeName());

    // TODO fix get names to be recursive for all ingredients
    holderRecipe.bind(currentRecipe.getmRecipeName() + ": " + currentRecipe.getmRecipeIngredientNames().get(0));
  }//end onBindViewHolder
;
  public static class RecipeDiff extends DiffUtil.ItemCallback<Recipe> {

    private Boolean isEqualRecipeName;
    private Boolean isEqualRecipeIngredient;

    @Override
    public boolean areItemsTheSame(@NonNull Recipe oldRecipeItem, @NonNull Recipe newRecipeItem) {
      return oldRecipeItem == newRecipeItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull Recipe oldRecipeItem, @NonNull Recipe newRecipeItem) {
      // check all parts of Recipe to see if they're the same
      isEqualRecipeName = oldRecipeItem.getmRecipeName().equals(newRecipeItem.getmRecipeName());
      isEqualRecipeIngredient = oldRecipeItem.getmRecipeIngredientNames().equals(newRecipeItem.getmRecipeIngredientNames());
      return isEqualRecipeName && isEqualRecipeIngredient;
    } //end areContentsTheSame


  }//end RecipeDiff



} //end class RecipeListAdapter

