package nl.androidappfactory.recipe.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CategoryTest {

	Category category;

	@Before
	public void setup() {
		category = new Category();
	}

	@Test
	public void testGetId() {

		String idValue = "3";
		category.setId(idValue);

		assertEquals(idValue, category.getId());
	}

	@Test
	public void testGetDescription() {

		String descriptionValue = "abcdef";
		category.setDescription(descriptionValue);

		assertEquals(descriptionValue, category.getDescription());
	}

}
