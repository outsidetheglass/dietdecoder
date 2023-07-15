package com.dietdecoder.dietdecoder.ui.ingredientlog;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogRepository;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class IngredientLogViewModel extends AndroidViewModel {


  private IngredientLogRepository mRepository;
  private List<IngredientLog> mViewModelAllIngredientLogsOnDate;
  private LiveData<List<IngredientLog>> mViewModelAllIngredientLogs;


  public IngredientLogViewModel(Application application) {
    super(application);
    mRepository = new IngredientLogRepository(application);
    mViewModelAllIngredientLogs = mRepository.repositoryGetAllIngredientLogs();

  }//end LogViewModel method


  //get all logs to list them
  public LiveData<List<IngredientLog>> viewModelGetAllIngredientLogs() {
    return mViewModelAllIngredientLogs;
  }


  // get single log using the instant
  public List<IngredientLog> viewModelGetAllIngredientLogByIngredientId(UUID id) {
    return mRepository.repositoryGetAllIngredientLogByIngredientId(id);
  }
  // get single log using the uuid
  public IngredientLog viewModelGetIngredientLogFromLogId(UUID uuid) {
    return mRepository.repositoryGetIngredientLogFromLogId(uuid);
  }
  //TODO add other properties of log type here
  //get all logs on date TODO make work
//  public List<IngredientLog> viewModelGetAllIngredientLogOnDate(Instant instant) {
//    mViewModelAllIngredientLogsOnDate = mRepository.repositoryGetAllIngredientLogOnDate(instant);
//    return mViewModelAllIngredientLogsOnDate;
//  }


  // cursor for exporting
  public Cursor viewModelGetCursorAllIngredientLog() {
    return mRepository.repositoryGetCursorAllIngredientLog();
  }


  // add to database
  public void viewModelInsertIngredientLog(IngredientLog ingredientLog) {
    mRepository.repositoryInsertIngredientLog(ingredientLog);
  }

  // edit log in database
  public void viewModelUpdateIngredientLog(IngredientLog ingredientLog) {
    mRepository.repositoryUpdateIngredientLog(ingredientLog);
  }

  // delete log in database
  public void viewModelDeleteIngredientLog(IngredientLog ingredientLog) {
    mRepository.repositoryDeleteIngredientLog(ingredientLog);
  }

  // get average duration of symptom from most recent symptoms of same type
  public Integer viewModelGetAverageAmountAllLogsByIngredientId(UUID ingredientId){
    Integer averageAmount = mRepository.repositoryGetAverageIngredientAmount(ingredientId);

    if ( averageAmount == null ) {
      // TODO get default amount from an ingredient itself, or other users
      averageAmount = 0;
    }
    return averageAmount;
  }

  // get most recent symptom log of symptom
  public IngredientLog viewModelGetMostRecentIngredientLogWithIngredient(UUID ingredientId){
    IngredientLog ingredientLogWithSameIngredient =
            mRepository.repositoryGetMostRecentIngredientLogWithIngredient(ingredientId);
    return ingredientLogWithSameIngredient;
  }



} //end LogViewModel class
