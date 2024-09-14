package it.sport.siw.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.sport.siw.model.Credentials;
import it.sport.siw.model.President;
import it.sport.siw.model.Team;
import it.sport.siw.repository.PresidentRepository;
import it.sport.siw.repository.TeamRepository;
import it.sport.siw.service.CredentialsService;


@Controller
public class PresidentController {

    @Autowired
    private PresidentRepository presidentRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private CredentialsService credentialsService;
    
    public void savePresident(President president) {
        presidentRepository.save(president);
    }
    
 // Form per inserire un nuovo presidente

    @GetMapping("/admin/formNewPresident")
    public String formNewPresident(Model model) {
        model.addAttribute("president", new President());
        model.addAttribute("credentials", new Credentials()); // Aggiungi le credenziali al modello
        model.addAttribute("teams", teamRepository.findAll()); // Lista di tutte le squadre
        return "admin/formNewPresident";
    }
    @PostMapping("/admin/president")
    public String newPresident(@ModelAttribute("president") President president, 
                               @RequestParam("username") String username, 
                               @RequestParam("password") String password,
                               Model model) {

        // Controlla se il presidente esiste già
        if (!presidentRepository.existsByNameAndSurname(president.getName(), president.getSurname())) {
            try {
            	if (president.getTeam() != null && teamRepository.existsById(president.getTeam().getId())) {
                    // Assegna il team al presidente, se esiste
                    Team team = teamRepository.findById(president.getTeam().getId()).get();
                    president.setTeam(team);
                } else {
                    model.addAttribute("messaggioErrore", "Il team selezionato non esiste");
                    return "admin/formNewPresident";  // Ritorna alla form se c'è un errore
                }
                // Salva il presidente nel database
                presidentRepository.save(president);

                // Crea le credenziali associate al presidente
                Credentials credentials = new Credentials();
                credentials.setUsername(username);
                credentials.setPassword(password);  // Assicurati di crittografare la password nel service
                credentials.setPresident(president);

                // Salva le credenziali e assegna il ruolo "PRESIDENT_ROLE"
                credentialsService.saveCredentials(credentials, Credentials.PRESIDENT_ROLE);

                // Aggiungi un messaggio di successo al modello
                model.addAttribute("successMessage", "Presidente aggiunto con successo!");

                // Reindirizza alla lista dei presidenti
                return "redirect:/admin/presidents";

            } catch (Exception e) {
                // Aggiungi un messaggio di errore al modello se qualcosa va storto
                model.addAttribute("errorMessage", "Errore nell'aggiunta del presidente.");
                return "admin/formNewPresident";
            }
        } else {
            // Mostra un messaggio di errore se il presidente esiste già
            model.addAttribute("messaggioErrore", "Questo presidente esiste già");
            return "admin/formNewPresident";
        }
    }

    @GetMapping("/admin/presidents")
    public String showPresidents(Model model) {
        model.addAttribute("presidents", presidentRepository.findAll());
        return "/admin/presidents"; 
    }

    @GetMapping("/admin/president/{id}")
    public String getPresident(@PathVariable("id") Long id, Model model) {
    	  Optional<President> optionalPresident = this.presidentRepository.findById(id);
    	    if (optionalPresident.isPresent()) {
    	        model.addAttribute("president", optionalPresident.get());
    	        return "/admin/president";  // Si riferisce alla tua pagina HTML 'president.html'
    	    } else {
    	        model.addAttribute("messaggioErrore", "Presidente non trovato");
    	        return "error";  // Pagina di errore se il presidente non è trovato
    	    }
    }


    // Form per scegliere il presidente da modificare
    @GetMapping("/admin/selectPresidentToEdit")
    public String selectPresidentToEdit(Model model) {
        model.addAttribute("presidents", this.presidentRepository.findAll());
        return "admin/selectPresidentToEdit";  // Pagina per la selezione del presidente da modificare
    }

 // Form di modifica per il presidente selezionato
    @GetMapping("/admin/formEditPresident/{id}")
    public String formEditPresident(@PathVariable("id") Long id, Model model) {
        Optional<President> optionalPresident = this.presidentRepository.findById(id);
        if (optionalPresident.isPresent()) {
            model.addAttribute("president", optionalPresident.get());
            model.addAttribute("teams", teamRepository.findAll());  // Lista di tutte le squadre
            return "admin/formEditPresident";
        } else {
            model.addAttribute("messaggioErrore", "Presidente non trovato");
            return "error";  // Pagina di errore
        }
    }

    @PostMapping("/admin/formEditPresident/{id}")  // Aggiunta della barra prima di {id}
    public String updatePresident(@PathVariable("id") Long id, @ModelAttribute("president") President president, Model model) {
        Optional<President> optionalPresident = this.presidentRepository.findById(id);
        if (optionalPresident.isPresent()) {
            President existingPresident = optionalPresident.get();
            existingPresident.setName(president.getName());
            existingPresident.setSurname(president.getSurname());
            existingPresident.setCity(president.getCity());
            existingPresident.setCf(president.getCf());

            // Associa la squadra al presidente, se selezionata
            if (president.getTeam() != null && teamRepository.existsById(president.getTeam().getId())) {
                Team team = teamRepository.findById(president.getTeam().getId()).get();
                existingPresident.setTeam(team);
            }

            // Salva il presidente aggiornato
            this.presidentRepository.save(existingPresident);
            model.addAttribute("messaggioSuccesso", "Presidente aggiornato con successo");
            return "redirect:/presidents";  // Redirect alla lista dei presidenti
        } else {
            model.addAttribute("messaggioErrore", "Errore nell'aggiornamento del presidente");
            return "admin/formEditPresident";
        }
    }
    // Form per scegliere il presidente da modificare
    @GetMapping("/admin/selectPresidentToDelete")
    public String selectPresidentToDelete(Model model) {
        model.addAttribute("presidents", this.presidentRepository.findAll());
        return "admin/selectPresidentToDelete";  // Pagina per la selezione del presidente da modificare
    }
    @GetMapping("/admin/selectPresidentToDelete/{id}")
    public String deletePresident(@PathVariable("id") Long id, Model model) {
        Optional<President> optionalPresident = presidentRepository.findById(id);
        
        if (optionalPresident.isPresent()) {
            President president = optionalPresident.get();
            
            // Dissocia il presidente dal team, se ne ha uno associato
            if (president.getTeam() != null) {
                Team team = president.getTeam();
                team.setPresident(null);  // Rimuovi la relazione del presidente dal team
                teamRepository.save(team);  // Salva le modifiche al team
                president.setTeam(null);  // Rimuovi la relazione team dal presidente
            }
            
            // Elimina il presidente dal repository
            presidentRepository.delete(president);
            model.addAttribute("successMessage", "Presidente eliminato con successo");
        } else {
            model.addAttribute("errorMessage", "Presidente non trovato");
        }

        return "redirect:/admin/selectPresidentToDelete";
    }


}
