package com.aduilio.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that the application has attempted to read a beer that
 * does not exist.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BeerNotFoundException extends Exception {

	private static final long serialVersionUID = -5453365429772776167L;

	public BeerNotFoundException(final Long id) {
		super("Invalid id " + id);
	}

	public BeerNotFoundException(final String name) {
		super("Invalid name " + name);
	}
}
