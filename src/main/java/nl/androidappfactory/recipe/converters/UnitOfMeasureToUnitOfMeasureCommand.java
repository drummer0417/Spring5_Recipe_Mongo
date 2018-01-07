/**
 * 
 */
package nl.androidappfactory.recipe.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import lombok.Synchronized;
import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import nl.androidappfactory.recipe.models.UnitOfMeasure;

/**
 * @author Hans van Meurs
 *
 */
@Component
public class UnitOfMeasureToUnitOfMeasureCommand implements Converter<UnitOfMeasure, UnitOfMeasureCommand> {

	@Synchronized
	@Nullable
	@Override
	public UnitOfMeasureCommand convert(UnitOfMeasure uom) {

		if (uom == null) {
			return null;
		}

		UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
		uomCommand.setId(uom.getId());
		uomCommand.setDescription(uom.getDescription());
		return uomCommand;
	}
}
