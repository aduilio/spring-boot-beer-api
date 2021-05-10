package com.aduilio.beerstock.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.aduilio.beerstock.enums.BeerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for Beer entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerDto {

	private Long id;

	@NotEmpty
	@Size(min = 2, max = 100)
	private String name;

	@NotEmpty
	@Size(min = 2, max = 100)
	private String brand;

	private int max;

	private int quantity;

	private BeerType type;
}
