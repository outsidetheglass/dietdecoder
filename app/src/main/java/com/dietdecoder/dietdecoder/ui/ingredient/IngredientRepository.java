package com.dietdecoder.dietdecoder.ui.ingredient;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.ingredient.IngredientDao;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class IngredientRepository {



  private IngredientDao mIngredientDao;
  private final DietDecoderRoomDatabase mIngredientDatabase;

  IngredientRepository(Application application) {
    // setup database to be returned via methods
    mIngredientDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mIngredientDao = mIngredientDatabase.ingredientDao();
  }


  // get only given ingredient using name
  public Ingredient repositoryGetIngredientFromName(String ingredientName) {
    return mIngredientDao.daoGetIngredientFromName(ingredientName);
  }
  // get only given ingredient using name
  public Ingredient repositoryGetIngredientFromId(UUID uuid) {
    return mIngredientDao.daoGetIngredientFromId(uuid);
  }


  // get only symptoms matching category from database
  ArrayList<Ingredient> repositoryGetAllIngredientArrayList() {
    return new ArrayList<Ingredient>(mIngredientDao.daoGetAllIngredientArrayList());
  }


  public LiveData<List<Ingredient>> repositoryGetAllIngredientsMatchingSearchName(String searchIngredientName) {
    // percent sign is the SQL wildcard
    String searchableSqlString = "%" + searchIngredientName + "%";
    return mIngredientDao.daoGetAllIngredientsMatchingSearchName(searchableSqlString);
  }

  public Ingredient repositoryGetIngredientFromSearchName(String searchIngredientName) {
    return mIngredientDao.daoGetIngredientFromSearchName(searchIngredientName);
  }


  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsert(Ingredient ingredient) {
    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoInsertIngredient(ingredient);
    });
  } // end insert

  void repositoryUpdate(Ingredient ingredient) {
    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoUpdateIngredient(ingredient);
    });
  }

  // You must call this on a non-UI thread
  void repositoryDelete(Ingredient ingredient) {
    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientDao.daoDeleteIngredient(ingredient);
    });
  } // end delete

  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  // TODO I deleted the private declaration before, but it might need it
  LiveData<List<Ingredient>> repositoryGetAllIngredients() {
    //use the dao instantiated in the IngredientRepository method
    // to get all ingredients, alphabetized
    return mIngredientDao.daoGetAlphabetizedIngredients();
  }


} //end IngredientRepository class
