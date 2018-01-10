package nl.androidappfactory.recipe.repositories.reactive;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.bootstrap.RecipeBootstrap;
import nl.androidappfactory.recipe.models.Category;
import nl.androidappfactory.recipe.repositories.CategoryRepository;
import nl.androidappfactory.recipe.repositories.RecipeRepository;
import nl.androidappfactory.recipe.repositories.UnitOfMeasureRepository;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
public class CategoryRepositoryReactiveIT {

	@Autowired
	CategoryReactiveRepository categoryReactiveRepository;

	@Autowired
	RecipeReactiveRepository recipeReactiveRepository;

	@Autowired
	UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	public RecipeRepository recipeRepository;

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {

		categoryReactiveRepository.deleteAll();

		RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository,
				unitOfMeasureRepository);

		recipeBootstrap.onApplicationEvent(null);
	}

	@Test
	public void countCaegories() {
		//
		Long numberOfCategories = categoryReactiveRepository.count().block();
		log.debug("in countCaegories, #caegories: " + numberOfCategories);

		assertEquals(new Long(4), numberOfCategories);

	}

	@Test
	public void testSave() throws Exception {
		Category category = new Category();
		category.setDescription("Dutch");

		categoryReactiveRepository.save(category).block();

		Long count = categoryReactiveRepository.count().block();

		assertEquals(Long.valueOf(5L), count);
	}
}
