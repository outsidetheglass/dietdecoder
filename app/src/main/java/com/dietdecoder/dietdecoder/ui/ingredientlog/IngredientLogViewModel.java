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


  private IngredientLogRepository mIngredientLogRepository;
  private List<IngredientLog> mViewModelAllOnDate;
  private LiveData<List<IngredientLog>> mViewModelAll;


  public IngredientLogViewModel(Application application) {
    super(application);
    mIngredientLogRepository = new IngredientLogRepository(application);
    mViewModelAll = mIngredientLogRepository.repositoryGetAllIngredientLogs();

  }//end LogViewModel method


  //get all logs to list them
  public LiveData<List<IngredientLog>> viewModelGetAllLiveData() {
    return mViewModelAll;
  }


  // get single log using the instant
  public List<IngredientLog> viewModelGetAllIngredientLogByIngredientId(UUID id) {
    return mIngredientLogRepository.repositoryGetAllIngredientLogByIngredientId(id);
  }
  // get single log using the uuid
  public IngredientLog viewModelGetLogFromLogId(UUID uuid) {
    return mIngredientLogRepository.repositoryGetIngredientLogFromLogId(uuid);
  }
  //TODO add other properties of log type here
  //get all logs on date TODO make work
//  public List<IngredientLog> viewModelGetAllIngredientLogOnDate(Instant instant) {
//    mViewModelAllOnDate = mRepository.repositoryGetAllIngredientLogOnDate(instant);
//    return mViewModelAllOnDate;
//  }


  // cursor for exporting
  public Cursor viewModelGetCursorAll() {
    return mIngredientLogRepository.repositoryGetCursorAllIngredientLog();
  }


  // add to database
  public void viewModelInsert(IngredientLog ingredientLog) {
    mIngredientLogRepository.repositoryInsertIngredientLog(ingredientLog);
  }

  // edit log in database
  public void viewModelUpdate(IngredientLog ingredientLog) {
    mIngredientLogRepository.repositoryUpdateIngredientLog(ingredientLog);
  }
  // edit log in database
  public IngredientLog viewModelDuplicate(IngredientLog ingredientLog) {
    return mIngredientLogRepository.repositoryDuplicateIngredientLog(ingredientLog);
  }

  // delete log in database
  public void viewModelDelete(IngredientLog ingredientLog) {
    mIngredientLogRepository.repositoryDeleteIngredientLog(ingredientLog);
  }

  // get average duration of symptom from most recent symptoms of same type
  public Integer viewModelGetAverageAmountAllLogsByIngredientId(UUID ingredientId){
    Integer averageAmount = mIngredientLogRepository.repositoryGetAverageIngredientAmount(ingredientId);

    if ( averageAmount == null ) {
      // TODO get default amount from an ingredient itself, or other users
      averageAmount = 0;
    }
    return averageAmount;
  }

  // get most recent symptom log of symptom
  public IngredientLog viewModelGetMostRecentLogWithIngredient(UUID ingredientId){
    IngredientLog ingredientLogWithSameIngredient =
            mIngredientLogRepository.repositoryGetMostRecentIngredientLogWithIngredient(ingredientId);
    return ingredientLogWithSameIngredient;
  }



} //end LogViewModel class
