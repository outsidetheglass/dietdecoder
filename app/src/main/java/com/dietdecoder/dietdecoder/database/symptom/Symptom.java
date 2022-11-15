package com.dietdecoder.dietdecoder.database.symptom;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "symptom_table")
public class Symptom {

  @PrimaryKey
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

  public Symptom(@NonNull String symptomName,
                 String symptomDescription,
                 String symptomCategory, String symptomSufferType) {
    this.mSymptomName = symptomName;
    this.mSymptomDescription = symptomDescription;
    this.mSymptomCategory = symptomCategory;
    //this.mSymptomIntensity = symptomIntensity;
    this.mSymptomSufferType = symptomSufferType;
  }

  public String getSymptomName(){return this.mSymptomName;}

  public String getSymptomDescription(){return this.mSymptomDescription;}

  public String getSymptomCategory(){return this.mSymptomCategory;}
//  public Integer getSymptomIntensity(){return this.mSymptomIntensity;}
  public String getSymptomSufferType(){return this.mSymptomSufferType;}

} //end Symptom Entity
