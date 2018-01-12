package nl.androidappfactory.recipe.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.RecipeCommand;
import nl.androidappfactory.recipe.services.ImageService;
import nl.androidappfactory.recipe.services.RecipeService;

@Slf4j
@Controller
public class ImageController {

	private ImageService imageService;
	private RecipeService recipeService;

	public ImageController(ImageService imageService, RecipeService recipeService) {
		this.imageService = imageService;
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/image")
	public String showImageUploadForm(@PathVariable String id, Model model) {

		log.debug("in showImageUploadForm..");
		model.addAttribute("recipe", recipeService.findCommandById(id).block());

		return "recipe/imageuploadform";
	}

	@PostMapping("/recipe/{id}/image")
	public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {

		log.debug("in handleImagePost, file: " + file.getName());
		imageService.saveImageFile(id, file).block();

		return "redirect:/recipe/" + id + "/show";
	}

	@GetMapping("/recipe/{id}/recipeimage")
	public void getImageFromDb(@PathVariable String id, HttpServletResponse response) throws IOException {

		RecipeCommand recipeCommand = recipeService.findCommandById(id).block();

		if (recipeCommand.getImage() != null) {
			byte[] byteArray = new byte[recipeCommand.getImage().length];
			int i = 0;

			for (Byte wrappedByte : recipeCommand.getImage()) {
				byteArray[i++] = wrappedByte; // auto unboxing
			}

			response.setContentType("image/jpeg");
			InputStream is = new ByteArrayInputStream(byteArray);
			IOUtils.copy(is, response.getOutputStream());
		}
	}
}
