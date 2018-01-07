package nl.androidappfactory.recipe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.converters.RecipeCommandToRecipe;
import nl.androidappfactory.recipe.converters.RecipeToRecipeCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.RecipeRepository;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private RecipeRepository recipeRepository;
	private RecipeCommandToRecipe commandToRecipeConverter;
	private RecipeToRecipeCommand recipeToCommandConverter;

	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe commandToRecipeConverter,
			RecipeToRecipeCommand recipeToCommandConverter) {
		this.recipeRepository = recipeRepository;
		this.commandToRecipeConverter = commandToRecipeConverter;
		this.recipeToCommandConverter = recipeToCommandConverter;
	}

	@Override
	public List<Recipe> getAllRecipes() {

		log.debug("in: getAllRecipes()");
		List<Recipe> recipes = (List<Recipe>) recipeRepository.findAllByOrderByDescription();

		return recipes;
	}

	@Override
	public Recipe findById(String id) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(id);

		if (!recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe not found for id: " + id);
		}
		return recipeOptional.get();
	}

	@Override
	public RecipeCommand findCommandById(String id) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(id);

		if (!recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe not found for id: " + id);
		}
		return recipeToCommandConverter.convert(recipeOptional.get());

	}

	@Override
	public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {

		recipeCommand.getCategories().clear();

		if (recipeCommand.getSelectedCategories() != null) {

			for (String id : recipeCommand.getSelectedCategories()) {
				if (id != null) {
					CategoryCommand categoryCommand = new CategoryCommand();
					categoryCommand.setId(id);
					recipeCommand.addCategory(categoryCommand);
					log.debug("newCategory, id: " + id);
				}
			}
		}

		Recipe recipe = commandToRecipeConverter.convert(recipeCommand);

		log.debug("#categories after convert: " + recipe.getCategories().size());

		Recipe savedRecipe = null;

		if (recipe != null) {
			savedRecipe = recipeRepository.save(recipe);
		}

		return recipeToCommandConverter.convert(savedRecipe);
	}

	@Override
	public void deleteByID(String id) {

		recipeRepository.deleteById(id);

	}

}
