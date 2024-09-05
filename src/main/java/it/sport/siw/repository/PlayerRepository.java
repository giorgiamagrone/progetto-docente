package it.sport.siw.repository;



import org.springframework.data.repository.CrudRepository;

import it.sport.siw.model.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {
	
}

