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

  // get symptomlog from ID
  @Query("SELECT * FROM symptom_log_table WHERE :matchThisUuid = symptomLogId")
  SymptomLog daoGetSymptomLogFromId(UUID matchThisUuid);

  @Query("SELECT * FROM symptom_log_table WHERE :matchThisName = symptomName")
  List<SymptomLog> daoGetAllSymptomLogByName(String matchThisName);

}