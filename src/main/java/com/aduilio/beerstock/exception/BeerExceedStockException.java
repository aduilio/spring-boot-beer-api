package com.aduilio.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown to indicate that the application has attempted to add beers and
 * overflowed the stock.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerExceedStockException extends Exception {

	private static final long serialVersionUID = 211500229754224047L;

	public BeerExceedStockException(final int free) {
		super("Space available for " + free + " beer(s)");
	}
}
