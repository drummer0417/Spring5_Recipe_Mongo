package nl.androidappfactory.recipe.controllers;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import nl.androidappfactory.recipe.config.WebConfig;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.services.RecipeService;
import reactor.core.publisher.Flux;

public class RouterFunctionTest {

	WebTestClient webTestClient;

	@Mock
	RecipeService recipeService;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		WebConfig webConfig = new WebConfig();

		RouterFunction<?> routerFunction = webConfig.routes(recipeService);

		webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
	}

	@Test
	public void testGetReicpesWithoutData() {

		when(recipeService.getAllRecipes()).thenReturn(Flux.empty());

		webTestClient.get().uri("/api/recipes")
				.accept(MediaType.APPLICATION_JSON)
				.exchange() // cause service to start
				.expectStatus().isOk();
	}

	@Test
	public void testGetReicpesData() {

		when(recipeService.getAllRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

		webTestClient.get().uri("/api/recipes")
				.accept(MediaType.APPLICATION_JSON)
				.exchange() // cause service to start
				.expectStatus().isOk()
				.expectBodyList(Recipe.class);
	}

}
