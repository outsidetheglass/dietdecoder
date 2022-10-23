package com.dietdecoder.dietdecoder.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.Recipe;

import java.util.List;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class RecipeViewModel extends AndroidViewModel {


  private RecipeRepository mRepository;
  private final LiveData<List<Recipe>> mViewModelAllRecipes;
  private LiveData<List<Recipe>> mViewModelAllRecipesWithIngredient;
  private String mViewModelRecipeName;


  public RecipeViewModel(Application application) {
    super(application);
    mRepository = new RecipeRepository(application);
    mViewModelAllRecipes = mRepository.repositoryGetAllRecipes();

  }//end RecipeViewModel method


  //get all recipes to list them
  public LiveData<List<Recipe>> viewModelGetAllRecipes() {
    return mViewModelAllRecipes;
  }

  //get all recipes with ingredient
  public LiveData<List<Recipe>> viewModelGetRecipesWithIngredient(String paramIngredient) {
    mViewModelAllRecipesWithIngredient = mRepository.repositoryGetRecipesWithIngredient(paramIngredient);
    return mViewModelAllRecipesWithIngredient;
  }


  // get single recipe using the name
  public Recipe viewModelGetRecipeFromName(String paramRecipeName) {
    return mRepository.repositoryGetRecipeFromName(paramRecipeName);
  }
  public Recipe viewModelGetRecipeFromNameIngredient(String recipeName, String recipeIngredient){
    return mRepository.repositoryGetRecipeFromNameIngredient(recipeName, recipeIngredient);
  }


  // add to database
  public void viewModelInsert(Recipe recipe) { mRepository.repositoryInsert(recipe); }

  // edit recipe in database
  public void viewModelUpdateName(String oldRecipeName, String oldRecipeIngredient, String newRecipeName) {
    mRepository.repositoryUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
  }

  // edit recipe in database
  public void viewModelUpdateIngredient(String oldRecipeName, String oldRecipeIngredient, String newRecipeIngredient) {
    mRepository.repositoryUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
  }

  // delete recipe in database
  public void viewModelDelete(String recipeName, String recipeIngredient) {
    mRepository.repositoryDelete(recipeName, recipeIngredient);
  }


} //end RecipeViewModel class
