package com.aduilio.beerstock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aduilio.beerstock.dto.BeerDto;
import com.aduilio.beerstock.exception.BeerAlreadyRegisteredException;
import com.aduilio.beerstock.exception.BeerNotFoundException;
import com.aduilio.beerstock.service.BeerService;

import lombok.AllArgsConstructor;

/**
 * Provides REST methods to access the Beer entity.
 */
@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor
public class BeerController {

	private final BeerService beerService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BeerDto create(@RequestBody @Validated final BeerDto beerDto) throws BeerAlreadyRegisteredException {
		final Long id = beerService.create(beerDto);
		return BeerDto.builder()
				.id(id)
				.build();
	}

	@GetMapping("/{name}")
	public BeerDto readByName(@PathVariable final String name) throws BeerNotFoundException {
		return beerService.readByName(name);
	}

	@GetMapping
	public List<BeerDto> list() {
		return beerService.list();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable final Long id) throws BeerNotFoundException {
		beerService.delete(id);
	}
}
