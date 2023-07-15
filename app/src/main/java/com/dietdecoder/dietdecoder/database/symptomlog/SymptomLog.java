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
  @ColumnInfo(name = "symptomLogId")
  private UUID symptomLogId;

  // when the log was made
  @ColumnInfo(name = "instantLogged")
  private Instant instantLogged;

  @NonNull
  @ColumnInfo(name = "symptomLogSymptomId")
  private UUID symptomLogSymptomId;

  @NonNull
  @ColumnInfo(name = "symptomLogSymptomName")
  private String symptomLogSymptomName;

  @ColumnInfo(name = "symptomLogSymptomDescription")
  private String symptomLogSymptomDescription;

  // how bad it was out of 10
  @ColumnInfo(name = "symptomLogSymptomIntensity")
  private Integer symptomLogSymptomIntensity;

  // when it began
  @ColumnInfo(name = "instantBegan")
  private Instant instantBegan;

  // when it changed to ending or less severe
  @ColumnInfo(name = "instantChanged")
  private Instant instantChanged;


// use Ignore for which parameters are optional
  @Ignore
  public SymptomLog(UUID symptomLogSymptomId, String symptomLogSymptomName) {
    // if only symptom was given
    // empty for description
    // changed default is an hour from now
    // default concern and intensity is lowest
    this(symptomLogSymptomId, symptomLogSymptomName,
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS),
            " ",
            1);
    // TODO set defaults for each symptom time duration if this is first time for
    //  setting that symptom
  }

  public SymptomLog(@NonNull UUID symptomLogSymptomId, @NonNull String symptomLogSymptomName,
                    Instant instantBegan,
                    Instant instantChanged,
                    String symptomLogSymptomDescription,
                    Integer symptomLogSymptomIntensity) {
    this.symptomLogId = UUID.randomUUID();
    this.symptomLogSymptomName = symptomLogSymptomName;
    this.instantLogged = Instant.now();
    this.instantBegan = instantBegan;
    this.instantChanged = instantChanged;
    this.symptomLogSymptomId = symptomLogSymptomId;
    this.symptomLogSymptomDescription = symptomLogSymptomDescription;
    this.symptomLogSymptomIntensity = symptomLogSymptomIntensity;
  }

  // basic getters for the parameters
  // setters
  // and toString
  public UUID getSymptomLogId() {
    return(this.symptomLogId);
  }

  public UUID getSymptomLogSymptomId() {
    return(this.symptomLogSymptomId);
  }
  public String getSymptomLogSymptomName() {
    return(this.symptomLogSymptomName);
  }
  public String getSymptomLogSymptomDescription() {
    return(this.symptomLogSymptomDescription);
  }

  public Integer getSymptomLogSymptomIntensity() {
    return(this.symptomLogSymptomIntensity);
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
    if (this.symptomLogSymptomIntensity != null){
      intensityString = intensityString.concat(this.symptomLogSymptomIntensity.toString() + "\n");
    }
      return ("ID:" + this.symptomLogId.toString() + "\n"+
              "Symptom Name:" + this.symptomLogSymptomName + "\n"+
              "Logged at: " + this.instantLogged.toString() + "\n"
              + "\nSymptom ID: " + this.symptomLogSymptomId + "\n"
              + "\nWhen Began: " + this.instantBegan.toString() + "\n"
        + "\nWhen Changed/Ended: " + this.instantChanged.toString() + "\n"
        + "\nSymptom Description: " + this.symptomLogSymptomDescription + "\n" + intensityString);
  }//end toString


  public void setSymptomLogId(UUID symptomLogId) {
    this.symptomLogId = symptomLogId;
  }

  public void setSymptomLogSymptomName(String symptomLogSymptomName) {
    this.symptomLogSymptomName = symptomLogSymptomName;
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

  public void setSymptomLogSymptomId(UUID symptomLogSymptomId) {
    this.symptomLogSymptomId = symptomLogSymptomId;
  }
  public void setSymptomLogSymptomDescription(String symptomLogSymptomDescription) {
    this.symptomLogSymptomDescription = symptomLogSymptomDescription;
  }

  public void setSymptomLogSymptomIntensity(Integer symptomLogSymptomIntensity) {
    this.symptomLogSymptomIntensity = symptomLogSymptomIntensity;
  }




}
