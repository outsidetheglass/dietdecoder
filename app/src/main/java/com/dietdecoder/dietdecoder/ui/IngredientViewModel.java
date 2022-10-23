package com.dietdecoder.dietdecoder.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.Ingredient;

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
  private final LiveData<List<Ingredient>> mViewModelAllIngredients;
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
  public LiveData<List<Ingredient>> viewModelGetIngredientsWithConcern(String paramConcern) {
    mViewModelAllIngredientsWithConcern = mRepository.repositoryGetIngredientsWithConcern(paramConcern);
    return mViewModelAllIngredientsWithConcern;
  }


  // get single ingredient using the name
  public Ingredient viewModelGetIngredientFromName(String paramIngredientName) {
    return mRepository.repositoryGetIngredientFromName(paramIngredientName);
  }
  public Ingredient viewModelGetIngredientFromNameConcern(String ingredientName, String ingredientConcern){
    return mRepository.repositoryGetIngredientFromNameConcern(ingredientName, ingredientConcern);
  }


  // add to database
  public void viewModelInsert(Ingredient ingredient) { mRepository.repositoryInsert(ingredient); }

  // edit ingredient in database
  public void viewModelUpdateName(String oldIngredientName, String oldIngredientConcern, String newIngredientName) {
    mRepository.repositoryUpdateName(oldIngredientName, oldIngredientConcern, newIngredientName);
  }

  // edit ingredient in database
  public void viewModelUpdateConcern(String oldIngredientName, String oldIngredientConcern, String newIngredientConcern) {
    mRepository.repositoryUpdateConcern(oldIngredientName, oldIngredientConcern, newIngredientConcern);
  }

  // delete ingredient in database
  public void viewModelDelete(String ingredientName, String ingredientConcern) {
    mRepository.repositoryDelete(ingredientName, ingredientConcern);
  }


} //end IngredientViewModel class
