package com.dietdecoder.dietdecoder.ui;

import android.content.Context;
import android.text.TextUtils;
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


  private RecipeViewHolder(View itemView) {
    super(itemView);

    recipeItemView = itemView.findViewById(R.id.textview_recipe);

  }


  public void bind(String text) {
    Log.d(TAG, "bind: "+ text);
    // if our string isn't empty, set it in view
    if (!TextUtils.isEmpty(text)) {
      recipeItemView.setText(text);
    }
  }


  static RecipeViewHolder create(ViewGroup parent) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.recyclerview_ingredient_item, parent, false);

    return new RecipeViewHolder(view);
  }



}
