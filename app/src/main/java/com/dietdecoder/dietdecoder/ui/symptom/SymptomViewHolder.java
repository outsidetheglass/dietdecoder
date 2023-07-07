package com.dietdecoder.dietdecoder.ui.symptom;

import static com.dietdecoder.dietdecoder.R.*;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
  ColorStateList selectedColor;
  ColorStateList unSelectedColor;

  private SymptomViewHolder(View itemView) {
    super(itemView);
    symptomViewHolderContext = itemView.getContext();
    symptomItemView = itemView.findViewById(id.textview_symptom_item);
    symptomCheckButton = itemView.findViewById(id.imagebutton_symptom_option_circle);
    selectedColor = itemView.getResources().getColorStateList(color.selected_text_color,
            symptomViewHolderContext.getTheme());
    unSelectedColor = itemView.getResources().getColorStateList(color.unselected_text_color,
            symptomViewHolderContext.getTheme());
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

        //change text color to show it was clicked, change to red
        symptomItemView.setTextColor(selectedColor);

        //TODO change pain face to question mark, when selected in here make it an icon for that
        // pain suffer type or default sick face

      }
    });

    symptomItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //change text color to show it was unclicked, change back to unselected color
        if (symptomItemView.getTextColors() == selectedColor ) {
          symptomItemView.setTextColor(unSelectedColor);
        }
      }
    });

  }


  static SymptomViewHolder create(ViewGroup symptomParent) {

    Context symptomViewHolderContext = symptomParent.getContext();
    LayoutInflater symptomInflater = LayoutInflater.from(symptomViewHolderContext);
    View symptomView = symptomInflater.inflate(
      layout.recyclerview_symptom_item,
      symptomParent,
      false
    );

    return new SymptomViewHolder(symptomView);
  }


}//end symptom view holder class
