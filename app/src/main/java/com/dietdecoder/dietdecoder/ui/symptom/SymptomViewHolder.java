package com.dietdecoder.dietdecoder.ui.symptom;

import static com.dietdecoder.dietdecoder.R.*;

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

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.symptomlog.ChooseSymptomActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.ArrayList;

public class SymptomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView mSymptomItemView;
  public ImageButton mSymptomItemButton;
  private Context mSymptomViewHolderContext;
  private Resources.Theme mSymptomViewHolderTheme;
  ColorStateList mSelectedColor;
  int mUnSelectedColor, mLayoutItem, mTextViewItem, mButtonItem;
  Drawable mSickFaceDrawable, mRedRoundcornersBackgroundDrawable, mGreenRoundcornersDrawable, mEmptyCircleDrawable;

  String mSymptomIdString;
  public Symptom mSymptom;

  // allow activities to access existing arraylist in the view holder
  private static ArrayList<Symptom> mSelectedArrayList;


  @SuppressLint("UseCompatLoadingForDrawables")
  private SymptomViewHolder(View itemView) {
    super(itemView);
    mSymptomViewHolderContext = itemView.getContext();

    if (mSymptomViewHolderContext.getClass() == ChooseSymptomActivity.class) {
      mTextViewItem = id.textview_choose_symptom_item;
      mButtonItem = id.imagebutton_choose_symptom_option;
    } else {
      mTextViewItem = id.textview_list_symptom_item;
      mButtonItem = id.imagebutton_list_symptom_option;
    }

    mSymptomItemView = itemView.findViewById(mTextViewItem);
    mSymptomItemButton = itemView.findViewById(mButtonItem);

    mSymptomViewHolderTheme = mSymptomViewHolderContext.getTheme();
    mSelectedColor = itemView.getResources().getColorStateList(color.selected_text_color,
            mSymptomViewHolderTheme);
    // green color of titles, not the default text color I want but close enough
    TypedValue typedValue = new TypedValue();
    mSymptomViewHolderTheme.resolveAttribute(android.R.attr.colorSecondary, typedValue, true);
    mUnSelectedColor = typedValue.data;
//    mUnSelectedColor = itemView.getResources().getColorStateList(color.,
//            mSymptomViewHolderTheme);

    // drawables for the selected and unselected options and their backgrounds
    mSickFaceDrawable = itemView.getResources().getDrawable(R.drawable.ic_baseline_sick,
            mSymptomViewHolderTheme);
    mRedRoundcornersBackgroundDrawable =
            itemView.getResources().getDrawable(R.drawable.red_roundcorners,
            mSymptomViewHolderTheme);
    mGreenRoundcornersDrawable =
            itemView.getResources().getDrawable(drawable.roundcorners,
                    mSymptomViewHolderTheme);
    mEmptyCircleDrawable =
            itemView.getResources().getDrawable(drawable.ic_baseline_empty_circle,
                    mSymptomViewHolderTheme);

  }


  public void bind(Symptom symptom) {

    // listOnlyTracked is to list only the symptoms that have to track set to true

    mSymptom = symptom;
    String symptomName = symptom.getSymptomName();
    mSymptomIdString = symptom.getSymptomId().toString();
    String symptomCategory = symptom.getSymptomCategory();
    String symptomDescription = symptom.getSymptomDescription();
    String symptomSufferType = symptom.getSymptomSufferType();

    String unImportantString =
            Util.setPlainDescriptionString(symptomDescription);

    // only show useful description, not just the name repeated
    if ( TextUtils.equals(symptomName, symptomDescription) ) {
      unImportantString = "";
    }

    SpannableStringBuilder printString =
            Util.setViewHolderRecyclerViewStringNameDescription(symptomName, unImportantString );


    // set part of it bold and part of it not bold
    mSymptomItemView.setText( printString );

    mSymptomItemButton.setOnClickListener(this::onClick);


  }


  // allow activities to access existing arraylist in the view holder
  public static ArrayList<Symptom> viewHolderGetSelectedArrayList(){
    return mSelectedArrayList;
  }


  static SymptomViewHolder create(ViewGroup symptomParent) {

    Context mSymptomViewHolderContext = symptomParent.getContext();
    LayoutInflater symptomInflater = LayoutInflater.from(mSymptomViewHolderContext);

    int layoutItem = layout.recyclerview_choose_symptom_item;
    // if not choose symptom, use list symptom
    if (mSymptomViewHolderContext.getClass() != ChooseSymptomActivity.class){
      layoutItem = layout.recyclerview_list_symptom_item;
    }

    View symptomView = symptomInflater.inflate(
      layoutItem,
      symptomParent,
      false
    );

    return new SymptomViewHolder(symptomView);
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      // which button was clicked
      case R.id.imagebutton_choose_symptom_option:

        // if the user wants to unselect the symptom, the color will be red
        // selected color is red
        boolean userWantsToUnSelect = mSymptomItemView.getTextColors() == mSelectedColor;

        //change text color to show it was unclicked, change back to unselected color
        if ( userWantsToUnSelect ) {
          mSymptomItemView.setTextColor(mUnSelectedColor);
          mSymptomItemButton.setBackground(mGreenRoundcornersDrawable);
          mSymptomItemButton.setImageDrawable(mEmptyCircleDrawable);

          // the symptom was selected before this, so the array list has it and needs it removed
          mSelectedArrayList.remove(mSymptom);
        }
        else {
          //change UI to show it was clicked
          // text color change to red
          mSymptomItemView.setTextColor(mSelectedColor);
          // change the empty circle to the sick face
          mSymptomItemButton.setImageDrawable(mSickFaceDrawable);
          // make the background of the sick face from a green circle to a red circle
          mSymptomItemButton.setBackground(null);

          // allow activities to access existing arraylist in the view holder
          // on click and the user is selecting it
          if (mSelectedArrayList == null) {
            mSelectedArrayList = new ArrayList<>();
          }
          mSelectedArrayList.add(mSymptom);
        }
        break;

      case R.id.imagebutton_list_symptom_option:

        // Initializing the popup menu and giving the reference as current logContext
        PopupMenu popupMenu = new PopupMenu(mSymptomViewHolderContext, mSymptomItemView);
        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.END);
        // if an option in the menu is clicked
        popupMenu.setOnMenuItemClickListener(menuItem -> {
          // which button was clicked
          switch (menuItem.getItemId()) {

            // go to the right activity, edit or delete or details,
            // and then the action to take is either duplicate, edit, or delete
            // and go with the ID array string of the object
            case R.id.duplicate_option:
              // edit fragment checks for if we're a duplicate or not for what to set
              Util.goToEditActivityActionTypeId(mSymptomViewHolderContext, null,
                      Util.ARGUMENT_ACTION_DUPLICATE,
                      Util.ARGUMENT_SYMPTOM_ID_ARRAY,  mSymptomIdString);
              break;

            case R.id.edit_option:
              // tell the edit activity we want the full edit fragment

              Util.goToEditActivityActionTypeId(mSymptomViewHolderContext, null,
                      Util.ARGUMENT_ACTION_EDIT, Util.ARGUMENT_SYMPTOM_ID_ARRAY,
                      mSymptomIdString);
              break;

            case R.id.delete_option:
              // delete this log, go activity double checking if they want to
              Util.goToDetailActivity(mSymptomViewHolderContext, Util.ARGUMENT_ACTION_DELETE,
                      Util.ARGUMENT_SYMPTOM_ID_ARRAY, mSymptomIdString);
              break;

            case R.id.detail_option:
              Util.goToDetailActivity(mSymptomViewHolderContext, Util.ARGUMENT_ACTION_DETAIL,
                      Util.ARGUMENT_SYMPTOM_ID_ARRAY, mSymptomIdString);
              break;

            default:
              break;
          }//end switch case for which menu item was chosen

//      mIntent.putExtra(Util.ARGUMENT_FOOD_LOG_ID, symptomLogIdString);
//      symptomLogContext.startActivity(mIntent);

          return true;
        });
        // Showing the popup menu
        popupMenu.show();

        break;

      default:
        break;
    }//end switch case



  }



}//end symptom view holder class
