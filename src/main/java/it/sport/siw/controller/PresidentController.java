package it.sport.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.sport.siw.model.President;
import it.sport.siw.repository.PresidentRepository;

import java.util.Optional;

@Controller
public class PresidentController {

    @Autowired
    private PresidentRepository presidentRepository;

    // Visualizza l'elenco dei presidenti
    @GetMapping("/presidents")
    public String listPresidents(Model model) {
        model.addAttribute("presidents", presidentRepository.findAll());
        return "indexPresident"; // Carica il file indexPresident.html
    }

    // Visualizza il form per aggiungere un nuovo presidente
    @GetMapping("/admin/formNewPresident")
    public String formNewPresident(Model model) {
        model.addAttribute("president", new President());
        return "formNewPresident"; // Carica il file formNewPresident.html
    }

    // Salva un nuovo presidente
    @PostMapping("/admin/president")
    public String savePresident(@ModelAttribute("president") President president, Model model) {
        presidentRepository.save(president);
        return "redirect:/presidents"; // Reindirizza alla lista dei presidenti dopo l'inserimento
    }

    // Visualizza i dettagli di un presidente tramite il suo ID
    @GetMapping("/president/{id}")
    public String viewPresident(@PathVariable("id") Long id, Model model) {
        Optional<President> president = presidentRepository.findById(id);
        if (president.isPresent()) {
            model.addAttribute("president", president.get());
            return "president"; // Carica il file president.html
        } else {
            model.addAttribute("messaggioErrore", "Presidente non trovato");
            return "redirect:/presidents"; // Reindirizza all'elenco dei presidenti se l'ID non esiste
        }
    }
}
