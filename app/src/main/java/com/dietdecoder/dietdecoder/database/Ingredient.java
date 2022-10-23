package com.dietdecoder.dietdecoder.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "ingredient_table")
public class Ingredient {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "ingredientName")
  private String mIngredientName;

  @ColumnInfo(name = "ingredientConcern")
  private String mIngredientConcern;

  public Ingredient(@NonNull String ingredientName,
                          String ingredientConcern) {
    this.mIngredientName = ingredientName;
    this.mIngredientConcern = ingredientConcern;
  }

  public String getIngredientName(){return this.mIngredientName;}

  public String getIngredientConcern(){return this.mIngredientConcern;}

} //end Ingredient Entity
