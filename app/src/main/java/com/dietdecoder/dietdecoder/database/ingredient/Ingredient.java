package com.dietdecoder.dietdecoder.database.ingredient;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dietdecoder.dietdecoder.database.Converters;

import java.util.UUID;


@Entity(tableName = "ingredient_table")
@TypeConverters({Converters.class})
public class Ingredient {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "ingredientId")
  private UUID ingredientId;

  @ColumnInfo(name = "ingredientName")
  private String ingredientName;

  @ColumnInfo(name = "ingredientBrand")
  private String ingredientBrand;


  // type of chemical and its amount
  // i.e. fish gets tyramines fast and potato is high in solanine and tequila has less histamines than wine
  @ColumnInfo(name = "ingredientChemicalName")
  private String ingredientChemicalName;
  @ColumnInfo(name = "ingredientChemicalAmountNumber")
  private Double ingredientChemicalAmountNumber;
  @ColumnInfo(name = "ingredientChemicalAmountUnit")
  private String ingredientChemicalAmountUnit;

  @Ignore
  public Ingredient(String ingredientName){
    this(ingredientName, "", "", 0.0, "");
  }

  public Ingredient(String ingredientName, String ingredientBrand, String ingredientChemicalName,
                    Double ingredientChemicalAmountNumber,
                    String ingredientChemicalAmountUnit) {
    this.ingredientId = UUID.randomUUID();
    this.ingredientName = ingredientName;
    this.ingredientBrand = ingredientBrand;
    this.ingredientChemicalName = ingredientChemicalName;
    this.ingredientChemicalAmountNumber = ingredientChemicalAmountNumber;
    this.ingredientChemicalAmountUnit = ingredientChemicalAmountUnit;
  }


  public UUID getIngredientId(){return ingredientId;}
  public void setIngredientId(UUID id){this.ingredientId = id;}

  public String getIngredientName(){return this.ingredientName;}
  public void setIngredientName(String string){this.ingredientName = string;}

  public String getIngredientBrand(){return this.ingredientBrand;}
  public void setIngredientBrand(String string){this.ingredientBrand = string;}


  public String getIngredientChemicalName(){return this.ingredientChemicalName;}
  public void setIngredientChemicalName(String string){this.ingredientChemicalName = string;}

  public Double getIngredientChemicalAmountNumber() {return this.ingredientChemicalAmountNumber;}
  public void setIngredientChemicalAmountNumber(Double ingredientChemicalAmountNumber){this.ingredientChemicalAmountNumber =
          ingredientChemicalAmountNumber;}

  public String getIngredientChemicalAmountUnit() {return this.ingredientChemicalAmountUnit;}
  public void setIngredientChemicalAmountUnit(String string){this.ingredientChemicalAmountUnit =
          string;}

  @Override
  public String toString() {
    return "Ingredient{" +
            "ingredientId='" + ingredientId + '\'' +
            "ingredientName='" + ingredientName + '\'' +
            "ingredientBrand='" + ingredientBrand + '\'' +
      ", ingredientChemicalName='" + ingredientChemicalName + '\'' +
      ", ingredientChemicalAmountNumber=" + ingredientChemicalAmountNumber +
      ", ingredientChemicalAmountUnit='" + ingredientChemicalAmountUnit + '\'' +
      '}';
  }

} //end Ingredient Entity
