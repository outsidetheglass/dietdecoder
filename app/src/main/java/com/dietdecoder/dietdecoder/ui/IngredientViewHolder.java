package com.dietdecoder.dietdecoder.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;

public class IngredientViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  private final TextView ingredientItemView;


  private IngredientViewHolder(View itemView) {
    super(itemView);

    ingredientItemView = itemView.findViewById(R.id.textview_ingredient);

  }


  public void bind(String text) {
    ingredientItemView.setText(text);
  }


  static IngredientViewHolder create(ViewGroup parent) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.recyclerview_ingredient_item, parent, false);

    return new IngredientViewHolder(view);
  }



}
