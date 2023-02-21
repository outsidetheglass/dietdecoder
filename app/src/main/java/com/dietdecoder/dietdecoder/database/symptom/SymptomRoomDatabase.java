package com.dietdecoder.dietdecoder.database.symptom;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dietdecoder.dietdecoder.database.symptom.Symptom;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO fix exportSchema
@Database(entities = {Symptom.class}, version = 2, exportSchema = false)
public abstract class SymptomRoomDatabase extends RoomDatabase {



  public abstract SymptomDao symptomDao();

  private static volatile SymptomRoomDatabase INSTANCE;

  private static final int NUMBER_OF_THREADS = 4;

  public Symptom mSymptomAphasia;

  public static final ExecutorService symptomDatabaseWriteExecutor =
    Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    private static Callback sRoomDatabaseCallback = new Callback() {
      @Override
      public void onCreate(@NonNull SupportSQLiteDatabase db) {

        super.onCreate(db);

        // If you want to keep data through app restarts,
        // comment out the following block
        symptomDatabaseWriteExecutor.execute(() -> {
          // Populate the database in the background.
          // If you want to start with more words, just add them.
          SymptomDao symptomDao = INSTANCE.symptomDao();
          //symptomDao.deleteAll();


          // Neurological related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "aphasia",
              "Difficulty focusing, feeling like you aren't in your body, difficulty finding words, general brain fog.",
              "neurological",
              "disassociation", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "low energy level",
              "Energy level super low. Hard to move, think, care. Not in a depressed like they don't exist anymore way, just in a everything is blurry and it's like moving in slow motion while the world is in normal speed.",
              "neurological",
              "energy", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "high energy level",
              "Energy level higher than normal. Hard to sit still, everything is exciting and feels fresh and ready to take over the world.",
              "neurological",
              "energy", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "dizziness",
              "Physical spot in the world feels disturbed even without moving.",
              "neurological",
              "orientation", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "vertigo",
              "Body feels it's moving even when not or distorted motions when head does move. Spinning or tilting feeling.",
              "neurological",
              "orientation", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "anxiety",
              "Heart aches or throbs too fast, like it's being stabbed and the stabbing is forcing it to run faster. Stomach coils in on itself. Not hungry. Feels like throwing up. Mind races, difficulty focusing on non-anxious thoughts.",
              "neurological",
              "mental", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "depression",
              "Listlessness. Things normally enjoyed aren't anymore. Difficulty moving or speaking or listening. Not hungry.",
              "neurological",
              "mental", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Mood swings or imbalances",
              "Mood swings or imbalances",
              "neurological",
              "mental", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Irritability",
              "Irritability",
              "neurological",
              "mental", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Inattentiveness",
              "Inattentiveness",
              "neurological",
              "mental", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Lack of concentration",
              "Lack of concentration",
              "neurological",
              "mental", Boolean.FALSE)
          );

          symptomDao.symptomDaoInsert(
            new Symptom(
              "light sensitivity",
              "Seeing sources of light hurts the eyes.",
              "neurological",
              "sensitivity", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "sound sensitivity",
              "Hearing hurts the ears.",
              "neurological",
              "sensitivity", Boolean.FALSE)
          );


          // Aura related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "distinct aura",
              "Inability to see displayed like fuzzy TV static with edges distinct from rest of vision.",
              "neurological",
              "aura", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "complex aura",
              "Inability to see parts or all of vision. Can have fuzzy electronic static in the visionless spots, but can also be blurry or indistinguishable.",
              "neurological",
              "aura", Boolean.FALSE)
          );


