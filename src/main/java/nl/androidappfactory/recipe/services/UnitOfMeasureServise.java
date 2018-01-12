package nl.androidappfactory.recipe.services;

import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureServise {

	public Flux<UnitOfMeasureCommand> getAll();
}
