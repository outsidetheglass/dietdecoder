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
  @ColumnInfo(name = "id")
  private UUID id;

  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "brand")
  private String brand;



  @Ignore
  public Ingredient(String name){
    this(name, "");
  }

  public Ingredient(String name, String brand) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.brand = brand;
  }


  public UUID getId(){return id;}
  public void setId(UUID id){this.id = id;}

  public String getName(){return this.name;}
  public void setName(String string){this.name = string;}

  public String getBrand(){return this.brand;}
  public void setBrand(String string){this.brand = string;}



  @Override
  public String toString() {
    return "Ingredient{" +
            "id='" + id + '\'' +
            "name='" + name + '\'' +
            "brand='" + brand + '\'' +
      '}';
  }

} //end Ingredient Entity
