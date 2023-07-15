package com.dietdecoder.dietdecoder.ui.symptom;

import static com.dietdecoder.dietdecoder.R.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.TypedValue;
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

public class SymptomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView mSymptomItemView;
  public ImageButton mSymptomCheckButton;
  private Context mSymptomViewHolderContext;
  private Resources.Theme mSymptomViewHolderTheme;
  ColorStateList mSelectedColor;
  int mUnSelectedColor;
  Drawable mSickFaceDrawable, mRedRoundcornersBackgroundDrawable, mGreenRoundcornersDrawable, mEmptyCircleDrawable;

  @SuppressLint("UseCompatLoadingForDrawables")
  private SymptomViewHolder(View itemView) {
    super(itemView);
    mSymptomViewHolderContext = itemView.getContext();
    mSymptomViewHolderTheme = mSymptomViewHolderContext.getTheme();

    mSymptomItemView = itemView.findViewById(id.textview_symptom_item);
    mSymptomCheckButton = itemView.findViewById(id.imagebutton_symptom_option_circle);
    mSelectedColor = itemView.getResources().getColorStateList(color.selected_text_color,
            mSymptomViewHolderTheme);
    // green color of titles, not the default text color I want but close enough
    TypedValue typedValue = new TypedValue();
    mSymptomViewHolderTheme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true);
    mUnSelectedColor = typedValue.data;
//    mUnSelectedColor = itemView.getResources().getColorStateList(color.,
//            mSymptomViewHolderTheme);

    // drawables for the selected and unselected options and their backgrounds
    mSickFaceDrawable = itemView.getResources().getDrawable(R.drawable.ic_baseline_sick,
            mSymptomViewHolderTheme);
    mRedRoundcornersBackgroundDrawable =
            itemView.getResources().getDrawable(R.drawable.red_roundcorners,
            mSymptomViewHolderTheme);
    mGreenRoundcornersDrawable =
            itemView.getResources().getDrawable(drawable.roundcorners,
                    mSymptomViewHolderTheme);
    mEmptyCircleDrawable =
            itemView.getResources().getDrawable(drawable.ic_baseline_empty_circle,
                    mSymptomViewHolderTheme);
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
    mSymptomItemView.setText( printString );

    mSymptomCheckButton.setOnClickListener(this::onClick);


  }


  static SymptomViewHolder create(ViewGroup symptomParent) {

    Context mSymptomViewHolderContext = symptomParent.getContext();
    LayoutInflater symptomInflater = LayoutInflater.from(mSymptomViewHolderContext);
    View symptomView = symptomInflater.inflate(
      layout.recyclerview_symptom_item,
      symptomParent,
      false
    );

    return new SymptomViewHolder(symptomView);
  }

  @Override
  public void onClick(View v) {


    // if the user wants to unselect the symptom, the color will be red
    // selected color is red
    boolean userWantsToUnSelect = mSymptomItemView.getTextColors() == mSelectedColor;

    // user wants to select the symptom
    // the color will be default or green
    // unselected color is green
    // if unselected color or default then change it to selected color
    // if selected color we're unselecting it, so change to unselected color
//        boolean userWantsToSelect =
//                mSymptomItemView.getTextColors() == mUnSelectedColor
//                        || mSymptomItemView.getTextColors() == getDefaultTextColor();

    //change text color to show it was unclicked, change back to unselected color
    if ( userWantsToUnSelect ) {
      mSymptomItemView.setTextColor(mUnSelectedColor);
      mSymptomCheckButton.setBackground(mGreenRoundcornersDrawable);
      mSymptomCheckButton.setImageDrawable(mEmptyCircleDrawable);
    }
    else {
      //change UI to show it was clicked
      // text color change to red
      mSymptomItemView.setTextColor(mSelectedColor);
      // change the empty circle to the sick face
      mSymptomCheckButton.setImageDrawable(mSickFaceDrawable);
      // make the background of the sick face from a green circle to a red circle
      mSymptomCheckButton.setBackground(null);
    }


  }

}//end symptom view holder class
