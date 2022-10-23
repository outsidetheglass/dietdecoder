package com.dietdecoder.dietdecoder.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.Recipe;

//TODO fix this, its accessing ingredients right now
public class RecipeListAdapter extends ListAdapter<Recipe, RecipeViewHolder> {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();


  public RecipeListAdapter(@NonNull DiffUtil.ItemCallback<Recipe> diffCallback) {
    super(diffCallback);
  }//end RecipeListAdapter


  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return RecipeViewHolder.create(parent);
  }// end RecipeViewHolder


  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    Recipe currentRecipe = getItem(position);
    holder.bind(currentRecipe.getRecipeName() + ": " + currentRecipe.getRecipeIngredient());
  }//end onBindViewHolder
;
  public static class RecipeDiff extends DiffUtil.ItemCallback<Recipe> {

    private Boolean isEqualName;
    private Boolean isEqualIngredient;

    @Override
    public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
      // check all parts of Recipe to see if they're the same
      isEqualName = oldItem.getRecipeName().equals(newItem.getRecipeName());
      isEqualIngredient = oldItem.getRecipeIngredient().equals(newItem.getRecipeIngredient());
      return isEqualName && isEqualIngredient;
    } //end areContentsTheSame


  }//end RecipeDiff



} //end class RecipeListAdapter

