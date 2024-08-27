package it.sport.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.sport.siw.model.Team;
import it.sport.siw.repository.TeamRepository;

@Controller
public class TeamController {
	@Autowired 
	TeamRepository teamRepository;
	
	@GetMapping("/index.html")
	public String index() {
		return "index.html";
	}
	@PostMapping("/teams")
	public String newMovie(@ModelAttribute("movie") Team team, Model model) {
		if (!teamRepository.existsByNameAndYear(team.getName(), team.getYear())) {
			this.teamRepository.save(team); 
			model.addAttribute("team", team);
			return "team.html";
		} else {
			model.addAttribute("messaggioErrore", "Questa squadra esiste già");
			return "formNewTeam.html"; 
		}
	}
	
	 @GetMapping("/formNewTeam")
	    public String formNewTeam(Model model) {
	    model.addAttribute("team", new Team());
	    return "formNewTeam.html";
	  }
	 
	 @PostMapping("/team")
	  public String newTeam(@ModelAttribute("team") Team team, Model model) {
		this.teamRepository.save(team);
	    model.addAttribute("team", team);
	      return "redirect:/team/"+team.getId();
	  }

	  @GetMapping("/team/{id}")
	  public String getTeam(@PathVariable("id") Long id, Model model) {
	    model.addAttribute("team", this.teamRepository.findById(id).get());
	    return "team.html";
	  }

	  @GetMapping("/team")
	  public String showTeams(Model model) {
	    model.addAttribute("teams", this.teamRepository.findAll());
	    return "teams.html";
	  }
	  
	 @GetMapping("/formSearchTeams")
	  public String formSearchTeam() {
	    return "formSearchTeams.html";
	  }

	  @PostMapping("/searchTeams")
	  public String searchTeams(Model model, @RequestParam Integer year) {
	      model.addAttribute("teams", this.teamRepository.findByYear(year));
	      return "foundTeam.html";
	 
}
}