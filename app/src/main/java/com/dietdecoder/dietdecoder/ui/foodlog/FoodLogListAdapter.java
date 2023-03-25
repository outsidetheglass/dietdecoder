package com.dietdecoder.dietdecoder.ui.foodlog;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.util.List;

public class FoodLogListAdapter extends ListAdapter<FoodLog, FoodLogViewHolder> {

  // make a TAG to use to log errors
  private final static String TAG = "TAG: FoodLogListAdapter";

  public FoodLogViewModel foodLogViewModel;
  public List<FoodLog> mFoodLogList;


  public FoodLogListAdapter(@NonNull DiffUtil.ItemCallback<FoodLog> diffCallback) {
    super(diffCallback);
    // don't need context in here because can just get it in onCreateViewHolder from parent.getContext()
  }//end LogListAdapter


  @Override
  public FoodLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // don't need to make button here because it's already in ViewHolder
    return FoodLogViewHolder.create(parent);
  }// end LogViewHolder


  @Override
  public void onBindViewHolder(FoodLogViewHolder holder, int position) {
    FoodLog currentFoodLog = getItem(position);
//    currentFoodLog.getIngredientId(
//            new ViewModelProvider().get(IngredientViewModel.class)
//                    .viewModelGetIngredientFromName(mName)
//                    .getIngredientId()
//    );
    holder.bind(currentFoodLog);
  }//end onBindViewHolder

  public static class LogDiff extends DiffUtil.ItemCallback<FoodLog> {

    private Boolean isEqualName;
    //private Boolean isEqualConcern;

    @Override
    public boolean areItemsTheSame(@NonNull FoodLog oldItem, @NonNull FoodLog newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull FoodLog oldItem, @NonNull FoodLog newItem) {
      Log.d(TAG, "Adapter, newitem name: " + newItem.getIngredientId());
      // check all parts of Log to see if they're the same
      isEqualName = oldItem.getFoodLogDateTimeInstant().equals(newItem.getFoodLogDateTimeInstant());
      //TODO add other properties of log type here
      //isEqualConcern = oldItem.getLogConcern().equals(newItem.getLogConcern());
      return isEqualName/* && isEqualConcern*/;
    } //end areContentsTheSame


  }//end LogDiff


  public void setLogList(List<FoodLog> foodLogList) {

    if ( foodLogList != null ) {
      this.mFoodLogList = foodLogList;
      notifyDataSetChanged();
    }
    else {
      // TODO initialize database here if need be
    }
  }

} //end class LogListAdapter

