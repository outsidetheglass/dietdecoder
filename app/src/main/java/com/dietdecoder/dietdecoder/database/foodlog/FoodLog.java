package com.dietdecoder.dietdecoder.database.foodlog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.Converters;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.UUID;


@Entity(tableName = "food_log_table")
@TypeConverters(Converters.class)
public class FoodLog {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "foodLogId")
  private UUID foodLogId;

  // when the log was made
  @NonNull
  @ColumnInfo(name = "instantLogged")
  private Instant instantLogged;

  @NonNull
  @ColumnInfo(name = "ingredientName")
  private String ingredientName;

  @NonNull
  @ColumnInfo(name = "ingredientId")
  private UUID ingredientId;

  // when it was consumed
  @ColumnInfo(name = "instantConsumed")
  private Instant instantConsumed;

  // when it was bought or pulled from the garden, as close to time it died and began the decomposition process as possible
  @ColumnInfo(name = "instantAcquired")
  private Instant instantAcquired;

  // when it was cooked or processed, the last part of the preparation process before being eaten
  @ColumnInfo(name = "instantCooked")
  private Instant instantCooked;

  // if purchased or known, when the food is expected to expire
  @ColumnInfo(name = "instantExpiration")
  private Instant instantExpiration;


// use Ignore for which parameters are optional
  @Ignore
  public FoodLog(UUID ingredientId, String ingredientName) {
    // if only ingredient was given
    // empty for brand
    // acquired was yesterday and cooked and eaten now
    this(ingredientId, ingredientName,
            Instant.now(),
            Instant.now().minus(1, ChronoUnit.DAYS),
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.DAYS)
    );
  }

  public FoodLog(@NonNull UUID ingredientId, @NonNull String ingredientName,
                 Instant instantConsumed,
                 Instant instantAcquired,
                 Instant instantCooked,
                 Instant instantExpiration) {
    this.foodLogId = UUID.randomUUID();
    this.ingredientId = ingredientId;
    this.instantLogged = Instant.now();
    this.instantConsumed = instantConsumed;
    this.instantAcquired = instantAcquired;
    this.instantCooked = instantCooked;
    this.instantExpiration = instantExpiration;
  }


  // helpful functions for calculating off of the times
  public Instant getFoodLogDateTimeInstant() {
    return(this.instantConsumed);
  }
  public String getFoodLogDateTimeString() {
    return Util.stringFromInstant(this.instantConsumed);
  }

  public Calendar getFoodLogCalendar() {
    return Util.calendarFromInstant(this.instantLogged);
  }

  // basic getters for the parameters
  // setters
  // and toString
  public String toString() {
    //if one of them is null it doesn't break
    if (this.instantAcquired != null && this.instantCooked != null && this.instantExpiration != null ) {
      return ("Logged at: " + this.instantLogged + "\n"
        + "\nConsumed at: " + this.instantConsumed + "\n"
        + "\nAcquired at: " + this.instantAcquired + "\n"
        + "\nIngredient Cooked at: " + this.instantCooked + "\n"
        + "\nIngredient ID: " + this.ingredientId + "\n");
    } else
    {
      return ("Logged at: " + this.instantLogged);
    }
  }//end toString

  public UUID getFoodLogId() {
    return foodLogId;
  }
  public void setFoodLogId(UUID id) {
    this.foodLogId = id;
  }

  public Instant getInstantLogged() {
    return instantLogged;
  }
  public void setInstantLogged(Instant instant) {
    this.instantLogged = instant;
  }


  public Instant getInstantConsumed() {
    return instantConsumed;
  }
  public void setInstantConsumed(Instant instant) {
    this.instantConsumed = instant;
  }


  public UUID getIngredientId() {
    return ingredientId;
  }
  public void setIngredientId(UUID id) {
    this.ingredientId = id;
  }

  public String getIngredientName() {
    return ingredientName;
  }
  public void setIngredientName(String name) {
    this.ingredientName = name;
  }

  public Instant getInstantAcquired() {
    return instantAcquired;
  }
  public void setInstantAcquired(Instant instantAcquired) {
    this.instantAcquired = instantAcquired;
  }

  public Instant getInstantCooked() {
    return instantCooked;
  }
  public void setInstantCooked(Instant instantCooked) {
    this.instantCooked = instantCooked;
  }

  public Instant getInstantExpiration() {
    return instantExpiration;
  }
  public void setInstantExpiration(Instant instant) {
    this.instantExpiration = instant;
  }



//    //TODO: fix age so it can calculate how old logged grocery is
//    //TODO: or maybe just put this logic into exporting logic
//  public String getFoodLogAge() {
////    Integer groceryAge = this.mDatetime - this.mLog.getLogDateTimeAcquired()
//    return("Date Acquired: " + this.instantAcquired + "\nDate Cooked: " + this.instantCooked +
//            "\nDate Used: " + this.mDateTime);
//  }



}
