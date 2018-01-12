package nl.androidappfactory.recipe.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.converters.CategoryToCategoryCommand;
import nl.androidappfactory.recipe.converters.RecipeCommandToRecipe;
import nl.androidappfactory.recipe.converters.RecipeToRecipeCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Category;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.reactive.CategoryReactiveRepository;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Flux;

/**
 * 
 * @author Hans van Meurs // copied from JT
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {

	public static final String NEW_DESCRIPTION = "New Description";
	public static final String RECIPE_NOT_FOUND = "Recipe not found for id: ";

	@Autowired
	RecipeService recipeService;

	@Autowired
	RecipeReactiveRepository recipeReactiveRepository;

	@Autowired
	RecipeCommandToRecipe recipeCommandToRecipe;

	@Autowired
	RecipeToRecipeCommand recipeToRecipeCommand;

	@Autowired
	CategoryReactiveRepository categoryReactiveRepository;

	@Autowired
	CategoryToCategoryCommand categoryToCategoryCommand;

	@Test
	public void testSaveOfDescription() throws Exception {
		// given
		Category category1 = categoryReactiveRepository.findByDescription("Mexican").block();
		CategoryCommand cat1 = categoryToCategoryCommand.convert(category1);

		Category category2 = categoryReactiveRepository.findByDescription("American").block();
		CategoryCommand cat2 = categoryToCategoryCommand.convert(category2);

		Flux<Recipe> recipes = recipeReactiveRepository.findAll();
		Recipe testRecipe = recipes.toIterable().iterator().next();
		RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);
		testRecipeCommand.setSelectedCategories(new String[] { cat1.getId(), cat2.getId() });

		// when
		testRecipeCommand.setDescription(NEW_DESCRIPTION);
		RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand).block();

		// then
		assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
		assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
		assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
		assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
	}

	// @Test(expected = NotFoundException.class)
	public void testDeleteRecipe() throws Exception {

		// given
		String idToDelete = "1";

		// when
		recipeService.deleteByID(idToDelete);
		try {

			// then
			recipeService.findCommandById(idToDelete);

		} catch (NotFoundException e) {

			// then 2
			assertEquals(RECIPE_NOT_FOUND + "1", e.getMessage());
			throw e;
		}
	}
}
