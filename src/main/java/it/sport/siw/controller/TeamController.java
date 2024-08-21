package it.sport.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.sport.siw.model.Team;
import it.sport.siw.service.TeamService;

@Controller
public class TeamController {
	@Autowired 
	TeamService teamService;
	
	 @GetMapping("/formNewTeam")
	    public String formNewTeam(Model model) {
	    model.addAttribute("team", new Team());
	    return "formNewTeam.html";
	  }
	 
	 @PostMapping("/team")
	  public String newTeam(@ModelAttribute("team") Team team, Model model) {
		this.teamService.save(team);
	    model.addAttribute("team", team);
	      return "redirect:team/"+team.getId();
	  }

	  @GetMapping("/team/{id}")
	  public String getTeam(@PathVariable("id") Long id, Model model) {
	    model.addAttribute("team", this.teamService.findById(id));
	    return "team.html";
	  }

	  @GetMapping("/team")
	  public String showTeams(Model model) {
	    model.addAttribute("teams", this.teamService.findAll());
	    return "teams.html";
	  }
}
