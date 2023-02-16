package com.dietdecoder.dietdecoder.database.recipe;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Database migrations are beyond the scope of this codelab,
// so we set exportSchema to false here to avoid a build warning.
// TODO: In a real app, you should consider setting a directory for Room to use to export the schema
// so you can check the current schema into your version control system.
@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeRoomDatabase extends RoomDatabase {



  public abstract RecipeDao recipeDao();

  private static volatile RecipeRoomDatabase RECIPEINSTANCE;

  private static final int NUMBER_OF_THREADS = 4;

  public static final ExecutorService recipeDatabaseWriteExecutor =
    Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    private static Callback sRecipeRoomDatabaseCallback = new Callback() {
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {

        super.onCreate(db);

        // If you want to keep data through app restarts,
        // comment out the following block
        recipeDatabaseWriteExecutor.execute(() -> {
          // Populate the database in the background.
          // If you want to start with more words, just add them.
          RecipeDao recipeDao = RECIPEINSTANCE.recipeDao();
          //dao.deleteAll();

          Recipe recipe = new Recipe("Taco", "corn tortilla", "Food");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Taco", "pinto bean", "Food");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Taco", "brown rice", "Food");

          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Egg cheese rice", "brown rice", "Food");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Egg cheese rice", "cheddar cheese", "Food");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Egg cheese rice", "egg", "Food");
          recipeDao.daoRecipeInsert(recipe);

          recipe = new Recipe("Oat milk tea", "oat milk", "Drink");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Oat milk tea", "ginger", "Drink");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Oat milk tea", "lavendar", "Drink");
          recipeDao.daoRecipeInsert(recipe);
          recipe = new Recipe("Oat milk tea", "maple syrup", "Drink");
          recipeDao.daoRecipeInsert(recipe);
        });

      }
    }; //end sRecipeRoomDatabaseCallback


  public static RecipeRoomDatabase getRecipeDatabase(final Context context) {

    if (RECIPEINSTANCE == null) {

      synchronized (RecipeRoomDatabase.class) {

        if (RECIPEINSTANCE == null) {

          RECIPEINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              RecipeRoomDatabase.class, "recipe_database"
            ) //end RECIPEINSTANCE

            .addCallback(sRecipeRoomDatabaseCallback)
                  // allow main thread queries can lock the UI for a long time, not the best way
                  // to get access to livedata main thread
                  // TODO fix this
                  .allowMainThreadQueries()
            //.addMigrations(MIGRATION_1_2)
            .build();

        }//end if null

      }//end synchronized

    }//end if statement

    return RECIPEINSTANCE;

  } //end getRecipeDatabase


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

} //end RecipeRoomDatabase
