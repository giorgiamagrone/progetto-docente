package it.sport.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.sport.siw.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
}

