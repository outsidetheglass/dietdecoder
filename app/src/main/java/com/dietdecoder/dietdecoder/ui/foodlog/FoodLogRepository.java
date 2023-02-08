package com.dietdecoder.dietdecoder.ui.foodlog;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLogRoomDatabase;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLogDao;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class FoodLogRepository {



  private FoodLogDao mFoodLogDao;
  private final FoodLogRoomDatabase mFoodLogDatabase;

  FoodLogRepository(Application application) {
    // setup database to be returned via methods
    mFoodLogDatabase = FoodLogRoomDatabase.getDatabase(application);
    mFoodLogDao = mFoodLogDatabase.foodLogDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  // TODO I deleted the private declaration before, but it might need it
  public LiveData<List<FoodLog>> repositoryGetAllFoodLogs() {
    //use the dao instantiated in the FoodLogRepository method
    // to get all logs, alphabetized
    return mFoodLogDao.daoGetAllFoodLog();
  }

  // get only one property from database for logs
  //TODO add other properties of log type here
//  LiveData<List<FoodLog>> repositoryGetFoodLogsWithConcern(String concern) {
//    return mFoodLogDao.daoGetFoodLogsWithConcern(concern);
//  }

  // get list of logs that are on a certain date
  public List<FoodLog> repositoryGetAllFoodLogOnDate(Instant instant) {
    return mFoodLogDao.daoGetAllFoodLogOnDate(instant);
  }

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



  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertFoodLog(FoodLog foodLog) {

    FoodLogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mFoodLogDao.daoFoodLogInsert(foodLog);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteFoodLog(FoodLog foodLog) {

    FoodLogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mFoodLogDao.daoFoodLogDelete(foodLog);
    });

  } // end delete


  void repositoryUpdateFoodLog(FoodLog foodLog) {

    FoodLogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mFoodLogDao.daoFoodLogUpdate(foodLog);
    });

  } // end update



} //end FoodLogRepository class
