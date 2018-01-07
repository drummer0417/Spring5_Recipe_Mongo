package nl.androidappfactory.recipe.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.commands.NotesCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.models.Difficulty;
import nl.androidappfactory.recipe.models.Recipe;

public class RecipeCommandToRecipeTest {
	public static final String RECIPE_ID = "1";
	public static final Integer COOK_TIME = Integer.valueOf("5");
	public static final Integer PREP_TIME = Integer.valueOf("7");
	public static final String DESCRIPTION = "My Recipe";
	public static final String DIRECTIONS = "Directions";
	public static final Difficulty DIFFICULTY = Difficulty.EASY;
	public static final Integer SERVINGS = Integer.valueOf("3");
	public static final String SOURCE = "Source";
	public static final String URL = "Some URL";
	public static final String CAT_ID_1 = "1";
	public static final String CAT_ID2 = "2";
	public static final String INGRED_ID_1 = "3";
	public static final String INGRED_ID_2 = "4";
	public static final String NOTES_ID = "9";
	public static Byte[] IMAGE;

	RecipeCommandToRecipe converter;

	@Before
	public void setUp() throws Exception {
		converter = new RecipeCommandToRecipe(
				new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
				new NotesCommandToNotes(),
				new CategoryCommandToCategory());

		String testString = "here is the test String";
		Byte[] testBytes = new Byte[testString.length()];

		int i = 0;
		for (byte aByte : testString.getBytes()) {
			testBytes[i++] = aByte;
		}

		IMAGE = testBytes;

	}

	@Test
	public void testNullObject() throws Exception {
		assertNull(converter.convert(null));
	}

	@Test
	public void testEmptyObject() throws Exception {
		Recipe recipe = converter.convert(new RecipeCommand());
		assertNotNull(recipe);
	}

	@Test
	public void convert() throws Exception {
		// given
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(RECIPE_ID);
		recipeCommand.setCookTime(COOK_TIME);
		recipeCommand.setPrepTime(PREP_TIME);
		recipeCommand.setDescription(DESCRIPTION);
		recipeCommand.setDifficulty(DIFFICULTY);
		recipeCommand.setDirections(DIRECTIONS);
		recipeCommand.setServings(SERVINGS);
		recipeCommand.setSource(SOURCE);
		recipeCommand.setUrl(URL);
		recipeCommand.setImage(IMAGE);

		NotesCommand notes = new NotesCommand();
		notes.setId(NOTES_ID);

		recipeCommand.setNotes(notes);

		CategoryCommand category = new CategoryCommand();
		category.setId(CAT_ID_1);

		CategoryCommand category2 = new CategoryCommand();
		category2.setId(CAT_ID2);

		recipeCommand.getCategories().add(category);
		recipeCommand.getCategories().add(category2);

		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setId(INGRED_ID_1);

		IngredientCommand ingredient2 = new IngredientCommand();
		ingredient2.setId(INGRED_ID_2);

		recipeCommand.getIngredients().add(ingredient);
		recipeCommand.getIngredients().add(ingredient2);

		// when
		Recipe recipe = converter.convert(recipeCommand);

		assertNotNull(recipe);
		assertEquals(RECIPE_ID, recipe.getId());
		assertEquals(COOK_TIME, recipe.getCookTime());
		assertEquals(PREP_TIME, recipe.getPrepTime());
		assertEquals(DESCRIPTION, recipe.getDescription());
		assertEquals(DIFFICULTY, recipe.getDifficulty());
		assertEquals(DIRECTIONS, recipe.getDirections());
		assertEquals(SERVINGS, recipe.getServings());
		assertEquals(SOURCE, recipe.getSource());
		assertEquals(URL, recipe.getUrl());
		assertEquals(NOTES_ID, recipe.getNotes().getId());
		assertEquals(2, recipe.getCategories().size());
		assertEquals(2, recipe.getIngredients().size());
		assertEquals(IMAGE.length, recipe.getImage().length);
	}

}