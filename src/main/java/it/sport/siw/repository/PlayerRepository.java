package it.sport.siw.repository;
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
}


