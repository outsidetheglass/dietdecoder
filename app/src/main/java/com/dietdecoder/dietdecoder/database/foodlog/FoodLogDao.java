package com.dietdecoder.dietdecoder.database.foodlog;

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
public interface FoodLogDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoFoodLogInsert(FoodLog foodLog);

  @Update
  void daoFoodLogUpdate(FoodLog foodLog);

  @Delete
  void daoFoodLogDelete(FoodLog foodLog);

  @Query("SELECT * FROM food_log_table ORDER BY dateTimeConsumed")
  LiveData<List<FoodLog>> daoGetAllFoodLog();


  // TODO fix this so it gets all on that date, not just that specific instant
  @Query("SELECT * FROM food_log_table WHERE :onThisDateTime = dateTimeConsumed ORDER BY dateTimeConsumed")
  List<FoodLog> daoGetAllFoodLogOnDate(Instant onThisDateTime);

  @Query("SELECT * FROM food_log_table WHERE :onThisInstant = dateTimeConsumed ORDER BY dateTimeConsumed")
  FoodLog daoGetFoodLogFromInstant(Instant onThisInstant);

  // TODO fix this so it gets all after that actual date
  @Query("SELECT * FROM food_log_table WHERE dateTimeConsumed >= :laterThanThisDateTime ORDER BY dateTimeConsumed")
  List<FoodLog> daoGetAllFoodLogAfterDateTime(Instant laterThanThisDateTime);

}
