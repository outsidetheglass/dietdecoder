//package com.dietdecoder.dietdecoder.ui.ingredient;
//
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.content.res.Resources;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.dietdecoder.dietdecoder.R;
//import com.dietdecoder.dietdecoder.Util;
//import com.dietdecoder.dietdecoder.activity.ingredientlog.ChooseIngredientActivity;
//import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//
//
//  // make a TAG to use to log errors
//  private final String TAG = "TAG: " + getClass().getSimpleName();
//
//  // to set the text for what shows up in the UI
//  public TextView mIngredientItemView;
//  private static final int mTextViewChooseInt = R.id.textview_choose_ingredient_item;
//  private static final int mTextViewListInt = R.id.textview_list_ingredient_item;
//
//  public ImageButton mButtonCircle, mButtonMore;
//  public ListView listView;
//  private Context mIngredientViewHolderContext;
//  private Resources.Theme mIngredientViewHolderTheme;
//  ColorStateList mSelectedColor;
//
//  int mUnSelectedColor, mTextViewItemInt, mButtonMoreInt,  mButtonItemInt, mButtonCircleInt;
////  private static final int mButtonMoreListInt = R.id.imagebutton_list_ingredient_more;
////  private static final int mButtonCircleListInt = R.id.imagebutton_list_ingredient_circle;
////  private static final int mButtonCircleChooseInt = R.id.imagebutton_choose_ingredient_option_circle;
//
//  Drawable mEmptyCircleDrawable, mGreenRoundcornersDrawable, mFoodBeverageDrawable;
//
//  String mIngredientName, mIngredientIdString, mIngredientChemicalName,
//  mIngredientChemicalAmountUnit, mIngredientBrand, mIngredientChemicalAmountNumberString;
//  Double mIngredientChemicalAmountNumber;
//  Ingredient mIngredient;
//  // allow activities to access existing arraylist in the view holder
//  private static ArrayList<Ingredient> mSelectedArrayList;
//
//  // allow activities to access existing arraylist in the view holder
//  public static ArrayList<Ingredient> viewHolderGetSelectedArrayList(){
//    return mSelectedArrayList;
//  }
//
//
//  private IngredientViewHolder(View itemView) {
//    super(itemView);
//    mIngredientViewHolderContext = itemView.getContext();
//    mSelectedArrayList = new ArrayList<>();
//
//    if (mIngredientViewHolderContext.getClass() == ChooseIngredientActivity.class) {
//      Log.d(TAG, "choose ingredient");
//      mTextViewItemInt = R.id.textview_choose_ingredient_item;
//      mButtonItemInt = R.id.imagebutton_choose_ingredient_circle;
//    } else {
//      Log.d(TAG, "list ingredient");
//      mTextViewItemInt = R.id.textview_list_ingredient_item;
//      mButtonItemInt = R.id.imagebutton_list_ingredient_more;
//    }
//    // TODO bind the search bar to the view holder, then make the ingredient in bind invisible if
//    //  its name doesn't match what's typed in the search bar
//    mIngredientItemView = itemView.findViewById(mTextViewItemInt);
//    mButtonMore = itemView.findViewById(mButtonMoreInt);
//    mButtonCircle = itemView.findViewById(mButtonCircleInt);
//
//    mIngredientViewHolderTheme = mIngredientViewHolderContext.getTheme();
//    mSelectedColor = itemView.getResources().getColorStateList(R.color.selected_text_color,
//            mIngredientViewHolderTheme);
//    // green color of titles, not the default text color I want but close enough
//    TypedValue typedValue = new TypedValue();
//    mIngredientViewHolderTheme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true);
//    mUnSelectedColor = typedValue.data;
////    mUnSelectedColor = itemView.getResources().getColorStateList(color.,
////            mIngredientViewHolderTheme);
//
//    // drawables for the selected and unselected options and their backgrounds
//    mFoodBeverageDrawable =
//            itemView.getResources().getDrawable(R.drawable.ic_baseline_emoji_food_beverage_24,
//            mIngredientViewHolderTheme);
//    mGreenRoundcornersDrawable =
//            itemView.getResources().getDrawable(R.drawable.roundcorners,
//                    mIngredientViewHolderTheme);
//    mEmptyCircleDrawable =
//            itemView.getResources().getDrawable(R.drawable.ic_baseline_empty_circle,
//                    mIngredientViewHolderTheme);
//    //TODO get list ingredient as type new food log working
////    listView = itemView.findViewById(R.id.list_view_new_food_log_name);
//  }
//
//
//  public void bind(Ingredient ingredient) {
//
//    this.mIngredient = ingredient;
//
//    // set all the parts of ingredient log to variables
//    setVariables();
//
//    Log.d(TAG, "mIngredientName: " + mIngredientName);
//    mIngredientItemView.setText(mIngredientName);
//
//    //TODO make this an if statement, if we're here to edit individual ingredients from a list of
//    // them then it should show the options three dots and set on click listener to make a menu
//    // of edit details delete and show this if we're here from list ingredient to make a new
//    // ingredient log
//    //if (ACTION == ACTION_NEW) then show empty circle and select unselect with text
//    // else show three dot options
//    mButtonCircle.setOnClickListener(this::onClick);
//
//
////
//    //TODO get add food log search ingredient database and autofill while user typing working
//    //listView.setOnClickListener(new View.OnClickListener() {
////      @Override
////      public void onClick(View v) {
////
////        Intent mIntent = new Intent(mIngredientContext, NewFoodLogActivity.class);
////        mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
////                Util.ARGUMENT_GO_TO_NAME);
////
////        // TODO figure out a better way to do listview
////        // if can't, then just do this. Go back to add name with chosen name
////        mIntent.putExtra(Util.ARGUMENT_INGREDIENT_ID, ingredientIdString);
////        mIntent.putExtra(Util.ARGUMENT_INGREDIENT_NAME, ingredientName);
////
////        mIngredientContext.startActivity(new Intent());
////      }
////    });
//  }
//
//  private void setVariables() {
//    // TODO bind the search bar to the view holder, then make the ingredient in bind invisible if
//    //  its name doesn't match what's typed in the search bar
//    mIngredientName = mIngredient.getName();
//    mIngredientIdString = mIngredient.getId().toString();
//
//    if ( !Objects.isNull(mIngredient.getBrand()) ) {
//      mIngredientBrand = mIngredient.getBrand();
//
//      }
//  }
//
//  static IngredientViewHolder create(ViewGroup ingredientParent) {
//
//    Context mIngredientContext = ingredientParent.getContext();
//    LayoutInflater ingredientInflater = LayoutInflater.from(mIngredientContext);
//    View ingredientView = ingredientInflater.inflate(
//      R.layout.recyclerview_choose_ingredient_item,
//      ingredientParent,
//      false
//    );
//
//    return new IngredientViewHolder(ingredientView);
//  }
//
//  private void chooseItemButton (){
//
//    // if the user wants to unselect, the color will be red
//    // selected color is red
//    boolean userWantsToUnSelect = mIngredientItemView.getTextColors() == mSelectedColor;
//
//    //change text color to show it was unclicked, change back to unselected color
//    if (userWantsToUnSelect) {
//      mIngredientItemView.setTextColor(mUnSelectedColor);
//      mButtonCircle.setBackground(mGreenRoundcornersDrawable);
//      mButtonCircle.setImageDrawable(mEmptyCircleDrawable);
//      // the object was selected before this, so the array list has it and needs it removed
//      mSelectedArrayList.remove(mIngredient);
//    } else {
//      //change UI to show it was clicked
//      // text color change to red
//      mIngredientItemView.setTextColor(mSelectedColor);
//      // change the empty circle to the sick face
//      mButtonCircle.setImageDrawable(mFoodBeverageDrawable);
//      // make the background of the sick face from a green circle to a red circle
//      mButtonCircle.setBackground(null);
//
//      // allow activities to access existing arraylist in the view holder
//      // on click and the user is selecting it
//      if (mSelectedArrayList == null) {
//        mSelectedArrayList = new ArrayList<>();
//      }
//      mSelectedArrayList.add(mIngredient);
//    }
//  }
//
//  private void moreItemButton(){
//
//    // Initializing the popup menu and giving the reference as current logContext
//    PopupMenu popupMenu = new PopupMenu(mIngredientViewHolderContext, mIngredientItemView);
//    // Inflating popup menu from popup_menu.xml file
//    popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
//    popupMenu.setGravity(Gravity.END);
//    // if an option in the menu is clicked
//    popupMenu.setOnMenuItemClickListener(menuItem -> {
//      // which button was clicked
//      switch (menuItem.getItemId()) {
//
//        // go to the right activity, edit or delete or details,
//        // and then the action to take is either duplicate, edit, or delete
//        // and go with the ID array string of the object
//        case R.id.duplicate_option:
//          // edit fragment checks for if we're a duplicate or not for what to set
//          Util.goToEditActivityActionTypeId(mIngredientViewHolderContext, null,
//                  Util.ARGUMENT_ACTION_DUPLICATE,
//                  Util.ARGUMENT_INGREDIENT_ID_ARRAY,  mIngredientIdString);
//          break;
//
//        case R.id.edit_option:
//          // tell the edit activity we want the full edit fragment
//
//          //Log.d(TAG, " edit imagebutton_list_ingredient_option: " + mIngredientIdString);
//          Util.goToAddEditIngredientActivity(mIngredientViewHolderContext, null,
//                  mIngredientIdString);
//          break;
//
//        case R.id.delete_option:
//          // delete this log, go activity double checking if they want to
//          Util.goToDetailActivity(mIngredientViewHolderContext, Util.ARGUMENT_ACTION_DELETE,
//                  Util.ARGUMENT_INGREDIENT_ID_ARRAY, mIngredientIdString);
//          break;
//
//        case R.id.detail_option:
//          Util.goToDetailActivity(mIngredientViewHolderContext, Util.ARGUMENT_ACTION_DETAIL,
//                  Util.ARGUMENT_INGREDIENT_ID_ARRAY, mIngredientIdString);
//          break;
//
//        default:
//          break;
//      }//end switch case for which menu item was chosen
//
////      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, ingredientLogIdString);
////      ingredientLogContext.startActivity(mIntent);
//
//      return true;
//    });
//    // Showing the popup menu
//    popupMenu.show();
//
//  }
//
//  @Override
//  public void onClick(View view) {
//
//    // which button was clicked
//    switch (view.getId()) {
//
//      case R.id.imagebutton_choose_ingredient_circle:
//        chooseItemButton();
//        break;
//
//      case R.id.imagebutton_list_ingredient_more:
//        moreItemButton();
//        break;
//
//      default:
//        break;
//    }//end switch case
//  }
//}//end ingredient view holder class
