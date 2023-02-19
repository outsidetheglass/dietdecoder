package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLogDao;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLogRoomDatabase;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

class SymptomLogRepository {



  private SymptomLogDao mSymptomLogDao;
  private final SymptomLogRoomDatabase mSymptomLogDatabase;

  SymptomLogRepository(Application application) {
    // setup database to be returned via methods
    mSymptomLogDatabase = SymptomLogRoomDatabase.getDatabase(application);
    mSymptomLogDao = mSymptomLogDatabase.symptomLogDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  public LiveData<List<SymptomLog>> repositoryGetAllSymptomLogs() {
    //use the dao instantiated in the SymptomLogRepository method
    // to get all logs, alphabetized
    return mSymptomLogDao.daoGetAllSymptomLog();
  }

  // get list of logs that are of a certain type
  public List<SymptomLog> repositoryGetAllSymptomLogByName(String name) {
    return mSymptomLogDao.daoGetAllSymptomLogByName(name);
  }

  // get single log that is on a certain date
  //TODO make this work
//  public SymptomLog repositoryGetSymptomLogFromConsumedInstant(Instant instant) {
//    return mSymptomLogDao.daoGetSymptomLogFromConsumedInstant(instant);
//  }

  // get single log that from uuid
  public SymptomLog repositoryGetSymptomLogFromId(UUID uuid) {
    return mSymptomLogDao.daoGetSymptomLogFromId(uuid);
  }

  // get all logs cursor
  public Cursor repositoryGetCursorAllSymptomLog() {
    return mSymptomLogDao.getCursorAllSymptomLog();
  }



  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertSymptomLog(SymptomLog symptomLog) {

    SymptomLogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomLogDao.daoSymptomLogInsert(symptomLog);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteSymptomLog(SymptomLog symptomLog) {

    SymptomLogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomLogDao.daoSymptomLogDelete(symptomLog);
    });

  } // end delete


  void repositoryUpdateSymptomLog(SymptomLog symptomLog) {

    SymptomLogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomLogDao.daoSymptomLogUpdate(symptomLog);
    });

  } // end update



} //end SymptomLogRepository class
