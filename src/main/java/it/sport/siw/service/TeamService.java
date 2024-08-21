package it.sport.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.sport.siw.model.Team;
import it.sport.siw.repository.TeamRepository;

@Service
public class TeamService {
	@Autowired
	private TeamRepository teamRepository;

	public Team findById(Long id) {
		return teamRepository.findById(id).get();
	}

	public Iterable<Team> findAll() {
		return teamRepository.findAll();
	}

	public void save(Team team) {
		// TODO Auto-public void inserisciPersona(Movie persona) {
		teamRepository.save(team);
		
	}
}