          // Stomach related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "stomach nausea",
              "Difficulty eating without gagging or throwing up. Feeling like throwing up. Not hungry.",
              "gut and digestive",
              "nausea", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "throat nausea",
              "Difficulty breathing without gagging or throwing up.",
              "throat",
              "nausea", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "regurgitation",
              "Feels as if stomach contents have come back up to the throat or mouth.",
              "throat",
              "nausea", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "stomach pain",
              "Throbbing or roiling in stomach, like something's trying to chew or stab its way out.",
              "gut and digestive",
              "ache, throb, stab", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "vomiting",
              "Throwing up.",
              "gut and digestive",
              "urge", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "diarrhea",
              "Loose, liquid, or watery bowel movement.",
              "gut and digestive",
              "urge", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "bloody bowel movement",
              "Blood in bowel movement.",
              "gut and digestive",
              "urge", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "constipation",
              "Hard and dry bowel movement, difficult to pass.",
              "gut and digestive",
              "urge", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "bloating",
              "Stomach feels tight, pressurized, too full.",
              "gut and digestive",
              "urge", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "stomach cramping",
              "Throbbing pain in the gut area.",
              "gut and digestive",
              "throb", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "excessive farting",
              "Farting more often than before, or more than 20 times a day.",
              "gut and digestive",
              "throb", Boolean.FALSE)
          );


          // Reproductive system
          symptomDao.symptomDaoInsert(
            new Symptom(
              "menstrual blood",
              "On your period. Blood coming out onto underwear, pants, or tampons or pads or while going to the bathroom.",
              "reproductive",
              "cycle", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "back cramping",
              "Throbbing pain in the lower spine area.",
              "back",
              "throb", Boolean.FALSE)
          );

          //Cardio related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "heartburn",
              "Heart burns like its surface is covered in hot embers that are trying to set everything around it on fire. Burning moves up around the neck and throat or as discomfort that feels like it’s located behind the breastbone. Bending over or lying down can make it feel worse. ",
              "cardio",
              "burn", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Arrhythmia",
              "Arrhythmia",
              "cardio",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Fast beating",
              "Fast beating",
              "cardio",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Fluttering heart rate",
              "Fluttering",
              "cardio",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Tachycardia",
              "Tachycardia",
              "cardio",
              "pain", Boolean.FALSE)
          );

          //Muscles related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "body ache",
              "Difficulty moving. Standing or exerting body causes slow throb of ache in muscles being used and worsens other symptoms.",
              "muscles",
              "ache", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "chest pain",
              "chest pain",
              "muscles",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "arm pain",
              "pain in arm. Can be symptom of heart attack if in conjunction with difficulty breathing, sweating, dizziness.",
              "muscles",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "jaw pain",
              "pain in jaw. Can be symptom of heart attack if in conjunction with difficulty breathing, sweating, dizziness.",
              "muscles",
              "pain", Boolean.FALSE)
          );

          // Headache related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "throbbing headache",
              "Head throbbing with pain behind the temples. Pulses with more pain on triggering sensations like light, sound.",
              "headache",
              "throb", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "burning headache",
              "Brain burning like it's on fire. Can no longer sense.",
              "headache",
              "burn", Boolean.FALSE)
          );

          // Skin related
          symptomDao.symptomDaoInsert(
            new Symptom(
              "rosacea",
              "Redness, pimples, swelling, and small and superficial dilated blood vessels. Often, the nose, cheeks, forehead, and chin are most involved.",
              "skin",
              "mark", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "rash",
              "Skin may change color, itch, become warm, bumpy, chapped, dry, cracked or blistered, swell, or be painful. Multiple kinds.",
              "skin",
              "itch", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "hives",
              "Kind of skin rash with red, raised, itchy bumps. Hives may burn or sting.",
              "skin",
              "itch, burn, sting", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "psoriasis",
              "These areas of skin are red, or purple on some people with darker skin, dry, itchy, and scaly.",
              "skin",
              "itch", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "eczema",
              "Skin itchiness, dry skin, rashes, scaly patches, blisters and skin infections.",
              "skin",
              "itch", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "acne",
              "Pimples, blackheads or whiteheads, oily skin, and possible scarring.",
              "skin",
              "mark", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "sweating",
              "Sweating",
              "skin",
              "mark", Boolean.FALSE)
          );

          //Respiratory symptoms
          symptomDao.symptomDaoInsert(
            new Symptom(
              "congestion",
              "congestion",
              "respiratory",
              "stuffy", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "asthma",
              "asthma",
              "respiratory",
              "breathing", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Runny nose",
              "Runny nose",
              "respiratory",
              "breathing", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Sneezing",
              "Sneezing",
              "respiratory",
              "urge", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Chronic cough",
              "Chronic cough",
              "respiratory",
              "breathing", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Dry cough",
              "Persistent dry cough.",
              "respiratory",
              "breathing", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Rhinitis",
              "Rhinitis",
              "respiratory",
              "breathing", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Difficulty breathing",
              "Difficulty breathing",
              "respiratory",
              "breathing", Boolean.FALSE)
          );



          //Circulatory symptoms
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Circulatory collapse",
              "Circulatory collapse",
              "circulatory",
              "circulatory", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Hypotension",
              "Hypotension",
              "circulatory",
              "circulatory", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Hypertension",
              "Hypertension",
              "circulatory",
              "circulatory", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Constant shivers or chills",
              "Constant shivers or chills",
              "circulatory",
              "circulatory", Boolean.FALSE)
          );

          //Other
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Fibromyalgia",
              "Fibromyalgia",
              "other",
              "other", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Watery eyes",
              "Watery eyes",
              "other",
              "other", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Fatigue",
              "Fatigue",
              "other",
              "other", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Sleep issues",
              "Sleep issues",
              "other",
              "other", Boolean.FALSE)
          );

          //Throat
          symptomDao.symptomDaoInsert(
            new Symptom(
              "Oedema",
              "Swelling often around eyes, mouth and throat.",
              "throat",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "sore throat",
              "sore throat",
              "throat",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "bitter taste in the back of the throat ",
              "bitter taste in the back of the throat ",
              "throat",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "sour taste in the mouth ",
              "sour taste in the mouth ",
              "throat",
              "pain", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "sore throat",
              "sore throat",
              "throat",
              "pain", Boolean.FALSE)
          );

          //Mouth
          symptomDao.symptomDaoInsert(
            new Symptom(
              "bad breath",
              "bad breath",
              "mouth",
              "mark", Boolean.FALSE)
          );
          symptomDao.symptomDaoInsert(
            new Symptom(
              "tooth enamel damage",
              "damage to tooth enamel due to excess acid ",
              "mouth",
              "mark", Boolean.FALSE)
          );


        });

      }
    }; //end sRoomDatabaseCallback


  public static SymptomRoomDatabase getDatabase(final Context context) {

    if (INSTANCE == null) {

      synchronized (SymptomRoomDatabase.class) {

        if (INSTANCE == null) {

          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              SymptomRoomDatabase.class, "symptom_database"
            ) //end INSTANCE

            .addCallback(sRoomDatabaseCallback)
                  // allow main thread queries can lock the UI for a long time, not the best way
                  // to get access to livedata main thread
                  // TODO fix this
                  .allowMainThreadQueries()
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
      database.execSQL("ALTER TABLE Symptom ADD COLUMN symptomToTrack BOOLEAN");
    }//end migrate

  }; //end Migration


} //end SymptomRoomDatabase
