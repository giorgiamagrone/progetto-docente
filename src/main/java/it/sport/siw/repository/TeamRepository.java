package it.sport.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.sport.siw.model.Team;

public interface TeamRepository extends CrudRepository <Team, Long>{
	
	public List<Team> findByYear(int year);
	
	public boolean existsByNameAndYear(String name, int year);
}
