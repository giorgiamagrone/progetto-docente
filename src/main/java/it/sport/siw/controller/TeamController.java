package it.sport.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;
import it.sport.siw.model.Team;
import it.sport.siw.repository.TeamRepository;

@Controller
public class TeamController {

    @Autowired 
    TeamRepository teamRepository;
    
    // Homepage
    @GetMapping("/index.html")
    public String index() {
        return "index.html";
    }

    // Visualizza il form per aggiungere una nuova squadra
    @GetMapping("/formNewTeam")
    public String formNewTeam(Model model) {
        model.addAttribute("team", new Team());
        return "formNewTeam.html"; // Carica il form per aggiungere una nuova squadra
    }

    // Salva una nuova squadra
    @PostMapping("/team")
    public String newTeam(@ModelAttribute("team") Team team, Model model) {
        if (!teamRepository.existsByNameAndYear(team.getName(), team.getYear())) {
            this.teamRepository.save(team);
            model.addAttribute("team", team);
            return "redirect:/team/" + team.getId(); // Reindirizza alla pagina dei dettagli della squadra
        } else {
            model.addAttribute("messaggioErrore", "Questa squadra esiste già");
            return "formNewTeam.html"; // Ritorna al form con un messaggio di errore
        }
    }

    // Visualizza i dettagli di una squadra tramite il suo ID
    @GetMapping("/team/{id}")
    public String getTeam(@PathVariable("id") Long id, Model model) {
        Optional<Team> team = this.teamRepository.findById(id);
        if (team.isPresent()) {
            model.addAttribute("team", team.get());
            return "team.html"; // Carica la pagina dei dettagli della squadra
        } else {
            model.addAttribute("messaggioErrore", "Squadra non trovata");
            return "redirect:/team"; // Reindirizza all'elenco delle squadre se l'ID non esiste
        }
    }

    // Visualizza tutte le squadre
    @GetMapping("/team")
    public String showTeams(Model model) {
        model.addAttribute("teams", this.teamRepository.findAll());
        return "teams.html"; // Carica la pagina che mostra tutte le squadre
    }

    // Visualizza il form per cercare una squadra per anno
    @GetMapping("/formSearchTeams")
    public String formSearchTeam() {
        return "formSearchTeams.html"; // Carica il form di ricerca per anno
    }

    // Cerca squadre per anno
    @PostMapping("/searchTeams")
    public String searchTeams(Model model, @RequestParam Integer year) {
        model.addAttribute("teams", this.teamRepository.findByYear(year));
        return "foundTeam.html"; // Carica la pagina con i risultati della ricerca
    }
}
