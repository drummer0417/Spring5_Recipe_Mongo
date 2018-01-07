package nl.androidappfactory.recipe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.exceptions.NotFoundException;
import nl.androidappfactory.recipe.models.Recipe;
import nl.androidappfactory.recipe.repositories.RecipeRepository;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public void saveImageFile(String recipeId, MultipartFile file) {

		log.debug("in saveImageFile: ");

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {
			throw new NotFoundException("Recipe not found");
		}
		Recipe recipe = recipeOptional.get();

		try {

			Byte[] bytesToSave = new Byte[file.getBytes().length];

			int l = 0;
			for (Byte aByte : file.getBytes()) {
				bytesToSave[l++] = aByte;
			}

			recipe.setImage(bytesToSave);
			recipeRepository.save(recipe);

		} catch (Exception e) {

			throw new RuntimeException(e);

		}
	}
}
