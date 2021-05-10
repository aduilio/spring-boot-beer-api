package com.aduilio.beerstock.utils;

import com.aduilio.beerstock.dto.BeerDto;
import com.aduilio.beerstock.entity.Beer;
import com.aduilio.beerstock.enums.BeerType;

public class BeerTestsUtil {

	public static final Long BEER_ID = 1L;
	public static final String BEER_NAME = "beer_name";
	public static final String BEER_BRAND = "beer_brand";
	public static final int BEER_MAX = 10;
	public static final int BEER_QTT = 2;
	public static final BeerType BEER_TYPE = BeerType.ALE;

	public static Beer createBeer() {
		return Beer.builder()
				.id(BEER_ID)
				.name(BEER_NAME)
				.brand(BEER_BRAND)
				.max(BEER_MAX)
				.quantity(BEER_QTT)
				.type(BEER_TYPE)
				.build();
	}

	public static BeerDto createBeerDto() {
		return BeerDto.builder()
				.id(BEER_ID)
				.name(BEER_NAME)
				.brand(BEER_BRAND)
				.max(BEER_MAX)
				.quantity(BEER_QTT)
				.type(BEER_TYPE)
				.build();
	}
}
