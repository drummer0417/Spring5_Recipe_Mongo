package nl.androidappfactory.recipe.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.IngredientCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import nl.androidappfactory.recipe.services.IngredientService;
import nl.androidappfactory.recipe.services.RecipeService;
import nl.androidappfactory.recipe.services.UnitOfMeasureServise;

@Slf4j
@Controller
public class IngredientController {

	RecipeService recipeService;
	IngredientService ingredientService;
	UnitOfMeasureServise unitOfMeasureService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureServise unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@GetMapping("/recipe/{recipeId}/ingredients")
	public String getAllIngredients(@PathVariable String recipeId, Model model) {

		log.debug("Getting ingredients: ");
		RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
		model.addAttribute("recipe", recipeCommand);

		return "recipe/ingredient/list";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
	public String getIngredient(
			@PathVariable String recipeId,
			@PathVariable String ingredientId,
			Model model) {

		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
		log.debug("after call service: " + ingredientCommand);
		model.addAttribute("ingredient", ingredientCommand);

		return "recipe/ingredient/show";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
	public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model modewl) {

		ingredientService.deleteIngredient(recipeId, ingredientId);

		return "redirect:/recipe/" + recipeId + "/ingredients";

	}

	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
	public String getIngredientForm(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

		IngredientCommand ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
		List<UnitOfMeasureCommand> uomList = unitOfMeasureService.getAll();

		model.addAttribute("ingredient", ingredient);
		model.addAttribute("uomList", uomList);

		log.debug("update ingredient: " + ingredient);
		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/new")
	public String getNewIngredientForm(@PathVariable String recipeId, Model model) {

		IngredientCommand ingredient = new IngredientCommand();
		// ingredient.setRecipeId(recipeId);
		ingredient.setUom(new UnitOfMeasureCommand());

		List<UnitOfMeasureCommand> uomList = unitOfMeasureService.getAll();

		model.addAttribute("ingredient", ingredient);
		model.addAttribute("uomList", uomList);

		log.debug("update ingredient: " + ingredient);
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("/recipe/{recipeId}/ingredient")
	public String saveIngredient(@PathVariable String recipeId, @ModelAttribute IngredientCommand ingredientCommand) {

		log.debug("in saveIngredient: " + ingredientCommand);

		if (ingredientCommand.getId() == null || ingredientCommand.getId().isEmpty()) {
			ingredientService.createIngredient(ingredientCommand);
		} else {
			ingredientService.updateIngredient(ingredientCommand);
		}
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}
}
