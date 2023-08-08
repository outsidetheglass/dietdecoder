package com.dietdecoder.dietdecoder.database.symptomlog;

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
import java.util.Calendar;
import java.util.UUID;


@Entity(tableName = "symptom_log_table")
@TypeConverters(Converters.class)
public class SymptomLog {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "logId")
  private UUID logId;

  // when the log was made
  @ColumnInfo(name = "instantLogged")
  private Instant instantLogged;

  @NonNull
  @ColumnInfo(name = "logSymptomId")
  private UUID logSymptomId;

  @ColumnInfo(name = "logSymptomDescription")
  private String logSymptomDescription;

  // how bad it was out of 10
  @ColumnInfo(name = "logSymptomIntensity")
  private Integer logSymptomIntensity;

  // when it began
  @ColumnInfo(name = "instantBegan")
  private Instant instantBegan;

  // when it changed to ending or less severe
  @ColumnInfo(name = "instantChanged")
  private Instant instantChanged;


// use Ignore for which parameters are optional
  @Ignore
  public SymptomLog(UUID logSymptomId) {
    // if only symptom was given
    // empty for description
    // changed default is an hour from now
    // default concern and intensity is lowest
    this(logSymptomId,
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS),
            " ",
            1);
    // TODO set defaults for each symptom time duration if this is first time for
    //  setting that symptom
  }

  public SymptomLog(@NonNull UUID logSymptomId,
                    Instant instantBegan,
                    Instant instantChanged,
                    String logSymptomDescription,
                    Integer logSymptomIntensity) {
    this.logId = UUID.randomUUID();
    this.instantLogged = Instant.now();
    this.instantBegan = instantBegan;
    this.instantChanged = instantChanged;
    this.logSymptomId = logSymptomId;
    this.logSymptomDescription = logSymptomDescription;
    this.logSymptomIntensity = logSymptomIntensity;
  }

  // basic getters for the parameters
  // setters
  // and toString
  public UUID getLogId() {
    return(this.logId);
  }

  public UUID getLogSymptomId() {
    return(this.logSymptomId);
  }
  public String getLogSymptomDescription() {
    return(this.logSymptomDescription);
  }

  public Integer getLogSymptomIntensity() {
    return(this.logSymptomIntensity);
  }

  public Instant getInstantLogged() {
    return(this.instantLogged);
  }
  public Instant getInstantBegan() {
    return(this.instantBegan);
  }
  public Instant getInstantChanged() {
    return(this.instantChanged);
  }

  public String toString() {
    String intensityString = "\n Intensity: ";
    if (this.logSymptomIntensity != null){
      intensityString = intensityString.concat(this.logSymptomIntensity.toString() + "\n");
    }
      return ("ID:" + this.logId.toString() + "\n"+
              "Logged at: " + this.instantLogged.toString() + "\n"
              + "\nSymptom ID: " + this.logSymptomId + "\n"
              + "\nWhen Began: " + this.instantBegan.toString() + "\n"
        + "\nWhen Changed/Ended: " + this.instantChanged.toString() + "\n"
        + "\nSymptom Description: " + this.logSymptomDescription + "\n" + intensityString);
  }//end toString


  public void setLogId(UUID logId) {
    this.logId = logId;
  }


  public void setInstantLogged(Instant instant) {
    this.instantLogged = instant;
  }
  public void setInstantBegan(Instant instant) {
    this.instantBegan = instant;
  }
  public void setInstantChanged(Instant instant) {
    this.instantChanged = instant;
  }

  public void setLogSymptomId(UUID logSymptomId) {
    this.logSymptomId = logSymptomId;
  }
  public void setLogSymptomDescription(String logSymptomDescription) {
    this.logSymptomDescription = logSymptomDescription;
  }

  public void setLogSymptomIntensity(Integer logSymptomIntensity) {
    this.logSymptomIntensity = logSymptomIntensity;
  }




}
