package com.dietdecoder.dietdecoder.ui.symptom;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.List;

public class SymptomListAdapter extends ListAdapter<Symptom, SymptomViewHolder> {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  public SymptomViewModel symptomViewModel;
  public List<Symptom> mSymptomList;


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
    holder.bind(currentSymptom);
  }//end onBindViewHolder


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
      isEqualName = oldItem.getSymptomName().equals(newItem.getSymptomName());
      isEqualSymptomDescription = oldItem.getSymptomDescription().equals(newItem.getSymptomDescription());
      isEqualSymptomCategory = oldItem.getSymptomCategory().equals(newItem.getSymptomCategory());
      isEqualSymptomSufferType = oldItem.getSymptomSufferType().equals(newItem.getSymptomSufferType());
      return isEqualName && isEqualSymptomDescription && isEqualSymptomCategory && isEqualSymptomSufferType;
    } //end areContentsTheSame


  }//end SymptomDiff


  public void setSymptomList(List<Symptom> symptomList) {

    if ( symptomList != null ) {
      this.mSymptomList = symptomList;
      notifyDataSetChanged();
    }
  }

} //end class SymptomListAdapter

