package nl.androidappfactory.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.Synchronized;
import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.models.Recipe;

/**
 * Created by jt on 6/21/17.
 */
@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

	private IngredientCommandToIngredient ingredientsConverter;
	private NotesCommandToNotes notesConverter;
	private CategoryCommandToCategory categoryConverter;

	public RecipeCommandToRecipe(
			IngredientCommandToIngredient ingredientsConverter,
			NotesCommandToNotes notesConverter,
			CategoryCommandToCategory categoryConverter) {
		this.ingredientsConverter = ingredientsConverter;
		this.notesConverter = notesConverter;
		this.categoryConverter = categoryConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public Recipe convert(RecipeCommand command) {
		if (command == null) {
			return null;
		}

		final Recipe recipe = new Recipe();
		if (command.getId() != null && !command.getId().isEmpty()) {
			recipe.setId(command.getId());
		}
		recipe.setDescription(command.getDescription());
		recipe.setPrepTime(command.getPrepTime());
		recipe.setCookTime(command.getCookTime());
		recipe.setServings(command.getServings());
		recipe.setSource(command.getSource());
		recipe.setUrl(command.getUrl());
		recipe.setDirections(command.getDirections());
		recipe.setImage(command.getImage());

		for (IngredientCommand ingredientCommand : command.getIngredients()) {
			recipe.addIngredient(ingredientsConverter.convert(ingredientCommand));
		}
		recipe.setDifficulty(command.getDifficulty());

		recipe.setNotes(notesConverter.convert(command.getNotes()));

		for (CategoryCommand categoryCommand : command.getCategories()) {
			recipe.addCategory(categoryConverter.convert(categoryCommand));
		}

		return recipe;
	}
}
