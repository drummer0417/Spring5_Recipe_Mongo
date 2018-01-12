package nl.androidappfactory.recipe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.converters.IngredientCommandToIngredient;
import nl.androidappfactory.recipe.converters.IngredientToIngredientCommand;
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

		// Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId);
		//
		// Optional<Recipe> recipeOptional = Optional.of(recipeMono.block());
		//
		// // if (!recipeOptional.isPresent()) {
		// // // todo impl error handling
		// // log.error("recipe id not found. Id: " + recipeId);
		// // }
		//
		// Recipe recipe = recipeOptional.get();
		//
		// Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
		// .filter(ingredient -> ingredient.getId().equals(ingredientId))
		// .map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();
		//
		// if (!ingredientCommandOptional.isPresent()) {
		// // todo impl error handling
		// log.error("Ingredient id not found: " + ingredientId);
		// }
		//
		// IngredientCommand ingredientCommand = ingredientCommandOptional.get();
		// ingredientCommand.setRecipeId(recipeId);
		//
		// return Mono.just(ingredientCommandOptional.get());
	}

	@Override
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {

		Mono<Recipe> recipeMono = recipeReactiveRepository.findById(command.getRecipeId());

		Optional<Recipe> recipeOptional = Optional.of(recipeMono.block());

		if (!recipeOptional.isPresent()) {

			// todo toss error if not found!
			log.error("Recipe not found for id: " + command.getRecipeId());
			return Mono.just(new IngredientCommand());
		} else {
			Recipe recipe = recipeOptional.get();

			Optional<Ingredient> ingredientOptional = recipe
					.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(command.getId()))
					.findFirst();

			if (ingredientOptional.isPresent()) {
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				// ingredientFound.setUom(UnitOfMeasureCommandToUnitOfMeasure command.getUom());
				// ingredientFound.setUom(unitOfMeasureReactiveRepository
				// .findById(command.getUom().getId())
				// .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); // todo address this
				recipeReactiveRepository.save(recipe).block();
				return Mono.just(ingredientToIngredientCommand.convert(ingredientFound));
			} else {
				// add new Ingredient
				Ingredient ingredient = ingredientCommandToIngredient.convert(command);
				// ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
				recipeReactiveRepository.save(recipe).block();
				return Mono.just(ingredientToIngredientCommand.convert(ingredient));
			}
		}
	}

	@Override
	public Mono<Void> deleteIngredient(String recipeId, String idToDelete) {

		log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

		Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId);

		Optional<Recipe> recipeOptional = Optional.of(recipeMono.block());

		if (recipeOptional.isPresent()) {
			Recipe recipe = recipeOptional.get();
			log.debug("found recipe");

			Optional<Ingredient> ingredientOptional = recipe
					.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(idToDelete))
					.findFirst();

			if (ingredientOptional.isPresent()) {
				log.debug("found Ingredient");
				// Ingredient ingredientToDelete = ingredientOptional.get();
				// ingredientToDelete.setRecipe(null);
				recipe.getIngredients().remove(ingredientOptional.get());
				recipeReactiveRepository.save(recipe).block();
			}
		} else {
			log.debug("Recipe Id Not found. Id:" + recipeId);
		}
		return Mono.empty();
	}
}
