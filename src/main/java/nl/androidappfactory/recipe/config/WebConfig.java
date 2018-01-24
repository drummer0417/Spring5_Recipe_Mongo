package nl.androidappfactory.recipe.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.services.RecipeService;

/**
 * Created by jt on 8/29/17.
 */
@Configuration
public class WebConfig {

	@Bean
	public RouterFunction<?> routes(RecipeService recipeService) {
		return RouterFunctions.route(GET("/api/recipes"),
				serverRequest -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(recipeService.getAllRecipes(), Recipe.class));
	}

	// @Bean
	// public RouterFunction<?> routes(CategoryService categoryService) {
	// return RouterFunctions.route(GET("/api/categories"),
	// serverRequest -> ServerResponse
	// .ok()
	// .contentType(MediaType.APPLICATION_JSON)
	// .body(categoryService.getAllCategories(), Category.class));
	// }
}