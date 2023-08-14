package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SymptomLogListAdapter extends ListAdapter<SymptomLog, SymptomLogViewHolder> {

  // make a TAG to use to log errors
  private final static String TAG = "TAG: SymptomLogListAdapter";

  public SymptomLogViewModel mSymptomLogViewModel;
  public SymptomViewModel mSymptomViewModel;
  public ArrayList<SymptomLog> mSymptomLogArrayList;
  public ArrayList<Symptom> mSymptomArrayList;



  public SymptomLogListAdapter(@NonNull DiffUtil.ItemCallback<SymptomLog> diffCallback
         // , SymptomViewModel symptomViewModel, SymptomLogViewModel symptomLogViewModel
  ) {
    super(diffCallback);
//    mSymptomViewModel = symptomViewModel;
//    mSymptomLogViewModel = symptomLogViewModel;
    //mSymptomArrayList = symptoms;
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
    Symptom currentSymptomLogSymptom = null;
    UUID currentSymptomLogSymptomId = currentSymptomLog.getLogSymptomId();

//    Log.d(TAG,currentSymptomLog.toString());
    // TODO fix, this will break if the symptom is one that isn't in the symptom database yet
    // find the symptom matching the id of the log we were given
    int i = 0;
    UUID symptomIdToCheck = mSymptomArrayList.get(i).getId();
    // while this symptom in the array's id does not match the current symptom log's symptom id
    // and don't go out of bounds by checking for an index of the array bigger than the array size
    while ( !Objects.equals(symptomIdToCheck,
            currentSymptomLogSymptomId ) && i < mSymptomArrayList.size()) {
      // check the next symptom id in the array
      i++;
      symptomIdToCheck = mSymptomArrayList.get(i).getId();
    }
    // when we break, check it's the correct symptom, if it's not that means it was the last in
    // the list and is invalid
    if ( Objects.equals(symptomIdToCheck,
            currentSymptomLogSymptomId )) {
      currentSymptomLogSymptom = mSymptomArrayList.get(i);
    } else {
      //TODO add logic here for alerting user for broken symptom
      Log.d(TAG,
              "symptom in this symptom log, this log id: " +
                      currentSymptomLogSymptomId.toString() +
                      " was not found in list of symptoms, need to add symptom" +
              " and find out how this symptom log got added without a valid symptom.");
    }

//    Log.d(TAG, currentSymptomLogSymptom.toString());
    holder.bind(currentSymptomLog, currentSymptomLogSymptom);
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
      // check all parts of Log to see if they're the same
      isEqualName =
              oldItem.getInstantLogged().equals(newItem.getInstantLogged());
      //TODO add other properties of log type here
      //isEqualConcern = oldItem.getLogConcern().equals(newItem.getLogConcern());
      return isEqualName/* && isEqualConcern*/;
    } //end areContentsTheSame


  }//end LogDiff


  public void setLogList(ArrayList<SymptomLog> symptomLogList) {

    if ( symptomLogList != null ) {
      this.mSymptomLogArrayList = symptomLogList;
      notifyDataSetChanged();
    }
  }

  public void setSymptomLogListSubmitList(List logs, ArrayList<Symptom> symptomArrayList){

    this.mSymptomArrayList = symptomArrayList;
    this.submitList(logs);
  }
} //end class LogListAdapter

