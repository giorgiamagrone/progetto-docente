package it.sport.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.sport.siw.model.Player;
import it.sport.siw.repository.TeamRepository;
import jakarta.validation.Valid;

@Controller
//@RequestMapping("/players")
public class PlayerController {
	@Autowired
	TeamRepository teamRepository;
    @PostMapping("/add")
    public String addPlayer(@RequestParam Long teamId, @ModelAttribute("player") @Valid Player player, BindingResult result) {
        if (result.hasErrors()) {
            return "playerForm";
        }
        teamRepository.addPlayerToTeam(teamId, player);
        return "redirect:/teams/" + teamId;
    }

    @PostMapping("/delete")
    public String deletePlayer(@RequestParam Long playerId) {
    	teamRepository.removePlayer(playerId);
        return "redirect:/teams";
    }
}
