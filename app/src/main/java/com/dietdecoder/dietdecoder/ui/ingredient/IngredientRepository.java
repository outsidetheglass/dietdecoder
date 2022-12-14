package com.dietdecoder.dietdecoder.ui.ingredient;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.ingredient.IngredientDao;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredient.IngredientRoomDatabase;

import java.util.List;

class IngredientRepository {



  private IngredientDao mIngredientDao;
  private final IngredientRoomDatabase mIngredientDatabase;

  IngredientRepository(Application application) {
    // setup database to be returned via methods
    mIngredientDatabase = IngredientRoomDatabase.getDatabase(application);
    mIngredientDao = mIngredientDatabase.ingredientDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  // TODO I deleted the private declaration before, but it might need it
  LiveData<List<Ingredient>> repositoryGetAllIngredients() {
    //use the dao instantiated in the IngredientRepository method
    // to get all ingredients, alphabetized
    return mIngredientDao.daoGetAlphabetizedIngredients();
  }

  // get only concern from database for ingredients
  //TODO add other properties of ingredient type here
//  LiveData<List<Ingredient>> repositoryGetIngredientsWithConcern(String concern) {
//    return mIngredientDao.daoGetIngredientsWithConcern(concern);
//  }

  // get only given ingredient using name
  public Ingredient repositoryGetIngredientFromName(String ingredientName) {
    return mIngredientDao.daoGetIngredientFromName(ingredientName);
  }
  //TODO add other properties of ingredient type here
//  public Ingredient repositoryGetIngredientFromNameConcern(String ingredientName, String ingredientConcern) {
//    return mIngredientDao.daoGetIngredientFromNameConcern(ingredientName, ingredientConcern);
//  }

  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertIngredient(Ingredient ingredient) {

    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoInsertIngredient(ingredient);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteIngredient(Ingredient ingredient) {

    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoDeleteIngredient(ingredient);
    });

  } // end delete

//TODO add other properties of ingredient type here
//  void repositoryUpdateConcern(String oldIngredientName, String oldIngredientConcern, String newIngredientConcern) {
//
//    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
//      mIngredientDao.daoUpdateConcern(oldIngredientName, oldIngredientConcern, newIngredientConcern);
//    });
//
//  } // end update concern

  void repositoryUpdateIngredientName(String oldIngredientName, String oldIngredientConcern, String newIngredientName) {

    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoUpdateIngredientName(oldIngredientName, oldIngredientConcern, newIngredientName);
    });

  } // end update concern



} //end IngredientRepository class
