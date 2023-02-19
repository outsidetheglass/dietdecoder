package com.dietdecoder.dietdecoder.database.symptomlog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dietdecoder.dietdecoder.database.Converters;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLogDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SymptomLog.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SymptomLogRoomDatabase extends RoomDatabase {



  public abstract SymptomLogDao symptomLogDao();

  private static volatile SymptomLogRoomDatabase INSTANCE;

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
          SymptomLogDao symptomLogDao = INSTANCE.symptomLogDao();
          //dao.deleteAll();

          SymptomLog symptomLog = new SymptomLog("Headache");
          symptomLogDao.daoSymptomLogInsert(symptomLog);
        });

      }
    }; //end sRoomDatabaseCallback


  public static SymptomLogRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {

      synchronized (SymptomLogRoomDatabase.class) {

        if (INSTANCE == null) {

          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              SymptomLogRoomDatabase.class, "symptom_log_database"
            ) //end INSTANCE

            .addCallback(sRoomDatabaseCallback)
              // allow main thread queries can lock the UI for a long time, not the best way
              // to get access to livedata main thread
              // TODO fix this
              .allowMainThreadQueries()
//            .addMigrations(MIGRATION_1_2)
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
    }//end public void migrate
  }; //end static final Migration


} //end LogRoomDatabase
