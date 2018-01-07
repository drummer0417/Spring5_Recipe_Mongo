package nl.androidappfactory.recipe.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2136752975381762250L;

	public NotFoundException() {}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable rootCause) {
		super(message, rootCause);

	}

}
