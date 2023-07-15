package com.dietdecoder.dietdecoder.ui.foodlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLogDao;
import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class FoodLogRepository {



  private FoodLogDao mFoodLogDao;
  private final DietDecoderRoomDatabase mFoodLogDatabase;

  FoodLogRepository(Application application) {
    // setup database to be returned via methods
    mFoodLogDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mFoodLogDao = mFoodLogDatabase.foodLogDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  public LiveData<List<FoodLog>> repositoryGetAllFoodLogs() {
    //use the dao instantiated in the FoodLogRepository method
    // to get all logs, alphabetized
    return mFoodLogDao.daoGetAllFoodLog();
  }

  // get list of logs that are of a certain type
//  public List<FoodLog> repositoryGetAllFoodLogByName(UUID id) {
//    return mFoodLogDao.daoGetAllFoodLogByName(id);
//  }

  // get single log that is on a certain date
  //TODO make this work
  public FoodLog repositoryGetFoodLogFromConsumedInstant(Instant instant) {
    return mFoodLogDao.daoGetFoodLogFromConsumedInstant(instant);
  }

  // get single log that from uuid
  public FoodLog repositoryGetFoodLogFromId(UUID uuid) {
    return mFoodLogDao.daoGetFoodLogFromId(uuid);
  }

  // get all logs cursor
  public Cursor repositoryGetCursorAllFoodLog() {
    return mFoodLogDao.daoGetCursorAllFoodLog();
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

  // get given number of symptom logs matching the given symptom's name
//  List<FoodLog> repositoryGetSomeFoodLogsByName(String symptomName, Integer howManyLogs){
//    List<FoodLog> someFoodLogs = null;
//    if ( !Objects.isNull(mFoodLogDao.daoGetAllFoodLogByName(symptomName)) ) {
//      someFoodLogs = mFoodLogDao.daoGetSomeFoodLogByName(symptomName, howManyLogs);
//    }
//    return someFoodLogs;
//  }//end get some logs

//  Duration repositoryGetAverageSymptomDuration(String symptomName){
//    // our eventual answer for how long the average duration of a recent list of symptom logs is
//    Duration averageDuration = Duration.ZERO;
//    //values to calculate as we go
//    Instant instantBegan, instantChanged;
//    //our list of logs if there is at least one
//    List<FoodLog> someFoodLogs = repositoryGetSomeFoodLogsByName(symptomName, 10);
//
//    // for each symptom log in our list
//    // calculate the duration from when it began to when it changed
//    // and add those durations to our list of all durations
//    for (FoodLog foodLog : someFoodLogs) {
//      instantBegan = foodLog.getInstantBegan();
//      instantChanged = foodLog.getInstantChanged();
//      Duration timeElapsed = Duration.between(instantBegan, instantChanged).abs();
//      // calculate the average
//      averageDuration = averageDuration.plus(timeElapsed).dividedBy(2);
//    }
//
//    return averageDuration;
//  } // end average duration method

  // get most recent symptom log with symptom
//  FoodLog repositoryGetMostRecentFoodLogWithSymptom(String symptomName){
//
//    FoodLog foodLogWithSameSymptom =
//            mFoodLogDao.daoGetSomeFoodLogByName(symptomName, 1).get(0);
//    return foodLogWithSameSymptom;
//  }

} //end FoodLogRepository class
