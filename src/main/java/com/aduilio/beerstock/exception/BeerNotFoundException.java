package com.aduilio.beerstock.exception;

/**
 * Thrown to indicate that the application has attempted to read a beer that
 * does not exist.
 */
public class BeerNotFoundException extends Exception {

	private static final long serialVersionUID = -5453365429772776167L;

	public BeerNotFoundException(final Long id) {
		super("Invalid id " + id);
	}
}
