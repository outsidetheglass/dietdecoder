package com.dietdecoder.dietdecoder.ui.recipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();

  // to set the text for what shows up in the UI
  private final TextView recipeItemView;


  private RecipeViewHolder(View itemViewRecipe) {
    super(itemViewRecipe);
    recipeItemView = itemViewRecipe.findViewById(R.id.textview_recipe_item);
  }


  public void bind(String textRecipe) {
    Log.d(TAG, "bind: "+ textRecipe);

    recipeItemView.setText(textRecipe);

  }


  static RecipeViewHolder create(ViewGroup parentRecipe) {
    Context contextRecipe = parentRecipe.getContext();
    LayoutInflater inflaterRecipe = LayoutInflater.from(contextRecipe);
    View viewRecipe = inflaterRecipe.inflate(R.layout.recyclerview_recipe_item, parentRecipe, false);

    return new RecipeViewHolder(viewRecipe);
  }



}
