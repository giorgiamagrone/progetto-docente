package it.sport.siw.controller;

import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
import it.sport.siw.service.CredentialsService;
import jakarta.transaction.Transactional;

@Controller
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

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
    @Transactional
    @PostMapping("/president/indexPlayer")
    public String newPlayer(@ModelAttribute("player") Player player, Principal principal, Model model) {
        // Check if contract is null or has null fields
        if (player.getContract() == null) {
            System.out.println("Contract is null");
        } else {
            System.out.println("Start Career: " + player.getContract().getStartCareer());
            System.out.println("Stop Career: " + player.getContract().getStopCareer());
        }

        String errorMessage = validatePlayer(player);
        if (errorMessage != null) {
            model.addAttribute("messaggioErrore", errorMessage);
            return "president/formNewPlayer";
        }

        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();
        player.setTeam(presidentTeam);

        playerRepository.save(player);

        Contract contract = player.getContract();
        contract.setPlayer(player);
        contractRepository.save(contract);

        return "redirect:/president/indexPlayer/" + player.getId();
    }

    private String validatePlayer(Player player) {
        if (player.getName() == null || player.getName().isEmpty()) {
            return "Il nome del giocatore è obbligatorio.";
        }
        if (player.getSurname() == null || player.getSurname().isEmpty()) {
            return "Il cognome del giocatore è obbligatorio.";
        }
        Contract contract = player.getContract();
        if (contract == null || contract.getStartCareer() == null || contract.getStopCareer() == null) {
            return "Le date di inizio e fine carriera sono obbligatorie.";
        }
        return null;
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
    public String editPlayer(@PathVariable("id") Long id, @ModelAttribute("player") Player updatedPlayer, Principal principal, Model model) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + id));

        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        if (!player.getTeam().equals(presidentTeam)) {
            model.addAttribute("messaggioErrore", "Non puoi modificare un giocatore che non appartiene alla tua squadra.");
            return "errorPage";
        }

        // Validazione manuale
        if (updatedPlayer.getName() == null || updatedPlayer.getName().isEmpty()) {
            model.addAttribute("messaggioErrore", "Il nome del giocatore è obbligatorio.");
            return "president/formEditPlayer";
        }
        if (updatedPlayer.getSurname() == null || updatedPlayer.getSurname().isEmpty()) {
            model.addAttribute("messaggioErrore", "Il cognome del giocatore è obbligatorio.");
            return "president/formEditPlayer";
        }

        // Aggiorna le informazioni del giocatore
        player.setName(updatedPlayer.getName());
        player.setSurname(updatedPlayer.getSurname());
        player.setRole(updatedPlayer.getRole());

        // Gestisci le modifiche al contratto se necessario
        Contract contract = player.getContract();
        if (contract != null) {
            contract.setStartCareer(updatedPlayer.getContract().getStartCareer());
            contract.setStopCareer(updatedPlayer.getContract().getStopCareer());
            contractRepository.save(contract);
        }

        playerRepository.save(player);
        return "redirect:/president/indexPlayer/" + player.getId();  // Reindirizza alla pagina dei dettagli del giocatore
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
