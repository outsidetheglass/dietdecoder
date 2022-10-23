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

  RecipeRepository(Application application) {
    // setup database to be returned via methods
    mRecipeDatabase = RecipeRoomDatabase.getDatabase(application);
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
  void repositoryInsert(Recipe recipe) {

    RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
      mRecipeDao.daoInsert(recipe);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDelete(String recipeName, String recipeIngredient) {

    RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
      mRecipeDao.daoDelete(recipeName, recipeIngredient);
    });

  } // end delete


  void repositoryUpdateIngredient(String oldRecipeName, String oldRecipeIngredient, String newRecipeIngredient) {

    RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
      mRecipeDao.daoUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
    });

  } // end update ingredient

  void repositoryUpdateName(String oldRecipeName, String oldRecipeIngredient, String newRecipeName) {

    RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
      mRecipeDao.daoUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
    });

  } // end update ingredient



} //end RecipeRepository class
