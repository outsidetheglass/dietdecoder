package com.dietdecoder.dietdecoder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {

  // can insert multiple times by passing IGNORE for conflict strategy
  // ingredients may seem the same but will have different brands, etc
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoInsert(Ingredient ingredient);

  @Delete
  void daoDelete(Ingredient ingredient);

  @Update(onConflict = OnConflictStrategy.IGNORE)
  void daoUpdate(Ingredient ingredient);


  // LiveData is a lifecycle library class for live database access
  // Select all ingredients from table and alphabetize them by name
  @Query("SELECT * FROM ingredient_table ORDER BY ingredientName ASC")
  LiveData<List<Ingredient>> daoGetAlphabetizedIngredients();

  // Sort by specific chemical and alphabetize them by ingredient name
  @Query("SELECT * FROM ingredient_table WHERE ingredientConcern LIKE :daoConcern ORDER BY ingredientName ASC")
  LiveData<List<Ingredient>> daoGetIngredientsWithConcern(String daoConcern);

  @Query("SELECT * FROM ingredient_table WHERE ingredientName = :daoIngredientName")
  Ingredient daoGetIngredientFromName(String daoIngredientName);

  /*

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
           "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

@Query("SELECT * FROM user WHERE first_name LIKE :search " +
       "OR last_name LIKE :search")
public List<User> findUserWithName(String search);

  @Query("DELETE FROM ingredient_table")
  void deleteAll();

   */

  //

}
