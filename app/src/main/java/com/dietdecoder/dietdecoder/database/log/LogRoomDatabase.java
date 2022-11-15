package com.dietdecoder.dietdecoder.database.log;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dietdecoder.dietdecoder.database.UUIDConverter;
import com.dietdecoder.dietdecoder.database.chemical.Chemical;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.log.Log;
import com.dietdecoder.dietdecoder.database.log.LogDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Log.class}, version = 1, exportSchema = false)
@TypeConverters({UUIDConverter.class})
public abstract class LogRoomDatabase extends RoomDatabase {



  public abstract LogDao logDao();

  private static volatile LogRoomDatabase INSTANCE;

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
          LogDao dao = INSTANCE.logDao();
          //dao.deleteAll();

          Log log = new Log("Oat milk");
          dao.daoLogInsert(log);
        });

      }
    }; //end sRoomDatabaseCallback


  public static LogRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {

      synchronized (LogRoomDatabase.class) {

        if (INSTANCE == null) {

          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              LogRoomDatabase.class, "log_database"
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

} //end LogRoomDatabase
