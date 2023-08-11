package com.dietdecoder.dietdecoder.database.ingredientlog;

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
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;

import java.util.List;
import java.util.UUID;

@Dao
@TypeConverters({Converters.class})
public interface IngredientLogDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoIngredientLogInsert(IngredientLog ingredientLog);

  @Update
  void daoIngredientLogUpdate(IngredientLog ingredientLog);

  @Delete
  void daoIngredientLogDelete(IngredientLog ingredientLog);

  @Query("SELECT * FROM ingredient_log_table")
  Cursor getCursorAllIngredientLog();

  @Query("SELECT * FROM ingredient_log_table ORDER BY instantConsumed DESC")
  LiveData<List<IngredientLog>> daoGetAllIngredientLog();

  // get ingredientlog from ID
  @Query("SELECT * FROM ingredient_log_table WHERE :matchThisUuid = logId")
  IngredientLog daoGetIngredientLogFromLogId(UUID matchThisUuid);

  // get all ingredient logs with a given ingredient
  @Query("SELECT * FROM ingredient_log_table WHERE :matchThisUuid = logIngredientId ORDER BY " +
          "instantConsumed DESC")
  List<IngredientLog> daoGetAllIngredientLogFromIngredientId(UUID matchThisUuid);

  // get a specified number of the last ingredients by ingredient id
  @Query("SELECT * FROM ingredient_log_table WHERE :matchThisUuid = logIngredientId ORDER BY " +
          "instantConsumed DESC " +
          "LIMIT :numberToGet")
  List<IngredientLog> daoGetSomeIngredientLogByIngredientId(UUID matchThisUuid, Integer numberToGet);



}
