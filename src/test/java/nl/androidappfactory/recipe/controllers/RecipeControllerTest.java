package nl.androidappfactory.recipe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.services.CategoryService;
import nl.androidappfactory.recipe.services.RecipeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author Hans van Meurs
 *
 */

public class RecipeControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	CategoryService categoryService;

	RecipeController controller;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		controller = new RecipeController(recipeService, categoryService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new ControllerExceptionHandler())
				.build();
	}

	@Test
	public void testGetRecipe() throws Exception {

		Recipe recipe = new Recipe();
		recipe.setId("1");

		when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/show"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {

		mockMvc.perform(get("/recipe/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");

		when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

		mockMvc.perform(post("/recipe")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "")
				.param("description", "some string")
				.param("directions", "a direction"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/show"));
	}

	@Test
	public void testGetUpdateView() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");

		CategoryCommand cat1 = new CategoryCommand();
		Flux<CategoryCommand> catFlux = Flux.just(cat1);

		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));
		when(categoryService.getAllCategoryCommands()).thenReturn(catFlux);

		mockMvc.perform(get("/recipe/2/update"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeFormValidationFail() throws Exception {
		RecipeCommand command = new RecipeCommand();
		command.setId("2");
		command.setDirections("asdffd");
		command.setDescription("abbb");
		when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

		mockMvc.perform(post("/recipe")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "")
				.param("description", "the description"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/recipeform"));
	}

	@Test
	public void testDeleteRecipe() throws Exception {

		// given
		RecipeCommand command = new RecipeCommand();
		command.setId("2");

		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

		// when
		mockMvc.perform(get("/recipe/2/delete"))
				// then
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));

		verify(recipeService, times(1)).deleteByID(anyString());
	}

	@Test
	public void testRecipeNotFound() throws Exception {

		when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

		mockMvc.perform(get("/recipe/1/show"))
				.andExpect(status().isNotFound())
				.andExpect(view().name("404error"));
	}

	@Test
	public void testInvalidNumber() throws Exception {

		when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

		mockMvc.perform(get("/recipe/1a/show"))
				.andExpect(status().isNotFound())
				.andExpect(view().name("404error"));
	}

}
