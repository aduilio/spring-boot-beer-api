package com.aduilio.beerstock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for beer quantity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDto {

	private int quantity;
}
