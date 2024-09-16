package it.sport.siw.controller;

import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.sport.siw.model.Contract;
import it.sport.siw.model.Credentials;
import it.sport.siw.model.Player;
import it.sport.siw.model.President;
import it.sport.siw.model.Team;
import it.sport.siw.repository.ContractRepository;
import it.sport.siw.repository.PlayerRepository;
import it.sport.siw.repository.PresidentRepository;
import it.sport.siw.service.CredentialsService;
import jakarta.transaction.Transactional;

@Controller
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PresidentRepository presidentRepository;
    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private ContractRepository contractRepository;

    @GetMapping("/president/indexPlayer/{id}")
    public String getPlayer(@PathVariable("id") Long id, Model model) {
        Player player = playerRepository.findByIdWithContract(id)
                .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        model.addAttribute("player", player);

        return "president/indexPlayer";  // Pagina per visualizzare i dettagli del giocatore
    }

    @GetMapping("/president/indexPlayers")
    public String indexPlayers(Model model, Principal principal) {
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();

        // Ottiene tutti i giocatori della squadra del presidente
        model.addAttribute("players", playerRepository.findByTeam(president.getTeam()));

        return "president/indexPlayers";
    }

    @GetMapping("/president/formNewPlayer")
    public String formNewPlayer(Model model) {
        Player player = new Player();
        player.setContract(new Contract());  
        model.addAttribute("player", player);
        return "president/formNewPlayer";
    }
    
    @PostMapping("/president/indexPlayer")
    public String newPlayer(@ModelAttribute Player player, Model model) {
        // Recupera l'utente autenticato
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Cerca il presidente loggato tramite username
        President president = presidentRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Presidente non trovato"));

        // Recupera la squadra associata al presidente loggato
        Team team = president.getTeam();
        if (team == null) {
            model.addAttribute("errorMessage", "Non hai una squadra associata.");
            return "/president/errorPage";  // Mostra un errore se il presidente non ha una squadra
        }

        // Cerca se esiste già un giocatore con lo stesso nome, cognome e data di nascita
        Optional<Player> existingPlayer = playerRepository.findPlayerByDetails(player.getName(), player.getSurname(), player.getDateOfBirth());

        if (existingPlayer.isPresent()) {
            // Verifica se il giocatore esistente ha un contratto attivo
            Player existing = existingPlayer.get();
            if (existing.getContract() != null && LocalDateTime.now().isBefore(existing.getContract().getStopCareer())) {
                model.addAttribute("errorMessage", "Questo giocatore è già tesserato in un'altra squadra.");
                return "/president/errorPage"; // Mostra la pagina di errore
            }
        }

        // Imposta la squadra del giocatore con la squadra del presidente loggato
        player.setTeam(team);

        // Salva il giocatore
        playerRepository.save(player);

        // Se c'è un contratto associato al giocatore, impostalo e salvalo
        Contract contract = player.getContract();
        if (contract != null) {
            contract.setPlayer(player);  // Associa il contratto al giocatore appena salvato
            contract.setStartCareer(LocalDateTime.now());  // Imposta l'inizio carriera al giorno corrente
            contractRepository.save(contract);  // Salva il contratto
        }

        // Aggiorna nuovamente il giocatore con il contratto appena salvato
        player.setContract(contract);
        playerRepository.save(player); // Salva nuovamente il giocatore con il contratto aggiornato

        model.addAttribute("successMessage", "Giocatore aggiunto con successo alla squadra " + team.getName() + ".");
        return "redirect:/president/indexPlayer/" + player.getId(); // Redirect alla pagina di successo o al dettaglio del giocatore
    }


    @GetMapping("/president/selectPlayerToEdit")
    public String selectPlayerToEdit(Model model, Principal principal) {
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();
        model.addAttribute("players", playerRepository.findByTeam(president.getTeam()));
        return "president/selectPlayerToEdit";  // Ritorna la vista per selezionare il giocatore da modificare
    }

    @GetMapping("/president/formEditPlayer/{id}")
    public String showEditPlayerForm(@PathVariable("id") Long id, Model model, Principal principal) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        if (!player.getTeam().equals(presidentTeam)) {
            throw new IllegalArgumentException("Non puoi modificare un giocatore che non appartiene alla tua squadra");
        }

        model.addAttribute("player", player);
        return "president/formEditPlayer";  // Ritorna la vista per modificare il giocatore
    }


    @PostMapping("/president/formEditPlayer/{id}")
    public String formEditPlayer(@PathVariable("id") Long playerId, 
                                 @ModelAttribute("player") Player player, 
                                 BindingResult result, 
                                 Model model) {

        // Trova il giocatore esistente
        Player existingPlayer = playerRepository.findById(playerId).orElse(null);

        if (existingPlayer != null) {
            // Aggiorna i campi del giocatore
            existingPlayer.setName(player.getName());
            existingPlayer.setSurname(player.getSurname());
            existingPlayer.setRole(player.getRole());

            // Aggiorna il contratto se presente
            if (player.getContract() != null) {
                Contract contract = player.getContract();
                existingPlayer.setContract(contract);  // Imposta il contratto aggiornato nel giocatore
                contract = contractRepository.save(contract);  // Salva il contratto aggiornato
            }

            // Salva il giocatore
            playerRepository.save(existingPlayer);
        }

        return "redirect:/president/selectPlayerToEdit"; // Redirect dopo il salvataggio
    }


    @GetMapping("/president/selectPlayerToDelete")
    public String selectPlayerToDelete(Model model, Principal principal) {
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        President president = credentials.getPresident();
        model.addAttribute("players", playerRepository.findByTeam(president.getTeam()));
        return "president/selectPlayerToDelete";  // Ritorna la vista per selezionare il giocatore da eliminare
    }

    @PostMapping("/president/deletePlayer/{id}")
    public String deletePlayer(@PathVariable("id") Long id, Principal principal, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        if (!player.getTeam().equals(presidentTeam)) {
            model.addAttribute("messaggioErrore", "Non puoi eliminare un giocatore che non appartiene alla tua squadra.");
            return "errorPage";
        }

        // Elimina il giocatore e il contratto (CascadeType.ALL assicura che il contratto venga eliminato)
        playerRepository.deleteById(id);

        return "redirect:/president/selectPlayerToDelete";  // Reindirizza alla pagina per selezionare altri giocatori da eliminare
    }
}
