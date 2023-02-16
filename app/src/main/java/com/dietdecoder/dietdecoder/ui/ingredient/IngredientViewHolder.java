package com.dietdecoder.dietdecoder.ui.ingredient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.foodlog.EditFoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.NewFoodLogNameFragment;
import com.dietdecoder.dietdecoder.activity.ingredient.DeleteIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.DetailIngredientActivity;
import com.dietdecoder.dietdecoder.activity.ingredient.EditIngredientActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

public class IngredientViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView ingredientItemView;
  public ImageButton ingredientItemOptionButton;
  public ListView listView;
  private Context ingredientContext;

  private IngredientViewHolder(View itemView) {
    super(itemView);
    ingredientContext = itemView.getContext();
    ingredientItemView = itemView.findViewById(R.id.textview_ingredient_item);
    ingredientItemOptionButton = itemView.findViewById(R.id.imagebutton_ingredient_option);
    listView = itemView.findViewById(R.id.list_view_new_food_log_name);
  }


  public void bind(Ingredient ingredient) {

    String ingredientName = ingredient.getIngredientName();
    String ingredientIdString = ingredient.getMIngredientId().toString();
    //TODO add other properties of ingredient type here
    //String ingredientConcern = ingredient.getIngredientConcern();

    ingredientItemView.setText(ingredientName);

    ingredientItemOptionButton.setOnClickListener(v -> {

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
    });

    listView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent mIntent = new Intent(ingredientContext, NewFoodLogActivity.class);
        mIntent.putExtra(Util.ARGUMENT_FRAGMENT_GO_TO,
                Util.ARGUMENT_GO_TO_NAME);

        // TODO figure out a better way to do listview
        // if can't, then just do this. Go back to add name with chosen name
        mIntent.putExtra(Util.ARGUMENT_INGREDIENT_ID, ingredientIdString);
        mIntent.putExtra(Util.ARGUMENT_INGREDIENT_NAME, ingredientName);

        ingredientContext.startActivity(new Intent());
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
