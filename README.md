Diet Decoder


Current status:

* Ingredients database has a primary key column of ingredient name and another column for chemical (to list chemicals of concern in the ingredient).
* Main Activity has scrollable list for all ingredients.
* Fab button on Main Activity goes to New Ingredient Activity.
* New Ingredient Activity has textviews for ingredient name and chemical. 
* Save Ingredient button adds the ingredient with its chemical to the database and goes back to Main Activity. If nothing is typed in, has a Toast for can't add empty ingredient.
* Main Activity's list uses live data, so it updates with the new ingredient when coming back from New Ingredient Activity after saving a new ingredient.
