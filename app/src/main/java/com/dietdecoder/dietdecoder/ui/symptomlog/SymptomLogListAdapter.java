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
    // TODO fix, this will break if the symptom is one that isn't in the symptom database yet
    // I should add a try catch here that adds symptom to database if null after this for loop
//    Log.d(TAG, "currentSymptomLog: " + currentSymptomLog.toString());
//    Log.d(TAG, "currentSymptomLog.getSymptomId(): " + currentSymptomLog.getSymptomLogId().toString());
//    Log.d(TAG,
//            "currentSymptomLog.getSymptomId(): " + currentSymptomLog.getSymptomLogSymptomId().toString());
//    for ( Symptom symptom : mSymptomArrayList) {
//      Log.d(TAG, "for loop: " + symptom.toString());
//      Log.d(TAG, "for loop getSymptomId(): " + symptom.getSymptomId().toString());
//      if ( Objects.equals(symptom.getSymptomId(), currentSymptomLog.getSymptomLogSymptomId()) ){
//        currentSymptomLogSymptom = symptom;
//      }
//    }
//    Log.d(TAG,
//            " currentSymptomLog.getSymptomId(): " + currentSymptomLog.getSymptomLogSymptomId().toString());
    //TODO get current symptom working somehow
//    UUID logId = currentSymptomLog.getSymptomLogId();
//    Symptom currentSymptomLogSymptom =
//            mSymptomViewModel.viewModelGetSymptomFromId(currentSymptomLog.getSymptomId());
//    ArrayList<Symptom> mSymptoms = mSymptomViewModel.viewModelGetAllSymptomArrayList();
//    Symptom randomSymptom = mSymptomViewModel.viewModelGetSymptomFromName("body " +
//            "ache");
//    Log.d(TAG, "logId " + logId.toString());
//    Symptom tryAgainSymptom =
//            mSymptomLogViewModel.viewModelGetSymptomFromSymptomLogId(logId);
//    Log.d(TAG, "tryAgainSymptom " + tryAgainSymptom.toString());
//    Log.d(TAG,
//            "symptom id in adapter" + mSymptomViewModel.viewModelGetSymptomFromId(mSymptoms.get(0).getSymptomId()).toString());
//    Log.d(TAG,
//            "randomSymptom: " + randomSymptom.toString());
//    Log.d(TAG,
//            " mSymptomViewModel.viewModelGetSymptomFromId(currentSymptomLog.getSymptomId(): " +
//                    mSymptomViewModel.viewModelGetSymptomFromId(
//                            currentSymptomLog.getSymptomId()
//                    ).toString());
//    Log.d(TAG,
//            " mSymptomViewModel.viewModelGetSymptomFromId(: " +
//                    mSymptomViewModel.viewModelGetSymptomFromId(
//                            UUID.fromString("12994891-cba8" +
//                                    "-41b3-b0db-a1dd54e02ad5")
//                    ).toString());
////
//    Log.d(TAG,
//            " currentSymptomLogSymptom: " + currentSymptomLogSymptom.toString());

    holder.bind(currentSymptomLog
            //, currentSymptomLogSymptom
    );
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

  public void setLogListSubmitList(List logs, ArrayList<Symptom> symptomArrayListList){

    this.mSymptomArrayList = symptomArrayListList;
    this.submitList(logs);
  }
} //end class LogListAdapter

