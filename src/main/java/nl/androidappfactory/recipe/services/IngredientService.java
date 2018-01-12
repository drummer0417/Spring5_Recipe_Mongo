package nl.androidappfactory.recipe.services;

import nl.androidappfactory.recipe.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {

	public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);

	public Mono<Void> deleteIngredient(String recipeId, String idToDelete);

}
