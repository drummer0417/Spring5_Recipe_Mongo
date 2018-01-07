package nl.androidappfactory.recipe.repositories;

import org.springframework.data.repository.CrudRepository;

import nl.androidappfactory.recipe.models.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
