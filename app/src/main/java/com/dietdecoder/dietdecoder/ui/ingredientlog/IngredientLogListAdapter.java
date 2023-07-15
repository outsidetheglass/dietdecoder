package com.dietdecoder.dietdecoder.ui.ingredientlog;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewHolder;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;

import java.util.ArrayList;

public class IngredientLogListAdapter extends ListAdapter<IngredientLog, IngredientLogViewHolder> {

  // make a TAG to use to log errors
  private final static String TAG = "TAG: IngredientLogListAdapter";

  public IngredientLogViewModel ingredientLogViewModel;
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
//    Ingredient currentIngredientLogIngredient =
//            mIngredientViewModel.viewModelGetIngredientFromId(currentIngredientLog.getIngredientLogIngredientId());
//    holder.bind(currentIngredientLog, currentIngredientLogIngredient);
    holder.bind(currentIngredientLog);
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

} //end class LogListAdapter

