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

import it.sport.siw.model.President;
import it.sport.siw.model.Team;
import it.sport.siw.repository.PresidentRepository;
import it.sport.siw.repository.TeamRepository;


@Controller
public class PresidentController {
	
	@Autowired
	private PresidentRepository presidentRepository;
	@Autowired
	private TeamRepository teamRepository;

	@GetMapping("/admin/formNewPresident")
	public String formNewPresident(Model model) {
	    model.addAttribute("president", new President());
	    model.addAttribute("teams", teamRepository.findAll()); // Lista di tutte le squadre
	    return "admin/formNewPresident"; // Pagina HTML modificata
	}

	@GetMapping(value="/president/indexPresident")
	public String indexPresident() {
		return "president/indexPresident";
	}
	
	@PostMapping("/admin/president")
	public String newPresident(@ModelAttribute("president") President president, Model model) {
		if (!presidentRepository.existsByNameAndSurname(president.getName(), president.getSurname())) {
			this.presidentRepository.save(president); 
			model.addAttribute("president", president);
			return "president";
		} else {
			model.addAttribute("messaggioErrore", "Questo presidente esiste già");
			return "admin/formNewPresident"; 
		}
	}
	
	@GetMapping("/president/{id}")
	public String getPresident(@PathVariable("id") Long id, Model model) {
	    Optional<President> optionalPresident = this.presidentRepository.findById(id);
	    
	    if (optionalPresident.isPresent()) {
	        model.addAttribute("president", optionalPresident.get());
	        return "president";
	    } else {
	        model.addAttribute("errorMessage", "Presidente non trovato");
	        return "error"; // O un'altra vista di errore
	    }
	}
	// Form per eliminare un presidente - Gestito dall'admin
	@GetMapping("/admin/formDeletePresident")
	public String formDeletePresident(Model model) {
	    model.addAttribute("presidents", this.presidentRepository.findAll());
	    return "admin/formDeletePresident";  // Nome della vista per eliminare
	}


	// Eliminazione di un presidente - Gestito dall'admin
	@PostMapping("/admin/president/delete")
	public String deletePresident(@RequestParam("id") Long id, Model model) {
	    if (this.presidentRepository.existsById(id)) {
	        this.presidentRepository.deleteById(id);
	        model.addAttribute("messaggioSuccesso", "Presidente eliminato con successo");
	    } else {
	        model.addAttribute("messaggioErrore", "Presidente non trovato");
	    }
	    model.addAttribute("presidents", this.presidentRepository.findAll());
	    return "admin/formDeletePresident"; 
	}
	// Torna alla vista con il form per eliminare
	    @GetMapping("/admin/formEditPresident/{id}")
	    public String formEditPresident(@PathVariable("id") Long id, Model model) {
	        President president = presidentRepository.findById(id).get();
	        model.addAttribute("president", president);
	        model.addAttribute("teams", teamRepository.findAll()); // Lista di tutte le squadre
	        return "admin/formEditPresident";
	    }

	    @PostMapping("/admin/president/edit")
	    public String updatePresident(@ModelAttribute("president") President president, Model model) {
	        if (presidentRepository.existsById(president.getId())) {
	            // Verifichiamo se la squadra selezionata esiste nel database
	            if (president.getTeam() != null && teamRepository.existsById(president.getTeam().getId())) {
	                Team team = teamRepository.findById(president.getTeam().getId()).get();
	                president.setTeam(team); // Associazione della squadra al presidente
	            }
	            
	            // Salviamo il presidente aggiornato
	            presidentRepository.save(president);
	            model.addAttribute("messaggioSuccesso", "Presidente aggiornato con successo");
	            return "redirect:/presidents";  // Redirect alla lista dei presidenti
	        } else {
	            model.addAttribute("messaggioErrore", "Errore nell'aggiornamento del presidente");
	            return "admin/formEditPresident";  // Torna alla vista di modifica con errore
	        }
	    }

    }
