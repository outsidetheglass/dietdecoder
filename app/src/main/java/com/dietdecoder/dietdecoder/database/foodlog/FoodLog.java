package com.dietdecoder.dietdecoder.database.foodlog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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

// use Ignore for which parameters are optional
  @Ignore
  public FoodLog(String ingredientName) {
    // if only ingredient was given
    // empty for brand
    // acquired was yesterday and cooked now and expires tomorrow
    this(ingredientName, "", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now());
  }

  public FoodLog(@NonNull String ingredientName, String brand,
                 Instant dateTimeAcquired, Instant dateTimeCooked) {
    this.mFoodLogId = UUID.randomUUID();
    this.mDateTime = Instant.now();
    this.mIngredientName = ingredientName;
    this.mBrand = brand;
    this.mDateTimeAcquired = dateTimeAcquired;
    this.mDateTimeCooked = dateTimeCooked;
  }


  // helpful functions for calculating off of the times
  public Instant getFoodLogDateTimeInstant() {
    return(this.mDateTime);
  }
  public String getFoodLogDateTimeString() {
    Calendar logCalendar = GregorianCalendar.from(this.mDateTime.atZone( ZoneId.systemDefault() )) ;
    String fullLogTime = logCalendar.getTime().toString();


    String logNumberDayOfMonth = String.valueOf(logCalendar.get(Calendar.DAY_OF_MONTH));
    String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
      "Sept", "Oct", "Nov", "Dec" };
    String logMonth = months[logCalendar.get(Calendar.MONTH)];

    String logYear = String.valueOf(logCalendar.get(Calendar.YEAR));
    String logShortYear = logYear.substring(2);

    String logDate =  logMonth + "/" + logNumberDayOfMonth + "/" + logShortYear;


    String[] days = new String[] { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };
    String logDayOfWeek = days[logCalendar.get(Calendar.DAY_OF_WEEK) - 1];

    String logTime = logCalendar.get(Calendar.HOUR_OF_DAY) + ":" + logCalendar.get(Calendar.MINUTE);


    return(logTime + " " + logDayOfWeek + ", " + logMonth + " " + logNumberDayOfMonth + " " + logYear);
  }
  public Calendar getFoodLogDateTimeCalendar() {
    return( GregorianCalendar.from(this.mDateTime.atZone( ZoneId.systemDefault() )) );
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
