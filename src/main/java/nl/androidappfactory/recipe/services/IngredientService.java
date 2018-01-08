package nl.androidappfactory.recipe.services;

import nl.androidappfactory.recipe.commands.IngredientCommand;

public interface IngredientService {

	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

	public IngredientCommand saveIngredientCommand(IngredientCommand command);

	public void deleteIngredient(String recipeId, String idToDelete);

}
