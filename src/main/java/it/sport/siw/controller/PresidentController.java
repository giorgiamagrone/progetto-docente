package it.sport.siw.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.sport.siw.model.President;
import it.sport.siw.repository.PresidentService;
import jakarta.validation.Valid;
@Controller
@RequestMapping("/presidents")
public class PresidentController {


	    @Autowired
	    private PresidentService presidentService;

	    // Visualizza l'elenco di tutti i presidenti
	    /*@GetMapping
	    public String listPresidents(Model model) {
	        model.addAttribute("presidents", presidentService.findAllPresidents());
	        return "presidentList";
	    }*/

	    // Mostra il form per creare un nuovo presidente
	    @GetMapping("/new")
	    public String showCreateForm(Model model) {
	        model.addAttribute("president", new President());
	        return "presidentForm";
	    }

	    // Salva il presidente
	    @PostMapping("/save")
	    public String savePresident(@ModelAttribute("president") @Valid President president, BindingResult result) {
	        if (result.hasErrors()) {
	            return "presidentForm";
	        }
	        presidentService.savePresident(president);
	        return "redirect:/presidents";
	    }

	    // Mostra i dettagli di un presidente
	    @GetMapping("/{id}")
	    public String showPresident(@PathVariable Long id, Model model) {
	        Optional<President> president = presidentService.findPresidentById(id);
	        if (president.isPresent()) {
	            model.addAttribute("president", president.get());
	            return "presidentDetail";
	        } else {
	            return "redirect:/presidents";
	        }
	    }

	    // Mostra il form per modificare un presidente esistente
	    @GetMapping("/edit/{id}")
	    public String showEditForm(@PathVariable Long id, Model model) {
	        Optional<President> president = presidentService.findPresidentById(id);
	        if (president.isPresent()) {
	            model.addAttribute("president", president.get());
	            return "presidentForm";
	        } else {
	            return "redirect:/presidents";
	        }
	    }

	    // Cancella un presidente
	    @GetMapping("/delete/{id}")
	    public String deletePresident(@PathVariable Long id) {
	        presidentService.deletePresident(id);
	        return "redirect:/presidents";
	    }
}
