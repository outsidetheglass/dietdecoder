package com.dietdecoder.dietdecoder.ui.ingredientlog;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IngredientLogListAdapter extends ListAdapter<IngredientLog, IngredientLogViewHolder> {

  // make a TAG to use to log errors
  private final static String TAG = "TAG: IngredientLogListAdapter";

  public IngredientLogViewModel mIngredientLogViewModel;
  public IngredientViewModel mIngredientViewModel;
  public ArrayList<IngredientLog> mIngredientLogArrayList;
  public ArrayList<Ingredient> mIngredientArrayList;


  public IngredientLogListAdapter(@NonNull DiffUtil.ItemCallback<IngredientLog> diffCallback
          //,                                  IngredientViewModel ingredientViewModel
  ) {
    super(diffCallback);
//    mIngredientViewModel = ingredientViewModel;
    // don't need context in here because can just get it in onCreateViewHolder from parent.getContext()
  }//end LogListAdapter


  @Override
  public IngredientLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // don't need to make button here because it's already in ViewHolder
    return IngredientLogViewHolder.create(parent);
  }// end LogViewHolder


  @Override
  public void onBindViewHolder(IngredientLogViewHolder holder, int position) {
    IngredientLog currentIngredientLog = getItem(position);
    Ingredient currentIngredientLogIngredient = null;
    UUID currentIngredientLogIngredientId = currentIngredientLog.getLogIngredientId();

    // TODO fix, this will break if the ingredient is one that isn't in the ingredient database yet
    // find the ingredient matching the id of the log we were given
    int i = 0;
    UUID idToCheck = mIngredientArrayList.get(i).getId();
    // while this ingredient in the array's id does not match the current ingredient log's
    // ingredient id
    // and don't go out of bounds by checking for an index of the array bigger than the array size
    while ( !Objects.equals(idToCheck,
            currentIngredientLogIngredientId ) && i < mIngredientArrayList.size()) {
      // check the next ingredient id in the array
      i++;
      idToCheck = mIngredientArrayList.get(i).getId();
    }
    // when we break, check it's the correct ingredient, if it's not that means it was the last in
    // the list and is invalid
    if ( Objects.equals(idToCheck,
            currentIngredientLogIngredientId )) {
      currentIngredientLogIngredient = mIngredientArrayList.get(i);
    } else {
      //TODO add logic here for alerting user for broken ingredient
      Log.d(TAG,
              "ingredient in this ingredient log, this log id: " +
                      currentIngredientLogIngredientId.toString() +
                      " was not found in list of ingredients, need to add ingredient" +
                      " and find out how this ingredient log got added without a valid ingredient" +
                      ".");
    }

    holder.bind(currentIngredientLog, currentIngredientLogIngredient);
  }//end onBindViewHolder

  public static class LogDiff extends DiffUtil.ItemCallback<IngredientLog> {

    private Boolean isEqualName;
    //private Boolean isEqualConcern;

    @Override
    public boolean areItemsTheSame(@NonNull IngredientLog oldItem, @NonNull IngredientLog newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull IngredientLog oldItem,
                                      @NonNull IngredientLog newItem) {
      // check all parts of Log to see if they're the same
      isEqualName =
              oldItem.getInstantLogged().equals(newItem.getInstantLogged());
      //TODO add other properties of log type here
      //isEqualConcern = oldItem.getLogConcern().equals(newItem.getLogConcern());
      return isEqualName/* && isEqualConcern*/;
    } //end areContentsTheSame


  }//end LogDiff


  public void setLogList(ArrayList<IngredientLog> ingredientLogList) {

    if ( ingredientLogList != null ) {
      this.mIngredientLogArrayList = ingredientLogList;
      notifyDataSetChanged();
    }
  }

  public void setIngredientLogListSubmitList(List logs, ArrayList<Ingredient> ingredientArrayList){

    this.mIngredientArrayList = ingredientArrayList;
    this.submitList(logs);
  }

} //end class LogListAdapter

