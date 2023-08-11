package com.dietdecoder.dietdecoder.activity.symptom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.MainActivity;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomListAdapter;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;

import java.util.List;

public class SymptomActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {



  // make a TAG to use to log errors
  private final String TAG = "TAG: " + getClass().getSimpleName();
  private final Activity thisActivity = SymptomActivity.this;

  private Context thisContext;

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
    }   else if (item.getItemId() == R.id.action_more) {

      // Initializing the popup menu and giving the reference as current logContext
      PopupMenu popupMenu = new PopupMenu(thisContext, findViewById(R.id.action_more));
      // Inflating popup menu from popup_menu.xml file
      popupMenu.getMenuInflater().inflate(R.menu.item_more_menu, popupMenu.getMenu());
      popupMenu.setGravity(Gravity.END);
      // if an option in the menu is clicked
      popupMenu.setOnMenuItemClickListener(moreMenuItem -> {
        // which button was clicked
        switch (moreMenuItem.getItemId()) {

          // go to the right activity
          case R.id.more_all_symptoms:
            Util.goToListSymptomActivity(null, thisActivity, null);
            break;

          case R.id.more_all_ingredients:
            Util.goToListIngredientActivity(thisContext, thisActivity, null);
            break;

          default:
            break;
        }//end switch case for which menu item was chosen

        return true;
      });
      // Showing the popup menu
      popupMenu.show();

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
    thisContext = this.getApplicationContext();

    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
            DividerItemDecoration.VERTICAL));

    // Setup view to get info from database
    mSymptomListAdapter = new SymptomListAdapter(new SymptomListAdapter.SymptomDiff());
    recyclerView.setAdapter(mSymptomListAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    //TODO check this if it works, if not, change it to how symptom log
    // Setup access to database
    mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
    // if not null, then set list of symptoms
    mActivityAllSymptoms = mSymptomViewModel.viewModelGetAllLiveData();
    // turn LiveData into list and set that in Adapter so we can get positions
    mSymptomListAdapter.setSymptomList(mActivityAllSymptoms.getValue());

    //TODO move this if statement to setSymptomList somehow
    if ( mActivityAllSymptoms != null ) {
      mActivityAllSymptoms.observe(this, symptoms -> {
        // Update the cached copy of the words in the adapter.
        mSymptomListAdapter.submitSymptomList(symptoms);
      });

    }


    Log.d(TAG, "onCreate: tags work");

  }//end onCreate


  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }
}//end SymptomActivity



