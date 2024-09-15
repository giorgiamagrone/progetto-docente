package it.sport.siw.repository;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import it.sport.siw.model.Player;
import it.sport.siw.model.Team;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long>{

		public boolean existsByNameAndSurname(String name, String surname);	

		@Query(value = "SELECT name FROM Player WHERE id NOT IN " +
		        "(SELECT id FROM Team WHERE id = :teamId)",  nativeQuery=true)
		Iterable<Player> findPlayersNotInTeam(@Param("teamId") Long teamId);
		Iterable<Player> findByTeam(Team team);

		@Query("SELECT p FROM Player p LEFT JOIN FETCH p.contract WHERE p.id = :id")
		Optional<Player> findByIdWithContract(@Param("id") Long id);
		@Query("SELECT p FROM Player p WHERE p.name = :name AND p.surname = :surname AND p.dateOfBirth = :dateOfBirth")
		Optional<Player> findPlayerByDetails(@Param("name") String name, @Param("surname") String surname, @Param("dateOfBirth") LocalDate dateOfBirth);


}


