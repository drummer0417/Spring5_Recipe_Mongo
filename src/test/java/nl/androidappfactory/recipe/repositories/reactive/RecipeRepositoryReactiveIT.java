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
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.CategoryRepository;
import nl.androidappfactory.recipe.repositories.RecipeRepository;
import nl.androidappfactory.recipe.repositories.UnitOfMeasureRepository;
import reactor.core.publisher.Mono;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeRepositoryReactiveIT {

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

		recipeRepository.deleteAll();

		RecipeBootstrap recipeBootstrap = new RecipeBootstrap(categoryRepository, recipeRepository,
				unitOfMeasureRepository);

		recipeBootstrap.onApplicationEvent(null);
	}

	@Test
	public void countRecipes() throws Exception {

		Recipe recipe = new Recipe();
		recipe.setDescription("Gebakken eitje");

		Mono<Recipe> savedRecipeMono = recipeReactiveRepository.save(recipe);

		log.debug("recipeMono: " + savedRecipeMono.block());

		// CountDownLatch countDownLatch = new CountDownLatch(1);
		//
		// Flux<Recipe> recipeFlux = recipeReactiveRepository.findAll();
		//
		// log.debug("before: ");
		// recipeFlux.delayElements(Duration.ofSeconds(0))
		// .doOnComplete(countDownLatch::countDown)
		// .subscribe(aRecipe -> log.debug("recipeFlux: " + aRecipe.getDescription()));
		//
		// countDownLatch.await();

		// log.debug("#recipeList: " + recipeList.size());
		// people.subscribe(person -> log.info(person.sayMyName()));
		log.debug("after: ");

		Long numberOfRecipes = recipeReactiveRepository.count().block();

		// assertEquals("123", recipeRead.getId());
		assertEquals(new Long(3), numberOfRecipes);
	}

	@Test
	public void findByDescription() throws Exception {

		String description = "Ommeletje";
		Recipe recipe = new Recipe();
		recipe.setDescription(description);

		Mono<Recipe> recipeMono = recipeReactiveRepository.save(recipe);

		log.debug("recipeMono: " + recipeMono.block());

		Recipe recipeRead = recipeReactiveRepository.findByDescription(description).block();

		assertEquals(description, recipeRead.getDescription());
		assertTrue(recipeRead != null && recipeRead.getId() != null && !recipe.getId().isEmpty());
	}

	@Test
	public void testRecipeSave() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setDescription("Yummy");

		recipeReactiveRepository.save(recipe).block();

		Long count = recipeReactiveRepository.count().block();

		assertEquals(Long.valueOf(3L), count);
	}
}
