package com.dietdecoder.dietdecoder.database.chemical;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity(tableName = "chemical_table")
public class Chemical {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "chemicalId")
  private UUID mChemicalId;

  @ColumnInfo(name = "chemicalName")
  private String mChemicalName;

  @ColumnInfo(name = "chemicalAmountNumber")
  private Double mChemicalAmountNumber;
  // unit to say milligrams per 100grams or per cup (240g) etc
  @ColumnInfo(name = "chemicalAmountUnit")
  private String mChemicalAmountUnit;

  // e.g. enzyme, amino acid, etc
  @ColumnInfo(name = "chemicalType")
  private String mChemicalType;

  // e.g. phytonutrient, DAO, histamine liberator
  @ColumnInfo(name = "chemicalNutrientGroup")
  private String mChemicalNutrientGroup;

  @Ignore
  public Chemical(@NonNull String chemicalName){
    // default type is unknown
    this(chemicalName, 0.0,"Not available", "Not available", "Not available");
  }

  @Ignore
  public Chemical(@NonNull String chemicalName, Double chemicalAmountNumber, String chemicalAmountUnit){
    // default type is unknown
    this(chemicalName, chemicalAmountNumber, chemicalAmountUnit, "Not available", "Not available");
  }


  public Chemical(@NonNull String chemicalName, Double chemicalAmountNumber, String chemicalAmountUnit, String chemicalType, String chemicalNutrientGroup) {
    this.mChemicalId = UUID.randomUUID();
    this.mChemicalName = chemicalName;
    this.mChemicalAmountNumber = chemicalAmountNumber;
    this.mChemicalAmountUnit = chemicalAmountUnit;
    this.mChemicalType = chemicalType;
    this.mChemicalNutrientGroup = chemicalNutrientGroup;
  }

  public UUID getmChemicalId(){return this.mChemicalId;}

  public void setmChemicalId(UUID id){this.mChemicalId = id;}

  public String getChemicalName(){return this.mChemicalName;}

  public String getChemicalNameAmount(){return (this.mChemicalName + this.mChemicalAmountNumber.toString() + this.mChemicalAmountUnit);}

  public String getChemicalAmount(){return (this.mChemicalAmountNumber.toString() + this.mChemicalAmountUnit);}

  public String getChemicalType(){return this.mChemicalType;}

  public String getChemicalNutrientGroup(){return this.mChemicalNutrientGroup;}

  public String toString(){
    return (
      "Name: " + mChemicalName
        + "\nAmount of it: " + this.getChemicalAmount()
        + "\nType: " + this.mChemicalType
        + "\nNutrient group: " + this.mChemicalNutrientGroup
    );
  }

} //end Chemical Entity
