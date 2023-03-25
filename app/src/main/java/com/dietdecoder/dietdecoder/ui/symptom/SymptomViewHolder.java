package com.dietdecoder.dietdecoder.ui.symptom;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.symptomlog.NewSymptomLogActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;

public class SymptomViewHolder extends RecyclerView.ViewHolder {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView symptomItemView;
  public ImageButton symptomCheckButton;
  private Context symptomViewHolderContext;

  private SymptomViewHolder(View itemView) {
    super(itemView);
    symptomViewHolderContext = itemView.getContext();
    symptomItemView = itemView.findViewById(R.id.textview_symptom_item);
    symptomCheckButton = itemView.findViewById(R.id.imagebutton_symptom_option);
  }


  public void bind(Symptom symptom) {

    String symptomName = symptom.getSymptomName();
    String symptomId = symptom.getSymptomId().toString();
    String symptomCategory = symptom.getSymptomCategory();
    String symptomDescription = symptom.getSymptomDescription();
    String symptomSufferType = symptom.getSymptomSufferType();

    String unImportantString =
            Util.setPlainDescriptionString(symptomDescription);

    // only show useful description, not just the name repeated
    if ( TextUtils.equals(symptomName, symptomDescription) ) {
      unImportantString = "";
    }

    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewStringNameDescription(symptomName, unImportantString );


    // set part of it bold and part of it not bold
    symptomItemView.setText( printString );

    symptomCheckButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // go to set the intensity of the symptom before coming back to ask about more symptoms
        Intent addSymptomIntensityIntent = new Intent(symptomViewHolderContext,
                NewSymptomLogActivity.class);
        addSymptomIntensityIntent.putExtra(Util.ARGUMENT_SYMPTOM_ID, symptomId);
        addSymptomIntensityIntent.putExtra(Util.ARGUMENT_GO_TO,
                Util.ARGUMENT_GO_TO_SYMPTOM_INTENSITY);
        addSymptomIntensityIntent.putExtra(Util.ARGUMENT_SYMPTOM_NAME, symptomName);
        symptomViewHolderContext.startActivity(addSymptomIntensityIntent);

      }
    });

  }


  static SymptomViewHolder create(ViewGroup symptomParent) {

    Context symptomViewHolderContext = symptomParent.getContext();
    LayoutInflater symptomInflater = LayoutInflater.from(symptomViewHolderContext);
    View symptomView = symptomInflater.inflate(
      R.layout.recyclerview_symptom_item,
      symptomParent,
      false
    );

    return new SymptomViewHolder(symptomView);
  }


}//end symptom view holder class
