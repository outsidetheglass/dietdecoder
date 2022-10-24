package com.dietdecoder.dietdecoder.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity(tableName = "grocery_table")
public class Grocery {

  @PrimaryKey
  @ColumnInfo(name = "dateTimeAcquired")
  private LocalDateTime mDateTimeAcquired;

  @ColumnInfo(name = "ingredient")
  private Ingredient mIngredient;

  @ColumnInfo(name = "brand")
  private String mBrand;

  @ColumnInfo(name = "dateTimeExpiration")
  private LocalDateTime mDateTimeExpiration;


  public Grocery(LocalDateTime dateTimeAcquired, Ingredient ingredient, String brand,
                 LocalDateTime dateTimeExpiration) {
    this.mDateTimeAcquired = dateTimeAcquired;
    this.mIngredient = ingredient;
    this.mBrand = brand;
    this.mDateTimeExpiration = dateTimeExpiration;
  }

  public LocalDateTime getGroceryDateTimeAcquired() {
    return(this.mDateTimeAcquired);
  }
  public String getGroceryIngredientName() {
    return(this.mIngredient.getIngredientName());
  }
  public String getGroceryBrand() {
    return(this.mBrand);
  }
  public LocalDateTime getGroceryDateTimeExpiration() {
    return(this.mDateTimeExpiration);
  }

  public String toString() {
    //TODO fix this so if one of them is null it doesn't break
    return("Acquired at: " + this.mDateTimeAcquired
      + "Ingredient Name: " + this.mIngredient.getIngredientName()
      + "Brand of Ingredient: " +  this.mBrand
      + "Expiration of Ingredient: " + this.mDateTimeExpiration);
  }
}
