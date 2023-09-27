package com.dietdecoder.dietdecoder.ui.ingredient;

import static com.dietdecoder.dietdecoder.R.color;
import static com.dietdecoder.dietdecoder.R.drawable;
import static com.dietdecoder.dietdecoder.R.id;
import static com.dietdecoder.dietdecoder.R.layout;
import static com.dietdecoder.dietdecoder.R.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.ingredientlog.ChooseIngredientActivity;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;

import java.util.ArrayList;

public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView mIngredientItemView;
  public ImageButton mIngredientItemButton;
  private Context mIngredientViewHolderContext;
  private Resources.Theme mIngredientViewHolderTheme;
  ColorStateList mSelectedColor;
  int mUnSelectedColor, mLayoutItem, mTextViewItem, mButtonItem;
  Drawable mSickFaceDrawable, mRedRoundcornersBackgroundDrawable,
          mGreenRoundcornersDrawable, mEmptyCircleDrawable;

  String mIngredientIdString;
  public Ingredient mIngredient;

  // allow activities to access existing arraylist in the view holder
  private static ArrayList<Ingredient> mSelectedArrayList;


  @SuppressLint("UseCompatLoadingForDrawables")
  private IngredientViewHolder(View itemView) {
    super(itemView);
    mIngredientViewHolderContext = itemView.getContext();
    mSelectedArrayList = new ArrayList<>();

    if (mIngredientViewHolderContext.getClass() == ChooseIngredientActivity.class) {
      mTextViewItem = id.textview_choose_ingredient_item;
      mButtonItem = id.imagebutton_choose_ingredient_circle;
    } else {
      mTextViewItem = id.textview_list_ingredient_item;
      mButtonItem = id.imagebutton_list_ingredient_more;
    }

    mIngredientItemView = itemView.findViewById(mTextViewItem);
    mIngredientItemButton = itemView.findViewById(mButtonItem);

    mIngredientViewHolderTheme = mIngredientViewHolderContext.getTheme();
    mSelectedColor = itemView.getResources().getColorStateList(color.selected_text_color,
            mIngredientViewHolderTheme);
    // green color of titles, not the default text color I want but close enough
    TypedValue typedValue = new TypedValue();
    mIngredientViewHolderTheme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true);
    mUnSelectedColor = typedValue.data;
