package com.dietdecoder.dietdecoder.ui.foodlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLogDao;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredient.IngredientDao;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class FoodLogRepository {



  private FoodLogDao mFoodLogDao;
//  private IngredientDao mIngredientDao;
  private final DietDecoderRoomDatabase mFoodLogDatabase;

  FoodLogRepository(Application application) {
    // setup database to be returned via methods
    mFoodLogDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mFoodLogDao = mFoodLogDatabase.foodLogDao();
//    mIngredientDao = mFoodLogDatabase.ingredientDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  public LiveData<List<FoodLog>> repositoryGetAllFoodLogs() {
    //use the dao instantiated in the FoodLogRepository method
    // to get all logs, alphabetized
    return mFoodLogDao.daoGetAllFoodLog();
  }


  // get list of logs that are on a certain date
  public List<FoodLog> repositoryGetAllFoodLogOnDate(Instant instant) {
    return mFoodLogDao.daoGetAllFoodLogOnDate(instant);
  }

//
//  public List<Ingredient> repositoryGetAllFoodLogIngredients(List<FoodLog> foodLogs) {
//    List<Ingredient> foodLogIngredients = null;
//    // for each food log we have
//    //TODO use SQL in the DAO to get all the ingredients matching any of the given foodlog ids
//    for (FoodLog foodLog: foodLogs) {
////      find the ingredient matching that food log's ingredient ID
//      Ingredient ingredient =
//              mIngredientDao.daoGetFoodLogIngredientFromId(foodLog.getIngredientId());
//      // if we got an ingredient matching that ID
//      if (!Objects.isNull(ingredient))  {
//        // then add it to our list of ingredients to return
//        foodLogIngredients.add(ingredient);
//      }
//    }
//    return foodLogIngredients;
//  }

  // get single log that is on a certain date
  public FoodLog repositoryGetFoodLogFromConsumedInstant(Instant instant) {
    return mFoodLogDao.daoGetFoodLogFromConsumedInstant(instant);
  }

  // get single log that from uuid
  public FoodLog repositoryGetFoodLogFromId(UUID uuid) {
    return mFoodLogDao.daoGetFoodLogFromId(uuid);
  }

  // get all logs happening after a certain date
  public List<FoodLog> repositoryGetAllFoodLogAfterDateTime(Instant instant) {
    return mFoodLogDao.daoGetAllFoodLogAfterDateTime(instant);
  }

  // get all logs cursor
  public Cursor repositoryGetCursorAllFoodLog() {
    return mFoodLogDao.getCursorAllFoodLog();
  }



  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertFoodLog(FoodLog foodLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mFoodLogDao.daoFoodLogInsert(foodLog);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteFoodLog(FoodLog foodLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mFoodLogDao.daoFoodLogDelete(foodLog);
    });

  } // end delete


  void repositoryUpdateFoodLog(FoodLog foodLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mFoodLogDao.daoFoodLogUpdate(foodLog);
    });

  } // end update



} //end FoodLogRepository class
