package nl.androidappfactory.recipe.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class IngredientController {

	private final static String URL_INGREDIENT_FORM = "recipe/ingredient/ingredientform";

	private RecipeService recipeService;
	private IngredientService ingredientService;
	private UnitOfMeasureServise unitOfMeasureService;

	private WebDataBinder webDataBinder;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureServise unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@InitBinder("ingredient")
	public void initWebDataBinder(WebDataBinder webDataBinder) {

		this.webDataBinder = webDataBinder;
	}

	@GetMapping("/recipe/{recipeId}/ingredients")
	public String getAllIngredients(@PathVariable String recipeId, Model model) {

		log.debug("Getting ingredients: ");
		RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();
		model.addAttribute("recipe", recipeCommand);

		return "recipe/ingredient/list";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
	public String getIngredient(
			@PathVariable String recipeId,
			@PathVariable String ingredientId,
			Model model) {

		log.debug("in getIngredient, reactive now :-) : ");
		// IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId)
		// .block();
		// log.debug("after call service: " + ingredientCommand);
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));

		return "recipe/ingredient/show";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
	public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model modewl) {

		ingredientService.deleteIngredient(recipeId, ingredientId).block();

		return "redirect:/recipe/" + recipeId + "/ingredients";

	}

	@GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
	public String getIngredientForm(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

		IngredientCommand ingredient = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId).block();

		model.addAttribute("ingredient", ingredient);

		log.debug("update ingredient: " + ingredient);
		return URL_INGREDIENT_FORM;
	}

	@GetMapping("/recipe/{recipeId}/ingredient/new")
	public String getNewIngredientForm(@PathVariable String recipeId, Model model) {

		IngredientCommand ingredient = new IngredientCommand();
		ingredient.setRecipeId(recipeId);
		ingredient.setUom(new UnitOfMeasureCommand());

		model.addAttribute("ingredient", ingredient);

		log.debug("new ingredient: " + ingredient);
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("/recipe/{recipeId}/ingredient")
	public String saveIngredient(@PathVariable String recipeId, @ModelAttribute("ingredient") IngredientCommand ingredient,
			Model model) {

		webDataBinder.validate();
		BindingResult bindingResult = webDataBinder.getBindingResult();

		if (bindingResult.hasErrors()) {
			for (ObjectError error : bindingResult.getAllErrors()) {
				log.error("bindingResult errors: " + error.getDefaultMessage());
			}

			return URL_INGREDIENT_FORM;
		}
		log.debug("in saveIngredient: " + ingredient);

		IngredientCommand savedIngredient = ingredientService.saveIngredientCommand(ingredient).block();

		log.info("after save: " + savedIngredient);

		return "redirect:/recipe/" + recipeId + "/ingredients";
	}

	@ModelAttribute("uomList")
	public Flux<UnitOfMeasureCommand> populateUomList() {
		return unitOfMeasureService.getAll();
	}
}
