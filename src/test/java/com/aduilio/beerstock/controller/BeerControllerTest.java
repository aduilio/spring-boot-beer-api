package com.aduilio.beerstock.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.aduilio.beerstock.dto.BeerDto;
import com.aduilio.beerstock.dto.QuantityDto;
import com.aduilio.beerstock.exception.BeerExceedStockException;
import com.aduilio.beerstock.exception.BeerNotFoundException;
import com.aduilio.beerstock.service.BeerService;
import com.aduilio.beerstock.utils.BeerTestsUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

	private static final String URL = "/api/v1/beers";

	private MockMvc mockMvc;

	@Mock
	private BeerService beerServiceMock;

	@InjectMocks
	private BeerController beerController;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(beerController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.setViewResolvers((s, locale) -> new MappingJackson2JsonView())
				.build();
	}

	@Test
	void createWithBeerShouldReturnId() throws JsonProcessingException, Exception {
		final BeerDto beerDto = BeerTestsUtil.createBeerDto();

		when(beerServiceMock.create(beerDto)).thenReturn(BeerTestsUtil.BEER_ID);

		mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(beerDto))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(BeerTestsUtil.BEER_ID.intValue())));
	}

	@Test
	void createWithoutFieldsShouldReturnError() throws JsonProcessingException, Exception {
		mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(new BeerDto()))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void readByNameWithValidNameShouldReturnBeer() throws JsonProcessingException, Exception {
		final BeerDto beerDto = BeerTestsUtil.createBeerDto();

		when(beerServiceMock.readByName(BeerTestsUtil.BEER_NAME)).thenReturn(beerDto);

		mockMvc.perform(get(URL + "/" + BeerTestsUtil.BEER_NAME).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(BeerTestsUtil.BEER_ID.intValue())))
				.andExpect(jsonPath("$.name", is(BeerTestsUtil.BEER_NAME)))
				.andExpect(jsonPath("$.brand", is(BeerTestsUtil.BEER_BRAND)))
				.andExpect(jsonPath("$.quantity", is(BeerTestsUtil.BEER_QTT)))
				.andExpect(jsonPath("$.max", is(BeerTestsUtil.BEER_MAX)));
	}

	@Test
	void readByNameWithInvalidNameShouldReturnError() throws JsonProcessingException, Exception {
		when(beerServiceMock.readByName(BeerTestsUtil.BEER_NAME)).thenThrow(BeerNotFoundException.class);

		mockMvc.perform(get(URL + "/" + BeerTestsUtil.BEER_NAME).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void listWithValueShouldReturnBeers() throws JsonProcessingException, Exception {
		final BeerDto beerDto = BeerTestsUtil.createBeerDto();

		when(beerServiceMock.list()).thenReturn(Collections.singletonList(beerDto));

		mockMvc.perform(get(URL).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(BeerTestsUtil.BEER_ID.intValue())))
				.andExpect(jsonPath("$[0].name", is(BeerTestsUtil.BEER_NAME)))
				.andExpect(jsonPath("$[0].brand", is(BeerTestsUtil.BEER_BRAND)))
				.andExpect(jsonPath("$[0].quantity", is(BeerTestsUtil.BEER_QTT)))
				.andExpect(jsonPath("$[0].max", is(BeerTestsUtil.BEER_MAX)));
	}

	@Test
	void deleteWithValidIdShouldExecute() throws JsonProcessingException, Exception {
		mockMvc.perform(delete(URL + "/" + BeerTestsUtil.BEER_ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteWithInvalidIdShouldReturnError() throws JsonProcessingException, Exception {
		doThrow(BeerNotFoundException.class).when(beerServiceMock)
				.delete(BeerTestsUtil.BEER_ID);

		mockMvc.perform(delete(URL + "/" + BeerTestsUtil.BEER_ID).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void stockWithValidIdShouldReturnBeer() throws JsonProcessingException, Exception {
		final BeerDto beerDto = BeerTestsUtil.createBeerDto();
		final int qtt = 1;
		beerDto.setQuantity(beerDto.getQuantity() + qtt);

		when(beerServiceMock.stock(beerDto.getId(), beerDto.getQuantity())).thenReturn(beerDto);

		mockMvc.perform(patch(URL + "/" + BeerTestsUtil.BEER_ID + "/stock").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(QuantityDto.builder()
						.quantity(beerDto.getQuantity())
						.build()))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(BeerTestsUtil.BEER_ID.intValue())))
				.andExpect(jsonPath("$.name", is(BeerTestsUtil.BEER_NAME)))
				.andExpect(jsonPath("$.brand", is(BeerTestsUtil.BEER_BRAND)))
				.andExpect(jsonPath("$.quantity", is(BeerTestsUtil.BEER_QTT + qtt)))
				.andExpect(jsonPath("$.max", is(BeerTestsUtil.BEER_MAX)));
	}

	@Test
	void stockWithInvalidIdShouldReturnError() throws JsonProcessingException, Exception {
		doThrow(BeerNotFoundException.class).when(beerServiceMock)
				.stock(BeerTestsUtil.BEER_ID, BeerTestsUtil.BEER_QTT);

		mockMvc.perform(patch(URL + "/" + BeerTestsUtil.BEER_ID + "/stock").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(QuantityDto.builder()
						.quantity(BeerTestsUtil.BEER_QTT)
						.build()))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void stockWithInvalidQuantityShouldReturnError() throws JsonProcessingException, Exception {
		doThrow(BeerExceedStockException.class).when(beerServiceMock)
				.stock(BeerTestsUtil.BEER_ID, BeerTestsUtil.BEER_QTT);

		mockMvc.perform(patch(URL + "/" + BeerTestsUtil.BEER_ID + "/stock").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(QuantityDto.builder()
						.quantity(BeerTestsUtil.BEER_QTT)
						.build()))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
