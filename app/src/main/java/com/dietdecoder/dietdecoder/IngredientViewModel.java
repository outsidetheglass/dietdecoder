package com.dietdecoder.dietdecoder;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class IngredientViewModel extends AndroidViewModel {


  private IngredientRepository mRepository;
  public final LiveData<List<Ingredient>> mViewModelAllIngredients;
  private LiveData<List<Ingredient>> mViewModelAllIngredientsWithConcern;
  private String mViewModelIngredientName;


  public IngredientViewModel (Application application) {
    super(application);
    mRepository = new IngredientRepository(application);
    mViewModelAllIngredients = mRepository.repositoryGetAllIngredients();

  }//end IngredientViewModel method


  //get all ingredients to list them
  public LiveData<List<Ingredient>> viewModelGetAllIngredients() {
    return mViewModelAllIngredients;
  }

  //get all ingredients with concern
  public LiveData<List<Ingredient>> viewModelGetIngredientsWithConcern(String concern) {
    mViewModelAllIngredientsWithConcern = mRepository.repositoryGetIngredientsWithConcern(concern);
    return mViewModelAllIngredientsWithConcern;
  }


  // get single ingredient using the name
  public Ingredient viewModelGetIngredientFromName(String ingredientName) {
    return mRepository.repositoryGetIngredientFromName(ingredientName);
  }


  // add to database
  public void viewModelInsert(Ingredient ingredient) { mRepository.repositoryInsert(ingredient); }

  // edit ingredient in database
  public void viewModelUpdate(Ingredient ingredient) { mRepository.repositoryUpdate(ingredient); }

  // delete ingredient in database
  public void viewModelDelete(Ingredient ingredient) { mRepository.repositoryDelete(ingredient); }


} //end IngredientViewModel class
