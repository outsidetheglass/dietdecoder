package com.dietdecoder.dietdecoder.ui.ingredient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.ingredient.DeleteIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.DetailIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.EditIngredientActivity;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;

public class IngredientViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView ingredientItemView;
  public ImageButton ingredientItemOptionButton;
  private Context ingredientContext;

  private IngredientViewHolder(View itemView) {
    super(itemView);
    ingredientContext = itemView.getContext();
    ingredientItemView = itemView.findViewById(R.id.textview_ingredient_item);
    ingredientItemOptionButton = itemView.findViewById(R.id.imagebutton_ingredient_option);
  }


  public void bind(Ingredient ingredient) {

    //TODO if I ever make ids this will be easier and should change
    String ingredientName = ingredient.getIngredientName();
    //TODO add other properties of ingredient type here
    //String ingredientConcern = ingredient.getIngredientConcern();

    ingredientItemView.setText(ingredientName);

    ingredientItemOptionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        // Initializing the popup menu and giving the reference as current ingredientContext
        PopupMenu popupMenu = new PopupMenu(ingredientContext, ingredientItemOptionButton);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.item_options_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem ingredientMenuItem) {

            // if edit clicked
            if ( ingredientMenuItem.getTitle().toString() == ingredientContext.getString(R.string.edit))
            {
              //TODO turn this into a fragment, or just a popup
              Intent editIngredientIntent = new Intent(ingredientContext, EditIngredientActivity.class);
              editIngredientIntent.putExtra("ingredient_name", ingredientName);
              //TODO add other properties of ingredient type here
              //editIngredientIntent.putExtra("ingredient_concern", ingredientConcern);
              ingredientContext.startActivity( editIngredientIntent );
            }
            // if delete clicked
            else if ( ingredientMenuItem.getTitle().toString()  == ingredientContext.getString(R.string.delete ))
            {
              ingredientContext.startActivity(
                new Intent(ingredientContext, DeleteIngredientActivity.class)
              );
            }
            // if more details clicked
            else if ( ingredientMenuItem.getTitle().toString() == ingredientContext.getString(R.string.detail) )
            {
              ingredientContext.startActivity(new Intent(ingredientContext, DetailIngredientActivity.class));
            }

            return true;
          }
        });
        // Showing the popup menu
        popupMenu.show();
      }
    });

  }


  static IngredientViewHolder create(ViewGroup ingredientParent) {

    Context ingredientContext = ingredientParent.getContext();
    LayoutInflater ingredientInflater = LayoutInflater.from(ingredientContext);
    View ingredientView = ingredientInflater.inflate(
      R.layout.recyclerview_ingredient_item,
      ingredientParent,
      false
    );

    return new IngredientViewHolder(ingredientView);
  }


}//end ingredient view holder class
