package com.dietdecoder.dietdecoder.ui.symptom;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class SymptomViewModel extends AndroidViewModel {


  private SymptomRepository mRepository;
  //private LiveData<List<Symptom>> mViewModelAllSymptomsToTrackWithConcern;
  private LiveData<List<Symptom>> mViewModelAllSymptomsToTrack;


  public SymptomViewModel(Application application) {
    super(application);
    mRepository = new SymptomRepository(application);
    mViewModelAllSymptomsToTrack = mRepository.repositoryGetSymptomsToTrack();

  }//end SymptomViewModel method


  /////////////
  // Basics //
  /////////////

  //get all symptoms to list them
  public LiveData<List<Symptom>> viewModelGetAllLiveData() {
    return mRepository.repositoryGetAllSymptoms();
  }

  public LiveData<List<Symptom>> viewModelGetAllLiveDataToTrack() {
    return mRepository.repositoryGetSymptomsToTrack();
  }

  //get all symptoms with concern
  //TODO LiveData won't work twice like this I think
//  public LiveData<List<Symptom>> viewModelGetSymptomsWithConcern(String paramConcern) {
//    mViewModelAllSymptomsToTrackWithConcern = mRepository.repositoryGetSymptomsWithConcern
//    (paramConcern);
//    return mViewModelAllSymptomsToTrackWithConcern;
//  }


  // get single symptom using the name
  public Symptom viewModelGetSymptomFromName(String paramSymptomName) {
    return mRepository.repositoryGetSymptomFromName(paramSymptomName);
  }
  //get single symptom from the UUID
  public Symptom viewModelGetSymptomFromId(UUID paramSymptomId) {
    return mRepository.repositoryGetSymptomFromId(paramSymptomId);
  }

  public List<Symptom> viewModelGetAllSymptomFromCategory(String symptomCategory){
    return mRepository.repositoryGetSymptomInCategory(symptomCategory);
  }
  public ArrayList<Symptom> viewModelGetAllSymptomArrayList(){
    return mRepository.repositoryGetAllSymptomArrayList();
  }


  // add to database
  public void viewModelInsert(Symptom symptom) { mRepository.repositoryInsert(symptom); }
  // edit symptom in database
  public void viewModelUpdate(Symptom symptom) {
    mRepository.repositoryUpdate(symptom);
  }
  // delete symptom in database
  public void viewModelDelete(Symptom symptom) {
    mRepository.repositoryDelete(symptom);
  }


} //end SymptomViewModel class
