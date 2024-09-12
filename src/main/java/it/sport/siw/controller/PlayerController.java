package it.sport.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.sport.siw.model.Player;
import it.sport.siw.repository.PlayerRepository;

@Controller
public class PlayerController {

    @Autowired 
    private PlayerRepository playerRepository;

    // Form per aggiungere un nuovo giocatore - Gestito dal presidente
    @GetMapping(value="/president/formNewPlayer")
    public String formNewPlayer(Model model) {
        model.addAttribute("player", new Player());
        return "president/formNewPlayer";
    }

    // Pagina di gestione dei giocatori - Gestito dal presidente
    @GetMapping(value="/president/indexPlayer")
    public String indexPlayer() {
        return "president/indexPlayer";
    }

    // Aggiunta di un nuovo giocatore - Gestito dal presidente
    @PostMapping("/president/player")
    public String newPlayer(@ModelAttribute("player") Player player, Model model) {
        if (!playerRepository.existsByNameAndSurname(player.getName(), player.getSurname())) {
            this.playerRepository.save(player);
            model.addAttribute("player", player);
            return "player";
        } else {
            model.addAttribute("messaggioErrore", "Questo giocatore esiste già");
            return "president/formNewPlayer";
        }
    }

    // Dettagli di un singolo giocatore
    @GetMapping("/player/{id}")
    public String getPlayer(@PathVariable("id") Long id, Model model) {
        model.addAttribute("player", this.playerRepository.findById(id).get());
        return "player";
    }

    // Lista di tutti i giocatori
    @GetMapping("/player")
    public String getPlayers(Model model) {
        model.addAttribute("players", this.playerRepository.findAll());
        return "players";
    }

    // Form per eliminare un giocatore - Gestito dal presidente
    @GetMapping("/president/formDeletePlayer")
    public String formDeletePlayer(Model model) {
        model.addAttribute("players", this.playerRepository.findAll());
        return "president/formDeletePlayer";  // Mostra la vista con il form per eliminare
    }

    // Eliminazione di un giocatore - Gestito dal presidente
    @PostMapping("/president/player/delete")
    public String deletePlayer(@RequestParam("Id") Long Id, Model model) {
        if (this.playerRepository.existsById(Id)) {
            this.playerRepository.deleteById(Id);
            model.addAttribute("messaggioSuccesso", "Giocatore eliminato con successo");
        } else {
            model.addAttribute("messaggioErrore", "Giocatore non trovato");
        }
        model.addAttribute("players", this.playerRepository.findAll());
        return "president/formDeletePlayer";  // Torna alla vista con il form per eliminare
    }
 // Form per modificare un giocatore - Gestito dal presidente
    @GetMapping("/president/formEditPlayer/{id}")
    public String formEditPlayer(@PathVariable("id") Long id, Model model) {
        Player player = this.playerRepository.findById(id).orElse(null);
        if (player != null) {
            model.addAttribute("player", player);
            return "president/formEditPlayer";  // Mostra la vista con il form per modificare
        } else {
            model.addAttribute("messaggioErrore", "Giocatore non trovato");
            return "president/indexPlayer";  // Se il giocatore non esiste, ritorna alla pagina principale
        }
    }

    // Modifica di un giocatore esistente - Gestito dal presidente
    @PostMapping("/president/player/edit/{id}")
    public String editPlayer(@PathVariable("id") Long id, 
                             @ModelAttribute("player") Player updatedPlayer, 
                             Model model) {
        Player existingPlayer = this.playerRepository.findById(id).orElse(null);
        if (existingPlayer != null) {
            // Aggiorna i dati del giocatore
            existingPlayer.setName(updatedPlayer.getName());
            existingPlayer.setSurname(updatedPlayer.getSurname());
            existingPlayer.setCity(updatedPlayer.getCity());
            existingPlayer.setDateOfBirth(updatedPlayer.getDateOfBirth());
            existingPlayer.setRole(updatedPlayer.getRole());

            // Salva le modifiche
            this.playerRepository.save(existingPlayer);
            model.addAttribute("player", existingPlayer);
            model.addAttribute("messaggioSuccesso", "Giocatore aggiornato con successo");
            return "player";  // Mostra la vista con i dettagli aggiornati del giocatore
        } else {
            model.addAttribute("messaggioErrore", "Giocatore non trovato");
            return "president/indexPlayer";  // Se il giocatore non esiste, ritorna alla pagina principale
        }
    }
}
