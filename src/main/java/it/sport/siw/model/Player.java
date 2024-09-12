package it.sport.siw.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String name;

	@NotBlank
	private String surname;

	private String city;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

	private String role;
	private LocalDateTime startCarreer;
	private LocalDateTime stopCarreer;
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDateTime getStartCarreer() {
		return startCarreer;
	}

	public void setStartCarreer(LocalDateTime startCarreer) {
		this.startCarreer = startCarreer;
	}

	public LocalDateTime getStopCarreer() {
		return stopCarreer;
	}

	public void setStopCarreer(LocalDateTime stopCarreer) {
		this.stopCarreer = stopCarreer;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, dateOfBirth, id, name, role, startCarreer, stopCarreer, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(city, other.city) && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(role, other.role)
				&& Objects.equals(startCarreer, other.startCarreer) && Objects.equals(stopCarreer, other.stopCarreer)
				&& Objects.equals(surname, other.surname);
	}

}
