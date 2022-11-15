package com.dietdecoder.dietdecoder.database.log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.database.Converters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Entity(tableName = "log_table")
@TypeConverters(Converters.class)
public class Log {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "logId")
  private UUID mLogId;

  @ColumnInfo(name = "dateTime")
  private Instant mDateTime;

  // change ingredient type to string ingredientName
  @NonNull
  @ColumnInfo(name = "ingredientName")
  private String mIngredientName;

  @ColumnInfo(name = "brand")
  private String mBrand;

  // when it was bought or pulled from the garden, as close to time it died and began the decomposition process as possible
  @ColumnInfo(name = "dateTimeAcquired")
  private Instant mDateTimeAcquired;

  // when it was cooked or processed, the last part of the preparation process before being eaten
  @ColumnInfo(name = "dateTimeCooked")
  private Instant mDateTimeCooked;

  @ColumnInfo(name = "dateTimeExpiration")
  private Instant mDateTimeExpiration;

// use Ignore for which parameters are optional
  @Ignore
  public Log(String ingredientName) {
    // if only ingredient was given
    // empty for brand
    // acquired was yesterday and cooked now and expires tomorrow
    this(ingredientName, "", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS));
  }

  public Log(@NonNull String ingredientName, String brand,
             Instant dateTimeAcquired, Instant dateTimeCooked,
             Instant dateTimeExpiration) {
    this.mLogId = UUID.randomUUID();
    this.mDateTime = Instant.now();
    this.mIngredientName = ingredientName;
    this.mBrand = brand;
    this.mDateTimeAcquired = dateTimeAcquired;
    this.mDateTimeCooked = dateTimeCooked;
    this.mDateTimeExpiration = dateTimeExpiration;
  }



  public Instant getLogDateTime() {
    return(this.mDateTime);
  }
  public String getLogAge() {
    //TODO: fix age so it can calculate how old logged grocery is
    //TODO: or maybe just put this logic into exporting logic
//    Integer groceryAge = this.mDatetime - this.mLog.getLogDateTimeAcquired()
    return("Date Acquired: " + this.mDateTimeAcquired + "\nDate Cooked: " + this.mDateTimeCooked + "\nDate Used: " + this.mDateTime);
  }
  public Instant getLogDateTimeAcquired() {
    return(this.mDateTimeAcquired);
  }
  public String getLogIngredientName() {
    return(this.mIngredientName);
  }
  public String getLogBrand() {
    return(this.mBrand);
  }
  public Instant getLogDateTimeExpiration() {
    return(this.mDateTimeExpiration);
  }
  public Instant getLogDateTimeCooked() {
    return(this.mDateTimeCooked);
  }

  public String toString() {
    //if one of them is null it doesn't break
    if (this.mDateTimeAcquired != null && this.mIngredientName != null && this.mBrand != null && this.mDateTimeExpiration != null ) {
      return ("Logged at: " + this.mDateTime
        + "Acquired at: " + this.mDateTimeAcquired
        + "\nIngredient Cooked at: " + this.mDateTimeCooked
        + "\nIngredient Name: " + this.mIngredientName
        + "\nBrand of Ingredient: " + this.mBrand
        + "\nExpiration of Ingredient: " + this.mDateTimeExpiration);
    } else
    {
      return ("Logged at: " + this.mDateTime);
    }
  }//end toString

  public UUID getMLogId() {
    return mLogId;
  }
  public void setMLogId(UUID id) {
    this.mLogId = id;
  }

  public Instant getMDateTime() {
    return mDateTime;
  }
  public void setMDateTime(Instant dateTime) {
    this.mDateTime = dateTime;
  }

  public String getMIngredientName() {
    return mIngredientName;
  }
  public void setIngredientName(String ingredientName) {
    this.mIngredientName = ingredientName;
  }

  public String getMBrand() {
    return mBrand;
  }
  public void setMBrand(String brand) {
    this.mBrand = brand;
  }

  public Instant getMDateTimeAcquired() {
    return mDateTimeAcquired;
  }
  public void setMDateTimeAcquired(Instant dateTimeAcquired) {
    this.mDateTimeAcquired = dateTimeAcquired;
  }


  public Instant getMDateTimeCooked() {
    return mDateTimeCooked;
  }
  public void setMDateTimeCooked(Instant dateTimeCooked) {
    this.mDateTimeCooked = dateTimeCooked;
  }


  public Instant getMDateTimeExpiration() {
    return mDateTimeExpiration;
  }
  public void setMDateTimeExpiration(Instant dateTimeExpiration) {
    this.mDateTimeExpiration = dateTimeExpiration;
  }
}
