package com.falabella.beertest.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.falabella.beertest.dto.BeerItem;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BeerControllerTest {

	@Autowired
	private MockMvc mvc;

	private static final String BASE_URL = "http://localhost:8080/beers";

	@Before
	public void testContext() {
		assertNotNull(mvc);
	}

	private ObjectMapper mapper = new ObjectMapper();

	private <T> T fromJsonResult(MvcResult result, Class<T> tClass) throws Exception {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return this.mapper.readValue(result.getResponse().getContentAsString(), tClass);
	}

	private byte[] toJson(Object object) throws Exception {
		return this.mapper.writeValueAsString(object).getBytes();
	}

	@Test
	public void listBeers() throws Exception {

		MvcResult results = mvc.perform(get(BASE_URL)).andExpect(status().is(200))
				.andExpect(header().string("Description", "Operacion exitosa"))
				.andReturn();

		BeerItem[] beers = fromJsonResult(results, BeerItem[].class);
		assertNotNull(beers);
		assertTrue(beers.length > 0);
	}

	@Test
	public void addBeer() throws Exception {
		BeerItem beer = new BeerItem();
		beer.setBrewery("CCU");
		beer.setCountry("Chile");
		beer.setName("Baltica");
		beer.setPrice(BigDecimal.valueOf(300));
		
		byte[] beerJson = toJson(beer);
		mvc.perform(post(BASE_URL).content(beerJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201))
				.andExpect(header().string("Description", "Cerveza creada"))
				.andReturn();
	}

	@Test
	public void addBeerError400() throws Exception {

		BeerItem beer = new BeerItem();
		beer.setBrewery("CCU");
		// beer.setCountry("Chile");
		beer.setName("Baltica");
		beer.setPrice(BigDecimal.valueOf(300));
		
		byte[] beerJson = toJson(beer);

		mvc.perform(post(BASE_URL).content(beerJson).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400))
		.andExpect(header().string("Description", "Request invalida"))
		.andReturn();
	}

	@Test
	public void addBeerError409() throws Exception {
		BeerItem beer = new BeerItem();
		beer.setId(3);
		beer.setBrewery("CCU");
		beer.setCountry("Chile");
		beer.setName("Baltica");
		beer.setPrice(BigDecimal.valueOf(300));
		
		byte[] beerJson = toJson(beer);

		mvc.perform(post(BASE_URL).content(beerJson).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(409))
		.andExpect(header().string("Description", "El ID de la cerveza ya existe"))
		.andReturn();
	}

	@Test
	public void searchById() throws Exception {
		MvcResult result = mvc.perform(get(BASE_URL+"/1"))
				.andExpect(status().isOk())
				.andExpect(header().string("Description", "Operacion exitosa"))
				.andReturn();
		BeerItem beer = fromJsonResult(result, BeerItem.class);
		
		assertNotNull(beer);
		assertFalse(beer.toString().contains("null"));
		assertTrue(beer.getId() == 1);
		
	}

	@Test
	public void searchByIdError404() throws Exception {		
		mvc.perform(get(BASE_URL+"/42"))
		.andExpect(status().is(404))
		.andExpect(header().string("Description", "El Id de la cerveza no existe"))
		.andReturn();
	}

}
