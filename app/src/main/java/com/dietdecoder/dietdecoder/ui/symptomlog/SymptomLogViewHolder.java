package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.content.Context;
import android.text.SpannableStringBuilder;
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
import com.dietdecoder.dietdecoder.activity.symptomlog.SymptomLogClickListener;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;

import java.time.Instant;
import java.util.ArrayList;

public class SymptomLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  Class mActivityClass;

  private static SymptomLogClickListener mListener;
  // to set the text for what shows up in the UI
  public TextView symptomLogItemView;
  private Context mSymptomLogContext;
  private SymptomLog mSymptomLog;
  ImageButton mSymptomLogCheckButton;
  public ArrayList<SymptomLog> mSelectedSymptomLogArrayList;

  String mSymptomLogIdString;

  private SymptomLogViewHolder(View itemView,
                               SymptomLogClickListener listener) {
    super(itemView);
    this.mListener = listener;
    mSymptomLogContext = itemView.getContext();
    mActivityClass = itemView.getClass();
    symptomLogItemView = itemView.findViewById(R.id.textview_symptom_log_item);
    mSymptomLogCheckButton = itemView.findViewById(R.id.imagebutton_symptom_log_option);

    itemView.setOnClickListener(this);
  }



  static SymptomLogViewHolder create(ViewGroup logParent) {

    Context logContext = logParent.getContext();
    LayoutInflater logInflater = LayoutInflater.from(logContext);
    View logView = logInflater.inflate(
            R.layout.recyclerview_list_symptom_log_item,
            logParent,
            false
    );

    return new SymptomLogViewHolder(logView, mListener);
  }


  public void bind(SymptomLog symptomLog, Symptom symptom) {
    // make the recyclerview populated with the info of each symptom log
    // get the info first
    // print it pretty
    // attach a listener to the options to act on
    // edit, duplicate, delete, or detail clicked

    this.mSymptomLog = symptomLog;

    // info on the symptomlog
    // in order to bind it to the recyclerview
    String mSymptomLogName = symptom.getName();
//    String mSymptomLogName = symptomLog.getSymptomLogSymptomName();
     mSymptomLogIdString = symptomLog.getLogId().toString();
    String mSymptomLogDescription = symptomLog.getLogSymptomDescription();
    Instant mSymptomLogBeganInstant = symptomLog.getInstantBegan();
    Instant mSymptomLogChangedInstant = symptomLog.getInstantChanged();
//    Integer mSymptomIntensityInteger = symptomLog.getIntensityScale();
    String mSymptomIntensityString = "Intensity N/A";
//    String mSymptomIntensityString = Util.setIntensityString(mSymptomIntensityInteger.toString());
    if ( symptomLog.getLogSymptomIntensity() != null) {
      mSymptomIntensityString = symptomLog.getLogSymptomIntensity().toString();
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
    mSymptomLogCheckButton.setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {
    if (mListener != null) {
      mListener.onSymptomLogClick(getAdapterPosition());
    }
    switch (view.getId()) {
      // when the options button next to the symptom log is chosen
      case R.id.imagebutton_symptom_log_option:
        
        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(mSymptomLogContext, mSymptomLogCheckButton);
        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.END);
        // if an option in the menu is clicked
        popupMenu.setOnMenuItemClickListener(symptomLogMenuItem -> {
          // which button was clicked
          switch (symptomLogMenuItem.getItemId()) {

            // go to the right activity, edit or delete or details,
            // and then the action to take is either duplicate, edit, or delete
            // and go with the ID array string of the object
            case R.id.duplicate_option:
              // edit fragment checks for if we're a duplicate or not for what to set
              Util.goToEditActivityActionTypeId(mSymptomLogContext, null,
                      Util.ARGUMENT_ACTION_DUPLICATE,
                      Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY,  mSymptomLogIdString);
              break;

            case R.id.edit_option:
              // tell the edit activity we want the full edit fragment

              Util.goToEditActivityActionTypeId(mSymptomLogContext, null,
                      Util.ARGUMENT_ACTION_EDIT, Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY,
                      mSymptomLogIdString);
              break;

            case R.id.delete_option:
              // delete this log, go activity double checking if they want to
              Util.goToDetailActivity(mSymptomLogContext, Util.ARGUMENT_ACTION_DELETE,
                      Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, mSymptomLogIdString);
              break;

            case R.id.detail_option:
              Util.goToDetailActivity(mSymptomLogContext, Util.ARGUMENT_ACTION_DETAIL,
                      Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, mSymptomLogIdString);
              break;

            default:
              break;
          }//end switch case for which menu item was chosen

//      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, symptomLogIdString);
//      mSymptomLogContext.startActivity(mIntent);

          return true;
        });
        // Showing the popup menu
        popupMenu.show();

        break;
      default:
        break;
    }// end switch case for options button clicked
  }
}//end log view holder class
