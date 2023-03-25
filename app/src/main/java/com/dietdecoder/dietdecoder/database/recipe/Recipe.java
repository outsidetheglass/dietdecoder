package com.dietdecoder.dietdecoder.database.recipe;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.database.Converters;

import java.util.UUID;


@Entity(tableName = "recipe_table")
@TypeConverters({Converters.class})
public class Recipe {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "recipeId")
  private UUID recipeId;

  @ColumnInfo(name = "recipeName")
  private String recipeName;

  @NonNull
  @ColumnInfo(name = "recipeIngredientId")
  private UUID recipeIngredientId;

  @ColumnInfo(name = "recipeCategory")
  private String recipeCategory;

  @Ignore
  public Recipe(UUID recipeIngredientId) {
    this(recipeIngredientId, "", "");
  }
  public Recipe(@NonNull UUID recipeIngredientId, String recipeName, String recipeCategory) {
    this.recipeId = UUID.randomUUID();
    this.recipeName = recipeName;
    this.recipeIngredientId = recipeIngredientId;
    this.recipeCategory = recipeCategory;
  }

  public UUID getRecipeId(){return recipeId;}
  public void setRecipeId(UUID id){this.recipeId = id;}

  public String getRecipeName(){return recipeName;}
  public UUID getRecipeIngredientId(){return recipeIngredientId;}
  public String getRecipeCategory(){return recipeCategory;}


  @Override
  public String toString() {
    return "Recipe{" +
      "mRecipeName='" + recipeName + '\'' +
      ", mRecipeIngredientName='" + recipeIngredientId + '\'' +
      ", mRecipeCategory='" + recipeCategory + '\'' +
      '}';
  }
} //end Recipe Entity
