package com.dietdecoder.dietdecoder.ui.recipe;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.database.recipe.RecipeDao;

import java.util.List;
import java.util.UUID;

class RecipeRepository {



  private RecipeDao mRecipeDao;
  private final DietDecoderRoomDatabase mRecipeDatabase;

  RecipeRepository(Application applicationRecipe) {
    // setup database to be returned via methods
    mRecipeDatabase = DietDecoderRoomDatabase.getDatabase(applicationRecipe);
    mRecipeDao = mRecipeDatabase.recipeDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  // TODO I deleted the private declaration before, but it might need it
  LiveData<List<Recipe>> repositoryGetAllRecipes() {
    //use the dao instantiated in the RecipeRepository method
    // to get all recipes, alphabetized
    return mRecipeDao.daoGetAlphabetizedRecipes();
  }

  // get only ingredient from database for recipes
  List<Recipe> repositoryGetRecipesWithIngredient(UUID ingredientId) {
    return mRecipeDao.daoGetRecipesWithIngredient(ingredientId);
  }

  // get only given recipe using name
  public List<Recipe> repositoryGetAllRecipeFromName(String recipeName) {
    return mRecipeDao.daoGetAllRecipeFromName(recipeName);
  }



  // get only given recipe using name
  public Recipe repositoryGetRecipeFromName(String recipeName) {
    return mRecipeDao.daoGetRecipeFromName(recipeName);
  }
  public Recipe repositoryGetRecipeFromId(UUID recipeId) {
    return mRecipeDao.daoGetRecipeFromId(recipeId);
  }


  public Recipe repositoryGetRecipeFromRecipeNameAndIngredient(String recipeName, UUID recipeIngredient) {
    return mRecipeDao.daoGetRecipeFromRecipeNameAndIngredient(recipeName, recipeIngredient);
  }

  public List<Recipe> repositoryGetAllRecipeFromCategory(String recipeCategory) {
    return mRecipeDao.daoGetAllRecipeFromCategory(recipeCategory);
  }
  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryRecipeInsert(Recipe recipe) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mRecipeDao.daoRecipeInsert(recipe);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryRecipeDelete(String recipeName, String recipeIngredient) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
//      mRecipeDao.daoRecipeDelete(recipeName, recipeIngredient);
    });

  } // end delete


  void repositoryRecipeUpdateIngredient(String oldRecipeName, String oldRecipeIngredient, String newRecipeIngredient) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
//      mRecipeDao.daoRecipeUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
    });

  } // end update ingredient

  void repositoryRecipeUpdateName(String oldRecipeName, String oldRecipeIngredient, String newRecipeName) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
//      mRecipeDao.daoRecipeUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
    });

  } // end update ingredient



} //end RecipeRepository class
