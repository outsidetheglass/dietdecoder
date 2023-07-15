package com.dietdecoder.dietdecoder.ui.foodlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogRepository;

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
public class FoodLogViewModel extends AndroidViewModel {


  private FoodLogRepository mRepository;
  private List<FoodLog> mViewModelAllFoodLogsOnDate;
  private LiveData<List<FoodLog>> mViewModelAllFoodLogs;


  public FoodLogViewModel(Application application) {
    super(application);
    mRepository = new FoodLogRepository(application);
    mViewModelAllFoodLogs = mRepository.repositoryGetAllFoodLogs();

  }//end LogViewModel method


  //get all logs to list them
  public LiveData<List<FoodLog>> viewModelGetAllFoodLogs() {
    return mViewModelAllFoodLogs;
  }


  //get all logs on date TODO make work
//  public List<FoodLog> viewModelGetAllFoodLogOnDate(Instant instant) {
//    mViewModelAllFoodLogsOnDate = mRepository.repositoryGetAllFoodLogOnDate(instant);
//    return mViewModelAllFoodLogsOnDate;
//  }


  // get single log using the instant
//  public List<FoodLog> viewModelGetFoodLogByIngredientName(String name) {
//    return mRepository.repositoryGetAllFoodLogByIngredientName(name);
//  }
  // get single log using the uuid
  public FoodLog viewModelGetFoodLogFromId(UUID uuid) {
    return mRepository.repositoryGetFoodLogFromId(uuid);
  }

  // cursor for exporting
  public Cursor viewModelGetCursorAllFoodLog() {
    return mRepository.repositoryGetCursorAllFoodLog();
  }


  //TODO add other properties of log type here
//  public FoodLog viewModelGetFoodLogFromNameConcern(String logName, String logConcern){
//    return mRepository.repositoryGetFoodLogFromNameConcern(logName, logConcern);
//  }


  // add to database
  public void viewModelInsertFoodLog(FoodLog foodLog) {
    mRepository.repositoryInsertFoodLog(foodLog);
  }

  // edit log in database
  public void viewModelUpdateFoodLog(FoodLog foodLog) {
    mRepository.repositoryUpdateFoodLog(foodLog);
  }

  // delete log in database
  public void viewModelDeleteFoodLog(FoodLog foodLog) {
    mRepository.repositoryDeleteFoodLog(foodLog);
  }

  // get average duration of symptom from most recent symptoms of same type
//  public Duration viewModelGetAverageSymptomDuration(String symptomName){
//    Duration averageDuration = mRepository.repositoryGetAverageSymptomDuration(symptomName);
//
//    if ( averageDuration.isZero() ) {
//      // TODO get default duration from symptom itself
//      averageDuration = Duration.ofSeconds(3600);
//    }
//    return averageDuration;
//  }
//
//  // get most recent symptom log of symptom
//  public FoodLog viewModelGetMostRecentFoodLogWithSymptom(String symptomName){
//    FoodLog foodLogWithSameSymptom =
//            mRepository.repositoryGetMostRecentFoodLogWithSymptom(symptomName);
//    return foodLogWithSameSymptom;
//  }



} //end LogViewModel class
