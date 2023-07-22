package com.dietdecoder.dietdecoder.ui.ingredient;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.DetailActivity;
import com.dietdecoder.dietdecoder.activity.EditActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.DeleteIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.DetailIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.EditIngredientActivity;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;

import java.util.Objects;

public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView mIngredientItemView;
  public ImageButton mIngredientItemButton;
  public ListView listView;
  private Context mIngredientViewHolderContext;
  private Resources.Theme mIngredientViewHolderTheme;
  ColorStateList mSelectedColor;
  int mUnSelectedColor;
  Drawable mEmptyCircleDrawable, mGreenRoundcornersDrawable, mFoodBeverageDrawable;


  private IngredientViewHolder(View itemView) {
    super(itemView);

    // TODO bind the search bar to the view holder, then make the ingredient in bind invisible if
    //  its name doesn't match what's typed in the search bar
    mIngredientViewHolderContext = itemView.getContext();
    mIngredientItemView = itemView.findViewById(R.id.textview_ingredient_item);
    mIngredientItemButton = itemView.findViewById(R.id.imagebutton_ingredient_option_circle);

    mIngredientViewHolderTheme = mIngredientViewHolderContext.getTheme();
    mSelectedColor = itemView.getResources().getColorStateList(R.color.selected_text_color,
            mIngredientViewHolderTheme);
    // green color of titles, not the default text color I want but close enough
    TypedValue typedValue = new TypedValue();
    mIngredientViewHolderTheme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true);
    mUnSelectedColor = typedValue.data;
//    mUnSelectedColor = itemView.getResources().getColorStateList(color.,
//            mIngredientViewHolderTheme);

    // drawables for the selected and unselected options and their backgrounds
    mFoodBeverageDrawable =
            itemView.getResources().getDrawable(R.drawable.ic_baseline_emoji_food_beverage_24,
            mIngredientViewHolderTheme);
    mGreenRoundcornersDrawable =
            itemView.getResources().getDrawable(R.drawable.roundcorners,
                    mIngredientViewHolderTheme);
    mEmptyCircleDrawable =
            itemView.getResources().getDrawable(R.drawable.ic_baseline_empty_circle,
                    mIngredientViewHolderTheme);
    //TODO get list ingredient as type new food log working
//    listView = itemView.findViewById(R.id.list_view_new_food_log_name);
  }


  public void bind(Ingredient ingredient) {

    // TODO bind the search bar to the view holder, then make the ingredient in bind invisible if
    //  its name doesn't match what's typed in the search bar
    String ingredientName = ingredient.getIngredientName();
    String ingredientIdString = ingredient.getIngredientId().toString();
    // if the other values exist, list them
    if (!Objects.isNull(ingredient.getIngredientChemicalName())) {

    }

    mIngredientItemView.setText(ingredientName);

    //TODO make this an if statement, if we're here to edit individual ingredients from a list of
    // them then it should show the options three dots and set on click listener to make a menu
    // of edit details delete and show this if we're here from list ingredient to make a new
    // ingredient log
    //if (ACTION == ACTION_NEW) then show empty circle and select unselect with text
    // else show three dot options
    mIngredientItemButton.setOnClickListener(this::onClick);


//
    //TODO get add food log search ingredient database and autofill while user typing working
    //listView.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//
//        Intent mIntent = new Intent(mIngredientContext, NewFoodLogActivity.class);
//        mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
//                Util.ARGUMENT_GO_TO_NAME);
//
//        // TODO figure out a better way to do listview
//        // if can't, then just do this. Go back to add name with chosen name
//        mIntent.putExtra(Util.ARGUMENT_INGREDIENT_ID, ingredientIdString);
//        mIntent.putExtra(Util.ARGUMENT_INGREDIENT_NAME, ingredientName);
//
//        mIngredientContext.startActivity(new Intent());
//      }
//    });
  }


  static IngredientViewHolder create(ViewGroup ingredientParent) {

    Context mIngredientContext = ingredientParent.getContext();
    LayoutInflater ingredientInflater = LayoutInflater.from(mIngredientContext);
    View ingredientView = ingredientInflater.inflate(
      R.layout.recyclerview_ingredient_item,
      ingredientParent,
      false
    );

    return new IngredientViewHolder(ingredientView);
  }


  @Override
  public void onClick(View v) {

    //TODO make a if else or a switch case for if action is new, then empty circle and select
    // unselect
    //switch (case)

    // if the user wants to unselect the symptom, the color will be red
    // selected color is red
    boolean userWantsToUnSelect = mIngredientItemView.getTextColors() == mSelectedColor;

    //change text color to show it was unclicked, change back to unselected color
    if (userWantsToUnSelect) {
      mIngredientItemView.setTextColor(mUnSelectedColor);
      mIngredientItemButton.setBackground(mGreenRoundcornersDrawable);
      mIngredientItemButton.setImageDrawable(mEmptyCircleDrawable);
    } else {
      //change UI to show it was clicked
      // text color change to red
      mIngredientItemView.setTextColor(mSelectedColor);
      // change the empty circle to the sick face
      mIngredientItemButton.setImageDrawable(mFoodBeverageDrawable);
      // make the background of the sick face from a green circle to a red circle
      mIngredientItemButton.setBackground(null);

    }

    //TODO make this work in the individual edit ingredient list
    //TODO this is the else do this if its three dots action list all ingredients
//    ingredientItemOptionButton.setOnClickListener(v -> {
//
//      // Initializing the popup menu and giving the reference as current mIngredientContext
//      PopupMenu popupMenu = new PopupMenu(mIngredientContext, ingredientItemOptionButton);
//
//      // Inflating popup menu from popup_menu.xml file
//      popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
//      popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem ingredientMenuItem) {
//
//          // if edit clicked
//          if ( ingredientMenuItem.getTitle().toString() == mIngredientContext.getString(R.string
//          .edit))
//          {
//            //TODO turn this into a fragment, or just a popup
//            Intent editIngredientIntent = new Intent(mIngredientContext, EditActivity.class);
//            editIngredientIntent.putExtra(Util.ARGUMENT_INGREDIENT_ID, ingredientIdString);
//            //TODO add other properties of ingredient type here
//            //editIngredientIntent.putExtra("ingredient_concern", ingredientConcern);
//            mIngredientContext.startActivity( editIngredientIntent );
//          }
//          // if delete clicked
//          else if ( ingredientMenuItem.getTitle().toString()  == mIngredientContext.getString(R
//          .string.delete ))
//          {
//            mIngredientContext.startActivity(
//                    new Intent(mIngredientContext, DetailActivity.class)
//            );
//          }
//          // if more details clicked
//          else if ( ingredientMenuItem.getTitle().toString() == mIngredientContext.getString(R
//          .string.detail) )
//          {
//            mIngredientContext.startActivity(new Intent(mIngredientContext, DetailActivity
//            .class));
//          }
//
//          return true;
//        }
//      });
//      // Showing the popup menu
//      popupMenu.show();
//    });

  }
}//end ingredient view holder class
