package com.dietdecoder.dietdecoder.database.symptom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.List;

@Dao
public interface SymptomDao {


  // allowing the insert multiple times by passing a
  // conflict resolution strategy
  // choosing ignore because symptoms may seem the same but will have different brands, etc
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void symptomDaoInsert(Symptom symptom);

  @Delete
  void symptomDaoDelete(Symptom symptom);

  @Update
  void symptomDaoUpdate(Symptom symptom);


  //TODO: fix my Query's by
  // replacing them with their object instead of strings
  // probably need to make ID's for that to work

  // Select all symptoms from table and alphabetize them by name
  @Query("SELECT * FROM symptom_table ORDER BY symptomName ASC")
  LiveData<List<Symptom>> daoGetAlphabetizedSymptoms();

  // Sort by specific chemical and alphabetize them by symptom name
  @Query("SELECT * FROM symptom_table WHERE symptomCategory LIKE :daoSymptomCategory ORDER BY symptomName ASC")
  List<Symptom> daoGetAllSymptomInCategory(String daoSymptomCategory);
//
//  @Query("SELECT * FROM symptom_table WHERE symptomName = :daoSymptomName AND symptomIntensity = :daoSymptomIntensity")
//  Symptom daoGetSymptomWithNameIntensity(String daoSymptomName, Integer daoSymptomIntensity);
//
  @Query("SELECT * FROM symptom_table WHERE symptomName = :daoSymptomName")
  Symptom daoGetSymptomWithName(String daoSymptomName);

}
