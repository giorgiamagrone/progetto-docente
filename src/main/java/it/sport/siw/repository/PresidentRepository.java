package it.sport.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.sport.siw.model.President;


public interface PresidentRepository extends CrudRepository<President, Long>{
	  
}
