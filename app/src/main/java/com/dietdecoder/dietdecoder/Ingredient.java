package com.dietdecoder.dietdecoder;

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

  @ColumnInfo(name = "ingredientChemical")
  private String mIngredientChemical;

  public Ingredient(@NonNull String ingredientName,
                          String ingredientChemical) {
    this.mIngredientName = ingredientName;
    this.mIngredientChemical = ingredientChemical;
  }

  public String getIngredientName(){return this.mIngredientName;}

  public String getIngredientChemical(){return this.mIngredientChemical;}

} //end Ingredient Entity
