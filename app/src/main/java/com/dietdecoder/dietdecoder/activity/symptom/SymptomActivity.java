package com.dietdecoder.dietdecoder.activity.symptom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SymptomActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {



  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = SymptomActivity.this;


  private SymptomViewModel mSymptomViewModel;
  private SymptomListAdapter mSymptomListAdapter;

  private LiveData<List<Symptom>> mActivityAllSymptoms;

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.action_settings) {
      Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

      // do something
    } else if (item.getItemId() == R.id.action_go_home) {
      // do something
      startActivity(new Intent(thisActivity, MainActivity.class));
    } else {
      // do something
    }

    return false;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_symptom);

    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_symptom);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);
    RecyclerView recyclerView = findViewById(R.id.recyclerview_symptom);

    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));

    // Setup view to get info from database
    mSymptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff());
    recyclerView.setAdapter(mSymptomListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Setup access to database
    mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
    // if not null, then set list of symptoms
    mActivityAllSymptoms = mSymptomViewModel.viewModelGetAllSymptoms();
    // turn LiveData into list and set that in Adapter so we can get positions
    mSymptomListAdapter.setSymptomList(mActivityAllSymptoms.getValue());

    //TODO move this if statement to setSymptomList somehow
    if ( mActivityAllSymptoms != null ) {
      mActivityAllSymptoms.observe(this, symptoms -> {
        // Update the cached copy of the words in the adapter.
        mSymptomListAdapter.submitList(symptoms);
      });

    }


    Log.d(TAG, "onCreate: tags work");

  }//end onCreate


  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }
}//end SymptomActivity



