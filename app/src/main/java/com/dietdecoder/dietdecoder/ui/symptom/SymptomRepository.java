package com.dietdecoder.dietdecoder.ui.symptom;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.DietDecoderRoomDatabase;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptom.SymptomDao;

import java.util.List;

class SymptomRepository {



  private SymptomDao mSymptomDao;
  private final DietDecoderRoomDatabase mSymptomDatabase;

  SymptomRepository(Application application) {
    // setup database to be returned via methods
    mSymptomDatabase = DietDecoderRoomDatabase.getDatabase(application);
    mSymptomDao = mSymptomDatabase.symptomDao();
  }

  LiveData<List<Symptom>> symptomRepositoryGetAllSymptoms() {
    //use the dao instantiated in the SymptomRepository method
    // to get all symptoms, alphabetized
    return mSymptomDao.daoGetAlphabetizedSymptoms();
  }

  LiveData<List<Symptom>> symptomRepositoryGetSymptomsToTrack() {
    //use the dao instantiated in the SymptomRepository method
    // to get all symptoms, alphabetized
    return mSymptomDao.daoGetSymptomsToTrack();
  }

  // get only symptoms matching category from database
  List<Symptom> symptomRepositoryGetSymptomInCategory(String symptomCategory) {
    return mSymptomDao.daoGetAllSymptomInCategory(symptomCategory);
  }

  // get only given symptom using name
  public Symptom symptomRepositoryGetSymptomFromName(String symptomName) {
    return mSymptomDao.daoGetSymptomWithName(symptomName);
  }

  // You must call this on a non-UI thread or your app will throw an exception. Room ensures
  // that you're not doing any long running operations on the main thread, blocking the UI.
  void symptomRepositoryInsert(Symptom symptom) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomDao.symptomDaoInsert(symptom);
    });

  } // end insert

  // You must call this on a non-UI thread
  void symptomRepositoryDelete(Symptom symptom) {

    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomDao.symptomDaoDelete(symptom);
    });

  } // end delete


  void symptomRepositoryUpdate(Symptom symptom) {
    DietDecoderRoomDatabase.databaseWriteExecutor.execute(() -> {
      mSymptomDao.symptomDaoUpdate(symptom);});
  } // end update


} //end SymptomRepository class
