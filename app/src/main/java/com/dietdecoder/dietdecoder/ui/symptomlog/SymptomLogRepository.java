package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLogDao;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class SymptomLogRepository {



  private SymptomLogDao mSymptomLogDao;
//  private SymptomDao mSymptomDao;
  private final DietDecoderRoomDatabase mDatabase;

  SymptomLogRepository(Application application) {
    // setup database to be returned via methods
    mDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mSymptomLogDao = mDatabase.symptomLogDao();
//    mSymptomDao = mDatabase.symptomDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  public LiveData<List<SymptomLog>> repositoryGetAllSymptomLogs() {
    //use the dao instantiated in the SymptomLogRepository method
    // to get all logs, alphabetized
    return mSymptomLogDao.daoGetAllSymptomLog();
  }


  // get list of logs that are of a certain type
  public List<SymptomLog> repositoryGetAllSymptomLogFromSymptomId(UUID id) {
    return mSymptomLogDao.daoGetAllSymptomLogFromSymptomId(id);
  }

  // get single log that is on a certain date
  //TODO make this work
//  public SymptomLog repositoryGetSymptomLogFromConsumedInstant(Instant instant) {
//    return mSymptomLogDao.daoGetSymptomLogFromConsumedInstant(instant);
//  }

  // get single log that from uuid
  public SymptomLog repositoryGetSymptomLogFromLogId(UUID uuid) {
    return mSymptomLogDao.daoGetSymptomLogFromLogId(uuid);
  }

  // experiment to see if I can get a object from another dao
//  public Symptom repositoryGetSymptomFromSymptomLogId(UUID uuid){
//    UUID symptomId = mSymptomLogDao.daoGetSymptomLogFromLogId(uuid).getSymptomLogSymptomId();
//    Symptom symptom = mSymptomDao.daoGetSymptomWithId(symptomId);
//    return symptom;
//  }

  // get all logs cursor
  public Cursor repositoryGetCursorAllSymptomLog() {
    return mSymptomLogDao.getCursorAllSymptomLog();
  }



  // duplicate log that from a log and return our new log
  public SymptomLog repositoryDuplicateSymptomLog(SymptomLog oldSymptomLog) {
    SymptomLog newSymptomLog = new SymptomLog(oldSymptomLog.getSymptomLogSymptomId());
    repositoryInsertSymptomLog(newSymptomLog);
    return newSymptomLog;
  }


  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertSymptomLog(SymptomLog symptomLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomLogDao.daoSymptomLogInsert(symptomLog);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteSymptomLog(SymptomLog symptomLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomLogDao.daoSymptomLogDelete(symptomLog);
    });

  } // end delete


  void repositoryUpdateSymptomLog(SymptomLog symptomLog) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomLogDao.daoSymptomLogUpdate(symptomLog);
    });

  } // end update



  // get given number of symptom logs matching the given symptom's name
  List<SymptomLog> repositoryGetSomeSymptomLog(Integer howManyLogs){
    List<SymptomLog> someSymptomLogs = null;

    if ( howManyLogs == null ){
      someSymptomLogs = mSymptomLogDao.daoGetArrayListAllSymptomLog();
    }
    {
      someSymptomLogs = mSymptomLogDao.daoGetSomeSymptomLog(howManyLogs);
    }

    return someSymptomLogs;
  }//end get some logs

  // get given number of symptom logs matching the given symptom's name
  List<SymptomLog> repositoryGetSomeSymptomLogsFromId(UUID symptomId, Integer howManyLogs){
    List<SymptomLog> someSymptomLogs = null;
    if ( !Objects.isNull(mSymptomLogDao.daoGetAllSymptomLogFromSymptomId(symptomId)) ) {
      someSymptomLogs = mSymptomLogDao.daoGetSomeSymptomLogFromSymptomId(symptomId, howManyLogs);
    }
    return someSymptomLogs;
  }//end get some logs

  Duration repositoryGetAverageSymptomDuration(UUID symptomId){
    // our eventual answer for how long the average duration of a recent list of symptom logs is
    Duration averageDuration = Duration.ZERO;
    //values to calculate as we go
    Instant instantBegan, instantChanged;
    //our list of logs if there is at least one
    List<SymptomLog> someSymptomLogs = repositoryGetSomeSymptomLogsFromId(symptomId, 10);

    // for each symptom log in our list
    // calculate the duration from when it began to when it changed
    // and add those durations to our list of all durations
    for (SymptomLog symptomLog : someSymptomLogs) {
      instantBegan = symptomLog.getInstantBegan();
      instantChanged = symptomLog.getInstantChanged();
      Duration timeElapsed = Duration.between(instantBegan, instantChanged).abs();
      // calculate the average
      averageDuration = averageDuration.plus(timeElapsed).dividedBy(2);
    }

    return averageDuration;
  } // end average duration method

  // get most recent symptom log with symptom
  SymptomLog repositoryGetMostRecentSymptomLogWithSymptom(UUID symptomId){

    SymptomLog symptomLogWithSameSymptom =
          mSymptomLogDao.daoGetSomeSymptomLogFromSymptomId(symptomId, 1).get(0);
    return symptomLogWithSameSymptom;
  }

} //end SymptomLogRepository class
