package com.dietdecoder.dietdecoder.ui;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.Recipe;

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
    Log.d(TAG, "onBindViewHolder: " + currentRecipe.getRecipeName());
    holderRecipe.bind(currentRecipe.getRecipeName() + ": " + currentRecipe.getRecipeIngredient());
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
      isEqualRecipeName = oldRecipeItem.getRecipeName().equals(newRecipeItem.getRecipeName());
      isEqualRecipeIngredient = oldRecipeItem.getRecipeIngredient().equals(newRecipeItem.getRecipeIngredient());
      return isEqualRecipeName && isEqualRecipeIngredient;
    } //end areContentsTheSame


  }//end RecipeDiff



} //end class RecipeListAdapter

