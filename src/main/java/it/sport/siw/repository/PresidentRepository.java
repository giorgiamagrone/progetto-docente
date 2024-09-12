package it.sport.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.sport.siw.model.President;

@Repository
public interface PresidentRepository extends CrudRepository<President, Long>{
	
	public boolean existsByNameAndSurname(String name, String surname);	

	@Query(value = "SELECT name FROM President WHERE id NOT IN " +
	        "(SELECT id FROM Team WHERE id = :teamId)",  nativeQuery=true)
	Iterable<President> findPresidentsNotInTeam(@Param("teamId") Long teamId);
}

