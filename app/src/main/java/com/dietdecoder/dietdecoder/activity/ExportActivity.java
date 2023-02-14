package com.dietdecoder.dietdecoder.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.foodlog.EditFoodLogFragment;
import com.dietdecoder.dietdecoder.activity.foodlog.FoodLogActivity;
import com.dietdecoder.dietdecoder.activity.foodlog.LogSpecificDateTimeFragment;
import com.dietdecoder.dietdecoder.activity.ingredient.IngredientActivity;
import com.dietdecoder.dietdecoder.database.foodlog.FoodLog;
import com.dietdecoder.dietdecoder.ui.foodlog.FoodLogViewModel;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

// control the fragments going back and forth
public class ExportActivity extends AppCompatActivity  {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ExportActivity.this;

    Button csvButton;

    FoodLogViewModel mFoodLogViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set view
        setContentView(R.layout.activity_export);

        mFoodLogViewModel = new ViewModelProvider(this).get(FoodLogViewModel.class);

        // Button to go to ingredient's list and edit and delete and add
        csvButton = findViewById(R.id.button_csv);
        csvButton.setOnClickListener(view -> {
            exportDatabaseToCsv(view);
        });
    }

    private void exportDatabaseToCsv(View view) {
        Toast.makeText(thisActivity, "Exporting as CSV now...", Toast.LENGTH_SHORT).show();
        Cursor cursor = mFoodLogViewModel.viewModelGetCursorAllFoodLog();

        // if the cursor found a database
        if (cursor.moveToFirst()) {
            // go through each item in the database
            do  {
                // get the UUID
                String path=cursor.getString(0);
                // get the info to put into the file
                String foodLogString =
                        mFoodLogViewModel.viewModelGetFoodLogFromId(UUID.fromString(path)).toString();
                // do something with it
                Log.d(TAG, foodLogString);
            }
            while (
                    // while there's still a database entry, go to next and repeat
                    cursor.moveToNext()
            );
        } //end moving through database


    }

    //TODO get write to file working
//    private void writeToFile(String data,Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                    context.openFileOutput("config.txt", Context.MODE_PRIVATE)
//            );
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
}

