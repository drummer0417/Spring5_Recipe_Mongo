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
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

	@Synchronized
	@Nullable
	@Override
	public UnitOfMeasure convert(UnitOfMeasureCommand command) {

		if (command == null) {
			return null;
		}

		UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
		unitOfMeasure.setId(command.getId());
		unitOfMeasure.setDescription(command.getDescription());
		return unitOfMeasure;
	}
}
