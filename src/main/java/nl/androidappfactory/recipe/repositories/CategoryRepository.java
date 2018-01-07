package nl.androidappfactory.recipe.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import nl.androidappfactory.recipe.models.Category;

public interface CategoryRepository extends CrudRepository<Category, String> {

	Optional<Category> findByDescription(String description);
}
