package nl.androidappfactory.recipe.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private RecipeReactiveRepository recipeReactiveRepository;

	public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
		this.recipeReactiveRepository = recipeReactiveRepository;
	}

	@Override
	public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

		log.debug("in saveImageFile: ");

		Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
				.map(recipe -> {
					Byte[] byteObjects = new Byte[0];
					try {
						byteObjects = new Byte[file.getBytes().length];

						int i = 0;
						for (byte b : file.getBytes()) {
							byteObjects[i++] = b;
						}
						recipe.setImage(byteObjects);
						return recipe;

					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				});

		recipeReactiveRepository.save(recipeMono.block()).block();

		return Mono.empty();
	}
}
