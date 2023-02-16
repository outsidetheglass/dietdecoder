package com.dietdecoder.dietdecoder.ui.foodlog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.foodlog.AreYouSureActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.DetailFoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.EditFoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;

import java.time.Instant;

public class FoodLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView foodLogItemView;
  public ImageButton foodLogItemOptionButton;
  private Context foodLogContext;
  private FoodLog mFoodLog;

  private FoodLogViewHolder(View itemView) {
    super(itemView);
    foodLogContext = itemView.getContext();
    foodLogItemView = itemView.findViewById(R.id.textview_log_item);
    foodLogItemOptionButton = itemView.findViewById(R.id.imagebutton_food_log_option);

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
    // make the recyclerview populated with the info of each food log
    // get the info first
    // print it pretty
    // attach a listener to the options to act on
    // edit, duplicate, delete, or detail clicked

    this.mFoodLog = foodLog;

    // info on the foodlog
    // in order to bind it to the recyclerview
    String mFoodLogDateTime = foodLog.getFoodLogDateTimeString();
    String mFoodLogIngredientName = foodLog.getMIngredientName();
    String mFoodLogBrand = foodLog.getMBrand();
    Instant mFoodLogDateTimeConsumed = foodLog.getMDateTimeConsumed();
    Instant mFoodLogDateTimeCooked = foodLog.getMDateTimeCooked();
    Instant mFoodLogDateTimeAcquired = foodLog.getMDateTimeAcquired();
    String mFoodLogString = foodLog.toString();

    String boldString = "";
    String notBoldString = "\n";
    String italicString = "\n";
    String notItalicString = "\n";

    // bind the name and the time the food was eaten to the recyclerview item
    // leave out brand if it isn't named
    if ( TextUtils.isEmpty(mFoodLogBrand)) {
      boldString = mFoodLogIngredientName;
      notBoldString = notBoldString
              .concat("(").concat(mFoodLogDateTime).concat(")");
      }
    else {
      boldString = mFoodLogIngredientName;
      notBoldString =
              notBoldString
                      .concat(mFoodLogBrand)
                      .concat("\n(").concat(mFoodLogDateTime).concat(")");
    }

    // for adding acquired and cooked to the string
    // how many days ago, if any, between when it was acquired and when it was consumed
    String acquiredRelativeDateToConsumed =
            Util.stringRelativeTimeFromInstant(mFoodLogDateTimeConsumed, mFoodLogDateTimeAcquired);
    // same for cooked
    String cookedRelativeDateToConsumed =
            Util.stringRelativeTimeFromInstant(mFoodLogDateTimeConsumed, mFoodLogDateTimeCooked);

    italicString = italicString.concat("Acquired: ").concat(
            acquiredRelativeDateToConsumed);

    notItalicString = notItalicString.concat("Cooked: ").concat(
            cookedRelativeDateToConsumed);


    // set part of it bold and part of it not bold
    foodLogItemView.setText(Util.setBoldItalicSpan(boldString, notBoldString, italicString,
            notItalicString));

    // if the item options is clicked, open the menu for options on that item
    foodLogItemOptionButton.setOnClickListener(this);

  }

  @Override
  public void onClick(View view) {
    // Initializing the popup menu and giving the reference as current logContext
    PopupMenu popupMenu = new PopupMenu(foodLogContext, foodLogItemOptionButton);
    // Inflating popup menu from popup_menu.xml file
    popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
    popupMenu.setGravity(Gravity.END);
    // if an option in the menu is clicked
    popupMenu.setOnMenuItemClickListener(foodLogMenuItem -> {

      // make edit the default for duplicate and edit both go there
      Intent mIntent = new Intent(foodLogContext, EditFoodLogActivity.class);

      // which button was clicked
      switch (foodLogMenuItem.getItemId()) {

        case R.id.duplicate_option:
          // edit fragment checks for if we're a duplicate or not for what to set
          mIntent.putExtra(Util.ARGUMENT_ACTION, Util.ARGUMENT_DUPLICATE);
          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
                  Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT);
          break;

        case R.id.edit_option:
          // tell the edit activity we want the full edit fragment
          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
                  Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT);
          Log.d(TAG, "inside edit option click: " + mIntent.getStringExtra(
                  Util.ARGUMENT_FRAGMENT_GO_TO));
          break;

        case R.id.delete_option:
// delete this log, go activity double checking if they want to
          mIntent = new Intent(foodLogContext, AreYouSureActivity.class);
          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
                  Util.ARGUMENT_GO_TO_DELETE_FOOD_LOG);
          break;

        case R.id.detail_option:
          mIntent = new Intent(foodLogContext, DetailFoodLogActivity.class);
          mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
                  Util.ARGUMENT_GO_TO_DETAIL_FOOD_LOG);
          break;

        default:
          break;
      }//end switch case

      // adding string after switch cases because delete and detail make new intents
      String foodLogIdString = mFoodLog.getMFoodLogId().toString();
      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLogIdString);

      foodLogContext.startActivity(mIntent);

      return true;
    });
    // Showing the popup menu
    popupMenu.show();

  }
}//end log view holder class
