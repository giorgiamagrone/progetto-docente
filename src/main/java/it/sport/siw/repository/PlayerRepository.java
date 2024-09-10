package it.sport.siw.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.sport.siw.model.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    // Metodo per verificare se un giocatore esiste in base a nome e cognome
    boolean existsByNameAndSurname(String name, String surname);

    // Metodo per trovare un giocatore per nome e cognome
    //Optional<Player> findByNameAndSurname(String name, String surname);
}
