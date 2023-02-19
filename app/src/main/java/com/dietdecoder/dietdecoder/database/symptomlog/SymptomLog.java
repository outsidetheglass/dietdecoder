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
  private UUID mSymptomLogId;

  // when the log was made
  @ColumnInfo(name = "instantLogged")
  private Instant mInstantLogged;

  @NonNull
  @ColumnInfo(name = "symptomName")
  private String mSymptomName;

  @ColumnInfo(name = "description")
  private String mDescription;

  @ColumnInfo(name = "concernScale")
  private Integer mConcernScale;

  @ColumnInfo(name = "severityScale")
  private Integer mSeverityScale;

  // when it began
  @ColumnInfo(name = "instantBegan")
  private Instant mInstantBegan;

  // when it changed to ending or less severe
  @ColumnInfo(name = "instantChanged")
  private Instant mInstantChanged;


// use Ignore for which parameters are optional
  @Ignore
  public SymptomLog(String symptomName) {
    // if only symptom was given
    // empty for description
    // changed default is an hour from now
    // default concern and severity is lowest
    this(symptomName, Instant.now(), Instant.now().plus(1, ChronoUnit.HOURS), " ", 1, 1);
  }

  public SymptomLog(@NonNull String symptomName,
                    Instant instantBegan, Instant instantChanged, String description,
                    Integer severityScale, Integer concernScale) {
    this.mSymptomLogId = UUID.randomUUID();
    this.mInstantLogged = Instant.now();
    this.mInstantBegan = instantBegan;
    this.mInstantChanged = instantChanged;
    this.mSymptomName = symptomName;
    this.mDescription = description;
    this.mConcernScale = concernScale;
    this.mSeverityScale = severityScale;
  }

  // basic getters for the parameters
  // setters
  // and toString
  public UUID getSymptomLogId() {
    return(this.mSymptomLogId);
  }

  public String getSymptomName() {
    return(this.mSymptomName);
  }
  public String getDescription() {
    return(this.mDescription);
  }

  public Integer getConcernScale() {
    return(this.mConcernScale);
  }
  public Integer getSeverityScale() {
    return(this.mSeverityScale);
  }

  public Instant getInstantLogged() {
    return(this.mInstantLogged);
  }
  public Instant getInstantBegan() {
    return(this.mInstantBegan);
  }
  public Instant getInstantChanged() {
    return(this.mInstantChanged);
  }

  public String toString() {
      return ("ID:" + this.mSymptomLogId.toString() + "\n"+
              "Logged at: " + this.mInstantLogged.toString() + "\n"
              + "\nSymptom name: " + this.mSymptomName + "\n"
              + "\nWhen Began: " + this.mInstantBegan.toString() + "\n"
        + "\nWhen Changed/Ended: " + this.mInstantChanged.toString() + "\n"
        + "\nSymptom Description: " + this.mDescription + "\n"
        + "\nConcern level: " + this.mConcernScale.toString() + "\n"
        + "\nSeverity: " + this.mSeverityScale.toString() + "\n");
  }//end toString


  public void setId(UUID id) {
    this.mSymptomLogId = id;
  }

  public void setInstantLogged(Instant instant) {
    this.mInstantLogged = instant;
  }
  public void setInstantBegan(Instant instant) {
    this.mInstantBegan = instant;
  }
  public void setInstantChanged(Instant instant) {
    this.mInstantChanged = instant;
  }

  public void setSymptomName(String string) {
    this.mSymptomName = string;
  }
  public void setDescription(String string) {
    this.mDescription = string;
  }

  public void setSeverityScale(Integer integer) {
    this.mSeverityScale = integer;
  }
  public void setConcernScale(Integer integer) {
    this.mConcernScale = integer;
  }



}
