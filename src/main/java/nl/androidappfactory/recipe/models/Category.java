package nl.androidappfactory.recipe.models;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Category {

	private String id;
	private String description;

	@DBRef
	private Set<Recipe> recipes;

	@Override
	public String toString() {
		return "CategoryRepository [id=" + id + ", description=" + description + "]";
	}

}