//    mUnSelectedColor = itemView.getResources().getColorStateList(color.,
//            mIngredientViewHolderTheme);

    // drawables for the selected and unselected options and their backgrounds
    mSickFaceDrawable = itemView.getResources().getDrawable(drawable.ic_baseline_sick,
            mIngredientViewHolderTheme);
    mRedRoundcornersBackgroundDrawable =
            itemView.getResources().getDrawable(drawable.red_roundcorners,
            mIngredientViewHolderTheme);
    mGreenRoundcornersDrawable =
            itemView.getResources().getDrawable(drawable.roundcorners,
                    mIngredientViewHolderTheme);
    mEmptyCircleDrawable =
            itemView.getResources().getDrawable(drawable.ic_baseline_empty_circle,
                    mIngredientViewHolderTheme);

  }


  public void bind(Ingredient ingredient) {

    // listOnlyTracked is to list only the ingredients that have to track set to true

    mIngredient = ingredient;
    String ingredientName = ingredient.getName();
    mIngredientIdString = ingredient.getId().toString();
    String ingredientBrand = ingredient.getBrand();

    String unImportantString =
            Util.setPlainDescriptionString(ingredientBrand);

    // only show useful description, not just the name repeated
    if ( TextUtils.equals(ingredientName, ingredientBrand) ) {
      unImportantString = "";
    }

    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewStringNameDescription(ingredientName, unImportantString );


    // set part of it bold and part of it not bold
    mIngredientItemView.setText( printString );

    mIngredientItemButton.setOnClickListener(this::onClick);


  }


  // allow activities to access existing arraylist in the view holder
  public static ArrayList<Ingredient> viewHolderGetSelectedArrayList(){
    return mSelectedArrayList;
  }


  static IngredientViewHolder create(ViewGroup ingredientParent) {

    Context mIngredientViewHolderContext = ingredientParent.getContext();
    LayoutInflater ingredientInflater = LayoutInflater.from(mIngredientViewHolderContext);

    int layoutItem = layout.recyclerview_choose_ingredient_item;
    // if not choose ingredient, use list ingredient
    if (mIngredientViewHolderContext.getClass() != ChooseIngredientActivity.class){
      layoutItem = layout.recyclerview_list_ingredient_item;
    }

    View ingredientView = ingredientInflater.inflate(
      layoutItem,
      ingredientParent,
      false
    );

    return new IngredientViewHolder(ingredientView);
  }

  private void chooseMoreButton(){
    // if the user wants to unselect the ingredient, the color will be red
    // selected color is red
    boolean userWantsToUnSelect = mIngredientItemView.getTextColors() == mSelectedColor;

    //change text color to show it was unclicked, change back to unselected color
    if ( userWantsToUnSelect ) {
      mIngredientItemView.setTextColor(mUnSelectedColor);
      mIngredientItemButton.setBackground(mGreenRoundcornersDrawable);
      mIngredientItemButton.setImageDrawable(mEmptyCircleDrawable);

      // the ingredient was selected before this, so the array list has it and needs it removed
      mSelectedArrayList.remove(mIngredient);
    }
    else {
      //change UI to show it was clicked
      // text color change to red
      mIngredientItemView.setTextColor(mSelectedColor);
      // change the empty circle to the sick face
      mIngredientItemButton.setImageDrawable(mSickFaceDrawable);
      // make the background of the sick face from a green circle to a red circle
      mIngredientItemButton.setBackground(null);

      // allow activities to access existing arraylist in the view holder
      // on click and the user is selecting it
      if (mSelectedArrayList == null) {
        mSelectedArrayList = new ArrayList<>();
      }
      mSelectedArrayList.add(mIngredient);
    }
  }

  private void listMoreButton(){

    // Initializing the popup menu and giving the reference as current logContext
    PopupMenu popupMenu = new PopupMenu(mIngredientViewHolderContext, mIngredientItemView);
    // Inflating popup menu from popup_menu.xml file
    popupMenu.getMenuInflater().inflate(menu.item_options_menu, popupMenu.getMenu());
    popupMenu.setGravity(Gravity.END);
    // if an option in the menu is clicked
    popupMenu.setOnMenuItemClickListener(menuItem -> {
      // which button was clicked
      switch (menuItem.getItemId()) {

        // go to the right activity, edit or delete or details,
        // and then the action to take is either duplicate, edit, or delete
        // and go with the ID array string of the object
        case id.duplicate_option:
          // edit fragment checks for if we're a duplicate or not for what to set
          Util.goToEditActivityActionTypeId(mIngredientViewHolderContext, null,
                  Util.ARGUMENT_ACTION_DUPLICATE,
                  Util.ARGUMENT_INGREDIENT_ID_ARRAY,  mIngredientIdString);
          break;

        case id.edit_option:
          // tell the edit activity we want the full edit fragment

          //Log.d(TAG, " edit imagebutton_list_ingredient_option: " + mIngredientIdString);
          Util.goToAddEditIngredientActivity(mIngredientViewHolderContext, null,
                  mIngredientIdString);
          break;

        case id.delete_option:
          // delete this log, go activity double checking if they want to
          Util.goToDetailActivity(mIngredientViewHolderContext, Util.ARGUMENT_ACTION_DELETE,
                  Util.ARGUMENT_INGREDIENT_ID_ARRAY, mIngredientIdString);
          break;

        case id.detail_option:
          Util.goToDetailActivity(mIngredientViewHolderContext, Util.ARGUMENT_ACTION_DETAIL,
                  Util.ARGUMENT_INGREDIENT_ID_ARRAY, mIngredientIdString);
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

  }

  @Override
  public void onClick(View view) {
    // which button was clicked
    switch (view.getId()) {

      case id.imagebutton_choose_ingredient_circle:
        chooseMoreButton();
        break;

      case id.imagebutton_list_ingredient_more:
        listMoreButton();
        break;

      default:
        break;
    }//end switch case



  }



}//end ingredient view holder class
