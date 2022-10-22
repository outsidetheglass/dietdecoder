package com.dietdecoder.dietdecoder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class IngredientListAdapter extends ListAdapter<Ingredient, IngredientViewHolder> {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();


  public IngredientListAdapter(@NonNull DiffUtil.ItemCallback<Ingredient> diffCallback) {
    super(diffCallback);
  }//end IngredientListAdapter


  @Override
  public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return IngredientViewHolder.create(parent);
  }// end IngredientViewHolder


  @Override
  public void onBindViewHolder(IngredientViewHolder holder, int position) {
    Ingredient currentIngredient = getItem(position);
    holder.bind(currentIngredient.getIngredientName() + ": " + currentIngredient.getIngredientConcern());
  }//end onBindViewHolder

  static class IngredientDiff extends DiffUtil.ItemCallback<Ingredient> {

    private Boolean isEqualName;
    private Boolean isEqualConcern;

    @Override
    public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
      // check all parts of Ingredient to see if they're the same
      isEqualName = oldItem.getIngredientName().equals(newItem.getIngredientName());
      isEqualConcern = oldItem.getIngredientConcern().equals(newItem.getIngredientConcern());
      return isEqualName && isEqualConcern;
    } //end areContentsTheSame


  }//end IngredientDiff



} //end class IngredientListAdapter

