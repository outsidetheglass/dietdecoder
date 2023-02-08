package com.dietdecoder.dietdecoder.ui.foodlog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;

import java.time.Instant;

public class FoodLogViewHolder extends RecyclerView.ViewHolder {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView foodLogItemView;
  public ImageButton foodLogItemOptionButton;
  private Context foodLogContext;


  private FoodLogViewHolder(View itemView) {
    super(itemView);
    foodLogContext = itemView.getContext();
    foodLogItemView = itemView.findViewById(R.id.textview_log_item);
    foodLogItemOptionButton = itemView.findViewById(R.id.imagebutton_food_log_option);

  }

  public void bind(FoodLog foodLog) {

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
//
//    foodLogItemView = Util.setTextWithSpan(foodLogItemView, boldString, notBoldString, Util.boldStyle);

    // if the item options is clicked, open the menu for options on that item
    foodLogItemOptionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(foodLogContext, "Was clicked", Toast.LENGTH_SHORT).show();

        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(foodLogContext, foodLogItemView);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem foodLogMenuItem) {

            // if edit clicked
            if ( foodLogMenuItem.getTitle().toString() == foodLogContext.getString(R.string.edit))
            {
              Toast.makeText(foodLogContext, "Edit was clicked", Toast.LENGTH_SHORT).show();
              // go to double check are they sure to update fragment with our food log id
              Intent intent = new Intent(foodLogContext, EditFoodLogActivity.class);
              intent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO, Util.ARGUMENT_GO_TO_EDIT_FOOD_LOG_FRAGMENT);
              intent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLog.getMFoodLogId().toString());
              foodLogContext.startActivity(intent);
            }
            // if delete clicked
            else if ( foodLogMenuItem.getTitle().toString()  == foodLogContext.getString(R.string.delete ))
            {
              Toast.makeText(foodLogContext, "Delete was clicked", Toast.LENGTH_SHORT).show();
              // delete this log, go activity double checking if they want to
              Intent intent = new Intent(foodLogContext, AreYouSureActivity.class);
              intent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO, Util.ARGUMENT_GO_TO_DELETE_FOOD_LOG);
              intent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLog.getMFoodLogId().toString());
              foodLogContext.startActivity(intent);

            }
            // if duplicate clicked
            else if ( foodLogMenuItem.getTitle().toString()  == foodLogContext.getString(R.string.duplicate ))
            {
              Toast.makeText(foodLogContext, "Duplicate was clicked", Toast.LENGTH_SHORT).show();

              Intent intent = new Intent(foodLogContext, AreYouSureActivity.class);
              intent.putExtra(Util.ARGUMENT_ACTION, Util.ARGUMENT_DUPLICATE);
              intent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, foodLog.getMFoodLogId().toString());
              foodLogContext.startActivity(intent);


//
//              Calendar foodLogCalendar = foodLog.getFoodLogDateTimeCalendar();
//              Integer foodLogNumberDayOfMonth = foodLogCalendar.get(Calendar.DAY_OF_MONTH);
//              Integer foodLogYear = foodLogCalendar.get(Calendar.YEAR);
//              Integer foodLogMonth = foodLogCalendar.get(Calendar.MONTH);
//              Integer foodLogHour = foodLogCalendar.get(Calendar.HOUR_OF_DAY);
//              Integer foodLogMinute = foodLogCalendar.get(Calendar.MINUTE);
//
//
//              foodLogContext.startActivity(
//                new Intent(foodLogContext, NewFoodLogActivity.class)
//                  .putExtra(
//                  "ingredientName", mFoodLogIngredientName)
//                  .putExtra("ingredientDateTimeDay", foodLogNumberDayOfMonth)
//                  .putExtra("ingredientDateTimeMonth", foodLogMonth)
//                  .putExtra("ingredientDateTimeYear", foodLogYear)
//                  .putExtra("ingredientDateTimeHour", foodLogHour)
//                  .putExtra("ingredientDateTimeMinute", foodLogMinute)
//                  .putExtra("ingredientDateTimeAcquired", mFoodLogDateTimeAcquired)
//                  .putExtra("ingredientDateTimeCooked", mFoodLogDateTimeCooked)
//              );
            }
            // if more details clicked
            else if ( foodLogMenuItem.getTitle().toString() == foodLogContext.getString(R.string.detail) )
            {
              Intent detailIntent = new Intent(foodLogContext, DetailFoodLogActivity.class);
              detailIntent.putExtra("food_log_detail", mFoodLogString);
              foodLogContext.startActivity(detailIntent);
            }

            return true;
          }
        });
        // Showing the popup menu
        popupMenu.show();
      }
    });

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


}//end log view holder class
