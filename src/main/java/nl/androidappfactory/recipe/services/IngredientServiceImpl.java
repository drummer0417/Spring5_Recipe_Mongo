package nl.androidappfactory.recipe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.converters.IngredientCommandToIngredient;
import nl.androidappfactory.recipe.converters.IngredientToIngredientCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Ingredient;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import nl.androidappfactory.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/28/17.
 */
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final RecipeReactiveRepository recipeReactiveRepository;
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand,
			IngredientCommandToIngredient ingredientCommandToIngredient,
			RecipeReactiveRepository recipeReactiveRepository,
			UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
	}

	@Override
	public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		return recipeReactiveRepository
				.findById(recipeId)
				.flatMapIterable(Recipe::getIngredients)
				.filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
				.single()
				.map(ingredient -> {
					IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
					command.setRecipeId(recipeId);
					return command;
				});
	}

	@Override
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {

		Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId()).block();

		if (recipe == null) {

			log.error("Recipe not found for id: " + command.getRecipeId());
			throw new NotFoundException("Recipe not found");
		} else {

			Optional<Ingredient> ingredientOptional = recipe
					.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(command.getId()))
					.findFirst();

			Ingredient ingredientSaved = null;

			if (ingredientOptional.isPresent()) {
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				ingredientFound.setUom(unitOfMeasureReactiveRepository.findById(command.getUom().getId()).block());
				if (ingredientFound.getUom() == null) {
					log.error("Unit of measure for ingredient not found: " + command.getUom().toString());
					throw new NotFoundException("Unit of measure not found: " + command.getUom().toString());
				}
				ingredientSaved = ingredientFound;
			} else {
				// add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(command);
				// ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
				ingredientSaved = ingredient;
			}
			recipeReactiveRepository.save(recipe).block();
			return Mono.just(ingredientToIngredientCommand.convert(ingredientSaved));
		}
	}

	@Override
	public Mono<Void> deleteIngredient(String recipeId, String idToDelete) {

		log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

		Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

		if (recipe != null) {

			Optional<Ingredient> ingredientOptional = recipe
					.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(idToDelete))
					.findFirst();

			if (ingredientOptional.isPresent()) {
				log.debug("found Ingredient");

				recipe.getIngredients().remove(ingredientOptional.get());
				recipeReactiveRepository.save(recipe).block();
			}
		} else {
			log.debug("Recipe Id Not found. Id:" + recipeId);
		}
		return Mono.empty();
	}
}
