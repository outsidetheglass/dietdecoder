package com.dietdecoder.dietdecoder.database.symptom;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

  // Select all symptoms from table and alphabetize them by name
  @Query("SELECT * FROM symptom_table ORDER BY name ASC")
  LiveData<List<Symptom>> daoGetAlphabetizedSymptoms();

  @Query("SELECT * FROM symptom_table WHERE trackOrNot ORDER BY name ASC")
  LiveData<List<Symptom>> daoGetSymptomsToTrack();

  // Sort by specific chemical and alphabetize them by symptom name
  @Query("SELECT * FROM symptom_table WHERE category LIKE :daoSymptomCategory ORDER BY " +
          "name ASC")
  List<Symptom> daoGetAllSymptomInCategory(String daoSymptomCategory);

  // Sort by specific and alphabetize them by name
  @Query("SELECT * FROM symptom_table ORDER BY name ASC")
  List<Symptom> daoGetAllSymptomArrayList();

  @Query("SELECT * FROM symptom_table " +
          "WHERE name " +
          "LIKE :searchSymptomName " +
          "ORDER BY name ASC")
  LiveData<List<Symptom>> daoGetAllSymptomsMatchingSearchName(String searchSymptomName);

@Query("SELECT * FROM symptom_table WHERE name = :daoSymptomName")
Symptom daoGetSymptomWithName(String daoSymptomName);

  @Query("SELECT * FROM symptom_table WHERE id = :daoSymptomId")
  Symptom daoGetSymptomWithId(UUID daoSymptomId);


  @Query("SELECT * FROM symptom_table LIMIT 1")
  Symptom daoGetAnySymptom();



}
