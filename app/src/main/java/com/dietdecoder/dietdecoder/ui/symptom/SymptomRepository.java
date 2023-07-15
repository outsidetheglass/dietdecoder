package com.dietdecoder.dietdecoder.ui.symptom;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptom.SymptomDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class SymptomRepository {



  private SymptomDao mSymptomDao;
  private final DietDecoderRoomDatabase mSymptomDatabase;

  SymptomRepository(Application application) {
    // setup database to be returned via methods
    mSymptomDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mSymptomDao = mSymptomDatabase.symptomDao();
  }

  LiveData<List<Symptom>> repositoryGetAllSymptoms() {
    //use the dao instantiated in the SymptomRepository method
    // to get all symptoms, alphabetized
    return mSymptomDao.daoGetAlphabetizedSymptoms();
  }

  LiveData<List<Symptom>> repositoryGetSymptomsToTrack() {
    //use the dao instantiated in the SymptomRepository method
    // to get all symptoms, alphabetized
    return mSymptomDao.daoGetSymptomsToTrack();
  }

  // get only symptoms matching category from database
  List<Symptom> repositoryGetSymptomInCategory(String symptomCategory) {
    return mSymptomDao.daoGetAllSymptomInCategory(symptomCategory);
  }

  // get only symptoms matching category from database
  ArrayList<Symptom> repositoryGetAllSymptomArrayList() {
    return new ArrayList<Symptom>(mSymptomDao.daoGetAllSymptomArrayList());
  }


  // get only given symptom using name
  public Symptom repositoryGetSymptomFromName(String symptomName) {
    return mSymptomDao.daoGetSymptomWithName(symptomName);
  }
  // get only given symptom using id
  public Symptom repositoryGetSymptomFromId(UUID symptomId) {
    return mSymptomDao.daoGetSymptomWithId(symptomId);
  }


  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void repositoryInsert(Symptom symptom) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomDao.symptomDaoInsert(symptom);
    });

  } // end insert

  // You must call this on a non-UI thread
  void repositoryDelete(Symptom symptom) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomDao.symptomDaoDelete(symptom);
    });

  } // end delete


  void repositoryUpdate(Symptom symptom) {
    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomDao.symptomDaoUpdate(symptom);});
  } // end update


} //end SymptomRepository class
