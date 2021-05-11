package com.aduilio.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that the application has attempted to add beers and
 * overflowed the stock.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerNegativeStockException extends Exception {

	private static final long serialVersionUID = 923549569082166414L;

	public BeerNegativeStockException(final int available) {
		super("Only available " + available + " beer(s)");
	}
}
