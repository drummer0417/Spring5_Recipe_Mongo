package nl.androidappfactory.recipe.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.services.CategoryService;
import nl.androidappfactory.recipe.services.RecipeService;

/**
 * Created by jt on 6/19/17.
 */
@Slf4j
@Controller
public class RecipeController {

	private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
	private final RecipeService recipeService;
	private final CategoryService categoryService;

	public RecipeController(RecipeService recipeService, CategoryService categoryService) {
		this.recipeService = recipeService;
		this.categoryService = categoryService;
	}

	@GetMapping("/recipe/{id}/show")
	public String showById(@PathVariable String id, Model model) {

		log.debug("id: " + id);
		model.addAttribute("recipe", recipeService.findById(id));

		return "recipe/show";
	}

	@GetMapping("recipe/new")
	public String newRecipe(Model model) {
		model.addAttribute("recipe", new RecipeCommand());

		return "recipe/recipeform";
	}

	@GetMapping("recipe/{id}/update")
	public String updateRecipeForm(@PathVariable String id, Model model) {

		RecipeCommand recipeCommand = recipeService.findCommandById(id);

		List<String> currentCategoryIds = new ArrayList<>();
		recipeCommand.getCategories()
				.forEach(category -> recipeCommand.getCurrentCategoryIds().add(String.valueOf(category.getId())));
		recipeCommand.setCategoryList(categoryService.getAllCategoryCommands());

		model.addAttribute("recipe", recipeCommand);

		return RECIPE_RECIPEFORM_URL;
	}

	@PostMapping("recipe")
	public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult) {

		log.debug("in saveOrUpdate: " + Arrays.toString(command.getSelectedCategories()));

		if (bindingResult.hasErrors()) {

			bindingResult.getAllErrors()
					.forEach(objectError -> log.debug("Validation errors: ........" + objectError.toString()));

			return RECIPE_RECIPEFORM_URL;
		}

		log.debug("After check binding reuslts... no errors : ");
		// Recipe recipe = null;
		// try {
		// recipe = recipeService.findById(recipeCommand.getId());
		// recipeCommand.setImage(recipe.getImage());
		// } catch (Exception e) {
		//
		// // Ignore... is new recipe TODO: handle exception in service
		// }

		RecipeCommand recipeAfterSave = recipeService.saveRecipeCommand(command);
		log.debug("after save: " + recipeAfterSave.getId());
		return "redirect:/recipe/" + recipeAfterSave.getId() + "/show";

	}

	public String deleteRecipe(@Valid @ModelAttribute RecipeCommand recipeCommand, BindingResult bindingResult) {

		log.debug("in saveOrUpdate: " + Arrays.toString(recipeCommand.getSelectedCategories()));

		if (bindingResult.hasErrors()) {

			bindingResult.getAllErrors()
					.forEach(objectError -> log.debug("Validation errors: ........" + objectError.toString()));

			return RECIPE_RECIPEFORM_URL;
		}

		log.debug("After check binding reuslts... no errors : ");
		// Recipe recipe = null;
		// try {
		// recipe = recipeService.findById(recipeCommand.getId());
		// recipeCommand.setImage(recipe.getImage());
		// } catch (Exception e) {
		//
		// // Ignore... is new recipe TODO: handle exception in service
		// }

		RecipeCommand recipeAfterSave = recipeService.saveRecipeCommand(recipeCommand);
		log.debug("after save: " + recipeAfterSave.getId());
		return "redirect:/recipe/" + recipeAfterSave.getId() + "/show";
	}

	@GetMapping("recipe/{id}/delete")
	public String deleteById(@PathVariable String id) {

		log.debug("Deleting id: " + id);

		recipeService.deleteByID(id);
		return "redirect:/";
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NotFoundException.class)
	public ModelAndView handleNotFound(Exception exception) {

		log.error("Handling not found exception");
		log.error(exception.getMessage());

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("404error");
		modelAndView.addObject("exception", exception);

		return modelAndView;
	}

}