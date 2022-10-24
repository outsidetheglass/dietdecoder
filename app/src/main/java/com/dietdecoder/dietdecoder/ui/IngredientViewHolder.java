package com.dietdecoder.dietdecoder.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;

public class IngredientViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  public TextView ingredientItemView;
  public Button deleteIngredientButton;


  private IngredientViewHolder(View itemView) {
    super(itemView);
    ingredientItemView = itemView.findViewById(R.id.textview_ingredient);
    deleteIngredientButton = itemView.findViewById(R.id.delete_button_ingredient);
  }


  public void bind(String text) {
    ingredientItemView.setText(text);
    ingredientItemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(itemView.getContext(), "This is: " + text, Toast.LENGTH_SHORT).show();

      }
    });
  }


  static IngredientViewHolder create(ViewGroup parent) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.recyclerview_ingredient_item, parent, false);

    return new IngredientViewHolder(view);
  }



}//end ingredient view holder class
