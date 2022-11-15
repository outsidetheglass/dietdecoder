package com.dietdecoder.dietdecoder.database.ingredient;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Database migrations are beyond the scope of this codelab,
// so we set exportSchema to false here to avoid a build warning.
// TODO: In a real app, you should consider setting a directory for Room to use to export the schema
// so you can check the current schema into your version control system.
@Database(entities = {Ingredient.class}, version = 1, exportSchema = false)
public abstract class IngredientRoomDatabase extends RoomDatabase {



  public abstract IngredientDao ingredientDao();

  private static volatile IngredientRoomDatabase INSTANCE;

  private static final int NUMBER_OF_THREADS = 4;

  public static final ExecutorService databaseWriteExecutor =
    Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {

        super.onCreate(db);

        // If you want to keep data through app restarts,
        // comment out the following block
        databaseWriteExecutor.execute(() -> {
          // Populate the database in the background.
          // If you want to start with more words, just add them.
          IngredientDao dao = INSTANCE.ingredientDao();
          //dao.deleteAll();

          Ingredient ingredient = new Ingredient("Miso paste", "tyramine", 1.1, "mL");
          dao.daoInsert(ingredient);
          ingredient = new Ingredient("Rice", "None", 0.0, "g");
          dao.daoInsert(ingredient);
        });

      }
    }; //end sRoomDatabaseCallback


  public static IngredientRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {

      synchronized (IngredientRoomDatabase.class) {

        if (INSTANCE == null) {

          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              IngredientRoomDatabase.class, "ingredient_database"
            ) //end INSTANCE

            .addCallback(sRoomDatabaseCallback)
            //.addMigrations(MIGRATION_1_2)
            .build();

        }//end if null

      }//end synchronized

    }//end if statement

    return INSTANCE;

  } //end getDatabase


  // how to add migration
//  static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//    @Override
//    public void migrate(SupportSQLiteDatabase database) {
//
//// Since we didn't alter the table, there's nothing else to do here.
//    }//end migrate
//
//  }; //end Migration

} //end IngredientRoomDatabase
