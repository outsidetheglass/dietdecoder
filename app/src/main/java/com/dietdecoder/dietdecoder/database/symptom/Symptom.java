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
import java.util.Objects;
import java.util.UUID;


@Entity(tableName = "symptom_table")
@TypeConverters(Converters.class)
public class Symptom {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private UUID mId;

  @NonNull
  @ColumnInfo(name = "name")
  private String mName;

  @ColumnInfo(name = "description")
  private String mDescription;

  //how much of a concern is this symptom for the user
  @ColumnInfo(name = "concernScale")
  private Integer mConcernScale;

  // like 1 for 1/10
  // This will just go in Log
  // TODO figure out how to make symptom intensity descriptions different per different intensity.
  // i.e. 5/10 will have a different description for aphasia than for heartburn
  // important because when symptomatic, figuring out how to phrase how you hurt is very hard
  // prompts for choose between this description and that for the intensities would really help
//  @ColumnInfo(name = "symptomIntensity")
//  private Integer mSymptomIntensity;

  // this is for cardio, respiratory, skin, etc
  @ColumnInfo(name = "category")
  private String mCategory;

  // this is burning pain, body disassociation, stabbing pain, depression, etc
  @ColumnInfo(name = "sufferType")
  private String mSufferType;

  // track this symptom or not
  @ColumnInfo(name = "trackOrNot")
  private Boolean mTrackOrNot;


  //TODO add conditions linked with this symptom

  // use Ignore for which parameters are optional
  @Ignore
  public Symptom(String name) {
    // if only symptom name was given
    // empty for description and type of suffering
    // default category is other
    this(name, "", "Other", "", Boolean.FALSE, 1);
  }
  public Symptom(@NonNull String name,
                 String description,
                 String category, String sufferType, Boolean trackOrNot,
                 Integer concernScale) {
    this.mId = UUID.randomUUID();
    this.mName = name;
    this.mDescription = description;
    this.mCategory = category;
    this.mSufferType = sufferType;
    this.mTrackOrNot = trackOrNot;
    this.mConcernScale = concernScale;

  }

  public UUID getId(){return this.mId;}
  public void setId(UUID id) {
    this.mId = id;
  }

  public String getName(){return this.mName;}
  public void setName(String name) {
    this.mName = name;
  }

  public String getDescription(){return this.mDescription;}
  public void setDescription(String description) {
    this.mDescription = description;
  }

  public String getCategory(){return this.mCategory;}
  public void setCategory(String category) {
    this.mCategory = category;
  }

public String getSufferType(){return this.mSufferType;}
  public void setSufferType(String sufferType) {
    this.mSufferType = sufferType;
  }

  public Boolean getTrackOrNot(){return this.mTrackOrNot;}
  public void setTrackOrNot(Boolean track) {
    this.mTrackOrNot = track;
  }

  public Integer getConcernScale() {
    return(this.mConcernScale);
  }
  public void setConcernScale(Integer integer) {
    this.mConcernScale = integer;
  }


  public String toString(){
    String description = "";
    if ( !Objects.isNull(this.mDescription) ) {
      description = "\nDescription: " + this.mDescription.toString();
    }
    String category = "";
    if ( !Objects.isNull(this.mCategory) ) {
      category = "\nCategory: " + this.mCategory.toString();
    }
    String sufferType = "";
    if ( !Objects.isNull(this.mSufferType) ) {
      sufferType = "\nSufferType: " + this.mSufferType.toString();
    }
    String trackOrNot = "";
    if ( !Objects.isNull(this.mTrackOrNot) ) {
      trackOrNot = "\nTrackOrNot: " + this.mTrackOrNot.toString();
    }
    String concernScale = "";
    if ( !Objects.isNull(this.mConcernScale) ) {
      concernScale = "\nConcernScale: " + this.mConcernScale.toString();
    }
    return "\nId: " + this.mId.toString() +
            "\nName: " + this.mName +
            description + category + sufferType + trackOrNot + concernScale;
  }
} //end Symptom Entity
