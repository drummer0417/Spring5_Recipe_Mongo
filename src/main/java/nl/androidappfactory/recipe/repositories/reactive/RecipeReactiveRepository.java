package nl.androidappfactory.recipe.repositories.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import nl.androidappfactory.recipe.models.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeReactiveRepository extends ReactiveCrudRepository<Recipe, String> {

	Mono<Recipe> findByDescription(String desctription);

	Flux<Recipe> findAllByOrderByDescription();

}
