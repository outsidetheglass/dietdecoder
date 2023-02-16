package com.dietdecoder.dietdecoder.ui.foodlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
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
  public FoodLog viewModelGetFoodLogFromConsumedInstant(Instant instant) {
    return mRepository.repositoryGetFoodLogFromConsumedInstant(instant);
  }
  // get single log using the uuid
  public FoodLog viewModelGetFoodLogFromId(UUID uuid) {
    return mRepository.repositoryGetFoodLogFromId(uuid);
  }

  // cursor for exporting
  public Cursor viewModelGetCursorAllFoodLog() {
    return mRepository.repositoryGetCursorAllFoodLog();
  }


  //TODO add other properties of log type here
  // and change log to foodlog
//  public Log viewModelGetLogFromNameConcern(String logName, String logConcern){
//    return mRepository.repositoryGetLogFromNameConcern(logName, logConcern);
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


} //end LogViewModel class
