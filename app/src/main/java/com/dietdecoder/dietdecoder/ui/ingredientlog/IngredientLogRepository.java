package com.dietdecoder.dietdecoder.ui.ingredientlog;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.ingredient.IngredientDao;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLogDao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class IngredientLogRepository {


  private final String TAG = "TAG: " + getClass().getSimpleName();


  private IngredientLogDao mIngredientLogDao;
  private IngredientDao mIngredientDao;
  private final DietDecoderRoomDatabase mDietDecoderDatabase;

  IngredientLogRepository(Application application) {
    // setup database to be returned via methods
    mDietDecoderDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mIngredientLogDao = mDietDecoderDatabase.ingredientLogDao();
    mIngredientDao = mDietDecoderDatabase.ingredientDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  public LiveData<List<IngredientLog>> repositoryGetAllIngredientLogs() {
    //use the dao instantiated in the IngredientLogRepository method
    // to get all logs, alphabetized
    return mIngredientLogDao.daoGetAllIngredientLog();
  }

  // get list of logs that are of a certain type
  public List<IngredientLog> repositoryGetAllIngredientLogByIngredientId(UUID id) {
    return mIngredientLogDao.daoGetAllIngredientLogFromIngredientId(id);
  }

  // get single log that is on a certain date
  //TODO make this work
//  public IngredientLog repositoryGetIngredientLogFromConsumedInstant(Instant instant) {
//    return mIngredientLogDao.daoGetIngredientLogFromConsumedInstant(instant);
//  }

  // get single log that from uuid
  public IngredientLog repositoryGetIngredientLogFromLogId(UUID uuid) {
    return mIngredientLogDao.daoGetIngredientLogFromLogId(uuid);
  }

  // get all logs cursor
  public Cursor repositoryGetCursorAllIngredientLog() {
    return mIngredientLogDao.getCursorAllIngredientLog();
  }



  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertIngredientLog(IngredientLog ingredientLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientLogDao.daoIngredientLogInsert(ingredientLog);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteIngredientLog(IngredientLog ingredientLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientLogDao.daoIngredientLogDelete(ingredientLog);
    });

  } // end delete


  void repositoryUpdateIngredientLog(IngredientLog ingredientLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mIngredientLogDao.daoIngredientLogUpdate(ingredientLog);
    });

  } // end update

  // duplicate the given log and update the new log's time consumed to now
  IngredientLog repositoryDuplicateIngredientLog(IngredientLog ingredientLog) {
    // we want the same log info as the given log, but with an updated time consumed

    // so set the ingredient log we were given to its new time consumed, now
    //get info on the ingredient to make the log based on defaults
    Instant instantAcquired = ingredientLog.getInstantAcquired();
    Instant instantCooked = ingredientLog.getInstantCooked();
    Instant instantNow = Instant.now();
    String subjectiveAmount = ingredientLog.getLogIngredientSubjectiveAmount();
    UUID ingredientId = ingredientLog.getLogIngredientId();

     IngredientLog ingredientLogToReturn = new IngredientLog(ingredientId);

     // set time consumed to now and everything else to be same as given ingredient log
     ingredientLogToReturn.setInstantConsumed(instantNow);
     ingredientLogToReturn.setInstantAcquired(instantAcquired);
     ingredientLogToReturn.setInstantCooked(instantCooked);
     ingredientLogToReturn.setLogIngredientSubjectiveAmount(subjectiveAmount);

     // put our duplicated log into the database
     mIngredientLogDao.daoIngredientLogInsert(ingredientLogToReturn);

     return ingredientLogToReturn;
  } // end update

  // get given number of ingredient logs matching the given ingredient's name
  List<IngredientLog> repositoryGetSomeIngredientLogsByIngredientId(UUID ingredientId,
                                                            Integer howManyLogs){
    List<IngredientLog> someIngredientLogs = null;
    if ( !Objects.isNull(mIngredientLogDao.daoGetAllIngredientLogFromIngredientId(ingredientId)) ) {
      someIngredientLogs = mIngredientLogDao.daoGetSomeIngredientLogByIngredientId(ingredientId,
              howManyLogs);
    }
    return someIngredientLogs;
  }//end get some logs

  Integer repositoryGetAverageIngredientAmount(UUID ingredientId){
    // our eventual answer for how much this ingredient is usually logged for
    ArrayList<Double> someIngredientLogsAmounts = new ArrayList<>();
    //our list of logs if there is at least one
    List<IngredientLog> someIngredientLogs =
            repositoryGetSomeIngredientLogsByIngredientId(ingredientId,
                    10);

    // for each ingredient log in our list
    // calculate the duration from when it began to when it changed
    // and add those durations to our list of all durations
//    for (IngredientLog ingredientLog : someIngredientLogs) {
//      someIngredientLogsAmounts.add(ingredientLog.getIngredientLogIngredientAmountNumber());
//    }

    // calculate the average
    Double average =
            someIngredientLogsAmounts.stream().mapToDouble(val -> val).average().orElse(0.0);

    return average.intValue();
  } // end average amount method


  // get most recent ingredient log with ingredient
  IngredientLog repositoryGetMostRecentIngredientLogWithIngredient(UUID ingredientId){

    IngredientLog ingredientLogWithSameIngredient =
          mIngredientLogDao.daoGetSomeIngredientLogByIngredientId(ingredientId, 1).get(0);

    return ingredientLogWithSameIngredient;
  }

  String repositoryGetIngredientNameFromIngredientId(UUID ingredientId){

    return mIngredientDao.daoGetIngredientFromId(ingredientId).getName();
  }
} //end IngredientLogRepository class
