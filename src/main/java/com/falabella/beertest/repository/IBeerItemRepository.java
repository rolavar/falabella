package com.falabella.beertest.repository;

import org.springframework.data.repository.CrudRepository;

import com.falabella.beertest.dto.BeerItem;

public interface IBeerItemRepository extends CrudRepository<BeerItem, Integer>{
	

}
