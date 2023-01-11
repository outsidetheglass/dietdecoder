package com.dietdecoder.dietdecoder.database.foodlog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dietdecoder.dietdecoder.database.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodLog.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FoodLogRoomDatabase extends RoomDatabase {



  public abstract FoodLogDao foodLogDao();

  private static volatile FoodLogRoomDatabase INSTANCE;

  private static final int NUMBER_OF_THREADS = 4;

  public static final ExecutorService databaseWriteExecutor =
    Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    private static Callback sRoomDatabaseCallback = new Callback() {
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {

        super.onCreate(db);

        // If you want to keep data through app restarts,
        // comment out the following block
        databaseWriteExecutor.execute(() -> {
          // Populate the database in the background.
          // If you want to start with more words, just add them.
          FoodLogDao foodLogDao = INSTANCE.foodLogDao();
          //dao.deleteAll();

          FoodLog foodLog = new FoodLog("Oat milk");
          foodLogDao.daoFoodLogInsert(foodLog);
        });

      }
    }; //end sRoomDatabaseCallback


  public static FoodLogRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {

      synchronized (FoodLogRoomDatabase.class) {

        if (INSTANCE == null) {

          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              FoodLogRoomDatabase.class, "food_log_database"
            ) //end INSTANCE

            .addCallback(sRoomDatabaseCallback)
            .addMigrations(MIGRATION_1_2)
            .build();

        }//end if null

      }//end synchronized

    }//end if statement

    return INSTANCE;

  } //end getDatabase


  // how to add migration
  static final Migration MIGRATION_1_2 = new Migration(1, 2) {

    @Override
    public void migrate(SupportSQLiteDatabase database) {

// Since we didn't alter the table, there's nothing else to do here.
    }//end migrate

  }; //end Migration

} //end LogRoomDatabase
