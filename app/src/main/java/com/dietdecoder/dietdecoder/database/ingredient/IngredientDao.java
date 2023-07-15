package com.dietdecoder.dietdecoder.database.ingredient;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.List;
import java.util.UUID;

@Dao
public interface IngredientDao {

  //TODO: fix my Query's by replacing them with Ingredient instead of strings
  // probably need to make ID's for that to work

  // allowing the insert multiple times by passing a
  // conflict resolution strategy
  // choosing ignore because ingredients may seem the same but will have different brands, etc
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void daoInsertIngredient(Ingredient ingredient);

  @Delete
  void daoDeleteIngredient(Ingredient ingredient);

  @Query("UPDATE ingredient_table SET ingredientChemicalName = :newIngredientChemicalName WHERE ingredientChemicalName = :oldIngredientChemicalName AND ingredientName = :oldIngredientName")
  void daoUpdateIngredientChemicalName(String oldIngredientName, String oldIngredientChemicalName, String newIngredientChemicalName);

  @Query("UPDATE ingredient_table SET ingredientName = :newIngredientName WHERE ingredientName = :oldIngredientName AND ingredientChemicalName = :oldIngredientChemicalName")
  void daoUpdateIngredientName(String oldIngredientName, String oldIngredientChemicalName, String newIngredientName);


  // Sort by specific chemical and alphabetize them by symptom name
  @Query("SELECT * FROM ingredient_table ORDER BY ingredientName ASC")
  List<Ingredient> daoGetAllIngredientArrayList();


  // LiveData is a lifecycle library class for live database access
  // Select all ingredients from table and alphabetize them by name
  @Query("SELECT * FROM ingredient_table ORDER BY ingredientName ASC")
  LiveData<List<Ingredient>> daoGetAlphabetizedIngredients();

  // Sort by specific chemical and alphabetize them by ingredient name
  @Query("SELECT * FROM ingredient_table WHERE ingredientChemicalName LIKE :daoChemicalName ORDER BY ingredientName ASC")
  LiveData<List<Ingredient>> daoGetIngredientsWithChemicalName(String daoChemicalName);

  @Query("SELECT * FROM ingredient_table WHERE ingredientName = :daoIngredientName")
  Ingredient daoGetIngredientFromName(String daoIngredientName);


  @Query("SELECT * FROM ingredient_table WHERE ingredientName = :daoIngredientName AND ingredientChemicalName = :daoIngredientChemicalName")
  Ingredient daoGetIngredientFromNameChemicalName(String daoIngredientName, String daoIngredientChemicalName);


  @Query("SELECT * FROM ingredient_table WHERE ingredientName LIKE :searchIngredientName")
  Ingredient daoGetIngredientFromSearchName(String searchIngredientName);

  @Query("SELECT * FROM ingredient_table WHERE ingredientId LIKE :searchIngredientId")
  Ingredient daoGetIngredientFromId(UUID searchIngredientId);


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
//  @Query("SELECT ingredient FROM ingredient_table")
//  String getIngredient(Ingredient ingredient);

}
