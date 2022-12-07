package com.dietdecoder.dietdecoder.database.recipe;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.database.Converters;

import java.util.List;
import java.util.UUID;


@Entity(tableName = "recipe_table")
@TypeConverters({Converters.class})
public class Recipe {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "recipeId")
  private UUID mRecipeId;

  @ColumnInfo(name = "recipeName")
  private String mRecipeName;

  @ColumnInfo(name = "recipeIngredientName")
  private String mRecipeIngredientName;

  @ColumnInfo(name = "recipeCategory")
  private String mRecipeCategory;

  public Recipe(String recipeName,
                String recipeIngredientName, String recipeCategory) {
    this.mRecipeId = UUID.randomUUID();
    this.mRecipeName = recipeName;
    this.mRecipeIngredientName = recipeIngredientName;
    this.mRecipeCategory = recipeCategory;
  }

  public UUID getMRecipeId(){return mRecipeId;}
  public void setMRecipeId(UUID id){this.mRecipeId = id;}

  public String getRecipeName(){return mRecipeName;}
  public String getRecipeIngredientName(){return mRecipeIngredientName;}
  public String getRecipeCategory(){return mRecipeCategory;}


  @Override
  public String toString() {
    return "Recipe{" +
      "mRecipeName='" + mRecipeName + '\'' +
      ", mRecipeIngredientName='" + mRecipeIngredientName + '\'' +
      ", mRecipeCategory='" + mRecipeCategory + '\'' +
      '}';
  }
} //end Recipe Entity
