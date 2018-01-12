package nl.androidappfactory.recipe.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.converters.CategoryToCategoryCommand;
import nl.androidappfactory.recipe.models.Category;
import nl.androidappfactory.recipe.repositories.reactive.CategoryReactiveRepository;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryReactiveRepository categoryReactiveRepository;
	private CategoryToCategoryCommand categoryToCommandConverter;

	public CategoryServiceImpl(CategoryReactiveRepository categoryReactigveRepository,
			CategoryToCategoryCommand categoryToCommandConverter) {
		this.categoryReactiveRepository = categoryReactigveRepository;
		this.categoryToCommandConverter = categoryToCommandConverter;
	}

	@Override
	public Flux<Category> getAllCategories() {

		log.debug("in getAllCategories: ");

		return categoryReactiveRepository.findAll();
	}

	@Override
	public Flux<CategoryCommand> getAllCategoryCommands() {

		log.debug("in getAllCategoryCommands: ");

		return categoryReactiveRepository.findAll().map(categoryToCommandConverter::convert);

	}

}
