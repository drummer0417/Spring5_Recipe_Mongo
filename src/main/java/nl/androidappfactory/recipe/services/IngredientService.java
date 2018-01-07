package nl.androidappfactory.recipe.services;

import nl.androidappfactory.recipe.commands.IngredientCommand;

public interface IngredientService {

	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

	public void deleteIngredient(String recipeId, String ingredientId);

	public IngredientCommand updateIngredient(IngredientCommand igredientCommand);

	public IngredientCommand createIngredient(IngredientCommand ingredientCommand);
}
