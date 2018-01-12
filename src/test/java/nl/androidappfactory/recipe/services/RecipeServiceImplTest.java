package nl.androidappfactory.recipe.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.androidappfactory.recipe.converters.CategoryToCategoryCommand;
import nl.androidappfactory.recipe.converters.RecipeCommandToRecipe;
import nl.androidappfactory.recipe.converters.RecipeToRecipeCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.reactive.CategoryReactiveRepository;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RecipeServiceImplTest {

	private RecipeService recipeService;

	@Mock
	private RecipeReactiveRepository recipeReactiveRepository;

	private RecipeCommandToRecipe recipeCommandToRecipe;
	private RecipeToRecipeCommand recipeToRecipeCommand;
	private CategoryReactiveRepository categoryReactiveRepository;
	private CategoryToCategoryCommand categoryToCommandConverter;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand,
				categoryReactiveRepository, categoryToCommandConverter);

	}

	@Test
	public void testRecipeById() {

		Recipe recipe = new Recipe();
		recipe.setId("1");

		Mono<Recipe> recipeMono = Mono.just(recipe);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);

		Recipe recipeReturned = recipeService.findById("1").block();

		assertNotNull("Null recipe returned", recipeReturned);
		verify(recipeReactiveRepository, times(1)).findById(anyString());
		verify(recipeReactiveRepository, never()).findAll();
	}

	@Test
	public void testGetAllRecipes() {

		Flux<Recipe> recipes = Flux.just(new Recipe());
		long expectedRecipesReturned = 1;

		when(recipeService.getAllRecipes()).thenReturn(recipes);

		// verify that recipeReactiveRepository.getAllRecipes returns a list with 1 recipe
		assertEquals(expectedRecipesReturned, recipeService.getAllRecipes().collectList().block().size());

		// verify that recipeReactiveRepository.getAllRecipes is called once and onde only
		verify(recipeReactiveRepository, times(1)).findAllByOrderByDescription();
	}

	@Test
	public void testDeleteRecipeByID() {

		String idToDelete = "1";

		when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

		recipeService.deleteByID(idToDelete);

		verify(recipeReactiveRepository, times(1)).deleteById(anyString());
	}

	@Test(expected = NotFoundException.class)
	public void testRecipeByIdNotFoundException() {

		when(recipeReactiveRepository.findById(anyString())).thenThrow(NotFoundException.class);

		recipeReactiveRepository.findById("1");
	}
}
