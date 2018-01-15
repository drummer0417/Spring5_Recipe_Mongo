package nl.androidappfactory.recipe.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.thymeleaf.exceptions.TemplateInputException;

import lombok.extern.slf4j.Slf4j;
import nl.androidappfactory.recipe.exceptions.NotFoundException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(WebExchangeBindException.class)
	public String handleException(Exception exception, Model model) {
		log.debug("log error: WebExchangeBindException");
		log.debug("exception: " + exception.getMessage());

		model.addAttribute("exception", exception);
		model.addAttribute("text", "You entered an invalid format. It should be a number");

		return "400error";
	}

	//
	// Moved to ControllerExceptionHandler.class
	//
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ NotFoundException.class, TemplateInputException.class })
	public String handleNotFound(Exception exception, Model model) {

		log.error("Handling not found exception");
		log.error(exception.getMessage());

		model.addAttribute("exception", exception);

		return "404error";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NumberFormatException.class)
	public String handleNumberFormatException(NumberFormatException exception, Model model) {
		log.debug("log error: NumberFormatException");
		log.debug("exception: " + exception.getMessage());

		model.addAttribute("text", "You entered an invalid format. It should be a number");
		model.addAttribute("exception", exception);

		return "error400";
	}
	//
	// @ResponseStatus(HttpStatus.BAD_REQUEST)
	// @ExceptionHandler(BindException.class)
	// public ModelAndView handleBindException(Exception exception) {
	// log.debug("log error: Bind Exception (root: NumberFormatExceptionn)");
	//
	// ModelAndView modelAndView = new ModelAndView();
	// modelAndView.setViewName("400error");
	// modelAndView.addObject("text", "Invalid value! It should be a number");
	// modelAndView.addObject("exception", exception);
	//
	// return modelAndView;
	// }

}
