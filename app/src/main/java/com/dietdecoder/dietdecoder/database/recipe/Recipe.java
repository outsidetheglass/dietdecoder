package com.dietdecoder.dietdecoder.database.recipe;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "recipe_table")
public class Recipe {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "recipeName")
  private String mRecipeName;

  @ColumnInfo(name = "recipeIngredient")
  private String mRecipeIngredient;

  public Recipe(@NonNull String recipeName,
                String recipeIngredient) {
    this.mRecipeName = recipeName;
    this.mRecipeIngredient = recipeIngredient;
  }

  public String getRecipeName(){return this.mRecipeName;}

  public String getRecipeIngredient(){return this.mRecipeIngredient;}

} //end Recipe Entity
