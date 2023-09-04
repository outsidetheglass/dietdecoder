package com.dietdecoder.dietdecoder.activity.ingredient;

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
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AddEditIngredientActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

  private final String TAG = "TAG: " + getClass().getSimpleName();
  //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
  private final Activity thisActivity = AddEditIngredientActivity.this;
  private Context thisContext;

  int mFragmentContainerView = Util.fragmentContainerViewAddIngredientLog;
  Bundle mBundle, mBundleNext;

  String mName, mBrand, mCategory, mNewName, mNewBrand, mNewCategory;
  ArrayList<String> mIngredientsToAddArrayListIdStrings, mIngredientLogsArrayListIdStrings;

  int mUnSelectedColor;
  Drawable mSickFaceDrawable, mRedRoundcornersBackgroundDrawable, mGreenRoundcornersDrawable, mEmptyCircleDrawable;

  private Fragment mNextFragment = null;
  private Resources.Theme mTheme;
  Button mSaveButton;

  Ingredient mIngredient;
  IngredientViewModel mIngredientViewModel;

  private EditText mEditNameView;
  private EditText mEditCategoryView;
  private EditText mEditBrandView;
  ImageButton mTrackOrNotButton;
  Boolean isNameViewEmpty, isCategoryViewEmpty, isBrandViewEmpty, isFromEdit,
          mSetIngredientToCheck;

  public AddEditIngredientActivity() {
    super(R.layout.activity_addedit_ingredient);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // make toolbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_addedit_ingredient);
    toolbar.setTitle(getResources().getString(R.string.app_name));
    toolbar.setOnMenuItemClickListener(this);


    // if we didn't have a savedinstancestate, like user turned the phone to horizontal
    if (savedInstanceState == null) {

      // declare and set variables
      thisContext = thisActivity.getApplicationContext();
      mTheme = thisContext.getTheme();

      mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
      mEditNameView = findViewById(R.id.value_edittext_addedit_ingredient_name);
      mEditBrandView = findViewById(R.id.value_edittext_addedit_ingredient_brand);
      mSaveButton = findViewById(R.id.button_addedit_ingredient_save);

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

        if (isFromEdit ) {
          // get the array and remove the brackets as a string, since we can
          // only edit one at a time it's not an array
          // then turn that into the ID and get the ingredient and set its values
          mIngredient =
                  mIngredientViewModel.viewModelGetFromId(UUID.fromString(
                          Util.cleanArrayString(
                                  mBundle.getString(Util.ARGUMENT_INGREDIENT_ID_ARRAY))
                  ));
          mName = mIngredient.getName();
          mBrand = mIngredient.getBrand();
          // put the values into the view
          mEditNameView.setText(mName);
          mEditBrandView.setText(mBrand);
          mEditCategoryView.setText(mCategory);
        }

      } else {
        // if not here from edit, then we're here to add a new one
        isFromEdit = Boolean.FALSE;
        // if adding ingredient, then default to track this ingredient
        mSetIngredientToCheck = Boolean.TRUE;
      }

    }



  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      //do something when edittext is clicked
//            case R.id.edittext_new_ingredient_intensity:
//
//                break;
      case R.id.button_addedit_ingredient_save:

        Log.d(TAG, " saving");
        if ( isFromEdit ){
          Log.d(TAG, " edit");
          // if the name in edit is different than it was, save it
          Boolean isNameNew = !TextUtils.equals(mEditNameView.getText().toString(),
                  mName);
          Boolean isBrandNew =
                  !TextUtils.equals(mEditBrandView.getText().toString(), mBrand);

          // only if at least one is new will we need to update
          if ( isNameNew || isBrandNew ) {
            // first set whichever ones are new
            if (isNameNew) {
              mIngredient.setName(mEditNameView.getText().toString());
            }
            if (isBrandNew) {
              mIngredient.setBrand(mEditBrandView.getText().toString());
            }


            // then update the ingredient
            mIngredientViewModel.viewModelUpdate(mIngredient);
            Util.goToChooseIngredientActivity(null, thisActivity);
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
            // there is a name, so make the ingredient
            mIngredient = new Ingredient(mEditNameView.getText().toString());
            // then set values if they were given
            if ( !Objects.isNull(mEditBrandView) ) {
              Log.d(TAG,
                      " mEditBrandView.getText().toString() " + mEditBrandView.getText().toString());
              mIngredient.setBrand(mEditBrandView.getText().toString());
            }

            Log.d(TAG,
                    " mEditNameView.getText().toString() " + mEditNameView.getText().toString());

            // add our new ingredient to the database
            mIngredientViewModel.viewModelInsert(mIngredient);
            Util.goToChooseIngredientActivity(null, thisActivity);
          }
        }

        break;
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

    } else {
      // do something
    }

    return false;
  }

  public void onPointerCaptureChanged(boolean hasCapture) {
    super.onPointerCaptureChanged(hasCapture);
  }
}

