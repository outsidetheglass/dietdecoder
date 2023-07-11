package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;

import java.time.Instant;

public class SymptomLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView symptomLogItemView;
  public ImageButton symptomLogItemOptionButton;
  private Context symptomLogContext;
  private SymptomLog mSymptomLog;

  private SymptomLogViewHolder(View itemView) {
    super(itemView);
    symptomLogContext = itemView.getContext();
    symptomLogItemView = itemView.findViewById(R.id.textview_symptom_log_item);
//    symptomLogItemOptionButton = itemView.findViewById(R.id.imagebutton_symptom_option_circle);

  }



  static SymptomLogViewHolder create(ViewGroup logParent) {

    Context logContext = logParent.getContext();
    LayoutInflater logInflater = LayoutInflater.from(logContext);
    View logView = logInflater.inflate(
            R.layout.recyclerview_symptom_log_item,
            logParent,
            false
    );

    return new SymptomLogViewHolder(logView);
  }


  public void bind(SymptomLog symptomLog) {
    // make the recyclerview populated with the info of each symptom log
    // get the info first
    // print it pretty
    // attach a listener to the options to act on
    // edit, duplicate, delete, or detail clicked

    this.mSymptomLog = symptomLog;

    // info on the symptomlog
    // in order to bind it to the recyclerview
    String mSymptomLogName = symptomLog.getSymptomName();
    String mSymptomLogDescription = symptomLog.getDescription();
    Instant mSymptomLogBeganInstant = symptomLog.getInstantBegan();
    Instant mSymptomLogChangedInstant = symptomLog.getInstantChanged();
//    Integer mSymptomIntensityInteger = symptomLog.getIntensityScale();
    String mSymptomIntensityString = "Intensity N/A";
//    String mSymptomIntensityString = Util.setIntensityString(mSymptomIntensityInteger.toString());
    if ( symptomLog.getIntensity() != null) {
      mSymptomIntensityString = symptomLog.getIntensity().toString();
    }
    //String mSymptomLogString = symptomLog.toString();


    // how many days ago, if any, between when it began and changed
    String changedRelativeDateToBeganString =
            Util.stringRelativeTimeFromInstant(mSymptomLogBeganInstant,
                    mSymptomLogChangedInstant);

    String unImportantString =
            Util.setDescriptionString(mSymptomLogDescription);

    String mSymptomLogBeganString = Util.stringFromInstant(mSymptomLogBeganInstant);

    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewString(mSymptomLogName,
            changedRelativeDateToBeganString, mSymptomIntensityString, unImportantString );


    // set part of it bold and part of it not bold
    symptomLogItemView.setText(printString);

    // if the item options is clicked, open the menu for options on that item
//    symptomLogItemOptionButton.setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {
    //when clicked change image to filled in with sick person image
    //ic_baseline_sick

    // Initializing the popup menu and giving the reference as current logContext
    PopupMenu popupMenu = new PopupMenu(symptomLogContext, symptomLogItemOptionButton);
    // Inflating popup menu from popup_menu.xml file
    popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
    popupMenu.setGravity(Gravity.END);
    // if an option in the menu is clicked
    popupMenu.setOnMenuItemClickListener(symptomLogMenuItem -> {

      // make edit the default for duplicate and edit both go there
//      Intent mIntent = new Intent(symptomLogContext, EditSymptomLogActivity.class);

      // which button was clicked
      switch (symptomLogMenuItem.getItemId()) {

        case R.id.duplicate_option:
          // edit fragment checks for if we're a duplicate or not for what to set
//          mIntent.putExtra(Util.ARGUMENT_ACTION, Util.ARGUMENT_DUPLICATE);
//          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
//                  Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT);
          break;

        case R.id.edit_option:
          // tell the edit activity we want the full edit fragment
//          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
//                  Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT);
//                  Util.ARGUMENT_FRAGMENT_GO_TO));
          break;

        case R.id.delete_option:
// delete this log, go activity double checking if they want to
//          mIntent = new Intent(symptomLogContext, AreYouSureActivity.class);
//          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
//                  Util.ARGUMENT_GO_TO_DELETE_FOOD_LOG);
          break;

        case R.id.detail_option:
//          mIntent = new Intent(symptomLogContext, DetailSymptomLogActivity.class);
//          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
//                  Util.ARGUMENT_GO_TO_DETAIL_FOOD_LOG);
          break;

        default:
          break;
      }//end switch case

      // adding string after switch cases because delete and detail make new intents
      String symptomLogIdString = mSymptomLog.getSymptomLogId().toString();
//      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, symptomLogIdString);
//      symptomLogContext.startActivity(mIntent);

      return true;
    });
    // Showing the popup menu
    popupMenu.show();

  }
}//end log view holder class
