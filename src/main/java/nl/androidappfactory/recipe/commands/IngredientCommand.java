package nl.androidappfactory.recipe.commands;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by jt on 6/21/17.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class IngredientCommand {

	private String id;
	private String recipeId;
	private String description;
	private BigDecimal amount;
	private UnitOfMeasureCommand uom;

}
