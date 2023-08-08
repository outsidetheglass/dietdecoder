package com.dietdecoder.dietdecoder.ui.symptomlog;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

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
  private final String TAG = "TAG: " + getClass().getSimpleName();


  private SymptomLogRepository mSymptomLogRepository;
  private List<SymptomLog> mViewModelAllSymptomLogsOnDate;
  private LiveData<List<SymptomLog>> mViewModelAllSymptomLogs;


  public SymptomLogViewModel(Application application) {
    super(application);
    mSymptomLogRepository = new SymptomLogRepository(application);
    mViewModelAllSymptomLogs = mSymptomLogRepository.repositoryGetAllSymptomLogs();

//    Log.d(TAG, mSymptomLogRepository.repositoryGetSomeSymptomLog(1).toString());
  }//end LogViewModel method

  // give null number to get all
  public List<SymptomLog> viewModelGetSomeLog(Integer numberOfSymptomLogsToGet){

    return mSymptomLogRepository.repositoryGetSomeSymptomLog(numberOfSymptomLogsToGet);
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
  public List<SymptomLog> viewModelGetLogFromSymptomId(UUID id) {
    return mSymptomLogRepository.repositoryGetAllSymptomLogFromSymptomId(id);
  }
  // get single log using the uuid
  public SymptomLog viewModelGetLogFromLogId(UUID uuid) {
    return mSymptomLogRepository.repositoryGetSymptomLogFromLogId(uuid);
  }

  // cursor for exporting
  public Cursor viewModelGetCursorAllLog() {
    return mSymptomLogRepository.repositoryGetCursorAllSymptomLog();
  }


  //TODO add other properties of log type here
//  public SymptomLog viewModelGetSymptomLogFromNameConcern(String logName, String logConcern){
//    return mRepository.repositoryGetSymptomLogFromNameConcern(logName, logConcern);
//  }

  // get average duration of symptom from most recent symptoms of same type
  public Duration viewModelGetAverageSymptomDuration(UUID symptomId){
    Duration averageDuration = mSymptomLogRepository.repositoryGetAverageSymptomDuration(symptomId);

    if ( averageDuration.isZero() ) {
      // TODO get default duration from symptom itself
      averageDuration = Duration.ofSeconds(3600);
    }
    return averageDuration;
  }

  // get most recent symptom log of symptom
  public SymptomLog viewModelGetMostRecentLogWithSymptom(UUID symptomId){
    SymptomLog symptomLogWithSameSymptom =
            mSymptomLogRepository.repositoryGetMostRecentSymptomLogWithSymptom(symptomId);
    return symptomLogWithSameSymptom;
  }

  /////////////
  // Basics //
  /////////////
  //get all logs to list them
  public LiveData<List<SymptomLog>> viewModelGetAllLiveData() {
    return mViewModelAllSymptomLogs;
  }

  // add to database
  public void viewModelInsert(SymptomLog symptomLog) {
    mSymptomLogRepository.repositoryInsertSymptomLog(symptomLog);
  }

  // edit log in database
  public void viewModelUpdate(SymptomLog symptomLog) {
    mSymptomLogRepository.repositoryUpdateSymptomLog(symptomLog);
  }

  // delete log in database
  public void viewModelDelete(SymptomLog symptomLog) {
    mSymptomLogRepository.repositoryDeleteSymptomLog(symptomLog);
  }

  public SymptomLog viewModelDuplicate(SymptomLog symptomLog) {
    return mSymptomLogRepository.repositoryDuplicateSymptomLog(symptomLog);
  }



} //end LogViewModel class
