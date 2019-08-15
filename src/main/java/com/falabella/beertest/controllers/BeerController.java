package com.falabella.beertest.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.PersistentObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.falabella.beertest.dto.BeerItem;
import com.falabella.beertest.repository.IBeerItemRepository;	

@RestController
@RequestMapping("beers")
public class BeerController {
	
	private final static Logger logger = LogManager.getLogger();

	private IBeerItemRepository beerRepository;
	
	private final static String SUCCESSFUL_OPERATION = "Operacion exitosa";
	private final static String BEER_DOESNT_EXISTS = "El Id de la cerveza no existe";
	
	@Autowired
	public void setBeerRepository(IBeerItemRepository beerRepository) {
		this.beerRepository = beerRepository;
	}


	@GetMapping
	public ResponseEntity<List<BeerItem>> searchBeers() {
		return ResponseEntity.ok()
				.header("Description","Operacion exitosa")
				.body(StreamSupport.stream(beerRepository.findAll().spliterator(),false).collect(Collectors.toList()));
	}

	
	@PostMapping
	public ResponseEntity<Void> addBeer(@RequestBody(required=true) BeerItem beer) {
		BeerItem newBeer = beerRepository.save(beer);
		return entityWithLocation(newBeer.getId());
	}
	
	@GetMapping(value= "/{beerId}")
	public ResponseEntity<BeerItem> searchBeerById(@PathVariable int beerId){
		Optional<BeerItem> beer = beerRepository.findById(beerId);
		if(beer.isPresent()) {
			return ResponseEntity.ok()
					.header("Description", SUCCESSFUL_OPERATION)
					.body(beer.get());
		}else {
			return ResponseEntity.notFound().header("Description", BEER_DOESNT_EXISTS).build();
		}
		
		
	}
	
	private ResponseEntity<Void> entityWithLocation(Object resourceId) {
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{resourceId}")
				.buildAndExpand(resourceId)
				.toUri();
		
		return ResponseEntity.created(location).header("Description", "Cerveza creada").build();

	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	private ResponseEntity<Void> badRequest() {
		logger.error("Bad request example");
		return ResponseEntity.badRequest().header("Description", "Request invalida").build();
	}
	
	@ExceptionHandler(PersistentObjectException.class)
	private ResponseEntity<Void> integrityViolation() {
		logger.error("Integrity violation example");
		return ResponseEntity.status(HttpStatus.CONFLICT).header("Description", "El ID de la cerveza ya existe").build();
	}
}

