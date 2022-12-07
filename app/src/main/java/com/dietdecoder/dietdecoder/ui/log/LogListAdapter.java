package com.dietdecoder.dietdecoder.ui.log;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.log.Log;
import com.dietdecoder.dietdecoder.ui.log.LogViewHolder;
import com.dietdecoder.dietdecoder.ui.log.LogViewModel;

import java.util.List;

public class LogListAdapter extends ListAdapter<Log, LogViewHolder> {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public LogViewModel logViewModel;
  public List<Log> mLogList;


  public LogListAdapter(@NonNull DiffUtil.ItemCallback<Log> diffCallback) {
    super(diffCallback);
    // don't need context in here because can just get it in onCreateViewHolder from parent.getContext()
  }//end LogListAdapter


  @Override
  public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // don't need to make button here because it's already in ViewHolder
    return LogViewHolder.create(parent);
  }// end LogViewHolder


  @Override
  public void onBindViewHolder(LogViewHolder holder, int position) {
    Log currentLog = getItem(position);
    holder.bind(currentLog);
  }//end onBindViewHolder


  public static class LogDiff extends DiffUtil.ItemCallback<Log> {

    private Boolean isEqualName;
    //private Boolean isEqualConcern;

    @Override
    public boolean areItemsTheSame(@NonNull Log oldItem, @NonNull Log newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull Log oldItem, @NonNull Log newItem) {
      // check all parts of Log to see if they're the same
      isEqualName = oldItem.getLogDateTimeInstant().equals(newItem.getLogDateTimeInstant());
      //TODO add other properties of log type here
      //isEqualConcern = oldItem.getLogConcern().equals(newItem.getLogConcern());
      return isEqualName/* && isEqualConcern*/;
    } //end areContentsTheSame


  }//end LogDiff


  public void setLogList(List<Log> logList) {

    if ( logList != null ) {
      this.mLogList = logList;
      notifyDataSetChanged();
    }
    else {
      // TODO initialize database here if need be
    }
  }

} //end class LogListAdapter

