package com.dietdecoder.dietdecoder.ui.symptom;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.List;

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
    mViewModelAllSymptomsToTrack = mRepository.symptomRepositoryGetSymptomsToTrack();

  }//end SymptomViewModel method


  //get all symptoms to list them
  public LiveData<List<Symptom>> viewModelGetSymptomsToTrack() {
    return mViewModelAllSymptomsToTrack;
  }

  public LiveData<List<Symptom>> viewModelGetAllSymptoms() {
    return mRepository.symptomRepositoryGetAllSymptoms();
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
    return mRepository.symptomRepositoryGetSymptomFromName(paramSymptomName);
  }
  public List<Symptom> viewModelGetAllSymptomFromCategory(String symptomCategory){
    return mRepository.symptomRepositoryGetSymptomInCategory(symptomCategory);
  }


  // add to database
  public void viewModelInsert(Symptom symptom) { mRepository.symptomRepositoryInsert(symptom); }
  // edit symptom in database
  public void viewModelUpdate(Symptom symptom) {
    mRepository.symptomRepositoryUpdate(symptom);
  }
  // delete symptom in database
  public void viewModelDelete(Symptom symptom) {
    mRepository.symptomRepositoryDelete(symptom);
  }


} //end SymptomViewModel class
