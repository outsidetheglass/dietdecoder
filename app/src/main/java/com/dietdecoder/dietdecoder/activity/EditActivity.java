package com.dietdecoder.dietdecoder.activity;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.activity.ingredientlog.EditIngredientLogFragment;
import com.dietdecoder.dietdecoder.activity.symptom.AddEditSymptomActivity;
import com.dietdecoder.dietdecoder.activity.symptomlog.EditSymptomLogFragment;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

// control the fragments going back and forth
public class EditActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = EditActivity.this;

    int mFragmentContainerView = R.id.fragment_container_view_edit;
    Bundle mBundle, mBundleNext;

    Integer mHour, mMinute, mDay, mMonth, mYear;
    String mWhichFragmentGoTo, mFromFragment, mWhichActivity, mAction;

    private Context thisContext;

    private Fragment mNextFragment = null;

    public EditActivity() {
        super(R.layout.activity_edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_edit);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);
        thisContext = this.getApplicationContext();

        // if there's an intent, it's the fragment passing info along
        if ( getIntent().getExtras() != null ) {
            mBundle = getIntent().getExtras();
            mBundleNext = mBundle;
            // get info from bundle
            mAction = mBundle.getString(Util.ARGUMENT_ACTION);

            // check why we're here
            Boolean isActionDuplicate = Objects.equals(mAction,
                    Util.ARGUMENT_ACTION_DUPLICATE);

            // if it's a food log and we want to duplicate it
            if ( Util.isIngredientLogBundle(mBundle) ) {
                // TODO fix duplicate, somewhere somehow it's editing the day and time of the log
                //  being duplicated, though the newly created log is set perfectly
                mNextFragment = new EditIngredientLogFragment();

            } else  if ( Util.isSymptomLogBundle(mBundle) ) {
                // we're here with symptom log

                //TODO get this working through Util the normal way
                //Util.goToEditSymptomLogActivity();
                mNextFragment = new EditSymptomLogFragment();
                // TODO add a duplicate in here, or figure out how to do it
                //  with the dao the right way

            }
            else  if (Util.isSymptomBundle(mBundle) ) {
                // we're here with symptom

                Util.goToAddEditSymptomActivity(null, thisActivity, mBundle.getString(Util.ARGUMENT_SYMPTOM_ID_ARRAY));
                // TODO add a duplicate in here

            }
            else  if ( Util.isIngredientBundle(mBundle) ) {
                // we're here with ingredient

                Util.goToAddEditIngredientActivity(null, thisActivity,
                        mBundle.getString(Util.ARGUMENT_INGREDIENT_ID_ARRAY));
                // TODO add a duplicate in here

            }

            // check which fragment we should start next based on
            // which button was pressed in the fragment we just came from
//            Fragment mNextFragment = whichFragmentNext(mWhichFragmentGoTo);

            if ( mNextFragment != null ) {
                // start the next fragment
                Util.startNextFragmentBundle(thisActivity,
                        getSupportFragmentManager().beginTransaction(),
                        mFragmentContainerView, mNextFragment, mBundleNext);
            } else {
                // it was null so go back to the main activity
                Util.goToMainActivity(null, thisActivity);
            }
        }
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

            // do something
        } else if (item.getItemId() == R.id.action_go_home) {
            // do something
            Util.goToMainActivity(null, thisActivity);
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

                    case R.id.more_export_activity:
                        Util.goToExportActivity(thisContext, thisActivity);
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
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

