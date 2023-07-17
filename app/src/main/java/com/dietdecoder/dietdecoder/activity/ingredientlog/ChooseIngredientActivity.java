package com.dietdecoder.dietdecoder.activity.ingredientlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dietdecoder.dietdecoder.R;
import com.dietdecoder.dietdecoder.Util;
import com.dietdecoder.dietdecoder.database.ingredient.Ingredient;
import com.dietdecoder.dietdecoder.database.ingredientlog.IngredientLog;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientListAdapter;
import com.dietdecoder.dietdecoder.ui.ingredient.IngredientViewModel;
import com.dietdecoder.dietdecoder.ui.ingredientlog.IngredientLogViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChooseIngredientActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private final String TAG = "TAG: " + getClass().getSimpleName();
    //Log.d(TAG, " whichFragmentNext, mJustNowString: " + mJustNowString);
    private final Activity thisActivity = ChooseIngredientActivity.this;
    private Context thisContext;

    //TODO fix names
//    int mFragmentContainerView = Util.fragmentContainerViewChooseIngredientLog;
    Bundle mBundle;

    String mTooManyTryAgainString, mSaveString, mEmptyTryAgainString;
    Button mButtonSaveName;

    ArrayList<String> mIngredientsSelectedIdsArrayListStrings = null;

    private Fragment nextFragment = null;

    IngredientListAdapter mIngredientListAdapter;
    IngredientViewModel mIngredientViewModel;
    IngredientLogViewModel mIngredientLogViewModel;
    RecyclerView recyclerViewIngredientNameChoices;

    ColorStateList selectedColor;
    ColorStateList unSelectedColor;

    public ChooseIngredientActivity() {
        super(R.layout.activity_choose_ingredient);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_choose_ingredient);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setOnMenuItemClickListener(this);

        thisContext = thisActivity.getApplicationContext();


        if (savedInstanceState == null) {

            // basic variable setup
            mIngredientsSelectedIdsArrayListStrings = new ArrayList<>();
            if ( getIntent().getExtras() == null ) {
                mBundle = new Bundle();
            } else {
                mBundle = getIntent().getExtras();
            }

            //save button
            mButtonSaveName = findViewById(R.id.button_choose_ingredient_save);
            mButtonSaveName.setOnClickListener(this);


            // make the view for listing the items in the log
            recyclerViewIngredientNameChoices =
                    findViewById(R.id.recyclerview_ingredient_name_choices);
            // add horizontal lines between each recyclerview item
            recyclerViewIngredientNameChoices.addItemDecoration(new DividerItemDecoration(recyclerViewIngredientNameChoices.getContext(),
                    DividerItemDecoration.VERTICAL));


            mIngredientListAdapter = new IngredientListAdapter(new IngredientListAdapter.IngredientDiff());
            recyclerViewIngredientNameChoices.setAdapter(mIngredientListAdapter);
            recyclerViewIngredientNameChoices.setLayoutManager(new LinearLayoutManager(this));
            mIngredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
            mIngredientLogViewModel = new ViewModelProvider(this).get(IngredientLogViewModel.class);

            mIngredientViewModel.viewModelGetAllIngredients().observe(this,
                    new Observer<List<Ingredient>>() {
                        @Override
                        public void onChanged(List<Ingredient> ingredients) {
                            // Update the cached copy of the words in the adapter.
                            mIngredientListAdapter.submitList(ingredients);

                        }
                    });


        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(thisActivity, "Settings was clicked!", Toast.LENGTH_SHORT).show();
            // do something

        } else if (item.getItemId() == R.id.action_go_home) {
            Util.goToMainActivity(null, thisActivity);
        }
        return false;
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    @Override
    public void onClick(View view) {

        // basic variables setup
        // get saving string from resources so everything can translate languages easy
        mSaveString = getResources().getString(R.string.saving);
        mEmptyTryAgainString = getResources().getString(R.string.empty_not_saved);
        mTooManyTryAgainString = getResources().getString(R.string.too_many_selected_not_saved);


        switch (view.getId()) {
            //do something when edittext is clicked
//            case R.id.edittext_new_ingredient_intensity:
//
//                break;
            case R.id.button_choose_ingredient_save:
                // get int
//
                mIngredientsSelectedIdsArrayListStrings = new ArrayList<>();
                for (int childCount = recyclerViewIngredientNameChoices.getChildCount(), i = 0; i < childCount; ++i) {

                    final RecyclerView.ViewHolder holder =
                            recyclerViewIngredientNameChoices.getChildViewHolder(recyclerViewIngredientNameChoices.getChildAt(i));
                    TextView viewHolderTextView =
                            holder.itemView.findViewById(R.id.textview_ingredient_item);
                    int textViewColorInt =
                            viewHolderTextView.getTextColors().getDefaultColor();

                    selectedColor =
                            holder.itemView.getResources().getColorStateList(R.color.selected_text_color,
                            getTheme());
                    int selectInt = selectedColor.getDefaultColor();

                    // if the text color of the current item in the list is the color set when
                    // selected by the user
                    if (Objects.equals(selectInt, textViewColorInt) ){
                        // get the ID of the ingredient that has the selected color text
                        int position = holder.getBindingAdapterPosition();
                        String currentIngredientSelectedIdString =
                                mIngredientViewModel.viewModelGetAllIngredients().getValue().get(position)
                                        .getIngredientId().toString();
                        // add that string ID to our array of selected ingredients
                        mIngredientsSelectedIdsArrayListStrings.add(currentIngredientSelectedIdString);

                    }
                }

                // after done with the for loop,
                // all the ingredients to add have been put into the array
                // check if we're here to edit a single log and therefore need only one selected
                Boolean needsOnlyOneLog = Boolean.FALSE;
                if ( mBundle.containsKey(Util.ARGUMENT_ACTION) ){
                    // if the action to be taken is edit,
                    // then we need to ensure we're only changing one
                    if (TextUtils.equals(mBundle.getString(Util.ARGUMENT_ACTION),
                            Util.ARGUMENT_ACTION_EDIT )){
                        needsOnlyOneLog = Boolean.TRUE;
                    }
                }
                Integer howManySelected = mIngredientsSelectedIdsArrayListStrings.size();

                // so check how many have been selected and put in the array
                // check if ingredients to add array is empty
                if ( mIngredientsSelectedIdsArrayListStrings.isEmpty() ) {
                    // if empty, alert user none were selected and don't do anything else
                    Toast.makeText(thisContext, mEmptyTryAgainString,
                            Toast.LENGTH_SHORT).show();
                } else if (  howManySelected > 1 && needsOnlyOneLog ) {
                    // the user selected more than one ingredient, but is here to only change one
                    // log
                    // tell them to try again and select only one
                    Toast.makeText(thisContext, mTooManyTryAgainString,
                            Toast.LENGTH_SHORT).show();
                } else if (  howManySelected == 1 && needsOnlyOneLog ) {
                    // the user selected only one ingredient, and is here to only change one log
                    Toast.makeText(thisContext, mSaveString,
                            Toast.LENGTH_SHORT).show();
                    // which means success so we can go back to edit after saving it
                    String ingredientLogIdString =
                            mBundle.getString(Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY);
                    String ingredientIdString = mIngredientsSelectedIdsArrayListStrings.get(0);
                    IngredientLog ingredientLog =
                            mIngredientLogViewModel.viewModelGetIngredientLogFromLogId(UUID.fromString(ingredientLogIdString));
                    // get the ingredient matching the ingredient ID we got from the UI choice
                    Ingredient ingredient =
                            mIngredientViewModel.viewModelGetIngredientFromId(UUID.fromString(ingredientIdString));
                    // save our updated ingredient log with the new ingredient ID and name
                    //TODO debug this it isn't saving
                    ingredientLog.setIngredientLogIngredientId(ingredient.getIngredientId());
                    mIngredientLogViewModel.viewModelUpdateIngredientLog(ingredientLog);

                    // done with setting the ingredient go back to editing this ingredient log
                    Util.goToEditActivityActionTypeId(null, thisActivity,
                            Util.ARGUMENT_ACTION_EDIT,
                            Util.ARGUMENT_SYMPTOM_LOG_ID_ARRAY, ingredientLogIdString);

                } else {
                    // if not empty, put the array into the intent to go add ingredients
                    mBundle =
                            Util.setBundleNewIngredientLogAmount(mIngredientsSelectedIdsArrayListStrings);
                    // go to set the intensity of the ingredient
                    Log.d(TAG, "ingredients array " + mIngredientsSelectedIdsArrayListStrings);
                    Util.goToAddIngredientLogWithBundle(thisActivity, mBundle);

                }

                break;
//
            default:
                break;
        }
    }//end onClick
}

