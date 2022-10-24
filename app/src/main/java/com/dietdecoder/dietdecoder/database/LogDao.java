package com.dietdecoder.dietdecoder.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LogDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insert(Log log);

  @Update
  void update(Log log);

  @Delete
  void delete(Log log);

  @Query("SELECT * FROM log_table ORDER BY dateTime")
  List<Ingredient> daoGetAllLog();

}
