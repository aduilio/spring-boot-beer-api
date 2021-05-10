package com.aduilio.beerstock.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.aduilio.beerstock.dto.BeerDto;
import com.aduilio.beerstock.entity.Beer;

/**
 * Maps the Beer entity and DTO.
 */
@Mapper
public interface BeerMapper {

	BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

	BeerDto mapBeerDtoFrom(final Beer beer);

	Beer mapBeerFrom(final BeerDto beerDto);
}
