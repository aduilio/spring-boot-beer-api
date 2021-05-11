package com.aduilio.beerstock.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.aduilio.beerstock.enums.BeerType;
import com.aduilio.beerstock.exception.BeerExceedStockException;
import com.aduilio.beerstock.exception.BeerNegativeStockException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a beer.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String brand;

	@Column(nullable = false)
	private int max;

	@Column(nullable = false)
	private int quantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BeerType type;

	public void increment(final int quantity) throws BeerExceedStockException, BeerNegativeStockException {
		if (quantity > 0 && this.max - this.quantity < quantity) {
			throw new BeerExceedStockException(this.max - this.quantity);
		}

		if (quantity < 0 && this.quantity - Math.abs(quantity) < 0) {
			throw new BeerNegativeStockException(this.quantity);
		}

		this.quantity += quantity;
	}
}
