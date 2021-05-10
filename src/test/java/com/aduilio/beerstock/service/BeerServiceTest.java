package com.aduilio.beerstock.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aduilio.beerstock.dto.BeerDto;
import com.aduilio.beerstock.entity.Beer;
import com.aduilio.beerstock.exception.BeerAlreadyRegisteredException;
import com.aduilio.beerstock.exception.BeerNotFoundException;
import com.aduilio.beerstock.mapper.BeerMapper;
import com.aduilio.beerstock.repository.BeerRepository;
import com.aduilio.beerstock.utils.BeerTestsUtil;

@ExtendWith(MockitoExtension.class)
class BeerServiceTest {

	@Mock
	private BeerRepository beerRepositoryMock;

	private final BeerMapper beerMapper = BeerMapper.INSTANCE;

	@InjectMocks
	private BeerService beerService;

	@Test
	void createWithBeerShouldReturnId() throws BeerAlreadyRegisteredException {
		final BeerDto beerDto = BeerTestsUtil.createBeerDto();
		final Beer expeted = beerMapper.mapBeerFrom(beerDto);

		when(beerRepositoryMock.findByName(BeerTestsUtil.BEER_NAME)).thenReturn(Optional.empty());
		when(beerRepositoryMock.save(expeted)).thenReturn(expeted);

		final Long result = beerService.create(beerDto);

		assertThat(result).isEqualTo(BeerTestsUtil.BEER_ID);
	}

	@Test
	void createWithInvalidBeerNameShouldThrowException() throws BeerAlreadyRegisteredException {
		final BeerDto beerDto = BeerTestsUtil.createBeerDto();
		final Beer expeted = beerMapper.mapBeerFrom(beerDto);

		when(beerRepositoryMock.findByName(BeerTestsUtil.BEER_NAME)).thenReturn(Optional.of(expeted));

		final BeerAlreadyRegisteredException exception = assertThrows(BeerAlreadyRegisteredException.class,
				() -> beerService.create(beerDto));

		assertThat(exception.getMessage()).isEqualTo("A beer already exists with name " + BeerTestsUtil.BEER_NAME);
	}

	@Test
	void readByNameWithValidNameShouldReturnBeer() throws BeerNotFoundException {
		final Beer beer = BeerTestsUtil.createBeer();
		final BeerDto expected = beerMapper.mapBeerDtoFrom(beer);

		when(beerRepositoryMock.findByName(BeerTestsUtil.BEER_NAME)).thenReturn(Optional.of(beer));

		final BeerDto result = beerService.readByName(BeerTestsUtil.BEER_NAME);

		assertThat(result).isEqualTo(expected);
	}

	@Test
	void readByNameWithInvalidNameShouldThrowException() throws BeerNotFoundException {
		when(beerRepositoryMock.findByName(BeerTestsUtil.BEER_NAME)).thenReturn(Optional.empty());

		final BeerNotFoundException exception = assertThrows(BeerNotFoundException.class,
				() -> beerService.readByName(BeerTestsUtil.BEER_NAME));

		assertThat(exception.getMessage()).isEqualTo("Invalid name " + BeerTestsUtil.BEER_NAME);
	}

	@Test
	void listWithValueShouldReturnBeers() throws BeerNotFoundException {
		final Beer beer = BeerTestsUtil.createBeer();
		final BeerDto expected = beerMapper.mapBeerDtoFrom(beer);

		when(beerRepositoryMock.findAll()).thenReturn(Collections.singletonList(beer));

		final List<BeerDto> result = beerService.list();

		assertThat(result.size()).isOne();
		assertThat(result.get(0)).isEqualTo(expected);
	}

	@Test
	void deleteWithValidIdShouldExecute() throws BeerNotFoundException {
		when(beerRepositoryMock.findById(BeerTestsUtil.BEER_ID)).thenReturn(Optional.of(BeerTestsUtil.createBeer()));

		beerService.delete(BeerTestsUtil.BEER_ID);

		verify(beerRepositoryMock, times(1)).deleteById(BeerTestsUtil.BEER_ID);
	}

	@Test
	void deleteWithInvalidIdShouldThrowException() throws BeerNotFoundException {
		when(beerRepositoryMock.findById(BeerTestsUtil.BEER_ID)).thenReturn(Optional.empty());

		final BeerNotFoundException exception = assertThrows(BeerNotFoundException.class,
				() -> beerService.delete(BeerTestsUtil.BEER_ID));

		assertThat(exception.getMessage()).isEqualTo("Invalid id " + BeerTestsUtil.BEER_ID);
		verify(beerRepositoryMock, never()).deleteById(BeerTestsUtil.BEER_ID);
	}
}
