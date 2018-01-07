package nl.androidappfactory.recipe.models;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notes {

	@Id
	private String id;
	private Recipe recipe;
	private String recipeNotes;

	public Notes() {}

	public Notes(String recipeNotes) {
		this.recipeNotes = recipeNotes;
	}

}
