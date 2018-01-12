package nl.androidappfactory.recipe.services;

import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.models.Category;
import reactor.core.publisher.Flux;

public interface CategoryService {

	public Flux<Category> getAllCategories();

	public Flux<CategoryCommand> getAllCategoryCommands();
}
