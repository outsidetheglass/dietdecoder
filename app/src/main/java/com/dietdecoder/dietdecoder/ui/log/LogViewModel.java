package com.dietdecoder.dietdecoder.ui.log;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.log.Log;
import com.dietdecoder.dietdecoder.ui.log.LogRepository;

import java.time.Instant;
import java.util.List;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class LogViewModel extends AndroidViewModel {


  private LogRepository mRepository;
  //TODO add other properties
  private List<Log> mViewModelAllLogsOnDate;
  private LiveData<List<Log>> mViewModelAllLogs;


  public LogViewModel (Application application) {
    super(application);
    mRepository = new LogRepository(application);
    mViewModelAllLogs = mRepository.repositoryGetAllLogs();

  }//end LogViewModel method


  //get all logs to list them
  public LiveData<List<Log>> viewModelGetAllLogs() {
    return mViewModelAllLogs;
  }

  //get all logs on date
  public List<Log> viewModelGetAllLogOnDate(Instant instant) {
    mViewModelAllLogsOnDate = mRepository.repositoryGetAllLogOnDate(instant);
    return mViewModelAllLogsOnDate;
  }


  // get single log using the instant
  public Log viewModelGetLogFromInstant(Instant instant) {
    return mRepository.repositoryGetLogFromInstant(instant);
  }

  //TODO add other properties of log type here
//  public Log viewModelGetLogFromNameConcern(String logName, String logConcern){
//    return mRepository.repositoryGetLogFromNameConcern(logName, logConcern);
//  }


  // add to database
  public void viewModelInsertLog(Log log) { mRepository.repositoryInsertLog(log); }

  // edit log in database
  public void viewModelUpdateLog(Log log) {
    mRepository.repositoryUpdateLog(log);
  }

  // delete log in database
  public void viewModelDeleteLog(Log log) {
    mRepository.repositoryDeleteLog(log);
  }


} //end LogViewModel class
