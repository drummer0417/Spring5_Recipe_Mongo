package nl.androidappfactory.recipe.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import nl.androidappfactory.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import nl.androidappfactory.recipe.models.UnitOfMeasure;
import nl.androidappfactory.recipe.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public final class UnitOfMeasureServiceImpl implements UnitOfMeasureServise {

	UnitOfMeasureReactiveRepository unitofmeasureReactiveRepository;
	UnitOfMeasureToUnitOfMeasureCommand converter;

	public UnitOfMeasureServiceImpl(UnitOfMeasureReactiveRepository unitofmeasureReactiveRepository,
			UnitOfMeasureToUnitOfMeasureCommand converter) {
		this.unitofmeasureReactiveRepository = unitofmeasureReactiveRepository;
		this.converter = converter;
	}

	@Override
	public Flux<UnitOfMeasureCommand> getAll() {

		log.info("in getAll... doing it reactive: ");
		Flux<UnitOfMeasure> uoms = unitofmeasureReactiveRepository.findAll();

		return uoms.map(converter::convert);
	}
}
