package com.dietdecoder.dietdecoder.database.symptom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.database.Converters;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Entity(tableName = "symptom_table")
@TypeConverters(Converters.class)
public class Symptom {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "symptomId")
  private UUID mSymptomId;

  @NonNull
  @ColumnInfo(name = "symptomName")
  private String mSymptomName;

  @ColumnInfo(name = "symptomDescription")
  private String mSymptomDescription;

  //how much of a concern is this symptom for the user
  @ColumnInfo(name = "symptomConcernScale")
  private Integer mSymptomConcernScale;

  // how long this symptom usually lasts
  @ColumnInfo(name = "symptomDurationSeconds")
  private Integer mSymptomDurationSeconds;

  // like 1 for 1/10
  // This will just go in Log
  // TODO figure out how to make symptom intensity descriptions different per different intensity.
  // i.e. 5/10 will have a different description for aphasia than for heartburn
  // important because when symptomatic, figuring out how to phrase how you hurt is very hard
  // prompts for choose between this description and that for the intensities would really help
//  @ColumnInfo(name = "symptomIntensity")
//  private Integer mSymptomIntensity;

  // this is for cardio, respiratory, skin, etc
  @ColumnInfo(name = "symptomCategory")
  private String mSymptomCategory;

  // this is burning pain, body disassociation, stabbing pain, depression, etc
  @ColumnInfo(name = "symptomSufferType")
  private String mSymptomSufferType;

  // track this symptom or not
  @ColumnInfo(name = "symptomToTrack")
  private Boolean mSymptomToTrack;


  //TODO add conditions linked with this symptom

  // use Ignore for which parameters are optional
  @Ignore
  public Symptom(String symptomName) {
    // if only symptom name was given
    // empty for description and type of suffering
    // default category is other
    this(symptomName, "", "Other", "", Boolean.FALSE, 1, 3600);
  }
  public Symptom(@NonNull String symptomName,
                 String symptomDescription,
                 String symptomCategory, String symptomSufferType, Boolean symptomToTrack,
                 Integer symptomConcernScale, Integer symptomDurationSeconds) {
    this.mSymptomId = UUID.randomUUID();
    this.mSymptomName = symptomName;
    this.mSymptomDescription = symptomDescription;
    this.mSymptomCategory = symptomCategory;
    //this.mSymptomIntensity = symptomIntensity;
    this.mSymptomSufferType = symptomSufferType;
    this.mSymptomToTrack = symptomToTrack;
    this.mSymptomConcernScale = symptomConcernScale;
    this.mSymptomDurationSeconds = symptomDurationSeconds;

  }

  public UUID getSymptomId(){return this.mSymptomId;}
  public void setSymptomId(UUID id) {
    this.mSymptomId = id;
  }

  public String getSymptomName(){return this.mSymptomName;}
  public void setSymptomName(String name) {
    this.mSymptomName = name;
  }

  public String getSymptomDescription(){return this.mSymptomDescription;}
  public void setSymptomDescription(String description) {
    this.mSymptomDescription = description;
  }

  public String getSymptomCategory(){return this.mSymptomCategory;}
  public void setSymptomCategory(String category) {
    this.mSymptomCategory = category;
  }

//  public Integer getSymptomIntensity(){return this.mSymptomIntensity;}
public String getSymptomSufferType(){return this.mSymptomSufferType;}
  public void setSymptomSufferType(String sufferType) {
    this.mSymptomSufferType = sufferType;
  }

  public Boolean getSymptomToTrack(){return this.mSymptomToTrack;}
  public void setSymptomToTrack(Boolean track) {
    this.mSymptomToTrack = track;
  }


  public Integer getSymptomConcernScale() {
    return(this.mSymptomConcernScale);
  }
  public void setSymptomConcernScale(Integer integer) {
    this.mSymptomConcernScale = integer;
  }

  public Integer getSymptomDurationSeconds() {
    return(this.mSymptomDurationSeconds);
  }
  public void setSymptomDurationSeconds(Integer integer) {
    this.mSymptomDurationSeconds = integer;
  }

  public String toString(){

    return "\nName: " + this.mSymptomName + "\nId: " + this.mSymptomId.toString();
  }
} //end Symptom Entity
