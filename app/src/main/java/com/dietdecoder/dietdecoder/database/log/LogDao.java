package com.dietdecoder.dietdecoder.database.log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.dietdecoder.dietdecoder.database.Converters;

import java.time.Instant;
import java.util.List;

@Dao
@TypeConverters({Converters.class})
public interface LogDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoLogInsert(Log log);

  @Update
  void daoLogUpdate(Log log);

  @Delete
  void daoLogDelete(Log log);

  @Query("SELECT * FROM log_table ORDER BY dateTime")
  LiveData<List<Log>> daoGetAllLog();


  // TODO fix this so it gets all on that date, not just that specific instant
  @Query("SELECT * FROM log_table WHERE :onThisDateTime = dateTime ORDER BY dateTime")
  List<Log> daoGetAllLogOnDate(Instant onThisDateTime);

  @Query("SELECT * FROM log_table WHERE :onThisInstant = dateTime ORDER BY dateTime")
  Log daoGetLogFromInstant(Instant onThisInstant);

  // TODO fix this so it gets all after that actual date
  @Query("SELECT * FROM log_table WHERE dateTime >= :laterThanThisDateTime ORDER BY dateTime")
  List<Log> daoGetAllLogAfterDateTime(Instant laterThanThisDateTime);

}
