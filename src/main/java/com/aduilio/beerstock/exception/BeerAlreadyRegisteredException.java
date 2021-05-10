package com.aduilio.beerstock.exception;

/**
 * Thrown to indicate that the application has attempted to create a beer with a
 * name that already exists.
 */
public class BeerAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = 8486505345635843445L;

	public BeerAlreadyRegisteredException(final String name) {
		super("A beer already exists with name " + name);
	}
}
