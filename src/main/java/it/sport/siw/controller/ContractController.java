package it.sport.siw.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.sport.siw.model.Contract;
import it.sport.siw.model.Credentials;
import it.sport.siw.model.Player;
import it.sport.siw.model.Team;
import it.sport.siw.repository.ContractRepository;
import it.sport.siw.repository.PlayerRepository;
import it.sport.siw.service.CredentialsService;

@Controller
public class ContractController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CredentialsService credentialsService;

    // Mostra il form per aggiungere un nuovo contratto a un giocatore
    @GetMapping("/president/formNewContract/{playerId}")
    public String formNewContract(@PathVariable("playerId") Long playerId, Model model) {
        // Recupera il giocatore
        Player player = playerRepository.findById(playerId)
                          .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + playerId));

        // Verifica se il giocatore ha già un contratto attivo
        if (player.getContract() != null) {
            model.addAttribute("messaggioErrore", "Il giocatore ha già un contratto attivo.");
            return "president/indexPlayer"; // Torna alla pagina dei dettagli del giocatore
        }

        // Aggiungi il giocatore e un nuovo contratto vuoto al modello
        model.addAttribute("player", player);
        model.addAttribute("contract", new Contract());

        // Ritorna il form per creare un nuovo contratto
        return "president/formNewContract";
    }

    // Gestisce l'invio del form del contratto
    @PostMapping("/president/addContract/{playerId}")
    public String addContract(@PathVariable("playerId") Long playerId, 
                              @ModelAttribute("contract") Contract contract,
                              Principal principal, Model model) {
        // Recupera il giocatore
        Player player = playerRepository.findById(playerId)
                          .orElseThrow(() -> new IllegalArgumentException("ID giocatore non valido: " + playerId));

        // Ottieni le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        // Verifica se il presidente loggato appartiene alla squadra del giocatore
        if (!player.getTeam().equals(presidentTeam)) {
            model.addAttribute("messaggioErrore", "Non puoi aggiungere un contratto per un giocatore che non appartiene alla tua squadra.");
            return "president/indexPlayer"; // Torna alla pagina dei dettagli del giocatore con un messaggio di errore
        }

        // Associa il giocatore al contratto
        contract.setPlayer(player);

        // Salva il contratto nel repository
        contractRepository.save(contract);

        // Reindirizza alla pagina dei dettagli del giocatore
        return "redirect:/president/indexPlayer/" + playerId;
    }

    // Mostra il form di modifica del contratto
    @GetMapping("/president/formEditContract/{contractId}")
    public String formEditContract(@PathVariable("contractId") Long contractId, Principal principal, Model model) {
        // Recupera il contratto
        Contract contract = contractRepository.findById(contractId)
                             .orElseThrow(() -> new IllegalArgumentException("ID contratto non valido: " + contractId));

        // Ottieni le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        // Verifica se il presidente loggato appartiene alla squadra del giocatore
        if (!contract.getPlayer().getTeam().equals(presidentTeam)) {
            model.addAttribute("messaggioErrore", "Non puoi modificare il contratto di un giocatore che non appartiene alla tua squadra.");
            return "president/indexPlayer";  // Torna alla pagina dei dettagli del giocatore con un messaggio di errore
        }

        // Aggiungi il contratto al modello e mostra la pagina di modifica
        model.addAttribute("contract", contract);
        return "president/formEditContract";  // Mostra il form di modifica del contratto
    }

    // Gestisce l'invio del form di modifica del contratto
    @PostMapping("/president/updateContract/{contractId}")
    public String updateContract(@PathVariable("contractId") Long contractId, 
                                 @ModelAttribute("contract") Contract updatedContract, 
                                 Principal principal, Model model) {
        // Recupera il contratto esistente
        Contract contract = contractRepository.findById(contractId)
                             .orElseThrow(() -> new IllegalArgumentException("ID contratto non valido: " + contractId));

        // Ottieni le credenziali del presidente loggato
        Credentials credentials = credentialsService.getCredentials(principal.getName());
        Team presidentTeam = credentials.getPresident().getTeam();

        // Verifica se il presidente loggato è il presidente della squadra del giocatore
        if (!contract.getPlayer().getTeam().equals(presidentTeam)) {
            model.addAttribute("messaggioErrore", "Non puoi modificare il contratto di un giocatore che non appartiene alla tua squadra.");
            return "president/indexPlayer";  // Torna alla pagina dei dettagli del giocatore con un messaggio di errore
        }

        // Aggiorna i campi del contratto solo se il presidente è autorizzato
        contract.setStartCareer(updatedContract.getStartCareer());
        contract.setStopCareer(updatedContract.getStopCareer());

        // Salva le modifiche
        contractRepository.save(contract);

        // Reindirizza alla pagina del giocatore
        return "redirect:/president/indexPlayer/" + contract.getPlayer().getId();
    }

}
