package nl.androidappfactory.recipe.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import nl.androidappfactory.recipe.models.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryIT {

	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {}

	@Test
	public void testGetTeaspoon() {

		String expectedUomDescription = "Teaspoon";

		Optional<UnitOfMeasure> uom = unitOfMeasureRepository.findByDescription("Teaspoon");

		assertEquals(expectedUomDescription, uom.get().getDescription());
	}

	@Test
	public void testgetCup() {

		String expectedUomDescription = "Cup";

		Optional<UnitOfMeasure> uom = unitOfMeasureRepository.findByDescription("Cup");

		assertEquals(expectedUomDescription, uom.get().getDescription());
	}

}
