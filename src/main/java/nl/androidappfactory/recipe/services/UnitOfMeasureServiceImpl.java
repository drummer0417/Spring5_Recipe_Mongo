package nl.androidappfactory.recipe.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import nl.androidappfactory.recipe.commands.UnitOfMeasureCommand;
import nl.androidappfactory.recipe.converters.UnitOfMeasureToUnitOfMeasureCommand;
import nl.androidappfactory.recipe.models.UnitOfMeasure;
import nl.androidappfactory.recipe.repositories.UnitOfMeasureRepository;

@Service
public final class UnitOfMeasureServiceImpl implements UnitOfMeasureServise {

	UnitOfMeasureRepository unitofmeasureRepository;
	UnitOfMeasureToUnitOfMeasureCommand converter;

	public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitofmeasureRepository,
			UnitOfMeasureToUnitOfMeasureCommand converter) {
		this.unitofmeasureRepository = unitofmeasureRepository;
		this.converter = converter;
	}

	@Override
	public List<UnitOfMeasureCommand> getAll() {

		List<UnitOfMeasure> uomList = (List<UnitOfMeasure>) unitofmeasureRepository.findAll();

		List<UnitOfMeasureCommand> uomCommandList = new ArrayList<>();

		for (UnitOfMeasure unitOfMeasure : uomList) {
			uomCommandList.add(converter.convert(unitOfMeasure));
		}
		return uomCommandList;
	}

}
