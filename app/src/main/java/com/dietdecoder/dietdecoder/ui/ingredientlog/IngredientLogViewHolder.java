package com.dietdecoder.dietdecoder.ui.ingredientlog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
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
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class IngredientLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  Class mActivityClass;

  // to set the text for what shows up in the UI
  public TextView mItemView;
  private Context mContext;
  private Resources.Theme mViewHolderTheme;
  private IngredientLog mLog;
  ColorStateList mSelectedColor;
  int mUnSelectedColor, mLayoutItem, mTextViewItem, mButtonItem;
  ImageButton mMoreOptionButton, mChooseOptionButton;
  Drawable mSickFaceDrawable, mRedRoundcornersBackgroundDrawable,
          mGreenRoundcornersDrawable, mEmptyCircleDrawable;

  UUID mLogIngredientId;
  String mLogIdString, mLogIngredientName, mLogAmount,
          mLogString,
          mLogCookedString, unImportantString, mLogConsumedString,
          mLogAcquiredString;
  Instant mLogConsumedInstant, mLogCookedInstant,
          mLogAcquiredInstant;

  IngredientViewModel mIngredientViewModel;
  Ingredient mLogIngredient;

  // allow activities to access existing arraylist in the view holder
  private static ArrayList<IngredientLog> mSelectedArrayList;

  private IngredientLogViewHolder(View itemView) {
    super(itemView);
    mContext = itemView.getContext();
    mActivityClass = itemView.getClass();
    mItemView = itemView.findViewById(R.id.textview_list_ingredient_log_item);
    mChooseOptionButton =
            itemView.findViewById(R.id.imagebutton_list_ingredient_log_choose_option);
    mMoreOptionButton =
            itemView.findViewById(R.id.imagebutton_list_ingredient_log_more_option);

    mViewHolderTheme = mContext.getTheme();
    mSelectedColor = itemView.getResources().getColorStateList(R.color.selected_text_color,
            mViewHolderTheme);
    // green color of titles, not the default text color I want but close enough
    TypedValue typedValue = new TypedValue();
    mViewHolderTheme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true);
    mUnSelectedColor = typedValue.data;

    // drawables for the selected and unselected options and their backgrounds
    mSickFaceDrawable = itemView.getResources().getDrawable(R.drawable.ic_baseline_sick,
            mViewHolderTheme);
    mRedRoundcornersBackgroundDrawable =
            itemView.getResources().getDrawable(R.drawable.red_roundcorners,
                    mViewHolderTheme);
    mGreenRoundcornersDrawable =
            itemView.getResources().getDrawable(R.drawable.roundcorners,
                    mViewHolderTheme);
    mEmptyCircleDrawable =
            itemView.getResources().getDrawable(R.drawable.ic_baseline_empty_circle,
                    mViewHolderTheme);
  }



  static IngredientLogViewHolder create(ViewGroup logParent) {

    Context logContext = logParent.getContext();
    LayoutInflater logInflater = LayoutInflater.from(logContext);
    View logView = logInflater.inflate(
            R.layout.recyclerview_list_ingredient_log_item,
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

    this.mLog = ingredientLog;
    this.mLogIngredient = ingredient;
    // set all the parts of ingredient log to variables
    setVariables();

    // info on the ingredientlog
    // in order to bind it to the recyclerview
    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewString(
                    mLogIngredientName, "", mLogConsumedString, "");

    // what gets put in the UI that isn't bold
    unImportantString =
            Util.setDescriptionString(
                    mLogAmount + ", " + mLogAcquiredString);

    // set part of it bold and part of it not bold
    mItemView.setText(printString);

    // if the item options is clicked, open the menu for options on that item
    mMoreOptionButton.setOnClickListener(this);

    mChooseOptionButton.setOnClickListener(this::onClick);
  }

  private void setVariables(){
    mLogIdString = mLog.getLogId().toString();
    mLogAmount = mLog.getLogIngredientSubjectiveAmount();

    // get the ingredient in the log
    mLogIngredientId = mLog.getLogIngredientId();
    mLogIngredientName = mLogIngredient.getIngredientName();

    // times between consumed and the others
    mLogConsumedInstant = mLog.getInstantConsumed();
    mLogConsumedString = Util.stringFromInstant(mLogConsumedInstant);
    // how many days ago, if any, between when it was cooked and consumed
    mLogCookedInstant = mLog.getInstantCooked();
    mLogCookedString =
            Util.stringFromInstant(mLogCookedInstant);
    // how many days separate when it was acquired/bought/harvested from when it was consumed
    mLogAcquiredInstant = mLog.getInstantAcquired();
    mLogAcquiredString =
            Util.stringFromInstant(mLogCookedInstant);

  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.imagebutton_list_ingredient_log_choose_option:
        // if the user wants to unselect the symptom, the color will be red
        // selected color is red
        boolean userWantsToUnSelect = mItemView.getTextColors() == mSelectedColor;

        //change text color to show it was unclicked, change back to unselected color
        if ( userWantsToUnSelect ) {
          mItemView.setTextColor(mUnSelectedColor);
          mChooseOptionButton.setBackground(mGreenRoundcornersDrawable);
          mChooseOptionButton.setImageDrawable(mEmptyCircleDrawable);

          // the symptom was selected before this, so the array list has it and needs it removed
          mSelectedArrayList.remove(mLog);
        }
        else {
          //change UI to show it was clicked
          // text color change to red
          mItemView.setTextColor(mSelectedColor);
          // change the empty circle to the sick face
          mChooseOptionButton.setImageDrawable(mSickFaceDrawable);
          // make the background of the sick face from a green circle to a red circle
          mChooseOptionButton.setBackground(null);

          // allow activities to access existing arraylist in the view holder
          // on click and the user is selecting it
          if (mSelectedArrayList == null) {
            mSelectedArrayList = new ArrayList<>();
          }
          mSelectedArrayList.add(mLog);
        }
        break;
      // when the options button next to the ingredient log is chosen
      case R.id.imagebutton_list_ingredient_log_more_option:

        if ( !Objects.isNull(mSelectedArrayList) ){
          mLogIdString = mSelectedArrayList.toString();
        }
        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(mContext, mMoreOptionButton);
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
              Util.goToEditActivityActionTypeId(mContext, null,
                      Util.ARGUMENT_ACTION_DUPLICATE,
                      Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY,  mLogIdString);
              break;

            case R.id.edit_option:
              // tell the edit activity we want the full edit fragment
              Util.goToEditActivityActionTypeId(mContext, null,
                      Util.ARGUMENT_ACTION_EDIT, Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY,
                      mLogIdString);
              break;

            case R.id.delete_option:
              // delete this log, go activity double checking if they want to
              Util.goToDetailActivity(mContext, Util.ARGUMENT_ACTION_DELETE,
                      Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mLogIdString);
              break;

            case R.id.detail_option:
              Util.goToDetailActivity(mContext, Util.ARGUMENT_ACTION_DETAIL,
                      Util.ARGUMENT_INGREDIENT_LOG_ID_ARRAY, mLogIdString);
              break;

            default:
              break;
          }//end switch case for which menu item was chosen

//      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, ingredientLogIdString);
//      mContext.startActivity(mIntent);

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
