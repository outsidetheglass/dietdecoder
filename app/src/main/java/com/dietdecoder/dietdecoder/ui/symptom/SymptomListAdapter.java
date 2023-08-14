package com.dietdecoder.dietdecoder.ui.symptom;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.ArrayList;
import java.util.List;

public class SymptomListAdapter extends ListAdapter<Symptom, SymptomViewHolder> {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public SymptomViewModel symptomViewModel;
  public List<Symptom> mSymptomList;
  public Boolean mListOnlyTracked = Boolean.FALSE;


  public SymptomListAdapter(@NonNull DiffUtil.ItemCallback<Symptom> diffCallback) {
    super(diffCallback);
  }//end SymptomListAdapter


  @Override
  public SymptomViewHolder onCreateViewHolder(ViewGroup symptomParent, int symptomViewType) {
    // don't need to make button here because it's already in ViewHolder
    return SymptomViewHolder.create(symptomParent);
  }// end SymptomViewHolder


  @Override
  public void onBindViewHolder(SymptomViewHolder holder, int symptomPosition) {
    Symptom currentSymptom = getItem(symptomPosition);
    // only show the symptom if it's one of the ones to track
    if ( this.mListOnlyTracked ){
      // if true and in here, then it's a symptom we need to list only if user said to track it
      if (currentSymptom.getTrackOrNot()) {
        holder.bind(currentSymptom);
      }
    } else{
      // list all because this isn't about whether it's tracked or not
      holder.bind(currentSymptom);
    }
  }//end onBindViewHolder

  public ArrayList<Symptom> getSelectedSymptomList(){
    return SymptomViewHolder.viewHolderGetSelectedArrayList();
  }

  public static class SymptomDiff extends DiffUtil.ItemCallback<Symptom> {

    private Boolean isEqualName;
    private Boolean isEqualSymptomDescription;
    private Boolean isEqualSymptomCategory;
    private Boolean isEqualSymptomSufferType;

    @Override
    public boolean areItemsTheSame(@NonNull Symptom oldItem, @NonNull Symptom newItem) {
      return oldItem == newItem;
    }//end areItemsTheSame

    @Override
    public boolean areContentsTheSame(@NonNull Symptom oldItem, @NonNull Symptom newItem) {
      // check all parts of Symptom to see if they're the same
      isEqualName = oldItem.getName().equals(newItem.getName());
      isEqualSymptomDescription = oldItem.getDescription().equals(newItem.getDescription());
      isEqualSymptomCategory = oldItem.getCategory().equals(newItem.getCategory());
      isEqualSymptomSufferType = oldItem.getSufferType().equals(newItem.getSufferType());
      return isEqualName && isEqualSymptomDescription && isEqualSymptomCategory && isEqualSymptomSufferType;
    } //end areContentsTheSame


  }//end SymptomDiff


  public void setSymptomList(List<Symptom> symptomList) {

    if ( symptomList != null ) {
      this.mSymptomList = symptomList;
      notifyDataSetChanged();
    }
  }

  public void submitSymptomList(List symptoms){

    // TODO remove this if I end up only using the one in the activity
    // if list only tracked, we're here from making a symptom log,
    // so list only the symptoms that have set to track to be true
//    if ( listOnlyTracked ){
//      this.mListOnlyTracked = Boolean.TRUE;
//    }

    this.submitList(symptoms);
  }

} //end class SymptomListAdapter

