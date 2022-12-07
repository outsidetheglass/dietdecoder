package com.dietdecoder.dietdecoder.database.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

  // allowing the insert multiple times by passing a conflict resolution strategy
  // choosing ignore because recipes may seem the same but will have different brands, etc
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoRecipeInsert(Recipe recipe);

  @Query("DELETE FROM recipe_table WHERE recipeName = :recipeName AND recipeIngredientName = :recipeIngredientName")
  void daoRecipeDelete(String recipeName, String recipeIngredientName);

  @Query("UPDATE recipe_table SET recipeIngredientName = :newRecipeIngredientName WHERE recipeIngredientName = :oldRecipeIngredientName AND recipeName = :oldRecipeName")
  void daoRecipeUpdateIngredient(String oldRecipeName, String oldRecipeIngredientName, String newRecipeIngredientName);

  @Query("UPDATE recipe_table SET recipeName = :newRecipeName WHERE recipeName = :oldRecipeName AND recipeIngredientName = :oldRecipeIngredientName")
  void daoRecipeUpdateName(String oldRecipeName, String oldRecipeIngredientName, String newRecipeName);


  // LiveData is a lifecycle library class for live database access
  // Select all recipes from table and alphabetize them by name
  @Query("SELECT * FROM recipe_table ORDER BY recipeName ASC")
  LiveData<List<Recipe>> daoGetAlphabetizedRecipes();


  // Sort by specific ingredient and alphabetize them by recipe name
  @Query("SELECT * FROM recipe_table WHERE recipeIngredientName LIKE :daoIngredient ORDER BY recipeName ASC")
  List<Recipe> daoGetRecipesWithIngredient(String daoIngredient);

  @Query("SELECT * FROM recipe_table WHERE recipeName = :daoRecipeName")
  List<Recipe> daoGetAllRecipeFromName(String daoRecipeName);


  @Query("SELECT * FROM recipe_table WHERE recipeName = :daoRecipeName")
  Recipe daoGetRecipeFromName(String daoRecipeName);

  @Query("SELECT * FROM recipe_table WHERE recipeName = :daoRecipeName AND recipeIngredientName = :daoRecipeIngredient")
  Recipe daoGetRecipeFromRecipeNameAndIngredient(String daoRecipeName, String daoRecipeIngredient);

}
