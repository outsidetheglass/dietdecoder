package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewHolder;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogViewModel;

import java.util.List;

public class SymptomLogListAdapter extends ListAdapter<SymptomLog, SymptomLogViewHolder> {

  // make a TAG to use to log errors
  private final static String TAG = "TAG: SymptomLogListAdapter";

  public SymptomLogViewModel symptomLogViewModel;
  public List<SymptomLog> mSymptomLogList;


  public SymptomLogListAdapter(@NonNull DiffUtil.ItemCallback<SymptomLog> diffCallback) {
    super(diffCallback);
    // don't need context in here because can just get it in onCreateViewHolder from parent.getContext()
  }//end LogListAdapter


  @Override
  public SymptomLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // don't need to make button here because it's already in ViewHolder
    return SymptomLogViewHolder.create(parent);
  }// end LogViewHolder


  @Override
  public void onBindViewHolder(SymptomLogViewHolder holder, int position) {
    SymptomLog currentSymptomLog = getItem(position);
    holder.bind(currentSymptomLog);
  }//end onBindViewHolder

  public static class LogDiff extends DiffUtil.ItemCallback<SymptomLog> {

    private Boolean isEqualName;
    //private Boolean isEqualConcern;

    @Override
    public boolean areItemsTheSame(@NonNull SymptomLog oldItem, @NonNull SymptomLog newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull SymptomLog oldItem, @NonNull SymptomLog newItem) {
      Log.d(TAG, "Adapter, newitem name: " + newItem.getSymptomName());
      // check all parts of Log to see if they're the same
      isEqualName =
              oldItem.getInstantLogged().equals(newItem.getInstantLogged());
      //TODO add other properties of log type here
      //isEqualConcern = oldItem.getLogConcern().equals(newItem.getLogConcern());
      return isEqualName/* && isEqualConcern*/;
    } //end areContentsTheSame


  }//end LogDiff


  public void setLogList(List<SymptomLog> symptomLogList) {

    if ( symptomLogList != null ) {
      this.mSymptomLogList = symptomLogList;
      notifyDataSetChanged();
    }
  }

} //end class LogListAdapter

