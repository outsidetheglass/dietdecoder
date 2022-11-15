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

  @ColumnInfo(name = "recipeIngredientNames")
  private List<String> mRecipeIngredientNames;

  public Recipe(@NonNull String recipeName,
                List<String> recipeIngredientNames) {
    this.mRecipeId = UUID.randomUUID();
    this.mRecipeName = recipeName;
    this.mRecipeIngredientNames = recipeIngredientNames;
  }

  public void setmRecipeId(UUID id){this.mRecipeId = id;}

  public UUID getmRecipeId(){return this.mRecipeId;}

  public String getmRecipeName(){return this.mRecipeName;}

  public List<String> getmRecipeIngredientNames(){return this.mRecipeIngredientNames;}

} //end Recipe Entity
