package com.dietdecoder.dietdecoder.database.ingredientlog;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.Converters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Entity(tableName = "ingredient_log_table")
@TypeConverters(Converters.class)
public class IngredientLog {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "logId")
  private UUID logId;

  // when the log was made
  @ColumnInfo(name = "instantLogged")
  private Instant instantLogged;

  // I hate how long it is but changing it is my theory, maybe it's messing with the id in
  // ingredient itself
  @NonNull
  @ColumnInfo(name = "logIngredientId")
  private UUID logIngredientId;

  // how much of it there was
//  @ColumnInfo(name = "ingredientLogIngredientAmountNumber")
//  private Double ingredientLogIngredientAmountNumber;

  // the unit for how much of it there was
  @ColumnInfo(name = "logIngredientSubjectiveAmount")
  private String logIngredientSubjectiveAmount;

  // when it began
  @ColumnInfo(name = "instantConsumed")
  private Instant instantConsumed;

  // when it was cooked
  @ColumnInfo(name = "instantCooked")
  private Instant instantCooked;

  // when it was acquired/harvested/bought
  @ColumnInfo(name = "instantAcquired")
  private Instant instantAcquired;


// use Ignore for which parameters are optional
  @Ignore
  public IngredientLog(UUID logIngredientId) {
    // if only ingredient was given
    // empty for description
    // changed default is an hour from now
    // default amount is lowest
    this(logIngredientId,
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS),
            Instant.now().minus(1, ChronoUnit.DAYS),
            "tons");
    // TODO set defaults for each ingredient time duration if this is first time for
    //  setting that ingredient
  }

  public IngredientLog(@NonNull UUID logIngredientId,
                       Instant instantConsumed,
                       Instant instantCooked,
                       Instant instantAcquired,
                       String logIngredientSubjectiveAmount) {
    this.logId = UUID.randomUUID();
    this.instantLogged = Instant.now();
    this.instantConsumed = instantConsumed;
    this.instantCooked = instantCooked;
    this.instantAcquired = instantAcquired;
    this.logIngredientId = logIngredientId;
    this.logIngredientSubjectiveAmount = logIngredientSubjectiveAmount;
  }

  // basic getters for the parameters
  // setters
  // and toString
  public UUID getLogId() {
    return(this.logId);
  }

  public UUID getLogIngredientId() {
    return(this.logIngredientId);
  }

//  public Double getIngredientLogIngredientAmountNumber() {
//    return(this.ingredientLogIngredientAmountNumber);
 // }
  public String getLogIngredientSubjectiveAmount() {
    return(this.logIngredientSubjectiveAmount);
  }

  public Instant getInstantLogged() {
    return(this.instantLogged);
  }
  public Instant getInstantConsumed() {
    return(this.instantConsumed);
  }
  public Instant getInstantAcquired() {
    return(this.instantAcquired);
  }
  public Instant getInstantCooked() {
    return(this.instantCooked);
  }

  public String toString() {
      return ( "\n Amount: "+this.logIngredientSubjectiveAmount + "\n"+"ID:" + this.logId.toString() + "\n"+
              "Logged at: " + Util.localDateTimeFromInstant(this.instantLogged).toString() + "\n"
              + "\nIngredient log's ingredient ID: " + this.logIngredientId + "\n"
              + "\nWhen Consumed: " + Util.localDateTimeFromInstant(this.instantConsumed).toString() + "\n"
        + "\nWhen Cooked/Ended: " + Util.localDateTimeFromInstant(this.instantCooked).toString() + "\n"
        + "\nIngredient Acquired: " + Util.localDateTimeFromInstant(this.instantAcquired).toString() + "\n"
              );
  }//end toString


  public void setLogId(UUID logId) {
    this.logId = logId;
  }

  public void setInstantLogged(Instant instantLogged) {
    this.instantLogged = instantLogged;
  }
  public void setInstantConsumed(Instant instantConsumed) {
    this.instantConsumed = instantConsumed;
  }
  public void setInstantCooked(Instant instantCooked) {
    this.instantCooked = instantCooked;
  }
  public void setInstantAcquired(Instant instantAcquired) {
    this.instantAcquired = instantAcquired;
  }

  public void setLogIngredientId(UUID logIngredientId) {
    this.logIngredientId = logIngredientId;
  }


  public void setLogIngredientSubjectiveAmount(String ingredientLogIngredientAmount) {
    this.logIngredientSubjectiveAmount = ingredientLogIngredientAmount;
  }




}
