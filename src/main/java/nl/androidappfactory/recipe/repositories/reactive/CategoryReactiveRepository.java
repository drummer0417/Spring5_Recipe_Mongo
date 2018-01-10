package nl.androidappfactory.recipe.repositories.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import nl.androidappfactory.recipe.models.Category;
import reactor.core.publisher.Mono;

public interface CategoryReactiveRepository extends ReactiveCrudRepository<Category, String> {

	Mono<Category> findByDescription(String description);

}
