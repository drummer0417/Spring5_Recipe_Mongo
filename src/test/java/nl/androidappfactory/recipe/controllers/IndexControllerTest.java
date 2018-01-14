package nl.androidappfactory.recipe.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.services.RecipeService;
import reactor.core.publisher.Flux;

/**
 * Created by jt on 6/17/17.
 */

public class IndexControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	Model model;

	IndexController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		controller = new IndexController(recipeService);
	}

	//
	// Ignore for now
	//

	//
	// Ignore for now
	//

	//
	// Ignore for now
	//

	// @Test
	// public void testMockMvc() throws Exception {
	//
	// String expectedView = "index";
	//
	// when(recipeService.getAllRecipes()).thenReturn(Flux.empty());
	//
	// MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	// mockMvc.perform(get("/"))
	// .andExpect(status().isOk())
	// .andExpect(view().name(expectedView));
	// }

	@Test
	public void getIndexPage() throws Exception {

		// given
		Recipe recipe1 = new Recipe();
		recipe1.setId("1");
		recipe1.setDescription("Frikandel");

		Recipe recipe2 = new Recipe();
		recipe2.setId("2");
		recipe2.setDescription("Friet");

		when(recipeService.getAllRecipes()).thenReturn(Flux.just(recipe1, recipe2));

		@SuppressWarnings("unchecked")
		ArgumentCaptor<List<Recipe>> argumentCaptor = ArgumentCaptor.forClass(List.class);

		// when
		String viewName = controller.getIndexPage(model);

		// then
		assertEquals("index", viewName);
		verify(recipeService, times(1)).getAllRecipes();
		verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
		List<Recipe> listInController = argumentCaptor.getValue();
		assertEquals(2, listInController.size());
	}
}
