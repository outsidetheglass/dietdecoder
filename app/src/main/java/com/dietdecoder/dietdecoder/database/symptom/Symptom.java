package com.dietdecoder.dietdecoder.database.symptom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Entity(tableName = "symptom_table")
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
    this(symptomName, "", "Other", "", Boolean.FALSE);
  }
  public Symptom(@NonNull String symptomName,
                 String symptomDescription,
                 String symptomCategory, String symptomSufferType, Boolean symptomToTrack) {
    this.mSymptomId = UUID.randomUUID();
    this.mSymptomName = symptomName;
    this.mSymptomDescription = symptomDescription;
    this.mSymptomCategory = symptomCategory;
    //this.mSymptomIntensity = symptomIntensity;
    this.mSymptomSufferType = symptomSufferType;
    this.mSymptomToTrack = symptomToTrack;
  }

  public UUID getSymptomId(){return this.mSymptomId;}
  public String getSymptomName(){return this.mSymptomName;}
  public String getSymptomDescription(){return this.mSymptomDescription;}
  public String getSymptomCategory(){return this.mSymptomCategory;}
//  public Integer getSymptomIntensity(){return this.mSymptomIntensity;}
public String getSymptomSufferType(){return this.mSymptomSufferType;}
  public Boolean getSymptomToTrack(){return this.mSymptomToTrack;}


  public void setSymptomId(UUID id) {
    this.mSymptomId = id;
  }
  public void setSymptomName(String name) {
    this.mSymptomName = name;
  }
  public void setSymptomDescription(String description) {
    this.mSymptomDescription = description;
  }
  public void setSymptomCategory(String category) {
    this.mSymptomCategory = category;
  }
  public void setSymptomSufferType(String sufferType) {
    this.mSymptomSufferType = sufferType;
  }
  public void setSymptomToTrack(Boolean track) {
    this.mSymptomToTrack = track;
  }

} //end Symptom Entity
