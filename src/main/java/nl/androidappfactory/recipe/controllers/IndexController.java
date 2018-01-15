package nl.androidappfactory.recipe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.services.RecipeService;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class IndexController {

	private RecipeService recipeService;

	public IndexController(RecipeService recipeService) {

		this.recipeService = recipeService;
	}

	@RequestMapping({ "/", "", "index", "recipes" })
	public String getIndexPage(Model model) {

		log.debug("in getAllRecipes()");
		Flux<Recipe> recipes = recipeService.getAllRecipes();
		model.addAttribute("recipes", recipes);
		return "index";
	}

}
