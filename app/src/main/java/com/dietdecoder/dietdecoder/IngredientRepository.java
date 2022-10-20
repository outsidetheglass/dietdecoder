package com.dietdecoder.dietdecoder;

import android.app.Application;

import androidx.lifecycle.LiveData;

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
  LiveData<List<Ingredient>> repositoryGetIngredientsWithConcern(String concern) {
    return mIngredientDao.daoGetIngredientsWithConcern(concern);
  }

  // get only given ingredient using name
  public Ingredient repositoryGetIngredientFromName(String ingredientName) {
    return mIngredientDao.daoGetIngredientFromName(ingredientName);
  }


  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsert(Ingredient ingredient) {

    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoInsert(ingredient);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDelete(Ingredient ingredient) {

    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoDelete(ingredient);
    });

  } // end delete


  void repositoryUpdate(Ingredient ingredient) {

    IngredientRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoUpdate(ingredient);
    });

  } // end update



} //end IngredientRepository class
