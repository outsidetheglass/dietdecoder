package com.dietdecoder.dietdecoder.ui.ingredient;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IngredientListAdapter extends ListAdapter<Ingredient, IngredientViewHolder> {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public IngredientViewModel ingredientViewModel;
  public List<Ingredient> mIngredientList;
  public String mFilterString;


  public IngredientListAdapter(@NonNull DiffUtil.ItemCallback<Ingredient> diffCallback) {
    super(diffCallback);
    // don't need context in here because can just get it in onCreateViewHolder from parent.getContext()
  }//end IngredientListAdapter


  @Override
  public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // don't need to make button here because it's already in ViewHolder
    return IngredientViewHolder.create(parent);
  }// end IngredientViewHolder


  public ArrayList<Ingredient> getSelectedList(){
    return IngredientViewHolder.viewHolderGetSelectedArrayList();
  }

  @Override
  public void onBindViewHolder(IngredientViewHolder holder, int position) {
    Ingredient currentIngredient = getItem(position);

    //TODO Ingredient log will need to search ingredients for the ingredient being added,
    // and if it doesn’t already exist add it in,
    // if it does then use existing ingredient ID.

    //TODO add search bar in top with add button next to it

    //TODO make it case insensitive

    // filter out the current is set to false by default
//    Boolean filterThisIngredientOut = Boolean.FALSE;

//    Log.d(TAG, "in bind, filterBoolean: " + filterThisIngredientOut.toString());
//    // if we have a filter
//    if (!Objects.isNull(mFilterString)){
//      // get the current ingredient name, set to lowercase (same as the filterstring was)
//      String currentIngredientName = currentIngredient.getIngredientName().toLowerCase();
//      // if the current ingredient name does not contain our filter
//      Log.d(TAG, "in bind, mFilterString: " + mFilterString);
//      if (!currentIngredientName.contains(mFilterString)){
//        Log.d(TAG, "in bind, mFilterString: " + mFilterString);
//        // then we want to filter this ingredient out of the list in order to only show
//        // ingredients matching our ingredient search filter string
//        filterThisIngredientOut = Boolean.TRUE;
//        Log.d(TAG, "in bind, filterBoolean: " + filterThisIngredientOut.toString());
//      }
//    }
    // only bind the holder to show ingredient if it contains our filter string
    // or no filter was given
//    if ( !filterThisIngredientOut ) {
//      Log.d(TAG, "in bind !filterThisIngredientOut, filterBoolean: " + filterThisIngredientOut.toString());
//      Log.d(TAG,
//              "in bind !filterThisIngredientOut, currentIngredient name: "
//                      + currentIngredient.getIngredientName().toString());
      holder.bind(currentIngredient);
//    }
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

  public void setFilterIngredientList(List logs, String filterString){

    this.mFilterString = filterString;
    this.submitList(logs);
  }

} //end class IngredientListAdapter

