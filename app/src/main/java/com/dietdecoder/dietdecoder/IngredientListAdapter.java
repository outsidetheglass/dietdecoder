package com.dietdecoder.dietdecoder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

public class IngredientListAdapter extends ListAdapter<Ingredient, IngredientViewHolder> implements CustomClickListener {

  // for the Toast for button
  private Context context;

  public IngredientListAdapter(@NonNull DiffUtil.ItemCallback<Ingredient> diffCallback, Context context) {
    super(diffCallback);
    //this.allIngredients = ingredientViewModel.mViewModelAllIngredients;
    this.context = context;
  }//end IngredientListAdapter


  @Override
  public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return IngredientViewHolder.create(parent);
  }// end IngredientViewHolder


  @Override
  public void onBindViewHolder(IngredientViewHolder holder, int position) {

    Ingredient currentIngredient = getItem(position);
    holder.bind(currentIngredient, position);
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


  //TODO: add edit and delete to database in here
  public void cardClicked(IngredientViewModel ingredientViewModel, Integer position) {
    Toast.makeText(context, "You clicked " + ingredientViewModel.mViewModelAllIngredients.getValue().get(position).getIngredientName(),
      Toast.LENGTH_LONG).show();

//    Toast.makeText(context, "You clicked " + ingredientViewModel.androidName,
//      Toast.LENGTH_LONG).show();
  }

} //end class IngredientListAdapter

