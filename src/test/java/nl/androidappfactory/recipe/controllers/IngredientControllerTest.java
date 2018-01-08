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
import nl.androidappfactory.recipe.services.IngredientService;
import nl.androidappfactory.recipe.services.RecipeService;
import nl.androidappfactory.recipe.services.UnitOfMeasureServise;

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

		when(recipeService.findCommandById(anyString())).thenReturn(recipeCommand);

		mockMvc.perform(get("/recipe/1/ingredients"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/list"))
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyString());

	}

	@Test
	public void testShowIngredient() throws Exception {

		// Given
		IngredientCommand ingredientCommand = new IngredientCommand();

		when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);

		mockMvc.perform(get("/recipe/1/ingredient/2/show"))
				.andExpect(status().isOk())
				.andExpect(view().name("recipe/ingredient/show"))
				.andExpect(model().attributeExists("ingredient"));
	}

	@Test
	public void testDeleteIngredient() throws Exception {

		mockMvc.perform(get("/recipe/1/ingredient/2/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/1/ingredients"));

		verify(ingredientService, times(1)).deleteIngredient(anyString(), anyString());
	}

	@Test
	public void saveIngredient() throws Exception {

		IngredientCommand ic = new IngredientCommand();
		ic.setId("3");

		mockMvc.perform(post("/recipe/2/ingredient")
				.param("id", String.valueOf(ic.getId())))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredients"));

		verify(ingredientService, times(1)).saveIngredientCommand(any());
	}

	@Test
	public void createIngredient() throws Exception {

		mockMvc.perform(post("/recipe/2/ingredient")
				.param("id", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/2/ingredients"));

		verify(ingredientService, times(1)).saveIngredientCommand(any());
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
