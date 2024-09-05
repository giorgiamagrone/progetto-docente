package it.sport.siw.repository;
	import it.sport.siw.model.President;
	import it.sport.siw.repository.PresidentRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

	import java.util.List;
	import java.util.Optional;

	@Service
	public class PresidentService {

	    @Autowired
	    private PresidentRepository presidentRepository;

	    // Metodo per ottenere tutti i presidenti
	    /*public List<President> findAllPresidents() {
	        return presidentRepository.findAll();
	    }*/

	    // Metodo per ottenere un presidente tramite ID
	    public Optional<President> findPresidentById(Long id) {
	        return presidentRepository.findById(id);
	    }

	    // Metodo per salvare un nuovo presidente o aggiornarne uno esistente
	    public President savePresident(President president) {
	        return presidentRepository.save(president);
	    }

	    // Metodo per eliminare un presidente
	    public void deletePresident(Long id) {
	        presidentRepository.deleteById(id);
	    }
}
