package it.sport.siw.controller;

import org.springframework.stereotype.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.sport.siw.model.Credentials;
import it.sport.siw.model.Player;
import it.sport.siw.model.President;
import it.sport.siw.model.Team;
import it.sport.siw.repository.PlayerRepository;
import it.sport.siw.service.CredentialsService;

@Controller
public class PlayerController {

    @Autowired 
    private PlayerRepository playerRepository;

    @Autowired 
    private CredentialsService credentialsService;
    @GetMapping("/president/indexPlayer/{id}")
    public String getPlayer(@PathVariable("id") Long id, Model model) {
        Player player = playerRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));
        model.addAttribute("player", player);
        return "president/indexPlayer"; // Pagina di visualizzazione dei dettagli del giocatore
    }
    @GetMapping("/president/indexPlayers")
    public String indexPlayers(Model model, Principal principal) {
        // Ottiene le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();

        // Ottiene tutti i giocatori della squadra del presidente
        model.addAttribute("players", playerRepository.findByTeam(president.getTeam()));
        
        // Ritorna alla vista indexPlayers.html per visualizzare i giocatori
        return "president/indexPlayers";
    }

    // Form per aggiungere un nuovo giocatore, squadra del presidente è già preimpostata
    @GetMapping("/president/formNewPlayer")
    public String formNewPlayer(Model model, Principal principal) {
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();
        
        Player newPlayer = new Player();
        newPlayer.setTeam(president.getTeam());  // Associa automaticamente il giocatore alla squadra del presidente
        
        model.addAttribute("player", newPlayer);
        return "president/formNewPlayer";  // Ritorna la vista per creare un nuovo giocatore
    }

    @PostMapping("/president/indexPlayer")
    public String newPlayer(@ModelAttribute("player") Player player, Principal principal, Model model) {
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();
        
        // Associa automaticamente il giocatore alla squadra del presidente
        player.setTeam(presidentTeam);

        if (player.getName() == null || player.getSurname() == null || player.getRole() == null) {  // Aggiunto controllo per il ruolo
            model.addAttribute("messaggioErrore", "Tutti i campi sono obbligatori");
            return "president/formNewPlayer";  // Torna al form se mancano informazioni
        }

        if (!playerRepository.existsByNameAndSurname(player.getName(), player.getSurname())) {
            playerRepository.save(player);
        } else {
            model.addAttribute("messaggioErrore", "Giocatore già esistente");
            return "president/formNewPlayer";  // Ritorna al form se il giocatore esiste già
        }

        return "redirect:/president/indexPlayer/" + player.getId();  // Reindirizza ai dettagli del giocatore
    }

 // Seleziona giocatore da modificare (solo della squadra del presidente)
    @GetMapping("/president/selectPlayerToEdit")
    public String selectPlayerToEdit(Model model, Principal principal) {
        // Ottiene le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();

        // Ottiene tutti i giocatori della squadra del presidente
        model.addAttribute("players", playerRepository.findByTeam(president.getTeam()));
        return "president/selectPlayerToEdit"; // Ritorna la vista per selezionare il giocatore da modificare
    }

    // Mostra il form di modifica per un singolo giocatore
    @GetMapping("/president/formEditPlayer/{id}")
    public String showEditPlayerForm(@PathVariable("id") Long id, Model model, Principal principal) {
        Player player = playerRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        // Ottiene le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        // Controlla che il giocatore appartenga alla squadra del presidente
        if (!player.getTeam().equals(presidentTeam)) {
            throw new IllegalArgumentException("Non puoi modificare un giocatore che non appartiene alla tua squadra");
        }

        model.addAttribute("player", player);
        return "president/formEditPlayer";  // Ritorna la vista per modificare il giocatore
    }

    // Salva le modifiche del giocatore, mantenendo la stessa squadra
    @PostMapping("/president/formEditPlayer/{id}")
    public String editPlayer(@PathVariable("id") Long id, @ModelAttribute("player") Player updatedPlayer, Principal principal) {
        Player player = playerRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        // Ottiene le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        // Controlla che il giocatore appartenga alla squadra del presidente
        if (!player.getTeam().equals(presidentTeam)) {
            throw new IllegalArgumentException("Non puoi modificare un giocatore che non appartiene alla tua squadra");
        }

        // Aggiorna solo le informazioni del giocatore, mantenendo la squadra invariata
        player.setName(updatedPlayer.getName());
        player.setSurname(updatedPlayer.getSurname());
        player.setRole(updatedPlayer.getRole());

        playerRepository.save(player);
        return "redirect:/president/indexPlayer/" + player.getId();  // Reindirizza alla pagina dei dettagli del giocatore
    }

 // Seleziona giocatore da eliminare (solo della squadra del presidente)
    @GetMapping("/president/selectPlayerToDelete")
    public String selectPlayerToDelete(Model model, Principal principal) {
        // Ottiene le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();

        // Ottiene tutti i giocatori della squadra del presidente
        model.addAttribute("players", playerRepository.findByTeam(president.getTeam()));
        return "president/selectPlayerToDelete"; // Ritorna la vista per selezionare il giocatore da eliminare
    }

    // Elimina un giocatore, solo se appartiene alla squadra del presidente (cambiato in POST)
    @PostMapping("/president/deletePlayer/{id}")
    public String deletePlayer(@PathVariable("id") Long id, Principal principal) {
        Player player = playerRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        // Ottiene le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        // Controlla che il giocatore appartenga alla squadra del presidente
        if (!player.getTeam().equals(presidentTeam)) {
            throw new IllegalArgumentException("Non puoi eliminare un giocatore che non appartiene alla tua squadra");
        }

        // Elimina il giocatore
        playerRepository.deleteById(id);
        
        // Reindirizza alla pagina per selezionare altri giocatori da eliminare
        return "redirect:/president/selectPlayerToDelete";
    }

}
