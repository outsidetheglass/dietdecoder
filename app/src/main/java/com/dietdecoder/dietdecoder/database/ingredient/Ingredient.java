package com.dietdecoder.dietdecoder.database.ingredient;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.database.UUIDConverter;
import com.dietdecoder.dietdecoder.database.chemical.Chemical;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity(tableName = "ingredient_table")
@TypeConverters({UUIDConverter.class})
public class Ingredient {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "ingredientId")
  private UUID mIngredientId;

  @ColumnInfo(name = "ingredientName")
  private String mIngredientName;

  // type of chemical and its amount
  // i.e. fish gets tyramines fast and potato is high in solanine and tequila has less histamines than wine
  @ColumnInfo(name = "ingredientChemicalName")
  private String mIngredientChemicalName;
  @ColumnInfo(name = "ingredientChemicalAmountNumber")
  private Double mIngredientChemicalAmountNumber;
  @ColumnInfo(name = "ingredientChemicalAmountUnit")
  private String mIngredientChemicalAmountUnit;

//  @ColumnInfo(name = "ingredientExpiration")
//  private LocalDateTime mIngredientExpiration;

  @Ignore
  public Ingredient(String ingredientName){
    this(ingredientName, "Not available", 0.0, "Not available");
  }

  public Ingredient(String ingredientName, String ingredientChemicalName, Double ingredientChemicalAmountNumber,
                    String ingredientChemicalAmountUnit) {
    this.mIngredientId = UUID.randomUUID();
    this.mIngredientName = ingredientName;
    this.mIngredientChemicalName = ingredientChemicalName;
    this.mIngredientChemicalAmountNumber = ingredientChemicalAmountNumber;
    this.mIngredientChemicalAmountUnit = ingredientChemicalAmountUnit;
  }

// with expiration
//  public Ingredient(@NonNull String ingredientName,
//                    Chemical ingredientChemical /*,LocalDateTime ingredientExpiration*/ ) {
  // default is five days expired
//  LocalDateTime.now().plusDays(5)
//    this.mIngredientName = ingredientName;
//    this.mIngredientChemicalNameAmount = ingredientChemical.getChemicalNameAmount();
//    //this.mIngredientExpiration = ingredientExpiration;
//  }


  public UUID getMIngredientId(){return mIngredientId;}

  public void setMIngredientId(UUID id){this.mIngredientId = id;}

  public String getIngredientName(){return this.mIngredientName;}

  public String getIngredientChemicalName(){return this.mIngredientChemicalName;}

  public Double getIngredientChemicalAmountNumber() {return this.mIngredientChemicalAmountNumber;}

  public String getIngredientChemicalAmountUnit() {return this.mIngredientChemicalAmountUnit;}

  @Override
  public String toString() {
    return "Ingredient{" +
      "mIngredientName='" + mIngredientName + '\'' +
      ", mIngredientChemicalName='" + mIngredientChemicalName + '\'' +
      ", mIngredientChemicalAmountNumber=" + mIngredientChemicalAmountNumber +
      ", mIngredientChemicalAmountUnit='" + mIngredientChemicalAmountUnit + '\'' +
      '}';
  }

  //public LocalDateTime getIngredientExpiration(){return this.mIngredientExpiration;}

} //end Ingredient Entity
