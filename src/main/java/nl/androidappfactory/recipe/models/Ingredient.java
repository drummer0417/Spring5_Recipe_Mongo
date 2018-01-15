package nl.androidappfactory.recipe.models;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ingredient {

	@Id
	private String id = UUID.randomUUID().toString();
	private String description;
	private BigDecimal amount;

	private UnitOfMeasure uom;

	public Ingredient() {}

	public Ingredient(String description, BigDecimal amount, UnitOfMeasure unitOfMeasure) {
		this.description = description;
		this.amount = amount;
		this.uom = unitOfMeasure;
	}
}
