package nl.androidappfactory.recipe.models;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document
public class Recipe {

	@Id
	private String id;
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	private String directions;
	private Set<Ingredient> ingredients = new HashSet<>();
	private Byte[] image;
	private Difficulty difficulty;
	private Notes notes;

	private Set<Category> categories = new HashSet<>();

	public void addCategory(Category category) {
		this.categories.add(category);
	}

	public void addIngredient(Ingredient ingredient) {
		// ingredient.setRecipe(this);
		this.ingredients.add(ingredient);
	}

	public void removeIngredient(Ingredient ingredient) {
		// ingredient.setRecipe(null);
		this.ingredients.remove(ingredient);
	}

	public void setNotes(Notes notes) {
		if (notes != null) {
			// notes.setRecipe(this);
			this.notes = notes;
		}
	}
}
