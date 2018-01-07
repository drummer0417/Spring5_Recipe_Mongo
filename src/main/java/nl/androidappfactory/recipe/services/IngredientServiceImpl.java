package nl.androidappfactory.recipe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.converters.IngredientCommandToIngredient;
import nl.androidappfactory.recipe.converters.IngredientToIngredientCommand;
import nl.androidappfactory.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Ingredient;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.IngredientRepository;
import nl.androidappfactory.recipe.repositories.RecipeRepository;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private RecipeRepository recipeRepository;
	private IngredientRepository ingredientRepository;

	private IngredientToIngredientCommand ingredientToIngredientCommand;
	private IngredientCommandToIngredient ingredientCommandToIngredient;
	private UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

	public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository,
			IngredientToIngredientCommand ingredientToIngredientCommand,
			IngredientCommandToIngredient ingredientCommandToIngredient,
			UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure) {
		this.recipeRepository = recipeRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientRepository = ingredientRepository;
		this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {

			throw new NotFoundException("Recipe not found");
		}

		// Set<Ingredient> ingredients = recipeOptional.get().getIngredients();
		// for (Ingredient ingredient : ingredients) {
		// if (ingredient.getId().equals(ingredientId)) {
		// return ingredientCommnadToIngredient.convert(ingredient);
		// }}

		Optional<IngredientCommand> ingredientCommandOptional = recipeOptional.get().getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

		if (ingredientCommandOptional.isPresent()) {
			return ingredientCommandOptional.get();
		} else {

			throw new NotFoundException("Ingredient not found");
		}
	}

	@Override
	public void deleteIngredient(String recipeId, String ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {

			throw new NotFoundException("Recipe not found");
		}
		Recipe recipe = recipeOptional.get();

		Ingredient ingredientToRemove = null;

		for (Ingredient ingredient : recipe.getIngredients()) {
			if (ingredient.getId().equals(ingredientId)) {

				ingredientToRemove = ingredient;
				break;
			}
		}

		recipe.removeIngredient(ingredientToRemove);
		recipeRepository.save(recipe);
		ingredientRepository.deleteById(ingredientToRemove.getId());

		log.debug("after save(): " + recipe);
	}

	@Override
	public IngredientCommand updateIngredient(IngredientCommand ingredientCommand) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

		Recipe savedRecipe = null;

		if (!recipeOptional.isPresent()) {

			throw new NotFoundException("Recipe not found");
		}

		Recipe recipe = recipeOptional.get();

		Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst();

		if (ingredientOptional.isPresent()) {
			// update existing recipe ingredient
			Ingredient recipeIngredient = ingredientOptional.get();
			recipeIngredient.setAmount(ingredientCommand.getAmount());
			recipeIngredient.setDescription(ingredientCommand.getDescription());
			recipeIngredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(ingredientCommand.getUom()));

		} else {
			// add new ingredient to list of ingredients
			recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));

		}
		savedRecipe = recipeRepository.save(recipe);

		Optional<Ingredient> savedIngredient = savedRecipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId())).findFirst();

		// Ingredient savedIngredient = ingredientRepository.save(i);
		IngredientCommand newIngredientCommand = ingredientToIngredientCommand.convert(savedIngredient.get());

		log.debug("in saveIngredient: " + ingredientCommand);

		return newIngredientCommand;
	}

	@Override
	public IngredientCommand createIngredient(IngredientCommand ingredientCommand) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

		if (!recipeOptional.isPresent()) {

			throw new NotFoundException("Recipe not found");
		}
		Ingredient savedIngredient = ingredientRepository
				.save(ingredientCommandToIngredient.convert(ingredientCommand));

		return ingredientToIngredientCommand.convert(savedIngredient);
	}

}
