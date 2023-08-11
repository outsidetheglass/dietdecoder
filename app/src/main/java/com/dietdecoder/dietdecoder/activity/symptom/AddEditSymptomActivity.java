package com.dietdecoder.dietdecoder.activity.symptom;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.symptom.Symptom;
import com.dietdecoder.dietdecoder.ui.symptom.SymptomViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AddEditSymptomActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {


    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = AddEditSymptomActivity.this;
    private Context thisContext;

    int mFragmentContainerView = Util.fragmentContainerViewAddSymptomLog;
    Bundle mBundle, mBundleNext;

    String mName, mDescription, mCategory, mNewName, mNewDescription, mNewCategory;
    ArrayList<String> mSymptomsToAddArrayListIdStrings, mSymptomLogsArrayListIdStrings;

    int mUnSelectedColor;
    Drawable mSickFaceDrawable, mRedRoundcornersBackgroundDrawable, mGreenRoundcornersDrawable, mEmptyCircleDrawable;

    private Fragment mNextFragment = null;
    private Resources.Theme mTheme;
    Button mSaveButton;

    Symptom mSymptom;
    SymptomViewModel mSymptomViewModel;

    private EditText mEditNameView;
    private EditText mEditCategoryView;
    private EditText mEditDescriptionView;
    ImageButton mTrackOrNotButton;
    Boolean isNameViewEmpty, isCategoryViewEmpty, isDescriptionViewEmpty, isFromEdit,
            mSetSymptomToCheck;

    public AddEditSymptomActivity() {
        super(R.layout.activity_addedit_symptom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_addedit_symptom);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);


        // if we didn't have a savedinstancestate, like user turned the phone to horizontal
        if (savedInstanceState == null) {

            // declare and set variables
            thisContext = thisActivity.getApplicationContext();
            mTheme = thisContext.getTheme();

            mSymptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
            mEditNameView = findViewById(R.id.value_edittext_addedit_symptom_name);
            mEditDescriptionView = findViewById(R.id.value_edittext_addedit_symptom_description);
            mEditCategoryView = findViewById(R.id.value_edittext_addedit_symptom_category);
            mTrackOrNotButton = findViewById(R.id.imagebutton_addedit_symptom_trackornot);
            mSaveButton = findViewById(R.id.button_addedit_symptom_save);

            mTrackOrNotButton.setOnClickListener(this::onClick);
            mSaveButton.setOnClickListener(this::onClick);

            mSickFaceDrawable = getResources().getDrawable(R.drawable.ic_baseline_sick,
                    mTheme);
            mGreenRoundcornersDrawable =
                    getResources().getDrawable(R.drawable.roundcorners,
                            mTheme);
            mRedRoundcornersBackgroundDrawable =
                    getResources().getDrawable(R.drawable.red_roundcorners,
                            mTheme);
            mEmptyCircleDrawable =
                    getResources().getDrawable(R.drawable.ic_baseline_empty_circle,
                            mTheme);

            // if here from edit, put that info into the views
            if ( getIntent().getExtras() != null ) {
                mBundle = getIntent().getExtras();
                isFromEdit = Util.isFromEdit(mBundle);

                // get the array and remove the brackets as a string, since we can
                // only edit one at a time it's not an array
                // then turn that into the ID and get the symptom and set its values
                mSymptom =
                        mSymptomViewModel.viewModelGetSymptomFromId( UUID.fromString(
                                Util.cleanArrayString(mBundle.getString(Util.ARGUMENT_SYMPTOM_ID_ARRAY))
                                ) );
                mName = mSymptom.getSymptomName();
                mDescription = mSymptom.getSymptomDescription();
                mCategory = mSymptom.getSymptomCategory();
                mEditNameView.setText(mName);
                mEditDescriptionView.setText(mDescription);
                mEditCategoryView.setText(mCategory);
                // put the values into the view

            } else {
                // if not here from edit, then we're here to add a new one
                isFromEdit = Boolean.FALSE;
                // if adding symptom, then default to track this symptom
                mSetSymptomToCheck = Boolean.TRUE;
            }

        }



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_symptom_intensity:
//
//                break;
            case R.id.button_addedit_symptom_save:

                Log.d(TAG, " saving");
                if ( isFromEdit ){
                    Log.d(TAG, " edit");
                    // if the name in edit is different than it was, save it
                    Boolean isNameNew = !TextUtils.equals(mEditNameView.getText().toString(),
                            mName);
                    Boolean isDescriptionNew =
                            !TextUtils.equals(mEditDescriptionView.getText().toString(), mDescription);
                    Boolean isCategoryNew =
                            !TextUtils.equals(mEditCategoryView.getText().toString(), mCategory) ;
                    // only if at least one is new will we need to update
                    if ( isNameNew || isDescriptionNew || isCategoryNew ) {
                        // first set whichever ones are new
                        if (isNameNew) {
                            mSymptom.setSymptomName(mEditNameView.getText().toString());
                        }
                        if (isDescriptionNew) {
                            mSymptom.setSymptomDescription(mEditDescriptionView.getText().toString());
                        }
                        if (isCategoryNew) {
                            mSymptom.setSymptomCategory(mEditCategoryView.getText().toString());
                        }

                        mSymptom.setSymptomToTrack(mSetSymptomToCheck);
                        // then update the symptom
                        mSymptomViewModel.viewModelUpdate(mSymptom);
                        Util.goToChooseSymptomActivity(null, thisActivity);
                    }
                } else {
                    Log.d(TAG, " adding");
                    // not from edit, so we're adding
                    // first check if name is empty, we can't add one without at least that
                    if ( TextUtils.isEmpty(mEditNameView.getText()) ){
                        Log.d(TAG, " empty");
                        Util.toastInvalidEmpty(thisActivity);
                    } else {
                        Log.d(TAG, " name");
                        // there is a name, so make the symptom
                        mSymptom = new Symptom(mEditNameView.getText().toString());
                        // then set values if they were given
                        if ( !Objects.isNull(mEditDescriptionView) ) {
                            Log.d(TAG,
                                    " mEditDescriptionView.getText().toString() " + mEditDescriptionView.getText().toString());
                            mSymptom.setSymptomDescription(mEditDescriptionView.getText().toString());
                        }
                        if ( !Objects.isNull(mEditCategoryView) ){
                            Log.d(TAG,
                                    " mEditCategoryView.getText().toString() " + mEditCategoryView.getText().toString());
                            mSymptom.setSymptomCategory(mEditCategoryView.getText().toString());
                        }

                        Log.d(TAG,
                                " mEditNameView.getText().toString() " + mEditNameView.getText().toString());
                        mSymptom.setSymptomToTrack(mSetSymptomToCheck);
                        // add our new symptom to the database
                        mSymptomViewModel.viewModelInsert(mSymptom);
                        Util.goToChooseSymptomActivity(null, thisActivity);
                    }
                }

                break;

            case R.id.imagebutton_addedit_symptom_trackornot:
                // the track or not button was clicked, so check if user is unselecting it or not
                // if the current drawable in the track or not is not empty, user is unselecting
                // might be using the sick face or just plain filled circle to be selected, so
                // basing off of not the empty circle is best way to check
                boolean userWantsToUnSelect =
                        mTrackOrNotButton.getDrawable() != mEmptyCircleDrawable;

                if ( mTrackOrNotButton.getDrawable() != mSickFaceDrawable  ) {
                    //change UI to show it was clicked
                    // change the empty circle to the sick face
                    mTrackOrNotButton.setImageDrawable(mSickFaceDrawable);
                    // TODO probably add a circle around sick face to show it's clickable
//                    mTrackOrNotButton.setBackground(mRedRoundcornersBackgroundDrawable);

                    mSetSymptomToCheck = Boolean.TRUE;
                }
                else  {
                    // change the sick face to an empty circle with green background
                    mTrackOrNotButton.setImageDrawable(mEmptyCircleDrawable);
//                    mTrackOrNotButton.setBackground(mGreenRoundcornersDrawable);

                    // the symptom was selected before this, needs it removed
                    mSetSymptomToCheck = Boolean.FALSE;
                }
            default:
                break;
        } // end cases switch
    }//end onClick

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();

            // do something
        } else if (item.getItemId() == R.id.action_go_home) {
            // do something
            Util.goToMainActivity(null, thisActivity);
        }  else if (item.getItemId() == R.id.action_more) {

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

        } else {
            // do something
        }

        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

