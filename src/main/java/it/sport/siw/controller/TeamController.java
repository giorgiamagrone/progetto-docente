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
import it.sport.siw.repository.TeamRepository;
import jakarta.validation.Valid;

@Controller
public class TeamController {

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
        Team team = this.teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid team Id:" + id));
        model.addAttribute("team", team);
        return "team";
    }

    @GetMapping("/team")
    public String showTeams(Model model) {
        model.addAttribute("teams", this.teamRepository.findAll());
        return "teams";
    }

    @PostMapping("/team")
    public String newTeam(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {
        // Valida il team
        this.teamValidator.validate(team, bindingResult);
        
        // Se ci sono errori, mostra nuovamente il form con i messaggi di errore
        if (bindingResult.hasErrors()) {
            return "admin/formNewTeam";  // Nome del template per il form di creazione del team
        }
        
        // Se non ci sono errori, salva il team e reindirizza alla pagina di dettaglio
        this.teamRepository.save(team);
        return "redirect:/team/" + team.getId();  // Reindirizza alla pagina di dettaglio del team
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

    @GetMapping("/admin/selectTeamToEdit")
    public String selectTeamToEdit(Model model) {
        model.addAttribute("teams", this.teamRepository.findAll());
        return "admin/selectTeamToEdit";
    }

    @GetMapping("/admin/formEditTeam/{id}")
    public String formEditTeam(@PathVariable("id") Long id, Model model) {
        Team team = this.teamRepository.findById(id).orElse(null);
        if (team != null) {
            model.addAttribute("team", team);
            return "admin/formEditTeam";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/admin/formEditTeam/{id}")
    public String updateTeam(@PathVariable("id") Long id, @Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {
        this.teamValidator.validate(team, bindingResult);
        if (!bindingResult.hasErrors()) {
            Team existingTeam = this.teamRepository.findById(id).orElse(null);
            if (existingTeam != null) {
                existingTeam.setName(team.getName());
                existingTeam.setYear(team.getYear());
                existingTeam.setAddress(team.getAddress());

                this.teamRepository.save(existingTeam);

                model.addAttribute("team", existingTeam);

                return "redirect:/team/" + id;
            } else {
                return "redirect:/error";
            }
        } else {
            model.addAttribute("team", team);
            return "admin/formEditTeam";
        }
    }
}
