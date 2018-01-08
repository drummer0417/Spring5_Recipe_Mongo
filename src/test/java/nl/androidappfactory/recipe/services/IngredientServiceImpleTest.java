package nl.androidappfactory.recipe.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.converters.IngredientCommandToIngredient;
import nl.androidappfactory.recipe.converters.IngredientToIngredientCommand;
import nl.androidappfactory.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import nl.androidappfactory.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import nl.androidappfactory.recipe.models.Ingredient;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.RecipeRepository;
import nl.androidappfactory.recipe.repositories.UnitOfMeasureRepository;

public class IngredientServiceImpleTest {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

	@Mock
	RecipeRepository recipeRepository;

	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;

	IngredientService ingredientService;

	// init converters
	public IngredientServiceImpleTest() {
		this.ingredientToIngredientCommand = new IngredientToIngredientCommand(
				new UnitOfMeasureToUnitOfMeasureCommand());
		this.ingredientCommandToIngredient = new IngredientCommandToIngredient(
				new UnitOfMeasureCommandToUnitOfMeasure());
		this.unitOfMeasureCommandToUnitOfMeasure = new UnitOfMeasureCommandToUnitOfMeasure();
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand,
				ingredientCommandToIngredient,
				recipeRepository,
				unitOfMeasureRepository);
	}

	@Test
	public void findByRecipeIdAndReceipeIdHappyPath() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId("1");

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId("1");

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId("1");

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId("3");

		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);
		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

		// then
		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3");

		// when
		assertEquals("3", ingredientCommand.getId());
		verify(recipeRepository, times(1)).findById(anyString());
	}

	@Test
	public void testSaveRecipeCommand() throws Exception {
		// given
		IngredientCommand command = new IngredientCommand();
		command.setId("3");
		command.setRecipeId("2");

		Optional<Recipe> recipeOptional = Optional.of(new Recipe());

		Recipe savedRecipe = new Recipe();
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId("3");

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
		when(recipeRepository.save(any())).thenReturn(savedRecipe);

		// when
		IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

		// then
		assertEquals("3", savedCommand.getId());
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));

	}

	@Test
	public void testDeleteById() throws Exception {
		// given
		Recipe recipe = new Recipe();
		Ingredient ingredient = new Ingredient();
		ingredient.setId("3");
		recipe.addIngredient(ingredient);
		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

		// when
		ingredientService.deleteIngredient("1", "3");

		// then
		verify(recipeRepository, times(1)).findById(anyString());
		verify(recipeRepository, times(1)).save(any(Recipe.class));
	}

}