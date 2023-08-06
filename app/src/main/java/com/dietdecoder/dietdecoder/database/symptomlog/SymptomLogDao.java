package com.dietdecoder.dietdecoder.database.symptomlog;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.dietdecoder.dietdecoder.database.Converters;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Dao
@TypeConverters({Converters.class})
public interface SymptomLogDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoSymptomLogInsert(SymptomLog symptomLog);

  @Update
  void daoSymptomLogUpdate(SymptomLog symptomLog);

  @Delete
  void daoSymptomLogDelete(SymptomLog symptomLog);

  @Query("SELECT * FROM symptom_log_table")
  Cursor getCursorAllSymptomLog();

  @Query("SELECT * FROM symptom_log_table ORDER BY instantBegan DESC")
  LiveData<List<SymptomLog>> daoGetAllSymptomLog();

  // this needs to have same order by as livedata
  @Query("SELECT * FROM symptom_log_table ORDER BY instantBegan DESC")
  List<SymptomLog> daoGetArrayListAllSymptomLog();

  // get symptomlog from ID
  @Query("SELECT * FROM symptom_log_table WHERE :matchThisUuid = symptomLogId")
  SymptomLog daoGetSymptomLogFromLogId(UUID matchThisUuid);

  // get all symptom logs with a given symptom
  @Query("SELECT * FROM symptom_log_table WHERE :matchThisId = symptomLogSymptomId  ORDER BY " +
                    "instantBegan DESC")
  List<SymptomLog> daoGetAllSymptomLogFromSymptomId(UUID matchThisId);

  // get a specified number of the last symptoms by symptom id
  @Query("SELECT * FROM symptom_log_table WHERE :symptomIdToGet = symptomLogSymptomId ORDER BY " +
          "instantBegan DESC " +
          "LIMIT :numberToGet")
  List<SymptomLog> daoGetSomeSymptomLogFromSymptomId(UUID symptomIdToGet, Integer numberToGet);


  @Query("SELECT * FROM symptom_log_table ORDER BY " +
          "instantBegan DESC " +
          "LIMIT :numberToGet")
  List<SymptomLog> daoGetSomeSymptomLog(Integer numberToGet);



}
