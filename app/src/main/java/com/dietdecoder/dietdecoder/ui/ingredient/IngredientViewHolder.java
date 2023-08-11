package com.dietdecoder.dietdecoder.ui.ingredient;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;

import java.util.ArrayList;
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

  String mIngredientName, mIngredientIdString, mIngredientChemicalName,
  mIngredientChemicalAmountUnit, mIngredientBrand, mIngredientChemicalAmountNumberString;
  Double mIngredientChemicalAmountNumber;
  Ingredient mIngredient;
  // allow activities to access existing arraylist in the view holder
  private static ArrayList<Ingredient> mSelectedArrayList;

  // allow activities to access existing arraylist in the view holder
  public static ArrayList<Ingredient> viewHolderGetSelectedArrayList(){
    return mSelectedArrayList;
  }


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

    this.mIngredient = ingredient;

    // set all the parts of ingredient log to variables
    setVariables();

    mIngredientItemView.setText(mIngredientName);

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

  private void setVariables() {
    // TODO bind the search bar to the view holder, then make the ingredient in bind invisible if
    //  its name doesn't match what's typed in the search bar
    mIngredientName = mIngredient.getIngredientName();
    mIngredientIdString = mIngredient.getIngredientId().toString();
    // if the other values exist, list them
    if ( !Objects.isNull(mIngredient.getIngredientChemicalName()) ) {
      mIngredientChemicalName = mIngredient.getIngredientChemicalName().toString();


      if ( !Objects.isNull(mIngredient.getIngredientChemicalAmountNumber()) ) {
        mIngredientChemicalAmountNumber = mIngredient.getIngredientChemicalAmountNumber();
        mIngredientChemicalAmountNumberString = mIngredientChemicalAmountNumber.toString();

      }
      if ( !Objects.isNull(mIngredient.getIngredientChemicalAmountUnit()) ) {
        mIngredientChemicalAmountUnit = mIngredient.getIngredientChemicalAmountUnit().toString();

      }
    }
    if ( !Objects.isNull(mIngredient.getIngredientBrand()) ) {
      mIngredientBrand = mIngredient.getIngredientBrand();

    }
  }

  static IngredientViewHolder create(ViewGroup ingredientParent) {

    Context mIngredientContext = ingredientParent.getContext();
    LayoutInflater ingredientInflater = LayoutInflater.from(mIngredientContext);
    View ingredientView = ingredientInflater.inflate(
      R.layout.recyclerview_choose_ingredient_item,
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
      // the object was selected before this, so the array list has it and needs it removed
      mSelectedArrayList.remove(mIngredient);
    } else {
      //change UI to show it was clicked
      // text color change to red
      mIngredientItemView.setTextColor(mSelectedColor);
      // change the empty circle to the sick face
      mIngredientItemButton.setImageDrawable(mFoodBeverageDrawable);
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
}//end ingredient view holder class
