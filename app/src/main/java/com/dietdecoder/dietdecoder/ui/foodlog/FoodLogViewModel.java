package com.dietdecoder.dietdecoder.ui.foodlog;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;

import java.time.Instant;
import java.util.List;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class FoodLogViewModel extends AndroidViewModel {


  private FoodLogRepository mRepository;
  //TODO add other properties
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

  //get all logs on date
  public List<FoodLog> viewModelGetAllFoodLogOnDate(Instant instant) {
    mViewModelAllFoodLogsOnDate = mRepository.repositoryGetAllFoodLogOnDate(instant);
    return mViewModelAllFoodLogsOnDate;
  }


  // get single log using the instant
  public FoodLog viewModelGetFoodLogFromInstant(Instant instant) {
    return mRepository.repositoryGetFoodLogFromInstant(instant);
  }

  //TODO add other properties of log type here
  // and change log to foodlog
//  public Log viewModelGetLogFromNameConcern(String logName, String logConcern){
//    return mRepository.repositoryGetLogFromNameConcern(logName, logConcern);
//  }


  // add to database
  public void viewModelInsertFoodLog(FoodLog foodLog) { mRepository.repositoryInsertFoodLog(foodLog); }

  // edit log in database
  public void viewModelUpdateFoodLog(FoodLog foodLog) {
    mRepository.repositoryUpdateFoodLog(foodLog);
  }

  // delete log in database
  public void viewModelDeleteFoodLog(FoodLog foodLog) {
    mRepository.repositoryDeleteFoodLog(foodLog);
  }


} //end LogViewModel class
