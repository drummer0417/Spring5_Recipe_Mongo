package nl.androidappfactory.recipe.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.converters.CategoryToCategoryCommand;
import nl.androidappfactory.recipe.converters.RecipeCommandToRecipe;
import nl.androidappfactory.recipe.converters.RecipeToRecipeCommand;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.reactive.CategoryReactiveRepository;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

	private RecipeReactiveRepository recipeReactiveRepository;
	private RecipeCommandToRecipe commandToRecipeConverter;
	private RecipeToRecipeCommand recipeToCommandConverter;
	private CategoryReactiveRepository categoryReactiveRepository;
	private CategoryToCategoryCommand categoryToCategoryCommand;

	public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
			RecipeCommandToRecipe commandToRecipeConverter, RecipeToRecipeCommand recipeToCommandConverter,
			CategoryReactiveRepository categoryReactiveRepository,
			CategoryToCategoryCommand categoryToCategoryCommand) {
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.commandToRecipeConverter = commandToRecipeConverter;
		this.recipeToCommandConverter = recipeToCommandConverter;
		this.categoryReactiveRepository = categoryReactiveRepository;
		this.categoryToCategoryCommand = categoryToCategoryCommand;
	}

	@Override
	public Flux<Recipe> getAllRecipes() {

		log.debug("in: getAllRecipes()");

		return recipeReactiveRepository.findAllByOrderByDescription();
	}

	@Override
	public Mono<Recipe> findById(String id) {

		Mono<Recipe> recipeMono = recipeReactiveRepository.findById(id);

		return recipeMono;
	}

	@Override
	public Mono<RecipeCommand> findCommandById(String id) {

		Mono<RecipeCommand> recipeMono = recipeReactiveRepository.findById(id)
				.map(recipe -> recipeToCommandConverter.convert(recipe));

		return recipeMono;

	}

	@Override
	public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {

		recipeCommand.getCategories().clear();

		recipeCommand.setCategories(getSelectedCategories(recipeCommand.getSelectedCategories()));

		Recipe recipe = commandToRecipeConverter.convert(recipeCommand);

		log.debug("#categories after convert: " + recipe.getCategories().size());

		Mono<RecipeCommand> savedRecipeCommand = null;

		if (recipe != null) {
			savedRecipeCommand = recipeReactiveRepository.save(recipe)
					// .map(recipeSaved -> recipeToCommandConverter.convert(recipeSaved));
					// is same as above
					.map(recipeToCommandConverter::convert);
		}

		return savedRecipeCommand;
	}

	private List<CategoryCommand> getSelectedCategories(String[] selectedCategoryIds) {

		List<CategoryCommand> categoryCommands = new ArrayList<>();

		if (selectedCategoryIds != null) {

			for (String id : selectedCategoryIds) {
				if (id != null) {
					CategoryCommand categoryCommand = categoryToCategoryCommand
							.convert(categoryReactiveRepository.findById(id).block());
					if (categoryCommand == null) {
						log.error("Category not found, id: " + id);
						throw new NotFoundException("\"Category not found, id: \" + id");
					}
					categoryCommands.add(categoryCommand);
					log.debug("newCategory, id: " + id);
				}
			}
		}
		return categoryCommands;
	}

	@Override
	public Mono<Void> deleteByID(String id) {

		recipeReactiveRepository.deleteById(id).block();
		return Mono.empty();
	}

}
