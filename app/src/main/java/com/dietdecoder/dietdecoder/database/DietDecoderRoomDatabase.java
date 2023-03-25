package com.dietdecoder.dietdecoder.database;

import android.content.Context;
import android.graphics.LinearGradient;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLogDao;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredient.IngredientDao;
import com.dietdecoder.dietdecoder.database.recipe.Recipe;
import com.dietdecoder.dietdecoder.database.recipe.RecipeDao;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.database.symptom.SymptomDao;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLog;
import com.dietdecoder.dietdecoder.database.symptomlog.SymptomLogDao;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FoodLog.class, Ingredient.class, Recipe.class, Symptom.class,
        SymptomLog.class},
        version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DietDecoderRoomDatabase extends RoomDatabase {



  public abstract FoodLogDao foodLogDao();
  public abstract IngredientDao ingredientDao();
  public abstract RecipeDao recipeDao();
  public abstract SymptomDao symptomDao();
  public abstract SymptomLogDao symptomLogDao();

  private static String headacheThrobbingName = "throbbing headache";

  private static volatile DietDecoderRoomDatabase INSTANCE;

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
          Ingredient ingredient = ingredientDaoWriteExecute();
          foodLogDaoWriteExecute(ingredient);
          recipeDaoWriteExecute(ingredient.getIngredientId());
          UUID symptomId = symptomDaoWriteExecute();

          symptomLogDaoWriteExecute(symptomId, headacheThrobbingName);
        });

      }
    }; //end sRoomDatabaseCallback


  public static DietDecoderRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {

      synchronized (DietDecoderRoomDatabase.class) {

        if (INSTANCE == null) {

          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              DietDecoderRoomDatabase.class, "food_log_database"
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
//  static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//    @Override
//    public void migrate(SupportSQLiteDatabase database) {
//
//// Since we didn't alter the table, there's nothing else to do here.
//    }//end public void migrate
//  }; //end static final Migration

  static final Ingredient ingredientDaoWriteExecute(){

    //dao.deleteAll();

    IngredientDao ingredientDao = INSTANCE.ingredientDao();

    Ingredient ingredient = new Ingredient("Rice", "Wild Foods", "None", 0.0, "g");
    ingredientDao.daoInsertIngredient(ingredient);

    ingredient = new Ingredient("Corn tortilla", "El Rancho", "tyramine", 1.1, "mL");
    ingredientDao.daoInsertIngredient(ingredient);

    return ingredient;

  }

  static final void foodLogDaoWriteExecute(Ingredient ingredient){

    //dao.deleteAll();
    FoodLogDao foodLogDao = INSTANCE.foodLogDao();
    //dao.deleteAll();

    FoodLog foodLog = new FoodLog(ingredient.getIngredientId(), ingredient.getIngredientName());
    foodLogDao.daoFoodLogInsert(foodLog);
  }

  static final void recipeDaoWriteExecute(UUID ingredientId){

    //dao.deleteAll();
    RecipeDao recipeDao = INSTANCE.recipeDao();
    //dao.deleteAll();

    Recipe recipe = new Recipe(ingredientId, "Taco",  "Food");
    recipeDao.daoRecipeInsert(recipe);
//    recipe = new Recipe("pinto bean","Taco",  "Food");
//    recipeDao.daoRecipeInsert(recipe);
//    recipe = new Recipe("brown rice","Taco",  "Food";
//            recipeDao.daoRecipeInsert(recipe);
  }

static final UUID symptomDaoWriteExecute(){
  SymptomDao symptomDao = INSTANCE.symptomDao();
  //symptomDao.deleteAll();

  // Neurological related
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "aphasia",
                  "Difficulty focusing, feeling like you aren't in your body, difficulty finding words, general brain fog.",
                  "neurological",
                  "disassociation", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "low energy level",
                  "Energy level super low. Hard to move, think, care. Not in a depressed like they don't exist anymore way, just in a everything is blurry and it's like moving in slow motion while the world is in normal speed.",
                  "neurological",
                  "energy", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "high energy level",
                  "Energy level higher than normal. Hard to sit still, everything is exciting and feels fresh and ready to take over the world.",
                  "neurological",
                  "energy", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "dizziness",
                  "Physical spot in the world feels disturbed even without moving.",
                  "neurological",
                  "orientation", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "vertigo",
                  "Body feels it's moving even when not or distorted motions when head does move. Spinning or tilting feeling.",
                  "neurological",
                  "orientation", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "anxiety",
                  "Heart aches or throbs too fast, like it's being stabbed and the stabbing is forcing it to run faster. Stomach coils in on itself. Not hungry. Feels like throwing up. Mind races, difficulty focusing on non-anxious thoughts.",
                  "neurological",
                  "mental", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "depression",
                  "Listlessness. Things normally enjoyed aren't anymore. Difficulty moving or speaking or listening. Not hungry.",
                  "neurological",
                  "mental", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Mood swings or imbalances",
                  "Mood swings or imbalances",
                  "neurological",
                  "mental", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Irritability",
                  "Irritability",
                  "neurological",
                  "mental", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Inattentiveness",
                  "Inattentiveness",
                  "neurological",
                  "mental", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Lack of concentration",
                  "Lack of concentration",
                  "neurological",
                  "mental", Boolean.TRUE, 0, 3600)
  );

  symptomDao.symptomDaoInsert(
          new Symptom(
                  "light sensitivity",
                  "Seeing sources of light hurts the eyes.",
                  "neurological",
                  "sensitivity", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "sound sensitivity",
                  "Hearing hurts the ears.",
                  "neurological",
                  "sensitivity", Boolean.FALSE, 0, 3600)
  );


  // Aura related
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "distinct aura",
                  "Inability to see. Displayed like fuzzy TV static with edges distinct from rest" +
                          " of vision.",
                  "neurological",
                  "aura", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "complex aura",
                  "Inability to see parts or all of vision. Can have fuzzy electronic static in the visionless spots, but can also be blurry or indistinguishable.",
                  "neurological",
                  "aura", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "sensory aura",
                  "Bad tingles like blood rushing back to a spot that fell asleep, over the whole" +
                          " body.",
                  "neurological",
                  "aura", Boolean.TRUE, 0, 3600)
  );


  // Stomach related
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "stomach nausea",
                  "Difficulty eating without gagging or throwing up. Feeling like throwing up. Not hungry.",
                  "gut and digestive",
                  "nausea", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "throat nausea",
                  "Difficulty breathing without gagging or throwing up.",
                  "throat",
                  "nausea", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "regurgitation",
                  "Feels as if stomach contents have come back up to the throat or mouth.",
                  "throat",
                  "nausea", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "stomach pain",
                  "Throbbing or roiling in stomach, like something's trying to chew or stab its way out.",
                  "gut and digestive",
                  "ache, throb, stab", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "vomiting",
                  "Throwing up.",
                  "gut and digestive",
                  "urge", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "diarrhea",
                  "Loose, liquid, or watery bowel movement.",
                  "gut and digestive",
                  "urge", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "bloody bowel movement",
                  "Blood in bowel movement.",
                  "gut and digestive",
                  "urge", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "constipation",
                  "Hard and dry bowel movement, difficult to pass.",
                  "gut and digestive",
                  "urge", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "bloating",
                  "Stomach feels tight, pressurized, too full.",
                  "gut and digestive",
                  "urge", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "stomach cramping",
                  "Throbbing pain in the gut area.",
                  "gut and digestive",
                  "throb", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "excessive farting",
                  "Farting more often than before, or more than 20 times a day.",
                  "gut and digestive",
                  "throb", Boolean.FALSE, 0, 3600)
  );


  // Reproductive system
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "menstrual blood",
                  "On your period. Blood coming out onto underwear, pants, or tampons or pads or while going to the bathroom.",
                  "reproductive",
                  "cycle", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "back cramping",
                  "Throbbing pain in the lower spine area.",
                  "back",
                  "throb", Boolean.TRUE, 0, 3600)
  );

  //Cardio related
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "heartburn",
                  "Heart burns like its surface is covered in hot embers that are trying to set everything around it on fire. Burning moves up around the neck and throat or as discomfort that feels like it’s located behind the breastbone. Bending over or lying down can make it feel worse. ",
                  "cardio",
                  "burn", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Arrhythmia",
                  "Arrhythmia",
                  "cardio",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Fast beating",
                  "Fast beating",
                  "cardio",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Fluttering heart rate",
                  "Fluttering",
                  "cardio",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Tachycardia",
                  "Tachycardia",
                  "cardio",
                  "pain", Boolean.FALSE, 0, 3600)
  );

  //Muscles related
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "body ache",
                  "Difficulty moving. Standing or exerting body causes slow throb of ache in muscles being used and worsens other symptoms.",
                  "muscles",
                  "ache", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "chest pain",
                  "chest pain",
                  "muscles",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "arm pain",
                  "pain in arm. Can be symptom of heart attack if in conjunction with difficulty breathing, sweating, dizziness.",
                  "muscles",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "jaw pain",
                  "pain in jaw. Can be symptom of heart attack if in conjunction with difficulty breathing, sweating, dizziness.",
                  "muscles",
                  "pain", Boolean.FALSE, 0, 3600)
  );

  // Headache related
  Symptom headacheThrobbing = new Symptom(
          headacheThrobbingName,
          "Head throbbing with pain behind the temples. Pulses with more pain on triggering sensations like light, sound.",
          "headache",
          "throb", Boolean.TRUE, 0, 3600);
  symptomDao.symptomDaoInsert(headacheThrobbing);
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "burning headache",
                  "Brain burning like it's on fire. Can no longer sense.",
                  "headache",
                  "burn", Boolean.TRUE, 0, 3600)
  );

  // Skin related
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "rosacea",
                  "Redness, pimples, swelling, and small and superficial dilated blood vessels. Often, the nose, cheeks, forehead, and chin are most involved.",
                  "skin",
                  "mark", Boolean.TRUE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "rash",
                  "Skin may change color, itch, become warm, bumpy, chapped, dry, cracked or blistered, swell, or be painful. Multiple kinds.",
                  "skin",
                  "itch", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "hives",
                  "Kind of skin rash with red, raised, itchy bumps. Hives may burn or sting.",
                  "skin",
                  "itch, burn, sting", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "psoriasis",
                  "These areas of skin are red, or purple on some people with darker skin, dry, itchy, and scaly.",
                  "skin",
                  "itch", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "eczema",
                  "Skin itchiness, dry skin, rashes, scaly patches, blisters and skin infections.",
                  "skin",
                  "itch", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "acne",
                  "Pimples, blackheads or whiteheads, oily skin, and possible scarring.",
                  "skin",
                  "mark", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "sweating",
                  "Sweating",
                  "skin",
                  "mark", Boolean.FALSE, 0, 3600)
  );

  //Respiratory symptoms
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "congestion",
                  "congestion",
                  "respiratory",
                  "stuffy", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "asthma",
                  "asthma",
                  "respiratory",
                  "breathing", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Runny nose",
                  "Runny nose",
                  "respiratory",
                  "breathing", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Sneezing",
                  "Sneezing",
                  "respiratory",
                  "urge", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Chronic cough",
                  "Chronic cough",
                  "respiratory",
                  "breathing", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Dry cough",
                  "Persistent dry cough.",
                  "respiratory",
                  "breathing", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Rhinitis",
                  "Rhinitis",
                  "respiratory",
                  "breathing", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Difficulty breathing",
                  "Difficulty breathing",
                  "respiratory",
                  "breathing", Boolean.FALSE, 0, 3600)
  );



  //Circulatory symptoms
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Circulatory collapse",
                  "Circulatory collapse",
                  "circulatory",
                  "circulatory", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Hypotension",
                  "Hypotension",
                  "circulatory",
                  "circulatory", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Hypertension",
                  "Hypertension",
                  "circulatory",
                  "circulatory", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Constant shivers or chills",
                  "Constant shivers or chills",
                  "circulatory",
                  "circulatory", Boolean.FALSE, 0, 3600)
  );

  //Other
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Fibromyalgia",
                  "Fibromyalgia",
                  "other",
                  "other", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Watery eyes",
                  "Watery eyes",
                  "other",
                  "other", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Fatigue",
                  "Fatigue",
                  "other",
                  "other", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Sleep issues",
                  "Sleep issues",
                  "other",
                  "other", Boolean.FALSE, 0, 3600)
  );

  //Throat
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "Oedema",
                  "Swelling often around eyes, mouth and throat.",
                  "throat",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "sore throat",
                  "sore throat",
                  "throat",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "bitter taste in the back of the throat ",
                  "bitter taste in the back of the throat ",
                  "throat",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "sour taste in the mouth ",
                  "sour taste in the mouth ",
                  "throat",
                  "pain", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "sore throat",
                  "sore throat",
                  "throat",
                  "pain", Boolean.FALSE, 0, 3600)
  );

  //Mouth
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "bad breath",
                  "bad breath",
                  "mouth",
                  "mark", Boolean.FALSE, 0, 3600)
  );
  symptomDao.symptomDaoInsert(
          new Symptom(
                  "tooth enamel damage",
                  "damage to tooth enamel due to excess acid ",
                  "mouth",
                  "mark", Boolean.FALSE, 0, 3600)
  );

  return headacheThrobbing.getSymptomId();
}

static final void symptomLogDaoWriteExecute(UUID symptomId, String symptomName){

  SymptomLogDao symptomLogDao = INSTANCE.symptomLogDao();
  //dao.deleteAll();

  SymptomLog symptomLog = new SymptomLog(symptomId, symptomName);
  symptomLogDao.daoSymptomLogInsert(symptomLog);
}
} //end LogRoomDatabase
