package nl.androidappfactory.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.Synchronized;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.models.Category;
import nl.androidappfactory.recipe.models.Ingredient;
import nl.androidappfactory.recipe.models.Recipe;

/**
 * Created by jt on 6/21/17.
 */
@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

	private final IngredientToIngredientCommand ingredientConverter;
	private final CategoryToCategoryCommand categoryConverter;
	private final NotesToNotesCommand notesConverter;

	public RecipeToRecipeCommand(CategoryToCategoryCommand categoryConverter,
			IngredientToIngredientCommand ingredientConverter,
			NotesToNotesCommand notesConverter) {
		this.ingredientConverter = ingredientConverter;
		this.categoryConverter = categoryConverter;
		this.notesConverter = notesConverter;
	}

	@Synchronized
	@Nullable
	@Override
	public RecipeCommand convert(Recipe recipe) {
		if (recipe == null) {
			return null;
		}

		RecipeCommand recipeCommand = new RecipeCommand();

		recipeCommand.setId(recipe.getId());
		recipeCommand.setDescription(recipe.getDescription());
		recipeCommand.setPrepTime(recipe.getPrepTime());
		recipeCommand.setServings(recipe.getServings());
		recipeCommand.setSource(recipe.getSource());
		recipeCommand.setCookTime(recipe.getCookTime());
		recipeCommand.setUrl(recipe.getUrl());
		recipeCommand.setDirections(recipe.getDirections());
		for (Ingredient ingredient : recipe.getIngredients()) {
			recipeCommand.getIngredients().add(ingredientConverter.convert(ingredient));
		}

		for (Category category : recipe.getCategories()) {
			recipeCommand.getCategories().add(categoryConverter.convert(category));
		}
		recipeCommand.setDifficulty(recipe.getDifficulty());
		recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));
		recipeCommand.setCookTime(recipe.getCookTime());
		recipeCommand.setImage(recipe.getImage());

		return recipeCommand;
	}
}
