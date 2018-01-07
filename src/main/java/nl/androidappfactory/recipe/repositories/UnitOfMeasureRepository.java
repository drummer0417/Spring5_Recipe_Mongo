package nl.androidappfactory.recipe.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import nl.androidappfactory.recipe.models.UnitOfMeasure;

public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

	Optional<UnitOfMeasure> findByDescription(String description);

}
