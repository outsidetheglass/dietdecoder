package com.dietdecoder.dietdecoder.database.foodlog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.activity.Util;
import com.dietdecoder.dietdecoder.database.Converters;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;


@Entity(tableName = "food_log_table")
@TypeConverters(Converters.class)
public class FoodLog {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "foodLogId")
  private UUID mFoodLogId;

  // dateTime is when the log was made
  @ColumnInfo(name = "dateTime")
  private Instant mDateTime;

  // change ingredient type to string ingredientName
  @NonNull
  @ColumnInfo(name = "ingredientName")
  private String mIngredientName;

  @ColumnInfo(name = "brand")
  private String mBrand;

  // when it was consumed
  @ColumnInfo(name = "dateTimeConsumed")
  private Instant mDateTimeConsumed;

  // when it was bought or pulled from the garden, as close to time it died and began the decomposition process as possible
  @ColumnInfo(name = "dateTimeAcquired")
  private Instant mDateTimeAcquired;

  // when it was cooked or processed, the last part of the preparation process before being eaten
  @ColumnInfo(name = "dateTimeCooked")
  private Instant mDateTimeCooked;


// use Ignore for which parameters are optional
  @Ignore
  public FoodLog(String ingredientName) {
    // if only ingredient was given
    // empty for brand
    // acquired was yesterday and cooked and eaten now
    this(ingredientName, "", Instant.now(), Instant.now().minus(1, ChronoUnit.DAYS), Instant.now());
  }

  public FoodLog(@NonNull String ingredientName, String brand,
                 Instant dateTimeConsumed, Instant dateTimeAcquired, Instant dateTimeCooked) {
    this.mFoodLogId = UUID.randomUUID();
    this.mDateTime = Instant.now();
    this.mDateTimeConsumed = dateTimeConsumed;
    this.mIngredientName = ingredientName;
    this.mBrand = brand;
    this.mDateTimeAcquired = dateTimeAcquired;
    this.mDateTimeCooked = dateTimeCooked;
  }


  // helpful functions for calculating off of the times
  public Instant getFoodLogDateTimeInstant() {
    return(this.mDateTimeConsumed);
  }
  public String getFoodLogDateTimeString() {
    return Util.stringFromInstant(this.mDateTimeConsumed);
  }

  public Calendar getFoodLogDateTimeCalendar() {
    return Util.calendarFromInstant(this.mDateTime);
  }

  public String getFoodLogAge() {
    //TODO: fix age so it can calculate how old logged grocery is
    //TODO: or maybe just put this logic into exporting logic
//    Integer groceryAge = this.mDatetime - this.mLog.getLogDateTimeAcquired()
    return("Date Acquired: " + this.mDateTimeAcquired + "\nDate Cooked: " + this.mDateTimeCooked + "\nDate Used: " + this.mDateTime);
  }


  // basic getters for the parameters
  // setters
  // and toString
  public String toString() {
    //if one of them is null it doesn't break
    if (this.mDateTimeAcquired != null && this.mIngredientName != null && this.mBrand != null ) {
      return ("Logged at: " + this.mDateTime + "\n"
        + "\nConsumed at: " + this.mDateTimeConsumed + "\n"
        + "\nAcquired at: " + this.mDateTimeAcquired + "\n"
        + "\nIngredient Cooked at: " + this.mDateTimeCooked + "\n"
        + "\nIngredient Name: " + this.mIngredientName + "\n"
        + "\nBrand of Ingredient: " + this.mBrand + "\n");
    } else
    {
      return ("Logged at: " + this.mDateTime);
    }
  }//end toString

  public UUID getMFoodLogId() {
    return mFoodLogId;
  }
  public void setMFoodLogId(UUID id) {
    this.mFoodLogId = id;
  }

  public Instant getMDateTime() {
    return mDateTime;
  }
  public void setMDateTime(Instant dateTime) {
    this.mDateTime = dateTime;
  }


  public Instant getMDateTimeConsumed() {
    return mDateTimeConsumed;
  }
  public void setMDateTimeConsumed(Instant dateTimeConsumed) {
    this.mDateTimeConsumed = dateTimeConsumed;
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


}
