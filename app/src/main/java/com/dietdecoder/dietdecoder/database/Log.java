package com.dietdecoder.dietdecoder.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;


@Entity(tableName = "log_table")
public class Log {

  @PrimaryKey
  @ColumnInfo(name = "dateTime")
  private LocalDateTime mDatetime;

  @ColumnInfo(name = "grocery")
  private Grocery mGrocery;

//  TODO: not sure why an Ignore constructor is in several examples
//  @Ignore
//  public Log(LocalDateTime datetime, Ingredient ingredient, String brand,
//      Integer age) {
//    this(datetime, ingredient, brand, age);
//  }

  public Log(LocalDateTime datetime, Grocery grocery) {
    this.mDatetime = datetime;
    this.mGrocery = grocery;
  }

  public LocalDateTime getLogDateTime() {
    return(this.mDatetime);
  }
  public String getLogGrocery() {
    return(this.mGrocery.toString());
  }
  public String getLogAge() {
    //TODO: fix age so it can calculate how old logged grocery is
    //TODO: or maybe just put this logic into exporting logic
//    Integer groceryAge = this.mDatetime - this.mGrocery.getGroceryDateTimeAcquired()
    return("Date Acquired: " + this.mGrocery.getGroceryDateTimeAcquired() + " Date Used: " + this.mDatetime);
  }
  public LocalDateTime getLogGroceryAcquisition() {
    return(this.mGrocery.getGroceryDateTimeAcquired());
  }
}
