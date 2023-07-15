package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.ui.symptomlog.SymptomLogRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class SymptomLogViewModel extends AndroidViewModel {


  private SymptomLogRepository mRepository;
  private List<SymptomLog> mViewModelAllSymptomLogsOnDate;
  private LiveData<List<SymptomLog>> mViewModelAllSymptomLogs;


  public SymptomLogViewModel(Application application) {
    super(application);
    mRepository = new SymptomLogRepository(application);
    mViewModelAllSymptomLogs = mRepository.repositoryGetAllSymptomLogs();

  }//end LogViewModel method


  //get all logs to list them
  public LiveData<List<SymptomLog>> viewModelGetAllSymptomLogs() {
    return mViewModelAllSymptomLogs;
  }

  // TODO get experiment to cross reference daos working
//  public Symptom viewModelGetSymptomFromSymptomLogId(UUID uuid){
//    return mRepository.repositoryGetSymptomFromSymptomLogId(uuid);
//  }

  //get all logs on date TODO make work
//  public List<SymptomLog> viewModelGetAllSymptomLogOnDate(Instant instant) {
//    mViewModelAllSymptomLogsOnDate = mRepository.repositoryGetAllSymptomLogOnDate(instant);
//    return mViewModelAllSymptomLogsOnDate;
//  }


  // get single log using the instant
  public List<SymptomLog> viewModelGetSymptomLogFromSymptomId(UUID id) {
    return mRepository.repositoryGetAllSymptomLogFromSymptomId(id);
  }
  // get single log using the uuid
  public SymptomLog viewModelGetSymptomLogFromLogId(UUID uuid) {
    return mRepository.repositoryGetSymptomLogFromLogId(uuid);
  }

  // cursor for exporting
  public Cursor viewModelGetCursorAllSymptomLog() {
    return mRepository.repositoryGetCursorAllSymptomLog();
  }


  //TODO add other properties of log type here
//  public SymptomLog viewModelGetSymptomLogFromNameConcern(String logName, String logConcern){
//    return mRepository.repositoryGetSymptomLogFromNameConcern(logName, logConcern);
//  }


  // add to database
  public void viewModelInsertSymptomLog(SymptomLog symptomLog) {
    mRepository.repositoryInsertSymptomLog(symptomLog);
  }

  // edit log in database
  public void viewModelUpdateSymptomLog(SymptomLog symptomLog) {
    mRepository.repositoryUpdateSymptomLog(symptomLog);
  }

  // delete log in database
  public void viewModelDeleteSymptomLog(SymptomLog symptomLog) {
    mRepository.repositoryDeleteSymptomLog(symptomLog);
  }

  // get average duration of symptom from most recent symptoms of same type
  public Duration viewModelGetAverageSymptomDuration(String symptomName){
    Duration averageDuration = mRepository.repositoryGetAverageSymptomDuration(symptomName);

    if ( averageDuration.isZero() ) {
      // TODO get default duration from symptom itself
      averageDuration = Duration.ofSeconds(3600);
    }
    return averageDuration;
  }

  // get most recent symptom log of symptom
  public SymptomLog viewModelGetMostRecentSymptomLogWithSymptom(String symptomName){
    SymptomLog symptomLogWithSameSymptom =
            mRepository.repositoryGetMostRecentSymptomLogWithSymptom(symptomName);
    return symptomLogWithSameSymptom;
  }



} //end LogViewModel class
