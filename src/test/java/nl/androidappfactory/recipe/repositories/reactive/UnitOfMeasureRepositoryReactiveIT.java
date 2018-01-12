package nl.androidappfactory.recipe.repositories.reactive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.bootstrap.RecipeBootstrap;
import nl.androidappfactory.recipe.models.UnitOfMeasure;
import nl.androidappfactory.recipe.repositories.RecipeRepository;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureRepositoryReactiveIT {

	@Autowired
	CategoryReactiveRepository categoryReactiveRepository;

	@Autowired
	RecipeReactiveRepository recipeReactiveRepository;

	@Autowired
	UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	@Autowired
	nl.androidappfactory.recipe.repositories.CategoryRepository categoryRepository;

	@Autowired
	public RecipeRepository recipeRepository;

	@Autowired
	nl.androidappfactory.recipe.repositories.UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {

		unitOfMeasureReactiveRepository.deleteAll();

		RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository,
				unitOfMeasureRepository);

		recipeBootstrap.onApplicationEvent(null);
	}

	@Test
	public void countUnitOfMeasures() {
		//
		Long numberOfUnitOfMeasures = unitOfMeasureReactiveRepository.count().block();
		log.debug("in count unitOfMeasure, #unitOfMeasures: " + numberOfUnitOfMeasures);

		assertEquals(new Long(9), numberOfUnitOfMeasures);

	}

	@Test
	public void findByDescription() throws Exception {

		String description = "Each";

		UnitOfMeasure uomRead = unitOfMeasureReactiveRepository.findByDescription(description).block();

		assertEquals(description, uomRead.getDescription());
		assertTrue(uomRead != null && uomRead.getId() != null && !uomRead.getId().isEmpty());
	}

	@Test
	public void testSave() throws Exception {

		log.debug("#uoms.........: " + unitOfMeasureReactiveRepository.count().block());

		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		unitOfMeasure.setDescription("Liter");

		unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

		Long count = unitOfMeasureReactiveRepository.count().block();

		assertEquals(Long.valueOf(10L), count);
	}

}
