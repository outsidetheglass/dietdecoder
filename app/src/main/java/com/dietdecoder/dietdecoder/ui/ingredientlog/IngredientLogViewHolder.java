package com.dietdecoder.dietdecoder.ui.ingredientlog;

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
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.time.Instant;
import java.util.UUID;

public class IngredientLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  Class mActivityClass;

  // to set the text for what shows up in the UI
  public TextView ingredientLogItemView;
  private Context ingredientLogContext;
  private IngredientLog mIngredientLog;
  ImageButton mIngredientLogOptionButton;

  UUID mIngredientLogIngredientId;
  String mIngredientLogIdString, mIngredientLogIngredientName, mIngredientLogAmount,
          mIngredientLogString,
          mIngredientLogCookedString, unImportantString, mIngredientLogConsumedString,
          mIngredientLogAcquiredString;
  Instant mIngredientLogConsumedInstant, mIngredientLogCookedInstant,
          mIngredientLogAcquiredInstant;

  IngredientViewModel mIngredientViewModel;
  Ingredient mIngredientLogIngredient;

  private IngredientLogViewHolder(View itemView) {
    super(itemView);
    ingredientLogContext = itemView.getContext();
    mActivityClass = itemView.getClass();
    ingredientLogItemView = itemView.findViewById(R.id.textview_ingredient_log_item);
    mIngredientLogOptionButton = itemView.findViewById(R.id.imagebutton_ingredient_log_option);

  }



  static IngredientLogViewHolder create(ViewGroup logParent) {

    Context logContext = logParent.getContext();
    LayoutInflater logInflater = LayoutInflater.from(logContext);
    View logView = logInflater.inflate(
            R.layout.recyclerview_ingredient_log_item,
            logParent,
            false
    );

    return new IngredientLogViewHolder(logView);
  }


  public void bind(IngredientLog ingredientLog, Ingredient ingredient) {
    // make the recyclerview populated with the info of each ingredient log
    // get the info first
    // print it pretty
    // attach a listener to the options to act on
    // edit, duplicate, delete, or detail clicked

    this.mIngredientLog = ingredientLog;
    // set all the parts of ingredient log to variables
    setVariables();

    // info on the ingredientlog
    // in order to bind it to the recyclerview
    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewString(
                    mIngredientLogIngredientName, "", mIngredientLogConsumedString, "");

    // what gets put in the UI that isn't bold
    unImportantString =
            Util.setDescriptionString(
                    mIngredientLogAmount + ", " + mIngredientLogAcquiredString);

    // set part of it bold and part of it not bold
    ingredientLogItemView.setText(printString);

    // if the item options is clicked, open the menu for options on that item
    mIngredientLogOptionButton.setOnClickListener(this);

  }

  private void setVariables(){
    mIngredientLogIdString = mIngredientLog.getLogId().toString();
    mIngredientLogAmount = mIngredientLog.getIngredientLogIngredientSubjectiveAmount();

    // get the ingredient in the log
    mIngredientLogIngredientId = mIngredientLog.getLogIngredientId();
    mIngredientLogIngredient =
            mIngredientViewModel.viewModelGetFromId(mIngredientLogIngredientId);
    mIngredientLogIngredientName = mIngredientLogIngredient.getIngredientName();

    // times between consumed and the others
    mIngredientLogConsumedInstant = mIngredientLog.getInstantConsumed();
    mIngredientLogConsumedString = Util.stringFromInstant(mIngredientLogConsumedInstant);
    // how many days ago, if any, between when it was cooked and consumed
    mIngredientLogCookedInstant = mIngredientLog.getInstantCooked();
    mIngredientLogCookedString =
            Util.stringFromInstant(mIngredientLogCookedInstant);
    // how many days separate when it was acquired/bought/harvested from when it was consumed
    mIngredientLogAcquiredInstant = mIngredientLog.getInstantAcquired();
    mIngredientLogAcquiredString =
            Util.stringFromInstant(mIngredientLogCookedInstant);

  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      // when the options button next to the ingredient log is chosen
      case R.id.imagebutton_ingredient_log_option:
        
        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(ingredientLogContext, mIngredientLogOptionButton);
        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.END);
        // if an option in the menu is clicked
        popupMenu.setOnMenuItemClickListener(ingredientLogMenuItem -> {
          // which button was clicked
          switch (ingredientLogMenuItem.getItemId()) {

            // go to the right activity, edit or delete or details,
            // and then the action to take is either duplicate, edit, or delete
            // and go with the ID array string of the object
            case R.id.duplicate_option:
              // edit fragment checks for if we're a duplicate or not for what to set
              Util.goToEditActivityActionTypeId(ingredientLogContext, null,
                      Util.ARGUMENT_ACTION_DUPLICATE,
                      Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY,  mIngredientLogIdString);
              break;

            case R.id.edit_option:
              // tell the edit activity we want the full edit fragment
              Util.goToEditActivityActionTypeId(ingredientLogContext, null,
                      Util.ARGUMENT_ACTION_EDIT, Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY,
                      mIngredientLogIdString);
              break;

            case R.id.delete_option:
              // delete this log, go activity double checking if they want to
              Util.goToDetailActivity(ingredientLogContext, Util.ARGUMENT_ACTION_DELETE,
                      Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mIngredientLogIdString);
              break;

            case R.id.detail_option:
              Util.goToDetailActivity(ingredientLogContext, Util.ARGUMENT_ACTION_DETAIL,
                      Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mIngredientLogIdString);
              break;

            default:
              break;
          }//end switch case for which menu item was chosen

//      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, ingredientLogIdString);
//      ingredientLogContext.startActivity(mIntent);

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
