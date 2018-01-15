package nl.androidappfactory.recipe.commands;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

	@NotBlank
	@Size(min = 2)
	private String description;

	@NotNull
	@Min(1)
	private BigDecimal amount;

	@NotNull
	private UnitOfMeasureCommand uom;

}
