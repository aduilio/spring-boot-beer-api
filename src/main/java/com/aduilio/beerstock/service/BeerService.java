package com.aduilio.beerstock.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aduilio.beerstock.dto.BeerDto;
import com.aduilio.beerstock.entity.Beer;
import com.aduilio.beerstock.exception.BeerAlreadyRegisteredException;
import com.aduilio.beerstock.exception.BeerNotFoundException;
import com.aduilio.beerstock.mapper.BeerMapper;
import com.aduilio.beerstock.repository.BeerRepository;

import lombok.AllArgsConstructor;

/**
 * Encapsulates the database access for the controller.
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

	private final BeerRepository beerRepository;

	/**
	 * Creates a beer.
	 *
	 * @param beerDto to be created
	 *
	 * @return the id of the beer created.
	 * @throws BeerAlreadyRegisteredException if the name already exists
	 */
	public Long create(final BeerDto beerDto) throws BeerAlreadyRegisteredException {
		verifyName(beerDto.getName());
		return beerRepository.save(BeerMapper.INSTANCE.mapBeerFrom(beerDto))
				.getId();
	}

	/**
	 * Reads the beer by name.
	 *
	 * @param name of the beer
	 *
	 * @return BeerDto
	 * @throws BeerNotFoundException of the name does not exist
	 */
	public BeerDto readByName(final String name) throws BeerNotFoundException {
		return beerRepository.findByName(name)
				.map(BeerMapper.INSTANCE::mapBeerDtoFrom)
				.orElseThrow(() -> new BeerNotFoundException(name));
	}

	/**
	 * Returns all the beers.
	 *
	 * @return {@link List} of {@link BeerDto}
	 */
	public List<BeerDto> list() {
		return beerRepository.findAll()
				.stream()
				.map(BeerMapper.INSTANCE::mapBeerDtoFrom)
				.collect(Collectors.toList());
	}

	/**
	 * Deletes a beer.
	 *
	 * @param id of the beer
	 *
	 * @throws BeerNotFoundException if the beer does not exist
	 */
	public void delete(final Long id) throws BeerNotFoundException {
		readById(id);
		beerRepository.deleteById(id);
	}

	private void verifyName(final String name) throws BeerAlreadyRegisteredException {
		final Optional<Beer> beer = beerRepository.findByName(name);
		if (beer.isPresent()) {
			throw new BeerAlreadyRegisteredException(name);
		}
	}

	private Beer readById(final Long id) throws BeerNotFoundException {
		return beerRepository.findById(id)
				.orElseThrow(() -> new BeerNotFoundException(id));
	}
}
