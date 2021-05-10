package com.aduilio.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that the application has attempted to create a beer with a
 * name that already exists.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = 8486505345635843445L;

	public BeerAlreadyRegisteredException(final String name) {
		super("A beer already exists with name " + name);
	}
}
