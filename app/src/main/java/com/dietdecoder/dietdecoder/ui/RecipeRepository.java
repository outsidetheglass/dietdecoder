package com.dietdecoder.dietdecoder.ui;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.Recipe;
import com.dietdecoder.dietdecoder.database.RecipeDao;
import com.dietdecoder.dietdecoder.database.RecipeRoomDatabase;

import java.util.List;

class RecipeRepository {



  private RecipeDao mRecipeDao;
  private final RecipeRoomDatabase mRecipeDatabase;

  RecipeRepository(Application applicationRecipe) {
    // setup database to be returned via methods
    mRecipeDatabase = RecipeRoomDatabase.getRecipeDatabase(applicationRecipe);
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
  LiveData<List<Recipe>> repositoryGetRecipesWithIngredient(String ingredient) {
    return mRecipeDao.daoGetRecipesWithIngredient(ingredient);
  }

  // get only given recipe using name
  public Recipe repositoryGetRecipeFromName(String recipeName) {
    return mRecipeDao.daoGetRecipeFromName(recipeName);
  }

  public Recipe repositoryGetRecipeFromNameIngredient(String recipeName, String recipeIngredient) {
    return mRecipeDao.daoGetRecipeFromNameIngredient(recipeName, recipeIngredient);
  }

  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryRecipeInsert(Recipe recipe) {

    RecipeRoomDatabase.recipeDatabaseWriteExecutor.execute(() -> {
      mRecipeDao.daoRecipeInsert(recipe);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryRecipeDelete(String recipeName, String recipeIngredient) {

    RecipeRoomDatabase.recipeDatabaseWriteExecutor.execute(() -> {
      mRecipeDao.daoRecipeDelete(recipeName, recipeIngredient);
    });

  } // end delete


  void repositoryRecipeUpdateIngredient(String oldRecipeName, String oldRecipeIngredient, String newRecipeIngredient) {

    RecipeRoomDatabase.recipeDatabaseWriteExecutor.execute(() -> {
      mRecipeDao.daoRecipeUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
    });

  } // end update ingredient

  void repositoryRecipeUpdateName(String oldRecipeName, String oldRecipeIngredient, String newRecipeName) {

    RecipeRoomDatabase.recipeDatabaseWriteExecutor.execute(() -> {
      mRecipeDao.daoRecipeUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
    });

  } // end update ingredient



} //end RecipeRepository class
