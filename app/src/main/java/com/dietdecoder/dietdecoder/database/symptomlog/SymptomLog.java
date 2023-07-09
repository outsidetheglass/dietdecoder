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
  @ColumnInfo(name = "symptomId")
  private UUID symptomId;

  @NonNull
  @ColumnInfo(name = "symptomName")
  private String symptomName;

  @ColumnInfo(name = "description")
  private String description;

  @ColumnInfo(name = "intensityScale")
  private Integer intensityScale;

  // when it began
  @ColumnInfo(name = "instantBegan")
  private Instant instantBegan;

  // when it changed to ending or less severe
  @ColumnInfo(name = "instantChanged")
  private Instant instantChanged;


// use Ignore for which parameters are optional
  @Ignore
  public SymptomLog(UUID symptomId, String symptomName) {
    // if only symptom was given
    // empty for description
    // changed default is an hour from now
    // default concern and intensity is lowest
    this(symptomId, symptomName,
            Instant.now(),
            Instant.now().plus(1, ChronoUnit.HOURS),
            " ",
            1);
    // TODO set defaults for each symptom time duration if this is first time for
    //  setting that symptom
  }

  public SymptomLog(@NonNull UUID symptomId, @NonNull String symptomName,
                    Instant instantBegan,
                    Instant instantChanged,
                    String description,
                    Integer intensityScale) {
    this.symptomLogId = UUID.randomUUID();
    this.symptomName = symptomName;
    this.instantLogged = Instant.now();
    this.instantBegan = instantBegan;
    this.instantChanged = instantChanged;
    this.symptomId = symptomId;
    this.description = description;
    this.intensityScale = intensityScale;
  }

  // basic getters for the parameters
  // setters
  // and toString
  public UUID getSymptomLogId() {
    return(this.symptomLogId);
  }

  public UUID getSymptomId() {
    return(this.symptomLogId);
  }
  public String getSymptomName() {
    return(this.symptomName);
  }
  public String getDescription() {
    return(this.description);
  }

  public Integer getIntensityScale() {
    return(this.intensityScale);
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
    if (this.intensityScale != null){
      intensityString = intensityString.concat(this.intensityScale.toString() + "\n");
    }
      return ("ID:" + this.symptomLogId.toString() + "\n"+
              "Logged at: " + this.instantLogged.toString() + "\n"
              + "\nSymptom ID: " + this.symptomLogId + "\n"
              + "\nWhen Began: " + this.instantBegan.toString() + "\n"
        + "\nWhen Changed/Ended: " + this.instantChanged.toString() + "\n"
        + "\nSymptom Description: " + this.description + "\n" + intensityString);
  }//end toString


  public void setSymptomLogId(UUID id) {
    this.symptomLogId = id;
  }

  public void setSymptomName(String name) {
    this.symptomName = name;
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

  public void setSymptomId(UUID id) {
    this.symptomLogId = id;
  }
  public void setDescription(String string) {
    this.description = string;
  }

  public void setIntensityScale(Integer integer) {
    this.intensityScale = integer;
  }




}
