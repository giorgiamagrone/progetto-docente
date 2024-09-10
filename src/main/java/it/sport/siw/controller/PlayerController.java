package it.sport.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.sport.siw.model.Player;
import it.sport.siw.repository.PlayerRepository;

import java.util.Optional;

@Controller
public class PlayerController {

    @Autowired 
    private PlayerRepository playerRepository;

    // Visualizza il form per aggiungere un nuovo giocatore
    @GetMapping(value = "/admin/formNewPlayer")
    public String formNewPlayer(Model model) {
        model.addAttribute("player", new Player());
        return "admin/formNewPlayer"; // Assicurati che questo percorso corrisponda alla tua directory dei template
    }
    
    // Visualizza l'indexPlayer
    @GetMapping(value = "/admin/indexPlayer")
    public String indexPlayer() {
        return "admin/indexPlayer";
    }

    // Aggiungi un nuovo giocatore
    @PostMapping("/admin/player")
    public String newPlayer(@ModelAttribute("player") Player player, Model model) {
        if (!playerRepository.existsByNameAndSurname(player.getName(), player.getSurname())) {
            this.playerRepository.save(player);
            model.addAttribute("player", player);
            return "player"; // Assicurati che il file player.html esista nella directory corretta
        } else {
            model.addAttribute("messaggioErrore", "Questo giocatore esiste già");
            return "admin/formNewPlayer"; // Ritorna alla form con il messaggio di errore
        }
    }

    // Visualizza i dettagli di un giocatore tramite il suo ID
    @GetMapping("/player/{id}")
    public String getPlayer(@PathVariable("id") Long id, Model model) {
        Optional<Player> player = this.playerRepository.findById(id);
        if (player.isPresent()) {
            model.addAttribute("player", player.get());
            return "player"; // Carica la pagina player.html con i dettagli del giocatore
        } else {
            model.addAttribute("messaggioErrore", "Giocatore non trovato");
            return "redirect:/player"; // Reindirizza alla lista dei giocatori se il giocatore non esiste
        }
    }

    // Visualizza l'elenco completo dei giocatori
    @GetMapping("/player")
    public String getPlayers(Model model) {
        model.addAttribute("players", this.playerRepository.findAll());
        return "players"; // Carica la pagina players.html che mostra l'elenco dei giocatori
    }
}
