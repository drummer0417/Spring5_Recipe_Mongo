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
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import nl.androidappfactory.recipe.services.IngredientService;
import nl.androidappfactory.recipe.services.RecipeService;
import nl.androidappfactory.recipe.services.UnitOfMeasureServise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class IngredientControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	IngredientService ingredientService;

	@Mock
	UnitOfMeasureServise unitOfMeasureService;

	IngredientController controller;

	MockMvc mockMvc;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

	}

	@Test
	public void testListIngredients() throws Exception {

		// Given
		RecipeCommand recipeCommand = new RecipeCommand();

		when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

		mockMvc.perform(get("/recipe/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/list"))
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyString());

	}

	@Test
	public void testShowIngredient() throws Exception {

		// Given
		Mono<IngredientCommand> ingredientCommandMono = Mono.just(new IngredientCommand());

		when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString()))
				.thenReturn(ingredientCommandMono);

		mockMvc.perform(get("/recipe/1/ingredient/2/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/show"))
				.andExpect(model().attributeExists("ingredient"));
	}

	@Test
	public void testDeleteIngredient() throws Exception {

		when(ingredientService.deleteIngredient(anyString(), anyString())).thenReturn(Mono.empty());

		mockMvc.perform(get("/recipe/1/ingredient/2/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/1/ingredients"));

		verify(ingredientService, times(1)).deleteIngredient(anyString(), anyString());
	}

	@Test
	public void saveIngredient() throws Exception {

		// given
		IngredientCommand command = new IngredientCommand();
		command.setId("3");
		command.setRecipeId("2");

		// when
		when(ingredientService.saveIngredientCommand(any())).thenReturn(Mono.just(command));

		mockMvc.perform(post("/recipe/2/ingredient")
				.param("id", "")
				.param("description", "iets"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredients"));

		verify(ingredientService, times(1)).saveIngredientCommand(any());
	}

	@Test
	public void testNewIngredient() throws Exception {

		// given
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId("1");

		// when
		when(unitOfMeasureService.getAll()).thenReturn(Flux.just(new UnitOfMeasureCommand()));

		// then
		mockMvc.perform(get("/recipe/1/ingredient/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/ingredientform"))
				.andExpect(model().attributeExists("ingredient"))
				.andExpect(model().attributeExists("uomList"));

		verify(unitOfMeasureService, times(1)).getAll();

	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
