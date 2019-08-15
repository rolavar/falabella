package com.falabella.beertest.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class HomeControllerTest {

	@Autowired
	private MockMvc mvc;

	private static final String BASE_URL = "http://localhost:8080/";

	@Before
	public void testContext() {
		assertNotNull(mvc);
	}

	@Test
	public void home() throws Exception {

		MvcResult results = mvc.perform(get(BASE_URL)).andExpect(status().is(200))
				.andReturn();
		
		assertEquals("Cerveza rica cerveza", results.getResponse().getContentAsString());

	}


}
