package com.dietdecoder.dietdecoder.database.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
import java.util.UUID;

@Dao
public interface RecipeDao {

  // allowing the insert multiple times by passing a conflict resolution strategy
  // choosing ignore because recipes may seem the same but will have different brands, etc
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoRecipeInsert(Recipe recipe);

  //TODO fix updates and delete
//  @Query("DELETE FROM recipe_table WHERE recipeName = :recipeName AND recipeIngredientName = :recipeIngredientName")
//  void daoRecipeDelete(String recipeName, String recipeIngredientName);

//  @Query("UPDATE recipe_table SET recipeIngredientId = :newRecipeIngredientName WHERE " +
//          "recipeIngredientId = :oldRecipeIngredientName AND recipeName = :oldRecipeName")
//  void daoRecipeUpdateIngredient(String oldRecipeName, String oldRecipeIngredientName, String newRecipeIngredientName);

//  @Query("UPDATE recipe_table SET recipeName = :newRecipeName WHERE recipeName = :oldRecipeName AND recipeIngredientName = :oldRecipeIngredientName")
//  void daoRecipeUpdateName(String oldRecipeName, String oldRecipeIngredientName, String newRecipeName);


  // LiveData is a lifecycle library class for live database access
  // Select all recipes from table and alphabetize them by name
  @Query("SELECT * FROM recipe_table ORDER BY recipeName ASC")
  LiveData<List<Recipe>> daoGetAlphabetizedRecipes();


  // Sort by specific ingredient and alphabetize them by recipe name
  @Query("SELECT * FROM recipe_table WHERE recipeIngredientId LIKE :daoIngredientId ORDER BY " +
          "recipeName ASC")
  List<Recipe> daoGetRecipesWithIngredient(UUID daoIngredientId);

  @Query("SELECT * FROM recipe_table WHERE recipeName = :daoRecipeName")
  List<Recipe> daoGetAllRecipeFromName(String daoRecipeName);

  @Query("SELECT * FROM recipe_table WHERE recipeCategory = :daoRecipeCategory")
  List<Recipe> daoGetAllRecipeFromCategory(String daoRecipeCategory);


  @Query("SELECT * FROM recipe_table WHERE recipeName = :daoRecipeName")
  Recipe daoGetRecipeFromName(String daoRecipeName);

  @Query("SELECT * FROM recipe_table WHERE recipeId = :daoRecipeId")
  Recipe daoGetRecipeFromId(UUID daoRecipeId);



  @Query("SELECT * FROM recipe_table WHERE recipeName = :daoRecipeName AND recipeIngredientId = " +
          ":daoRecipeIngredientId")
  Recipe daoGetRecipeFromRecipeNameAndIngredient(String daoRecipeName,
                                                   UUID daoRecipeIngredientId);

}
