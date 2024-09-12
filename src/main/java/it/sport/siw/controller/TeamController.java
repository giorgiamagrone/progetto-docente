package it.sport.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.sport.siw.controller.validator.TeamValidator;
import it.sport.siw.model.Team;
import it.sport.siw.repository.PresidentRepository;
import it.sport.siw.repository.TeamRepository;
//import it.sport.siw.service.TeamService;
import jakarta.validation.Valid;

@Controller
public class TeamController {
	// @Autowired
	// private TeamService teamService;

	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private TeamValidator teamValidator;

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/team/{id}")
	public String getTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamRepository.findById(id).get());
		return "team";
	}
	
	@GetMapping("/team")
	public String showTeams(Model model) {
		model.addAttribute("teams", this.teamRepository.findAll());
		return "teams";
	}

	@PostMapping("/team")
	public String newTeam(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {

	    this.teamValidator.validate(team, bindingResult);
	    if (!bindingResult.hasErrors()) {
	        this.teamRepository.save(team);
	        model.addAttribute("team", team);
	        return "team.html";
	    } else {
	        return "admin/formNewTeam";
	    }
	}
	@GetMapping("/admin/formNewTeam")
	public String formNewTeam(Model model) {
	    model.addAttribute("team", new Team());
	    return "admin/formNewTeam";
	}

	@GetMapping("/formSearchTeam")
	public String formSearchTeams(Model model) {
	    model.addAttribute("teamSearch", new Team()); 
	    return "formSearchTeam";  
	}

	@PostMapping("/formSearchTeam")
	public String searchTeams(Model model, @RequestParam("year") int year) {
	    model.addAttribute("teams", this.teamRepository.findByYear(year));
	    return "foundTeam";
	}

    // Metodo per visualizzare il form di modifica per una squadra esistente
    @GetMapping("/admin/formEditTeam/{id}")
    public String formEditTeam(@PathVariable("id") Long id, Model model) {
        Team team = this.teamRepository.findById(id).orElse(null);
        
        if (team == null) {
            return "redirect:/error"; // Reindirizza a una pagina di errore se il team non esiste
        }

        model.addAttribute("team", team);
        return "admin/formEditTeam"; // Mostra il form di modifica dei dettagli della squadra
    }

    // Metodo per aggiornare una squadra esistente
    @PostMapping("/admin/formEditTeam/{id}")
    public String updateTeam(@PathVariable("id") Long id, @Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {
        // Validiamo i dati della squadra
        this.teamValidator.validate(team, bindingResult);
        if (!bindingResult.hasErrors()) {
            // Recuperiamo la squadra esistente dal database
            Team existingTeam = this.teamRepository.findById(id).orElse(null);
            if (existingTeam == null) {
                return "redirect:/error"; // Se la squadra non esiste, reindirizza a una pagina di errore
            }

            // Aggiorniamo solo i campi della squadra, senza modificare il presidente
            existingTeam.setName(team.getName());
            existingTeam.setYear(team.getYear());
            existingTeam.setAddress(team.getAddress());
            
            // Salviamo le modifiche
            this.teamRepository.save(existingTeam);

            // Aggiungiamo la squadra aggiornata al modello
            model.addAttribute("team", existingTeam);

            // Redirigiamo alla pagina della squadra
            return "redirect:/team/" + id;
        } else {
            // In caso di errori di validazione, torniamo al form di modifica con gli errori
            model.addAttribute("team", team);
            return "admin/formEditTeam";
        }
    }

    }

