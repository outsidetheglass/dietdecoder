package com.dietdecoder.dietdecoder.database.foodlog;

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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Dao
@TypeConverters({Converters.class})
public interface FoodLogDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoFoodLogInsert(FoodLog foodLog);

  @Update
  void daoFoodLogUpdate(FoodLog foodLog);

  @Delete
  void daoFoodLogDelete(FoodLog foodLog);
// TODO figure out duplication the right way, it's currently being done in EditFoodLogFragment
//  @Query("INSERT INTO food_log_table ")
//  INSERT INTO menuship3
//          (headhash, menucardhash, menucathash, producthash)
//  SELECT
//  headhash, 'qqq', menucathash, producthash
//          FROM
//  menuship3
//          WHERE
//  menucardhash = 'aaa' ;
  // or
  // @Query("UPDATE playlist SET play_order = play_order + 1 WHERE play_order >=:insertion_point")
  //void prepareForInsertion(long insertion_point);

  @Query("SELECT * FROM food_log_table")
  Cursor daoGetCursorAllFoodLog();

  //TODO change this into a multimap instead of putting name and id for ingredient in every time
  @Query("SELECT * FROM food_log_table ORDER BY instantConsumed DESC")
  LiveData<List<FoodLog>> daoGetAllFoodLog();


  // TODO fix this so it gets all on that date, not just that specific instant
  @Query("SELECT * FROM food_log_table WHERE :onThisInstant = instantConsumed ORDER BY " +
          "instantConsumed")
  List<FoodLog> daoGetAllFoodLogOnDate(Instant onThisInstant);

  @Query("SELECT * FROM food_log_table WHERE :onThisInstant = instantConsumed ORDER BY instantConsumed")
  FoodLog daoGetFoodLogFromConsumedInstant(Instant onThisInstant);

  // get foodlog from ID
//  @Query("SELECT * FROM food_log_table WHERE :matchThisUuid = foodLogId")
//  LiveData<FoodLog> daoGetFoodLogFromId(UUID matchThisUuid);


  @Query("SELECT * FROM food_log_table WHERE :matchThisUuid = foodLogId")
  FoodLog daoGetFoodLogFromId(UUID matchThisUuid);

  // TODO fix this so it gets all after that actual date
  @Query("SELECT * FROM food_log_table WHERE instantConsumed >= :laterThanThisDateTime ORDER BY " +
          "instantConsumed")
  List<FoodLog> daoGetAllFoodLogAfterDateTime(Instant laterThanThisDateTime);

}
