package nl.androidappfactory.recipe.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.CategoryCommand;
import nl.androidappfactory.recipe.converters.CategoryToCategoryCommand;
import nl.androidappfactory.recipe.repositories.CategoryRepository;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	private CategoryToCategoryCommand categoryConverter;

	public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryCommand categoryConverter) {
		this.categoryRepository = categoryRepository;
		this.categoryConverter = categoryConverter;
	}

	@Override
	public List<nl.androidappfactory.recipe.models.Category> getAllCategories() {

		log.debug("in getAllCategories: ");

		List<nl.androidappfactory.recipe.models.Category> categories = new ArrayList<>();

		categoryRepository.findAll().forEach(category -> categories.add(category));

		return categories;
	}

	@Override
	public List<CategoryCommand> getAllCategoryCommands() {

		log.debug("in getAllCategoryCommands: ");

		List<CategoryCommand> categories = new ArrayList<>();

		categoryRepository.findAll().forEach(category -> categories.add(categoryConverter.convert(category)));

		return categories;
	}

}
