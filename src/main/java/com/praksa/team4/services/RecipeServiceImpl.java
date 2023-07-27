package com.praksa.team4.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.praksa.team4.entities.Recipe;
import com.praksa.team4.entities.dto.RecipeDTO;
import com.praksa.team4.repositories.RecipeRepository;
import com.praksa.team4.util.ErrorMessageHelper;
import com.praksa.team4.util.RESTError;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	
	protected final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	public ResponseEntity<?> createRecipe(RecipeDTO newRecipe, BindingResult result) {

		if (result.hasErrors()) {
	        logger.info("Validating input parameters for recipe");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		Recipe existingRecipe = recipeRepository.findByName(newRecipe.getName());
        logger.info("Checking whether theres an existing recipe in the database");

		if (existingRecipe != null) {
	        logger.error("Recipe with the same name already exists");
			return new ResponseEntity<RESTError>(new RESTError(1, "A subject with the same name already exists"), HttpStatus.CONFLICT);
		}
		
		Recipe recipe = new Recipe();
		
		recipe.setName(newRecipe.getName());
		recipe.setTime(newRecipe.getTime());
		recipe.setSteps(newRecipe.getSteps());
		recipe.setAmount(newRecipe.getAmount());

		recipeRepository.save(recipe);
        logger.info("Saving recipe to the database");
        
		return new ResponseEntity<Recipe>(recipe, HttpStatus.CREATED);
	}
	
	public ResponseEntity<?> updateRecipe(RecipeDTO updatedRecipe, BindingResult result, Integer id) {

		if (result.hasErrors()) {
	        logger.info("Validating input parameters for recipe");
			return new ResponseEntity<>(ErrorMessageHelper.createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		Recipe recipe = recipeRepository.findById(id).orElse(null);

		if (recipe == null) {
	        logger.error("No recipe with " + id + " ID found");
			return new ResponseEntity<RESTError>(new RESTError(1, "No recipe with " + id + " ID found"), HttpStatus.NOT_FOUND);
		}

		recipe.setName(updatedRecipe.getName());
		recipe.setTime(updatedRecipe.getTime());
		recipe.setSteps(updatedRecipe.getSteps());
		recipe.setAmount(updatedRecipe.getAmount());

		recipeRepository.save(recipe);
        logger.info("Saving recipe to the database");

		return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
	}
}
