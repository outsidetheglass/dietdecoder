package com.dietdecoder.dietdecoder.ui.recipe;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dietdecoder.dietdecoder.database.recipe.Recipe;

import java.util.List;

// NOTE: all other extends in other java files must be AndroidViewModel like this
// i.e. remove Fragment, Activity, View
//ViewModels don't survive the app's process being killed
// in the background when the OS needs more resources.
// For UI data that needs to survive process death due to
// running out of resources,
// you can use the Saved State module for ViewModels.
public class RecipeViewModel extends AndroidViewModel {


  private RecipeRepository mRecipeRepository;
  private final LiveData<List<Recipe>> mViewModelAllRecipes;
  private LiveData<List<Recipe>> mViewModelAllRecipesWithIngredient;
  private String mViewModelRecipeName;


  public RecipeViewModel(Application applicationRecipe) {
    super(applicationRecipe);
    mRecipeRepository = new RecipeRepository(applicationRecipe);
    mViewModelAllRecipes = mRecipeRepository.repositoryGetAllRecipes();

  }//end RecipeViewModel method


  //get all recipes to list them
  public LiveData<List<Recipe>> viewModelGetAllRecipes() {
    return mViewModelAllRecipes;
  }

  //get all recipes with ingredient
  public List<Recipe> viewModelGetRecipesWithIngredient(String paramIngredientName) {
    return mRecipeRepository.repositoryGetRecipesWithIngredient(paramIngredientName);
  }

  public List<Recipe> viewModelGetAllRecipeFromName(String paramRecipeName){
    return mRecipeRepository.repositoryGetAllRecipeFromName(paramRecipeName);
  }

  // get single recipe using the name
  public Recipe viewModelGetRecipeFromName(String paramRecipeName) {
    return mRecipeRepository.repositoryGetRecipeFromName(paramRecipeName);
  }
  // Get single recipe matching the name with a specific ingredient
  public Recipe viewModelGetRecipeFromRecipeNameAndIngredient(String recipeName, String recipeIngredient){
    return mRecipeRepository.repositoryGetRecipeFromRecipeNameAndIngredient(recipeName, recipeIngredient);
  }

  public List<Recipe> viewModelGetAllRecipeFromCategory(String recipeCategory){
    return mRecipeRepository.repositoryGetAllRecipeFromCategory(recipeCategory);
  }


  // add to database
  public void viewModelRecipeInsert(Recipe recipe) {
    mRecipeRepository.repositoryRecipeInsert(recipe); }

  // edit recipe in database
  public void viewModelRecipeUpdateName(String oldRecipeName, String oldRecipeIngredient, String newRecipeName) {
    mRecipeRepository.repositoryRecipeUpdateName(oldRecipeName, oldRecipeIngredient, newRecipeName);
  }

  // edit recipe in database
  public void viewModelRecipeUpdateIngredient(String oldRecipeName, String oldRecipeIngredient, String newRecipeIngredient) {
    mRecipeRepository.repositoryRecipeUpdateIngredient(oldRecipeName, oldRecipeIngredient, newRecipeIngredient);
  }

  // delete recipe in database
  public void viewModelRecipeDelete(String recipeName, String recipeIngredient) {
    mRecipeRepository.repositoryRecipeDelete(recipeName, recipeIngredient);
  }


} //end RecipeViewModel class
