package com.dietdecoder.dietdecoder.ui.ingredient;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;

import java.util.List;

public class IngredientListAdapter extends ListAdapter<Ingredient, IngredientViewHolder> {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public IngredientViewModel ingredientViewModel;
  public List<Ingredient> mIngredientList;


  public IngredientListAdapter(@NonNull DiffUtil.ItemCallback<Ingredient> diffCallback) {
    super(diffCallback);
    // don't need context in here because can just get it in onCreateViewHolder from parent.getContext()
  }//end IngredientListAdapter


  @Override
  public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // don't need to make button here because it's already in ViewHolder
    return IngredientViewHolder.create(parent);
  }// end IngredientViewHolder


  @Override
  public void onBindViewHolder(IngredientViewHolder holder, int position) {
    Ingredient currentIngredient = getItem(position);
    holder.bind(currentIngredient);
  }//end onBindViewHolder


  public static class IngredientDiff extends DiffUtil.ItemCallback<Ingredient> {

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
      //TODO add other properties of ingredient type here
      //isEqualConcern = oldItem.getIngredientConcern().equals(newItem.getIngredientConcern());
      return isEqualName && isEqualConcern;
    } //end areContentsTheSame


  }//end IngredientDiff


  public void setIngredientList(List<Ingredient> ingredientList) {

    if ( ingredientList != null ) {
      this.mIngredientList = ingredientList;
      notifyDataSetChanged();
    }
    else {
      // TODO initialize database here if need be
    }
  }

} //end class IngredientListAdapter

