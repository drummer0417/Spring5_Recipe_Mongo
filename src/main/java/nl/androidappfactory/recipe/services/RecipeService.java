package nl.androidappfactory.recipe.services;

import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.models.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {

	Flux<Recipe> getAllRecipes();

	Mono<Recipe> findById(String l);

	Mono<RecipeCommand> findCommandById(String id);

	Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);

	Mono<Void> deleteByID(String idToDelete);

}
