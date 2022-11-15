package com.dietdecoder.dietdecoder.ui.log;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.log.LogDao;
import com.dietdecoder.dietdecoder.database.log.Log;
import com.dietdecoder.dietdecoder.database.log.LogRoomDatabase;

import java.time.Instant;
import java.util.List;

class LogRepository {



  private LogDao mLogDao;
  private final LogRoomDatabase mLogDatabase;

  LogRepository(Application application) {
    // setup database to be returned via methods
    mLogDatabase = LogRoomDatabase.getDatabase(application);
    mLogDao = mLogDatabase.logDao();
  }


  // queries on a separate thread
  // Observed LiveData will notify the observer when the data has changed.
  // TODO I deleted the private declaration before, but it might need it
  public LiveData<List<Log>> repositoryGetAllLogs() {
    //use the dao instantiated in the LogRepository method
    // to get all logs, alphabetized
    return mLogDao.daoGetAllLog();
  }

  // get only one property from database for logs
  //TODO add other properties of log type here
//  LiveData<List<Log>> repositoryGetLogsWithConcern(String concern) {
//    return mLogDao.daoGetLogsWithConcern(concern);
//  }

  // get list of logs that are on a certain date
  public List<Log> repositoryGetAllLogOnDate(Instant instant) {
    return mLogDao.daoGetAllLogOnDate(instant);
  }

  // get single log that is on a certain date
  public Log repositoryGetLogFromInstant(Instant instant) {
    return mLogDao.daoGetLogFromInstant(instant);
  }

  // get all logs happening after a certain date
  public List<Log> repositoryGetAllLogAfterDateTime(Instant instant) {
    return mLogDao.daoGetAllLogAfterDateTime(instant);
  }



  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsertLog(Log log) {

    LogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mLogDao.daoLogInsert(log);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDeleteLog(Log log) {

    LogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mLogDao.daoLogDelete(log);
    });

  } // end delete


  void repositoryUpdateLog(Log log) {

    LogRoomDatabase.databaseWriteExecutor.execute(() -> {
      mLogDao.daoLogUpdate(log);
    });

  } // end update concern



} //end LogRepository class
