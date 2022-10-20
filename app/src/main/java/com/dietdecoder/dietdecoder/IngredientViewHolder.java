package com.dietdecoder.dietdecoder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class IngredientViewHolder extends RecyclerView.ViewHolder {



  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();

  // to set the text for what shows up in the UI
  private final TextView ingredientItemView;

  // to set button listener
  private IngredientListAdapter mIngredientListAdapter;

  /*TODO remove these if tutorial on adding button works
  private final Button ingredientDeleteButton;*/


  private IngredientViewHolder(View itemView) {
    super(itemView);

    ingredientItemView = itemView.findViewById(R.id.textView_ingredients);
    /*TODO remove this if tutorial on adding button works
    ingredientDeleteButton = itemView.findViewById(R.id.button_delete_ingredient);*/

    //add button listener
    //itemView.setOnClickListener(this);
  }


  public void bind(String text) {
    ingredientItemView.setText(text);
// All this probably wrong, TODO delete this if I get button working
//    ingredientItemView.setOnClickListener(view -> {
//       //TODO: this is probably where to set on click the text make delete button appear
//      //ingredientDeleteButton.
//      // TODO: add toast that asks if they're sure they want to delete
//      /*
//      Intent replyIntent = new Intent();if (TextUtils.isEmpty(mEditIngredientView.getText())) {
//        setResult(RESULT_CANCELED, replyIntent);
//      } else {
//        String ingredient = mEditIngredientView.getText().toString();
//        replyIntent.putExtra(EXTRA_REPLY, ingredient);
//        setResult(RESULT_OK, replyIntent);
//      }
//      finish();*/
//    });
  }

/*
  @Override
  public void onClick(View view) {
    int position = getAdapterPosition();
    mIngredientListAdapter.mOnClickListener.onListItemClick(position);
  }*/


  static IngredientViewHolder create(ViewGroup parent) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.recyclerview_item, parent, false);

    return new IngredientViewHolder(view);
  }



}
