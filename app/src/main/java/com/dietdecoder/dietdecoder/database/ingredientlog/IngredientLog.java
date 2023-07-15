package com.dietdecoder.dietdecoder.database.ingredientlog;

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


@Entity(tableName = "ingredient_log_table")
@TypeConverters(Converters.class)
public class IngredientLog {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "ingredientLogId")
  private UUID ingredientLogId;

  // when the log was made
  @ColumnInfo(name = "instantLogged")
  private Instant instantLogged;

  // I hate how long it is but changing it is my theory, maybe it's messing with the id in
  // ingredient itself
  @NonNull
  @ColumnInfo(name = "ingredientLogIngredientId")
  private UUID ingredientLogIngredientId;

  // how much of it there was
  @ColumnInfo(name = "ingredientLogIngredientAmountNumber")
  private Double ingredientLogIngredientAmountNumber;

  // the unit for how much of it there was
  @ColumnInfo(name = "ingredientLogIngredientAmountUnit")
  private String ingredientLogIngredientAmountUnit;

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
  public IngredientLog(UUID ingredientLogIngredientId) {
    // if only ingredient was given
    // empty for description
    // changed default is an hour from now
    // default amount is lowest
    this(ingredientLogIngredientId,
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS),
            Instant.now().minus(1, ChronoUnit.DAYS),
            1.0, "gram");
    // TODO set defaults for each ingredient time duration if this is first time for
    //  setting that ingredient
  }

  public IngredientLog(@NonNull UUID ingredientLogIngredientId,
                       Instant instantConsumed,
                       Instant instantCooked,
                       Instant instantAcquired,
                       Double ingredientLogIngredientAmountNumber,
                       String ingredientLogIngredientAmountUnit) {
    this.ingredientLogId = UUID.randomUUID();
    this.instantLogged = Instant.now();
    this.instantConsumed = instantConsumed;
    this.instantCooked = instantCooked;
    this.instantAcquired = instantAcquired;
    this.ingredientLogIngredientId = ingredientLogIngredientId;
    this.ingredientLogIngredientAmountNumber = ingredientLogIngredientAmountNumber;
    this.ingredientLogIngredientAmountUnit = ingredientLogIngredientAmountUnit;
  }

  // basic getters for the parameters
  // setters
  // and toString
  public UUID getIngredientLogId() {
    return(this.ingredientLogId);
  }

  public UUID getIngredientLogIngredientId() {
    return(this.ingredientLogIngredientId);
  }

  public Double getIngredientLogIngredientAmountNumber() {
    return(this.ingredientLogIngredientAmountNumber);
  }
  public String getIngredientLogIngredientAmountUnit() {
    return(this.ingredientLogIngredientAmountUnit);
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
    String amountString = "\n Amount: ";
    if (this.ingredientLogIngredientAmountNumber != null){
      amountString =
              amountString.concat(this.ingredientLogIngredientAmountNumber.toString() + " " +
                      this.ingredientLogIngredientAmountUnit +
              "\n");
    }
      return ("ID:" + this.ingredientLogId.toString() + "\n"+
              "Logged at: " + this.instantLogged.toString() + "\n"
              + "\nIngredient log's ingredient ID: " + this.ingredientLogIngredientId + "\n"
              + "\nWhen Consumed: " + this.instantConsumed.toString() + "\n"
        + "\nWhen Cooked/Ended: " + this.instantCooked.toString() + "\n"
        + "\nIngredient Acquired: " + this.instantAcquired + "\n"
              + amountString);
  }//end toString


  public void setIngredientLogId(UUID ingredientLogId) {
    this.ingredientLogId = ingredientLogId;
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

  public void setIngredientLogIngredientId(UUID ingredientLogIngredientId) {
    this.ingredientLogIngredientId = ingredientLogIngredientId;
  }

  public void setIngredientLogIngredientAmount(Double ingredientLogIngredientAmountNumber) {
    this.ingredientLogIngredientAmountNumber = ingredientLogIngredientAmountNumber;
  }

  public void setIngredientLogIngredientAmountUnit(String ingredientLogIngredientAmountUnit) {
    this.ingredientLogIngredientAmountUnit = ingredientLogIngredientAmountUnit;
  }




}