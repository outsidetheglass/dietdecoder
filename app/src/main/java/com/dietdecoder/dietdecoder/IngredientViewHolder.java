package com.dietdecoder.dietdecoder;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.databinding.RecyclerviewItemBinding;

import java.util.List;

public class IngredientViewHolder extends RecyclerView.ViewHolder {


  public RecyclerviewItemBinding recyclerviewItemBinding;

  // make a TAG to use to log errors
  private final String TAG = getClass().getSimpleName();


  private IngredientViewHolder(RecyclerviewItemBinding recyclerviewItemBinding) {
    super(recyclerviewItemBinding.getRoot());
    this.recyclerviewItemBinding = recyclerviewItemBinding;

  }


  public void bind(Object object) {
    recyclerviewItemBinding.setVariable(BR.model, object);
    recyclerviewItemBinding.executePendingBindings();
  }


  static IngredientViewHolder create(ViewGroup parent) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    RecyclerviewItemBinding binding = DataBindingUtil.inflate(
      inflater,
      R.layout.recyclerview_item, parent, false);

    return new IngredientViewHolder(binding);
  }



}
