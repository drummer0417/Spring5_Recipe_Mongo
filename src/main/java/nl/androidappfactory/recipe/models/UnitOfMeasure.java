package nl.androidappfactory.recipe.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Document
@ToString
public class UnitOfMeasure {

	@Id
	private String id;
	private String description;

}
