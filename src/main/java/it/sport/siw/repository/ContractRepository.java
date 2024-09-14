package it.sport.siw.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import it.sport.siw.model.Contract;
import it.sport.siw.model.Player;

public interface ContractRepository extends CrudRepository<Contract, Long> {
	@Query("SELECT c FROM Contract c WHERE c.player.id = :playerId AND (c.stopCareer IS NULL OR c.stopCareer > CURRENT_TIMESTAMP)")
    Optional<Contract> findActiveContractByPlayerId(@Param("playerId") Long playerId);
    Optional<Contract> findByPlayerAndStopCareerAfter(Player player, LocalDateTime now);

}
