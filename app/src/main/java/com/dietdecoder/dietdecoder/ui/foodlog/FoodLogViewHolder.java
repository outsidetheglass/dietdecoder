package com.dietdecoder.dietdecoder.ui.foodlog;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;

import java.time.Instant;

public class FoodLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView foodLogItemView;
  private Context foodLogContext;
  private FoodLog mFoodLog;
  ImageButton mFoodLogCheckButton;

  String mFoodLogIdString;

  private FoodLogViewHolder(View itemView) {
    super(itemView);
    foodLogContext = itemView.getContext();
    foodLogItemView = itemView.findViewById(R.id.textview_food_log_item);
    mFoodLogCheckButton = itemView.findViewById(R.id.imagebutton_food_log_option);

  }



  static FoodLogViewHolder create(ViewGroup logParent) {

    Context logContext = logParent.getContext();
    LayoutInflater logInflater = LayoutInflater.from(logContext);
    View logView = logInflater.inflate(
            R.layout.recyclerview_food_log_item,
            logParent,
            false
    );

    return new FoodLogViewHolder(logView);
  }


  public void bind(FoodLog foodLog) {
    // make the recyclerview populated with the info of each symptom log
    // get the info first
    // print it pretty
    // attach a listener to the options to act on
    // edit, duplicate, delete, or detail clicked

    this.mFoodLog = foodLog;

    // info on the foodlog
    // in order to bind it to the recyclerview
    String mFoodLogName = foodLog.getIngredientName();
    mFoodLogIdString = foodLog.getFoodLogId().toString();
    Instant mFoodLogInstantAcquired = foodLog.getInstantAcquired();
    Instant mFoodLogInstantCooked = foodLog.getInstantCooked();
    Instant mFoodLogInstantConsumed = foodLog.getInstantConsumed();
//    Integer mSymptomIntensityInteger = foodLog.getIntensityScale();
    String mSymptomIntensityString = "Intensity N/A";
//    String mSymptomIntensityString = Util.setIntensityString(mSymptomIntensityInteger.toString());
//    if ( foodLog.getIntensity() != null) {
//      mSymptomIntensityString = foodLog.getIntensity().toString();
//    }
    //String mFoodLogString = foodLog.toString();


    // how many days ago, if any, between when it began and changed
    String changedRelativeDateToBeganString =
            Util.stringRelativeTimeFromInstant(mFoodLogInstantCooked,
                    mFoodLogInstantConsumed);

    String unImportantString =
            Util.setDescriptionString(mFoodLogInstantAcquired.toString());

    String mFoodLogBeganString = Util.stringFromInstant(mFoodLogInstantCooked);

    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewString(mFoodLogName,
                    changedRelativeDateToBeganString, mSymptomIntensityString, unImportantString );


    // set part of it bold and part of it not bold
    foodLogItemView.setText(printString);

    // if the item options is clicked, open the menu for options on that item
    mFoodLogCheckButton.setOnClickListener(this);



// info on the foodlog
    // in order to bind it to the recyclerview
//    String mFoodLogDateTime = foodLog.getFoodLogDateTimeString();
//    String mFoodLogIngredientName = foodLog.getIngredientId().toString();
//    Instant mFoodLogDateTimeConsumed = foodLog.getInstantConsumed();
//    Instant mFoodLogDateTimeCooked = foodLog.getInstantCooked();
//    Instant mFoodLogDateTimeAcquired = foodLog.getInstantAcquired();
//    String mFoodLogString = foodLog.toString();
//
//
//    // TODO fix this to be in Util so Ingredient and Recipe and Symptom can list as easily
//    // for adding acquired and cooked to the string
//    // how many days ago, if any, between when it was acquired and when it was consumed
//    String acquiredRelativeDateToConsumed =
//            Util.stringRelativeTimeFromInstant(mFoodLogDateTimeConsumed, mFoodLogDateTimeAcquired);
//    // same for cooked
//    String cookedRelativeDateToConsumed =
//            Util.stringRelativeTimeFromInstant(mFoodLogDateTimeConsumed, mFoodLogDateTimeCooked);
//
//    String unImportantString =
//            Util.setAcquiredString(acquiredRelativeDateToConsumed) + Util.setCookedString(cookedRelativeDateToConsumed);
//
//    String mFoodLogConsumedString = Util.stringFromInstant(mFoodLogDateTimeConsumed);
//
//    SpannableStringBuilder printString = Util.setViewHolderRecyclerViewString(mFoodLogIngredientName,
//            "foodLogBrand", mFoodLogConsumedString, unImportantString );
//
//
//    // set part of it bold and part of it not bold
//    foodLogItemView.setText(printString);
//
//    // if the item options is clicked, open the menu for options on that item
//    foodLogItemOptionButton.setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      // when the options button next to the symptom log is chosen
      case R.id.imagebutton_food_log_option:

        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(foodLogContext, mFoodLogCheckButton);
        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.END);
        // if an option in the menu is clicked
        popupMenu.setOnMenuItemClickListener(foodLogMenuItem -> {
          // which button was clicked
          switch (foodLogMenuItem.getItemId()) {

            // go to the right activity, edit or delete or details,
            // and then the action to take is either duplicate, edit, or delete
            // and go with the ID array string of the object
            case R.id.duplicate_option:
              // edit fragment checks for if we're a duplicate or not for what to set
              Util.goToEditActivityActionTypeId(foodLogContext, null,
                      Util.ARGUMENT_ACTION_DUPLICATE,
                      Util.ARGUMENT_FOOD_LOG_ID_ARRAY,  mFoodLogIdString);
              break;

            case R.id.edit_option:
              // tell the edit activity we want the full edit fragment
              Util.goToEditActivityActionTypeId(foodLogContext, null,
                      Util.ARGUMENT_ACTION_EDIT, Util.ARGUMENT_FOOD_LOG_ID_ARRAY,
                      mFoodLogIdString);
              break;

            case R.id.delete_option:
              // delete this log, go activity double checking if they want to
              Util.goToDetailActivity(foodLogContext, Util.ARGUMENT_ACTION_DELETE,
                      Util.ARGUMENT_FOOD_LOG_ID_ARRAY, mFoodLogIdString);
              break;

            case R.id.detail_option:
              Util.goToDetailActivity(foodLogContext, Util.ARGUMENT_ACTION_DETAIL,
                      Util.ARGUMENT_FOOD_LOG_ID_ARRAY, mFoodLogIdString);
              break;

            default:
              break;
          }//end switch case for which menu item was chosen

//      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);
//      foodLogContext.startActivity(mIntent);

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

