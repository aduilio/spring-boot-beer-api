package com.aduilio.beerstock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aduilio.beerstock.entity.Beer;

/**
 * Provides the access to the database.
 */
public interface BeerRepository extends JpaRepository<Beer, Long> {

	/**
	 * Finds a beer by name.
	 *
	 * @param name of the beer
	 *
	 * @return an {@link Optional} of {@link Beer}
	 */
	Optional<Beer> findByName(String name);
}
