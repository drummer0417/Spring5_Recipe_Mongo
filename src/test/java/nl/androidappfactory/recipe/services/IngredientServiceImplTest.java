package nl.androidappfactory.recipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import nl.androidappfactory.recipe.converters.IngredientCommandToIngredient;
import nl.androidappfactory.recipe.converters.IngredientToIngredientCommand;
import nl.androidappfactory.recipe.converters.UnitOfMeasureCommandToUnitOfMeasure;
import nl.androidappfactory.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import nl.androidappfactory.recipe.models.Ingredient;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.models.UnitOfMeasure;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import nl.androidappfactory.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Mono;

public class IngredientServiceImplTest {

	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

	@Mock
	RecipeReactiveRepository recipeReactiveRepository;

	@Mock
	UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	IngredientService ingredientService;

	// init converters
	public IngredientServiceImplTest() {
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
				recipeReactiveRepository,
				unitOfMeasureReactiveRepository);
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
		Mono<Recipe> recipeMono = Mono.just(recipe);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);

		// then
		Mono<IngredientCommand> ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3");

		// when
		assertEquals("3", ingredientCommand.block().getId());
		verify(recipeReactiveRepository, times(1)).findById(anyString());
	}

	@Test
	public void testSaveRecipeCommand() throws Exception {
		// given
		IngredientCommand command = new IngredientCommand();
		command.setId("3");
		command.setRecipeId("2");

		UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
		uomCommand.setId("1");
		command.setUom(uomCommand);

		UnitOfMeasure uom = unitOfMeasureCommandToUnitOfMeasure.convert(uomCommand);

		Mono<Recipe> recipeMono = Mono.just(new Recipe());

		Recipe savedRecipe = new Recipe();
		savedRecipe.addIngredient(new Ingredient());
		savedRecipe.getIngredients().iterator().next().setId("3");
		Mono<Recipe> savedRecipeMono = Mono.just(savedRecipe);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);
		when(recipeReactiveRepository.save(any())).thenReturn(savedRecipeMono);
		when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(uom));

		// when
		Mono<IngredientCommand> savedCommand = ingredientService.saveIngredientCommand(command);

		// then
		assertNotNull(savedCommand.block().getId());
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));

	}

	@Test
	public void testDeleteById() throws Exception {
		// given
		Recipe recipe = new Recipe();
		recipe.setId("1");
		Ingredient ingredient = new Ingredient();
		ingredient.setId("3");
		recipe.addIngredient(ingredient);
		Mono<Recipe> recipeMono = Mono.just(recipe);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);
		when(recipeReactiveRepository.save(any())).thenReturn(Mono.empty());

		// when
		ingredientService.deleteIngredient("1", "3");

		// then
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, times(1)).save(any(Recipe.class));
	}

}