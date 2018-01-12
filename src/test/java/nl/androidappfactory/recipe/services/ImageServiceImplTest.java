/**
 * 
 */
package nl.androidappfactory.recipe.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Mono;

public class ImageServiceImplTest {

	@Mock
	RecipeReactiveRepository recipeReactiveRepository;

	ImageService imageService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		imageService = new ImageServiceImpl(recipeReactiveRepository);
	}

	@Test
	public void saveImageFile() throws Exception {
		// given
		String id = "1";
		MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
				"Android App Factory".getBytes());

		Recipe recipe = new Recipe();
		recipe.setId(id);
		Mono<Recipe> recipeMono = Mono.just(recipe);

		when(recipeReactiveRepository.findById(anyString())).thenReturn(recipeMono);
		when(recipeReactiveRepository.save(any())).thenReturn(Mono.empty());

		ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

		// when
		imageService.saveImageFile(id, multipartFile);

		// then
		verify(recipeReactiveRepository, times(1)).save(argumentCaptor.capture());
		Recipe savedRecipe = argumentCaptor.getValue();
		assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
	}

}
