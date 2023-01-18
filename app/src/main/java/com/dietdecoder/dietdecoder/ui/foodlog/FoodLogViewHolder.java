package com.dietdecoder.dietdecoder.ui.foodlog;

import android.content.Context;
import android.content.Intent;
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
import com.dietdecoder.dietdecoder.activity.foodlog.DetailFoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;

import java.time.Instant;
import java.util.Calendar;

public class FoodLogViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView foodLogItemView;
  public ImageButton logItemOptionButton;
  private Context foodLogContext;

  private FoodLogViewHolder(View itemView) {
    super(itemView);
    foodLogContext = itemView.getContext();
    foodLogItemView = itemView.findViewById(R.id.textview_log_item);

//    //TODO get edit working and uncomment this and figure out how to place it right
//    logItemOptionButton = itemView.findViewById(R.id.imagebutton_log_option);
  }


  public void bind(FoodLog foodLog) {

    // info on the foodlog
    // in order to bind it to the recyclerview
    String mFoodLogDateTime = foodLog.getFoodLogDateTimeString();
    String mFoodLogIngredientName = foodLog.getMIngredientName();
    String mFoodLogBrand = foodLog.getMBrand();
    Instant mFoodLogDateTimeCooked = foodLog.getMDateTimeCooked();
    Instant mFoodLogDateTimeAcquired = foodLog.getMDateTimeAcquired();
    String mFoodLogString = foodLog.toString();

    // bind the name and the time the food was eaten to the recyclerview item
    foodLogItemView.setText(mFoodLogIngredientName + "\n(" + mFoodLogDateTime + ")");

    // if the item is clicked, open the menu for options on that item
    // change this to OptionButton when that's working, or not
    foodLogItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

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
              //TODO turn this into a fragment, or just a popup
//              Intent editLogIntent = new Intent(logContext, EditLogActivity.class);
//              editLogIntent.putExtra("log_datetime", logDateTime);
//              //TODO add other properties of log type here
//              //editLogIntent.putExtra("log_concern", logConcern);
//              logContext.startActivity( editLogIntent );
            }
            // if delete clicked
            else if ( foodLogMenuItem.getTitle().toString()  == foodLogContext.getString(R.string.delete ))
            {
              Toast.makeText(foodLogContext, "Delete was clicked", Toast.LENGTH_SHORT).show();
//              logContext.startActivity(
//                new Intent(logContext, DeleteLogActivity.class)
//              );
            }
            // if duplicate clicked
            else if ( foodLogMenuItem.getTitle().toString()  == foodLogContext.getString(R.string.duplicate ))
            {
              Toast.makeText(foodLogContext, "Duplicate was clicked", Toast.LENGTH_SHORT).show();

              Calendar foodLogCalendar = foodLog.getFoodLogDateTimeCalendar();
              Integer foodLogNumberDayOfMonth = foodLogCalendar.get(Calendar.DAY_OF_MONTH);
              Integer foodLogYear = foodLogCalendar.get(Calendar.YEAR);
              Integer foodLogMonth = foodLogCalendar.get(Calendar.MONTH);
              Integer foodLogHour = foodLogCalendar.get(Calendar.HOUR_OF_DAY);
              Integer foodLogMinute = foodLogCalendar.get(Calendar.MINUTE);


              foodLogContext.startActivity(
                new Intent(foodLogContext, NewFoodLogActivity.class)
                  .putExtra(
                  "ingredientName", mFoodLogIngredientName)
                  .putExtra("ingredientDateTimeDay", foodLogNumberDayOfMonth)
                  .putExtra("ingredientDateTimeMonth", foodLogMonth)
                  .putExtra("ingredientDateTimeYear", foodLogYear)
                  .putExtra("ingredientDateTimeHour", foodLogHour)
                  .putExtra("ingredientDateTimeMinute", foodLogMinute)
                  .putExtra("ingredientDateTimeAcquired", mFoodLogDateTimeAcquired)
                  .putExtra("ingredientDateTimeCooked", mFoodLogDateTimeCooked)
              );
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
