package com.falabella.beertest.dto;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Persistable;

@Entity
@Table(name="BEER")
public class BeerItem implements Persistable<Integer> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@NotBlank
	@Column(name="NAME")
	private String name;
	
	@NotNull
	@NotBlank
	@Column(name="BREWERY")
	private String brewery;
	
	@NotNull
	@NotBlank
	@Column(name="COUNTRY",nullable=false)
	private String country;
	
	@NotNull
	@Column(name="PRICE")
	private BigDecimal price;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrewery() {
		return brewery;
	}

	public void setBrewery(String brewery) {
		this.brewery = brewery;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public boolean isNew() {
		return true;
	}

	@Override
	public String toString() {
		return "BeerItem [id=" + id + ", name=" + name + ", brewery=" + brewery + ", country=" + country + ", price="
				+ price + "]";
	}
	
	
		

}
