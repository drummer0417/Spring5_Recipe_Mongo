package nl.androidappfactory.recipe.repositories.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import nl.androidappfactory.recipe.models.UnitOfMeasure;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveCrudRepository<UnitOfMeasure, String> {

	Mono<UnitOfMeasure> findByDescription(String description);

}
